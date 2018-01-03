package br.unb.cic.reminders.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.controller.AllRemindersFilter;
import br.unb.cic.reminders.controller.CategoryFilter;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.controller.PriorityFilter;
import br.unb.cic.reminders.controller.ReminderFilter;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders2.R;

/**
 * A list fragment that holds the list of categories.
 * 
 * @author rbonifacio
 */
public class FilterListFragment extends Fragment implements OnItemClickListener {

	private static final String CURRENT_FILTER_KEY = "current_filter";

	private static String TAG = "filter fragment list";

	private int currentFilterIndex;

	private List<FiltersListChangeListener> listeners;

	private FiltersListChangeListener filtersChangeListener;

	private int currentFilterId;
	// Used to list the filters.
	private ReminderFilterArrayAdapter adapter;
	private View view;

	private Button btAddCategory;
	private ListView lvFilters;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// this way, we could retrieve the selected filter
		// after an orientation change.
		if (savedInstanceState != null) {
			currentFilterIndex = savedInstanceState.getInt(CURRENT_FILTER_KEY);
		}

		/*
		 * categoryList = new ArrayList<String>();
		 * 
		 * setListAdapter(new ArrayAdapter<String>(getActivity(),
		 * R.layout.list_content));
		 * 
		 * ListView thisListView = getListView();
		 * 
		 * categoriesArrayAdapter = (ArrayAdapter<String>)getListAdapter();
		 * 
		 * thisListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		 * thisListView.setBackgroundColor(Color.WHITE);
		 */

		currentFilterId = 0;

		adapter = null;
	}

	/**
	 * Creates the view.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.categories_list_fragment, container, false);
		createUI();
		return view;
	}

	/**
	 * When resumed updates the filters list view.
	 */
	@Override
	public void onResume() {
		super.onResume();
		updateListView();
	}

	/**
	 * Creates the context menu where shows the Edit and Delete options.
	 */
	// @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle(R.string.context_menu_category_title);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.category_list_fragment_context_menu, menu);
	}

	/**
	 * Provides each action based in the chosen option(Edit or Delete).
	 **/
	/*
	 * @Override public boolean onContextItemSelected(MenuItem item) {
	 * 
	 * if(item.getGroupId() == R.id.context_menu_category) { //Used to verify if
	 * it is the right context_menu //Gets the item position and gets the
	 * category in that position: AdapterView.AdapterContextMenuInfo info =
	 * (AdapterView.AdapterContextMenuInfo)item.getMenuInfo(); Category category
	 * = (Category)lvCategory.getAdapter().getItem(info.position);
	 * 
	 * //Switch between the options in the context menu(Edit and Delete) switch
	 * (item.getItemId()) { case R.id.edit: //Passes the current reminder to be
	 * edited via Intent and Invokes edit method DialogFragment newFragment =
	 * new EditCategoryDialogFragment(category);
	 * newFragment.show(getFragmentManager(),
	 * ""+R.string.dialog_editcategory_title); updateListView(); return true;
	 * case R.id.delete: //Invokes delete method try {//Deletes from the bank;
	 * Controller
	 * .instance(getActivity().getApplicationContext()).deleteCategory(
	 * category); } catch (DBException e) { Log.e(TAG,e.getMessage()); }
	 * updateListView(); return true; default: return
	 * super.onContextItemSelected(item); }
	 * 
	 * } return super.onContextItemSelected(item); }
	 */

	public void addListener(FiltersListChangeListener filter) {
		if (listeners == null)
			listeners = new ArrayList<FiltersListChangeListener>();
		listeners.add(filter);
	}

	public void notifyListeners(ReminderFilter filter) {
		for (FiltersListChangeListener c : listeners) {
			c.onSelectedFilterChanged(filter);
		}
	}

	/**
	 * Creates the User Interface.
	 **/
	private void createUI() {

		// Button to add a Category:
		// btAddCategory = (Button) view.findViewById(R.id.btAddCategory);
		// btAddCategory.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {// Creates one DialogFragment to add a
		// // category
		// DialogFragment newFragment = new AddCategoryDialogFragment();
		// newFragment.show(getFragmentManager(), ""
		// + R.string.dialog_addcategory_title);
		// }
		// });

		// List of filters
		lvFilters = (ListView) view.findViewById(R.id.listCategories);
		lvFilters.setOnItemClickListener(this);
		registerForContextMenu(lvFilters);
		updateListView();
	}

	/**
	 * Used to populate the list view with the current filters and other
	 * filters.
	 */
	private void updateListView() {

		List<ReminderFilter> filters = new ArrayList<ReminderFilter>();

		// Add the allReminders to the filter list
		AllRemindersFilter allRemindersFilter = new AllRemindersFilter(getActivity());
		filters.add(allRemindersFilter);

		// Add the priorities to the filter list
		PriorityFilter highPriorityFilter = new PriorityFilter(Priority.HIGH, getActivity());
		filters.add(highPriorityFilter);
		PriorityFilter normalPriorityFilter = new PriorityFilter(Priority.NORMAL, getActivity());
		filters.add(normalPriorityFilter);
		PriorityFilter lowPriorityFilter = new PriorityFilter(Priority.LOW, getActivity());
		filters.add(lowPriorityFilter);

		// Get all categories
		List<Category> categories = new ArrayList<Category>();
		try {
			categories = Controller.instance(getActivity().getApplicationContext()).listCategories();
			notifyListeners(null);
		} catch (DBException e) {
			Log.e(CURRENT_FILTER_KEY, "STORAGE_SERVICE error. Message: " + e.getMessage()); // TODO:
																							// Handle
																							// it
																							// properly.
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(CURRENT_FILTER_KEY, "STORAGE_SERVICE error. Message: " + e.getMessage()); // TODO:
																							// Handle
																							// it
																							// properly.
			e.printStackTrace();
		}

		// Add the categories to the filter list
		ReminderFilter filter;
		for (Category c : categories) {
			filter = new CategoryFilter(c, getActivity());
			filters.add(filter);
		}

		adapter = new ReminderFilterArrayAdapter(getActivity().getApplicationContext(), filters);

		lvFilters.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		notifyListeners(adapter.getItem(position));

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}