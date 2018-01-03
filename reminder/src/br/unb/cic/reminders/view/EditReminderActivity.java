package br.unb.cic.reminders.view;

import java.util.List;

import android.content.Intent;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders.model.Reminder;

public class EditReminderActivity extends ReminderActivity {

	@Override
	protected void initializeValues() {
		Intent intent = getIntent();

		// Gets the information from the intent if it is an edit call
		long reminderId = intent.getLongExtra("id", 0);
		String text = intent.getStringExtra("text");
		String details = intent.getStringExtra("details");
		String categoryName = intent.getStringExtra("category_name");
		String categoryId = intent.getStringExtra("category_id");
		String date = intent.getStringExtra("date");
		String hour = intent.getStringExtra("hour");
		String priority = intent.getStringExtra("priority");

		Category category = new Category();
		category.setId(Long.parseLong(categoryId));
		category.setName(categoryName);

		reminder.setId(reminderId);
		edtReminder.setText(text);
		edtDetails.setText(details);
		updateSpinnerDateHour(spinnerDate, date);
		updateDateFromString(date);
		updateSpinnerDateHour(spinnerTime, hour);
		updateTimeFromString(hour);
		spinnerPriority.setSelection(Priority.fromCode(Integer.parseInt(priority, 10)).getCode());
		try {
			spinnerCategory.setSelection(categoryToIndex(category));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void persist(Reminder reminder) {

		try {
			Category category = findCategory(reminder.getCategory());
			if (category != null) {
				reminder.setCategory(category);
			} else {
				Controller.instance(getApplicationContext()).addCategory(reminder.getCategory());
				reminder.setCategory(findCategory(reminder.getCategory()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Controller.instance(getApplicationContext()).updateReminder(reminder);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private Category findCategory(Category category) throws Exception {
		List<Category> categories = Controller.instance(getApplicationContext()).listCategories();
		for (Category c : categories) {
			if (c.getName().equals(category.getName()))
				return c;
		}
		return null;
	}

}
