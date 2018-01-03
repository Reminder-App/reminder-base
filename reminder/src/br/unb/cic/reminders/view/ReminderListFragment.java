package br.unb.cic.reminders.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import util.Utility;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.controller.AllRemindersFilter;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.controller.ReminderFilter;
import br.unb.cic.reminders.model.Reminder;
import br.unb.cic.reminders2.R;

public class ReminderListFragment extends Fragment implements FiltersListChangeListener {

	private static String TAG = "reminder fragment list";

	private ListView lvReminderLate, lvReminderToday, lvReminderNextDays, lvReminderNoDate;
	private ReminderArrayAdapter adapter;

	// Used to keep the adapter of the listview that called the context menu:
	private ReminderArrayAdapter contextMenuAdapter;

	// Used to reference the interface views to the code in the createUI():
	private View view;

	/**
	 * Called when the activity is first created by the activity.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		view = inflater.inflate(R.layout.reminders_list_fragment, container, false);
		createUI();
		return view;
	}

	/**
	 * onResume method used to update the activity.
	 */
	@Override
	public void onResume() {
		super.onResume();
		updateListView(null);
	}

	/**
	 * Creates the context menu where shows the Edit and Delete options.
	 */
	// @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// Save the adapter which created this context menu:
		ListView view = (ListView) v;
		contextMenuAdapter = (ReminderArrayAdapter) view.getAdapter();

		menu.setHeaderTitle(R.string.context_menu_reminder_title);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.reminder_list_fragment_context_menu, menu);
	}

	/**
	 * Provides each action based in the chosen option(Edit or Delete).
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getGroupId() == R.id.context_menu_reminder) { // Used to verify
			// if it is the right context_menu

			// Gets the item position and gets the reminder in that position:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			Reminder reminder = (Reminder) contextMenuAdapter.getItem(info.position);

			// Switch between the options in the context menu(Edit, Delete and
			// Share)
			switch (item.getItemId()) {
			case R.id.edit:
				// Passes the current reminder to be edited via Intent and
				// Invokes edit method
				Intent editIntent = new Intent(getActivity().getApplicationContext(), EditReminderActivity.class);
				editIntent.putExtra("id", reminder.getId());
				editIntent.putExtra("text", reminder.getText());
				editIntent.putExtra("details", reminder.getDetails());
				editIntent.putExtra("category_name", reminder.getCategory().getName());
				editIntent.putExtra("category_id", Long.toString(reminder.getCategory().getId()));
				editIntent.putExtra("date", reminder.getDate());
				editIntent.putExtra("hour", reminder.getHour());
				editIntent.putExtra("priority", Integer.toString(reminder.getPriority()));
				startActivity(editIntent);
				updateListView(null);
				return true;
			case R.id.delete:
				// Invokes delete method
				try {// Deletes from the bank;
					Controller.instance(getActivity().getApplicationContext()).deleteReminder(reminder);
				} catch (DBException e) {
					Log.e(TAG, e.getMessage());
				}
				updateListView(null);
				return true;
			default:
				return super.onContextItemSelected(item);
			}

		}
		return super.onContextItemSelected(item);
	}

	/**
	 * Creates the User Interface.
	 **/
	public void createUI() {
		// Take the ListView view form the layout and apply the
		// ReminderArrayAdapter to it
		lvReminderLate = (ListView) view.findViewById(R.id.lvRemindersLate);
		lvReminderToday = (ListView) view.findViewById(R.id.lvRemindersToday);
		lvReminderNextDays = (ListView) view.findViewById(R.id.lvRemindersNextDays);
		lvReminderNoDate = (ListView) view.findViewById(R.id.lvRemindersNoDate);
		updateListView(null);
		registerForContextMenu(lvReminderLate);
		registerForContextMenu(lvReminderToday);
		registerForContextMenu(lvReminderNextDays);
		registerForContextMenu(lvReminderNoDate);
	}

	/**
	 * Updates the ListView with the current reminders.
	 */
	public void updateListView(ReminderFilter filter) {
		if (filter == null)
			filter = new AllRemindersFilter(getActivity());
		adapter = new ReminderArrayAdapter(getActivity().getApplicationContext(), filter.getReminderList());

		// Divide the adapter in 4 possibilities of dates(Late, Today, Next Days
		// and No Date)
		// and distribute it to each responsible ListView:
		ReminderArrayAdapter adapterLate, adapterToday, adapterNextDays, adapterNoDate;

		Reminder r = new Reminder();
		List<Reminder> remindersLate = new ArrayList<Reminder>();
		List<Reminder> remindersToday = new ArrayList<Reminder>();
		List<Reminder> remindersNextDays = new ArrayList<Reminder>();
		List<Reminder> remindersNoDate = new ArrayList<Reminder>();

		// Runs through the adapter and divides to the correspondent adapter
		for (int i = 0; i < adapter.getCount(); ++i) {
			r = adapter.getItem(i);
			if (r.getDate() != null) {
				String day = r.getDate().substring(0, 2);
				String month = r.getDate().substring(3, 5);
				String year = r.getDate().substring(6, 10);

				Calendar cal = Calendar.getInstance();
				GregorianCalendar gc = new GregorianCalendar();
				if (r.getHour() != null) {
					String hour = r.getHour().substring(0, 2);
					String min = r.getHour().substring(3, 5);
					gc.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day),
							Integer.parseInt(hour), Integer.parseInt(min));
				} else {
					gc.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
				}
				// if(Integer.parseInt(day)<cal.get(Calendar.DAY_OF_MONTH))
				if (gc.before(cal))
					remindersLate.add(r);
				else if ((cal.get(Calendar.YEAR) == gc.get(Calendar.YEAR))
						&& (cal.get(Calendar.MONTH) == gc.get(Calendar.MONTH))
						&& (cal.get(Calendar.DAY_OF_MONTH) == gc.get(Calendar.DAY_OF_MONTH)))
					remindersToday.add(r);
				else
					remindersNextDays.add(r);
			} else {
				remindersNoDate.add(r);
			}
		}

		// Creates each adapter:
		adapterLate = new ReminderArrayAdapter(getActivity().getApplicationContext(), remindersLate,
				Color.rgb(0xED, 0x1C, 0x24), ReminderArrayAdapter.LATE);
		adapterToday = new ReminderArrayAdapter(getActivity().getApplicationContext(), remindersToday,
				Color.rgb(0x33, 0xB5, 0xE5), ReminderArrayAdapter.TODAY);
		adapterNextDays = new ReminderArrayAdapter(getActivity().getApplicationContext(), remindersNextDays,
				Color.rgb(0x99, 0x99, 0x99), ReminderArrayAdapter.NEXT_DAYS);
		adapterNoDate = new ReminderArrayAdapter(getActivity().getApplicationContext(), remindersNoDate,
				Color.rgb(0x00, 0x00, 0x00), ReminderArrayAdapter.NO_DATE);

		// Apply the adapters and rearrange the ListViews to its contents size:
		lvReminderLate.setAdapter(adapterLate);
		Utility.setListViewHeightBasedOnChildren(lvReminderLate);
		lvReminderToday.setAdapter(adapterToday);
		Utility.setListViewHeightBasedOnChildren(lvReminderToday);
		lvReminderNextDays.setAdapter(adapterNextDays);
		Utility.setListViewHeightBasedOnChildren(lvReminderNextDays);
		lvReminderNoDate.setAdapter(adapterNoDate);
		Utility.setListViewHeightBasedOnChildren(lvReminderNoDate);
	}

	public void onSelectedFilterChanged(ReminderFilter filter) {
		updateListView(filter);
	}
}