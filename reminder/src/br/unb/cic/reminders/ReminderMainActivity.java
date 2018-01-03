package br.unb.cic.reminders;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import br.unb.cic.reminders.view.AddReminderActivity;
import br.unb.cic.reminders.view.FilterListFragment;
import br.unb.cic.reminders.view.ReminderListFragment;
import br.unb.cic.reminders2.R;

public class ReminderMainActivity extends Activity {

	private static String TAG = "lembretes";

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminders_main_activity);
		createUI();
	}

	/**
	 * Creates the User Interface. Creates the ListFragment.
	 */
	private void createUI() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ReminderListFragment listReminderFragment = new ReminderListFragment();
		FilterListFragment listCategoryFragment = new FilterListFragment();
		listCategoryFragment.addListener(listReminderFragment);
		ft.add(R.id.listReminders, listReminderFragment);
		ft.add(R.id.listCategories, listCategoryFragment);
		ft.commit();
	}

	/**
	 * Create Options Menu. Creates the buttons on the ActionBar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_menu, menu);
		return true;
	}

	/**
	 * Switch between the buttons in the options menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_addReminder:
			// app icon in action bar clicked; go home
			Intent reminderIntent = new Intent(getApplicationContext(), AddReminderActivity.class);
			startActivity(reminderIntent);
			return true;
		case R.id.menu_searchReminder:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
