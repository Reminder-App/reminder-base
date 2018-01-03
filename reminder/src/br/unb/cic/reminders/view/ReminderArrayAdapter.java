package br.unb.cic.reminders.view;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.model.Reminder;
import br.unb.cic.reminders2.R;

public class ReminderArrayAdapter extends ArrayAdapter<Reminder> {

	private Context context;

	private int rowColor = Color.BLACK;// Used to change the row texts color

	// rowType: used to identify which type of row it might produce, once
	// that each one has a different way to show the Date, for example.
	private int rowType = NEXT_DAYS;

	// Type constants:
	public static final int LATE = 0;
	public static final int TODAY = 1;
	public static final int NEXT_DAYS = 2;
	public static final int NO_DATE = 3;

	/**
	 * This class is used in the parts of the view which is needed to list the
	 * reminders, as an adapter to these components listing the Reminders items.
	 * 
	 * @param context
	 *            , and the reminders objects(List<Reminders>).
	 * 
	 * @author positivo
	 * 
	 */
	public ReminderArrayAdapter(Context context, List<Reminder> objects) {
		super(context, R.layout.reminder_row, objects);
		this.context = context;

		this.rowColor = Color.BLACK;
		this.rowType = NEXT_DAYS;
	}

	public ReminderArrayAdapter(Context context, List<Reminder> objects, int rowColor, int rowType) {
		super(context, R.layout.reminder_row, objects);
		this.context = context;

		this.rowColor = rowColor;
		this.rowType = rowType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout reminderRow;

		// Inflate the row view
		if (convertView == null) {
			reminderRow = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(R.layout.reminder_row, reminderRow, true);
		} else {
			reminderRow = (LinearLayout) convertView;
		}

		// Takes the views included in the row
		ImageView ivPriority = (ImageView) reminderRow.findViewById(R.id.ivPriority);
		TextView tvReminder = (TextView) reminderRow.findViewById(R.id.txtReminder);
		TextView tvDateFirst = (TextView) reminderRow.findViewById(R.id.txtDateFirst);
		TextView tvDateSecond = (TextView) reminderRow.findViewById(R.id.txtDateSecond);
		CheckBox tvDone = (CheckBox) reminderRow.findViewById(R.id.cbDone);
		tvDone.setTag(position);
		tvDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					Reminder reminder = getItem((Integer) buttonView.getTag());
					reminder.setDone(isChecked);
					Controller.instance(getContext()).updateReminder(reminder);
				} catch (DBException e) {
					e.printStackTrace();
				}
			}
		});

		// Fill the views with its contents
		// Fill the imageview based in the priority
		if (getItem(position).getPriority() == 1)
			ivPriority.setImageResource(R.drawable.important);
		else if (getItem(position).getPriority() == 2)
			ivPriority.setImageResource(R.drawable.urgent);

		tvReminder.setTextColor(rowColor);
		// if it is "Importante" or "Urgente" it might be bold!
		if (getItem(position).getPriority() != 0)
			tvReminder.setTypeface(null, Typeface.BOLD);
		tvReminder.setText(getItem(position).getText());

		// Based in the type of the row includes the specified date
		tvDateFirst.setTextColor(rowColor);
		tvDateFirst.setText(getDateFirst(position));
		tvDateSecond.setTextColor(rowColor);
		tvDateSecond.setText(getDateSecond(position));

		tvDone.setChecked(getItem(position).isDone());

		return reminderRow;
	}

	/**
	 * Used to get the DateFirst based in the rowType and proximity to the
	 * current date.
	 * 
	 * @param position
	 * @return
	 */
	private String getDateFirst(int position) {

		if (getItem(position).getDate() == null) {
			return "";
		}

		String months[] = { "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ" };
		String week[] = { "", "DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB" };

		// Today and thatDay Calendars being initialized
		Calendar today = Calendar.getInstance();
		GregorianCalendar thatDay = new GregorianCalendar();
		thatDay.set(Integer.parseInt(getItem(position).getDate().substring(6, 10)), // year
				Integer.parseInt(getItem(position).getDate().substring(3, 5)) - 1, // month
				Integer.parseInt(getItem(position).getDate().substring(0, 2)));// day

		// switch based on the row type
		switch (rowType) {
		case LATE:
			long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
			long days = diff / (24 * 60 * 60 * 1000);
			if (days == 1)
				return "Ontem";
			else
				return "Há " + days + " dias";
			// break;

		case TODAY:
			return getDatesHour(position);
		// break;

		case NEXT_DAYS:
			diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
			days = diff / (24 * 60 * 60 * 1000);

			if (days == 1) {
				return getDatesHour(position);
			} else if (days < 6) {
				return week[thatDay.get(Calendar.DAY_OF_WEEK)];
			} else {
				return thatDay.get(Calendar.DAY_OF_MONTH) + " " + months[thatDay.get(Calendar.MONTH)];
			}

			// break;

		default:
			break;
		}

		return getItem(position).getDate();

	}

	/**
	 * Used to get the DateSecond based in the rowType
	 * 
	 * @param position
	 * @return
	 */
	private String getDateSecond(int position) {

		switch (rowType) {
		case LATE:
			return getDatesHour(position);
		// break;
		case TODAY:
			return "hoje";
		// break;
		case NEXT_DAYS:
			// Today and thatDay Calendars being initialized
			Calendar today = Calendar.getInstance();
			GregorianCalendar thatDay = new GregorianCalendar();
			thatDay.set(Integer.parseInt(getItem(position).getDate().substring(6, 10)), // year
					Integer.parseInt(getItem(position).getDate().substring(3, 5)) - 1, // month
					Integer.parseInt(getItem(position).getDate().substring(0, 2)));// day

			long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
			long days = diff / (24 * 60 * 60 * 1000);

			if (days == 1)
				return "amanha";
			else
				return getDatesHour(position);
			// break;
		default:
			break;
		}

		return getItem(position).getHour();
	}

	/**
	 * Gets the hour in the DateFirst/DateSecond format
	 * 
	 * @param position
	 * @return
	 */
	private String getDatesHour(int position) {

		if (getItem(position).getHour() == null) {
			return "";
		}

		if (getItem(position).getHour().substring(3, 5) != "00")
			return getItem(position).getHour().substring(0, 2) + "h" + getItem(position).getHour().substring(3, 5);
		else
			return getItem(position).getHour().substring(0, 2) + "h";
	}

	/**
	 * Get rowColor
	 * 
	 * @return
	 */
	public int getRowColor() {
		return rowColor;
	}

	/**
	 * Set rowColor
	 * 
	 * @param rowColor
	 */
	public void setRowColor(int rowColor) {
		this.rowColor = rowColor;
	}

	/**
	 * Get rowType
	 * 
	 * @return
	 */
	public int getRowType() {
		return rowType;
	}

	/**
	 * Set rowType
	 * 
	 * @param rowType
	 */
	public void setRowType(int rowType) {
		this.rowType = rowType;
	}

}