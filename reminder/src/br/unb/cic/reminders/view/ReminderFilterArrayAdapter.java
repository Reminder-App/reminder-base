package br.unb.cic.reminders.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.unb.cic.reminders.controller.ReminderFilter;
import br.unb.cic.reminders2.R;

public class ReminderFilterArrayAdapter extends ArrayAdapter<ReminderFilter> {

	/**
	 * This class is used in the parts of the view which is needed to list the
	 * filters, as an adapter to these components listing the ReminderFilter
	 * items.
	 * 
	 * @author positivo
	 */

	public ReminderFilterArrayAdapter(Context context, List<ReminderFilter> objects) {
		super(context, R.layout.category_row, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout filterRow;

		// Inflate the view
		if (convertView == null) {
			filterRow = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(R.layout.category_row, filterRow, true);
		} else {
			filterRow = (LinearLayout) convertView;
		}

		TextView tvFilter = (TextView) filterRow.findViewById(R.id.row_categoryName);
		tvFilter.setText(getItem(position).getName());

		TextView tvNumReminders = (TextView) filterRow.findViewById(R.id.row_categoryCounter);
		tvNumReminders.setText(Integer.toString(getItem(position).getNumReminders()));

		return filterRow;
	}

}
