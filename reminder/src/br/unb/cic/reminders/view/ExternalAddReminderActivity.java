package br.unb.cic.reminders.view;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders.model.Reminder;

public class ExternalAddReminderActivity extends ReminderActivity {

	private boolean isNewCategory = false;
	private Category newCategory = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		reminder = new Reminder();
		try {
			setReminderFromIntent();
		} catch (Exception e) {
			Intent intent2 = new Intent(getApplicationContext(), AddReminderActivity.class);
			startActivity(intent2);
			finish();
		}
		super.onCreate(savedInstanceState);
	}

	private void setReminderFromIntent() throws Exception {
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		// Gets the information from the intent if it is an edit call
		if (action.equals("br.com.positivo.reminders.ADD_REMINDER") && "text/plain".equals(type)) {
			String text = intent.getStringExtra("text");
			String details = intent.getStringExtra("details");
			String categoryName = intent.getStringExtra("category_name");
			String date = intent.getStringExtra("date");
			String hour = intent.getStringExtra("hour");
			String priority = intent.getStringExtra("priority");

			List<Category> categories = Controller.instance(getApplicationContext()).listCategories();
			for (Category c : categories) {
				if (c.getName().equals(categoryName)) {
					newCategory = c;
					break;
				}
			}
			if (newCategory == null) {
				isNewCategory = true;
				newCategory = new Category();
				newCategory.setName(categoryName);
			}
			reminder.setText(text);
			reminder.setDetails(details);
			reminder.setDate(date);
			reminder.setHour(hour);
			reminder.setPriority(Priority.fromCode(Integer.parseInt(priority)));
			reminder.setCategory(newCategory);
		} else
			reminder = null;

	}

	@Override
	protected void initializeValues() {

		if (!reminder.isValid())
			return;

		edtReminder.setText(reminder.getText());
		edtDetails.setText(reminder.getDetails());
		updateSpinnerDateHour(spinnerDate, reminder.getDate());
		updateDateFromString(reminder.getDate());
		updateSpinnerDateHour(spinnerTime, reminder.getHour());
		updateTimeFromString(reminder.getHour());
		spinnerPriority.setSelection(reminder.getPriority());
		try {
			if (isNewCategory)
				spinnerCategory.setSelection(spinnerCategory.getCount() - 2);
			else
				spinnerCategory.setSelection(categoryToIndex(reminder.getCategory()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void persist(Reminder reminder) {
		try {
			if (isNewCategory) {
				Controller.instance(getApplicationContext()).addCategory(reminder.getCategory());
				reminder.setCategory(findCategory(reminder.getCategory()));
			}
			Controller.instance(getApplicationContext()).addReminder(reminder);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected List<Category> getCategories() throws Exception {

		List<Category> categories = super.getCategories();
		if (isNewCategory) {
			categories.add(newCategory);
		}
		return categories;
	}

	private Category findCategory(Category category) throws Exception {
		List<Category> categories = Controller.instance(getApplicationContext()).listCategories();
		for (Category c : categories) {
			if (c.getName().equals(category.getName()))
				return c;
		}
		return null;
	}

	private int categoryToIndex(Category category) throws Exception {
		List<Category> categories = Controller.instance(getApplicationContext()).listCategories();
		int i = 0;
		for (Category c : categories) {
			if (c.getName().equals(category.getName())) {
				return i;
			}
			i++;
		}
		return 0;
	}

}
