package br.unb.cic.reminders.view;

import java.util.Arrays;
import java.util.List;

import util.Mask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.InvalidDateException;
import br.unb.cic.reminders.model.InvalidFormatException;
import br.unb.cic.reminders.model.InvalidTextException;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders.model.Reminder;
import br.unb.cic.reminders2.R;

/**
 * Activity used to add reminders (local or from external applications).
 * 
 * If an external app wants to add a reminder, it must pass all the
 * corresponding reminders' attributes to the intent which starts this activity.
 * They are: "text", "category", "date" and "hour".
 */

public class ReminderAddActivity extends Activity {

	private EditText edtReminder, edtDetails, edtDate, edtHour; // ,
																// edtPriority,
																// edtCategory;

	private Category selectedCategory;

	private Priority selectedPriority;

	private Spinner spinnerPriority, spinnerCategory;

	private Button btnSave, btnCancel;

	private boolean editingReminder;
	private Long previewReminderId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_add);

		/*
		 * Checks whether reminder's data exists in the intent, that is, verify
		 * if this activity was started by an external app.
		 */
		Reminder existingReminder = getReminderFromIntent();

		if (existingReminder == null) {
			// Used to get the informations of the Reminder being edited:
			editingReminder = true;
			Reminder editReminder = getExistingReminder();
			initialize(editReminder);
		} else {
			/*
			 * Initialize the view, optionally filling the fields with content
			 * sent by another app. This is the ONLY purpose of the object
			 * "existingReminder".
			 */
			editingReminder = false;
			initialize(existingReminder);
		}

		configureActionListener();

	}

	private void configureActionListener() {
		addListenerToBtnSave();
		addListenerToBtnCancel();
		addListenerToSpinnerPriority();
		addListenerToSpinnerCategory();
	}

	private void addListenerToSpinnerCategory() {
		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// get the category from the spinner
				selectedCategory = (Category) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// well... do nothing
			}
		});
	}

	private void addListenerToSpinnerPriority() {
		spinnerPriority.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				selectedPriority = (Priority) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// well... do nothing
			}
		});
	}

	private void addListenerToBtnSave() {
		btnSave.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				try {
					Reminder reminder = createReminder();
					if (editingReminder) {
						reminder.setId(previewReminderId);
						Controller.instance(getApplicationContext()).updateReminder(reminder);
					} else {
						Controller.instance(getApplicationContext()).addReminder(reminder);
					}
					finish(); // Kills the activity
				} catch (Exception e) {
					Log.e("ReminderAddActivity", e.getMessage());
					e.printStackTrace();
				}
			}

		});
	}

	private void addListenerToBtnCancel() {
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/*
	 * Just an auxiliary method to create reminders from the UI data.
	 * 
	 * @return a new reminder.
	 */
	private Reminder createReminder() {
		/*
		 * Since view's not passing all the information yet (priority ...), we
		 * instantiate a reminder initially passing only "text" and "date".
		 */
		Reminder reminder = new Reminder();
		try {
			reminder.setText(edtReminder.getText().toString());
			reminder.setDetails(edtDetails.getText().toString());
			reminder.setDate(edtDate.getText().toString());
			reminder.setHour(edtHour.getText().toString());
			reminder.setCategory(selectedCategory);
			reminder.setPriority(selectedPriority);
		} catch (InvalidTextException e) {
			Toast.makeText(getApplicationContext(), "Texto invalido.", Toast.LENGTH_SHORT).show();
		} catch (InvalidDateException e) {
			Toast.makeText(getApplicationContext(), "Data invalida.", Toast.LENGTH_SHORT).show();
		} catch (InvalidHourException e) {
			Toast.makeText(getApplicationContext(), "Hora invalida.", Toast.LENGTH_SHORT).show();
		}
		return reminder;
	}

	/*
	 * This method is used to verify if it is an editing call and if it is gets
	 * the information of the reminder wanted to be updated and its id that will
	 * be used latter to update the Reminder from the Intent.
	 */
	private Reminder getExistingReminder() {
		Reminder reminder = null;

		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		// Gets the information from the intent if it is an edit call
		if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
			previewReminderId = intent.getLongExtra("id", 0);
			String text = intent.getStringExtra("text");
			String categoryName = intent.getStringExtra("category_name");
			String categoryId = intent.getStringExtra("category_id");
			String date = intent.getStringExtra("date");
			String hour = intent.getStringExtra("hour");
			String priority = intent.getStringExtra("priority");

			reminder = new Reminder();
			reminder.setText(text);
			reminder.setPriority(Priority.fromCode(Integer.parseInt(priority, 10)));
			Category category = new Category();
			category.setName(categoryName);
			category.setId(Long.parseLong(categoryId));
			reminder.setCategory(category);
			reminder.setDate(date);
			reminder.setHour(hour);
			reminder.setId(previewReminderId);
		}

		return reminder;
	}

	/*
	 * Integration code (with another apps).
	 * 
	 * The code below handles incoming requests from external apps which wants
	 * to add a reminder (eg.: Users may want to add a reminder for a TV program
	 * which they're seeing in GuiaTV app).
	 */
	private Reminder getReminderFromIntent() {
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		/*
		 * External apps which wants to share content with "Lembretes" must
		 * follow the protocol described by the "if" below, that is, they must
		 * instantiate an intent and set:
		 * 
		 * ACTION as "br.com.positivo.reminders.ADD_REMINDER"; MIME as
		 * "text/plain";
		 * 
		 * and pass the contents through "putExtra" in the following fields:
		 * "text", "details", "category", "date", "hour" and "priority".
		 */
		if ("br.com.positivo.reminders.ADD_REMINDER".equals(action) && "text/plain".equals(type)) {
			try {
				String text = intent.getStringExtra("text");
				String details = intent.getStringExtra("details");
				String category = intent.getStringExtra("category");
				String date = intent.getStringExtra("date");
				String hour = intent.getStringExtra("hour");
				String priority = intent.getStringExtra("priority");

				/*
				 * Code below just validates the data supplied, since validation
				 * code is implemented in setter methods.
				 */

				// Would we better apply validations here?
				Reminder reminder = new Reminder();
				reminder.setText(text);
				reminder.setDetails(details);

				Category auxCategory = new Category();
				auxCategory.setName(category);
				reminder.setCategory(auxCategory);

				reminder.setDate(date);
				reminder.setHour(hour);

				/*
				 * Upon here, all validations were applied and we have a valid
				 * reminder. So, we return it.
				 */
				return reminder;
			} catch (InvalidFormatException e) {
				/*
				 * What would be better? 1. Return user to the calling activity.
				 * 2. Show an empty reminder's add form (implemented now).
				 */
			}
		}
		/*
		 * If this activity was not started from an external app or a valid
		 * reminder could not be derived, we simply return "null".
		 */
		return null;
	}

	/*
	 * Initializes the view.
	 * 
	 * This method just initializes the form for adding a new reminder. If a
	 * reminder object is supplied, "initialize" pre-populates the form with
	 * that content.
	 */
	private void initialize(Reminder reminder) {
		try {
			edtReminder = (EditText) findViewById(R.id.edtReminder);

			edtDetails = (EditText) findViewById(R.id.edtDetails);

			// edtDate = (EditText) findViewById(R.id.edtDate);
			// edtDate.addTextChangedListener(Mask.insert("##-##-####",
			// edtDate));

			// edtHour = (EditText) findViewById(R.id.edtHour);
			// edtHour.addTextChangedListener(Mask.insert("##:##", edtHour));

			spinnerPriority = getSpinnerPriority();

			spinnerCategory = getSpinnerCategory();

			if (reminder != null) {
				updateFieldsFromReminder(reminder);
			}

			btnSave = (Button) findViewById(R.id.btnSave);
			btnCancel = (Button) findViewById(R.id.btnCancel);
		} catch (Exception e) {
			// TODO: we really need to think about our exception handling
			// approach
			e.printStackTrace();
		}
	}

	private void updateFieldsFromReminder(Reminder reminder) throws Exception {
		edtReminder.setText(reminder.getText());
		edtDetails.setText(reminder.getDetails());
		edtDate.setText(reminder.getDate());
		edtHour.setText(reminder.getHour());
		spinnerPriority.setSelection(reminder.getPriority());
		spinnerCategory.setSelection(categoryToIndex(reminder.getCategory()));
	}

	private Spinner getSpinnerCategory() throws Exception {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCategories);

		SpinnerAdapterGenerator<Category> adapterCategoryGenerator = new SpinnerAdapterGenerator<Category>();

		List<Category> categories = Controller.instance(getApplicationContext()).listCategories();

		spinner.setAdapter(adapterCategoryGenerator.getSpinnerAdapter(categories, this));

		return spinner;
	}

	private Spinner getSpinnerPriority() {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerPriorities);

		SpinnerAdapterGenerator<Priority> adapterPriorityGenerator = new SpinnerAdapterGenerator<Priority>();

		List<Priority> priorityValues = Arrays.asList(Priority.values());

		ArrayAdapter<Priority> priorityArrayAdapter = adapterPriorityGenerator.getSpinnerAdapter(priorityValues, this);

		spinner.setAdapter(priorityArrayAdapter);

		spinner.setSelection(Priority.NORMAL.getCode());

		return spinner;
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
