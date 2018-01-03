package br.unb.cic.reminders.view;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.unb.cic.reminders.calendar.CalendarEventCreator;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.model.CalendarNotFoundException;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.InvalidDateException;
import br.unb.cic.reminders.model.InvalidTextException;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders.model.Reminder;
import br.unb.cic.reminders2.R;

public abstract class ReminderActivity extends Activity {

	// private Category selectedCategory;
	private Priority selectedPriority;
	private CalendarEventCreator creator;
	protected Reminder reminder;
	protected Calendar date, time;

	protected EditText edtReminder, edtDetails, edtDate, edtTime;
	protected Spinner spinnerDate, spinnerTime, spinnerPriority, spinnerCategory;
	private Button btnSave, btnCancel;
	private CheckBox cbCalendar;

	// TODO: Rename XML from reminder_add to reminder_form.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_add);

		if (reminder == null)
			reminder = new Reminder();
		initializeFields();
		initializeListeners();
		initializeValues();
	}

	private void initializeFields() {
		btnSave = (Button) findViewById(R.id.btnSave);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		edtReminder = (EditText) findViewById(R.id.edtReminder);
		edtDetails = (EditText) findViewById(R.id.edtDetails);
		spinnerDate = getSpinnerDate();
		spinnerTime = getSpinnerTime();
		// edtDate = (EditText) findViewById(R.id.selectedDate);
		// edtTime = (EditText) findViewById(R.id.selectedTime);
		spinnerPriority = getSpinnerPriority();
		cbCalendar = (CheckBox) findViewById(R.id.cbCalendar);
		try {
			spinnerCategory = getSpinnerCategory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initializeListeners() {
		addListenerToBtnSave();
		addListenerToBtnCancel();
		addListenerToSpinnerDate();
		addListenerToSpinnerTime();
		addListenerToSpinnerPriority();
		addListenerToSpinnerCategory();
	}

	protected abstract void initializeValues();

	private void addListenerToBtnSave() {
		btnSave.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				try {
					createReminder();
					persist(reminder);
					finish();
				} catch (Exception e) {
					Log.e("ReminderActivity", e.getMessage());
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

	private void addListenerToSpinnerDate() {

		/* DO NOT DELETE THESE COMMENTS! (Alexandre) */
		/*
		 * They work and give a different behaviour to the spinner! Maybe we
		 * need this for the next sprint, depending on the Designer.
		 */
		// spinnerDate.setOnTouchListener(new View.OnTouchListener() {
		// @SuppressWarnings("unchecked")
		// public boolean onTouch(View v, MotionEvent event) {
		// if(spinnerDate.getAdapter().getCount() > 2) {
		// spinnerDate.setTag((String) spinnerDate.getSelectedItem());
		// ArrayAdapter<String> adapter = (ArrayAdapter<String>)
		// spinnerDate.getAdapter();
		// adapter.remove((String) spinnerDate.getSelectedItem());
		// spinnerDate.setSelection(0);
		// }
		// return false;
		// }
		// });
		//
		// spinnerDate.setOnKeyListener(new View.OnKeyListener() {
		// @SuppressWarnings("unchecked")
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// if(spinnerDate.getAdapter().getCount() > 2) {
		// spinnerDate.setTag((String) spinnerDate.getSelectedItem());
		// ArrayAdapter<String> adapter = (ArrayAdapter<String>)
		// spinnerDate.getAdapter();
		// adapter.remove((String) spinnerDate.getSelectedItem());
		// spinnerDate.setSelection(0);
		// }
		// return false;
		// }
		// });

		spinnerDate.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				spinnerDate = getSpinnerDate();
				return false;
			}
		});

		spinnerDate.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				spinnerDate = getSpinnerDate();
				return false;
			}
		});

		spinnerDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				switch (pos) {
				case 0:
					date = null;
					break;
				case 1:
					if (date == null)
						date = Calendar.getInstance();
					DialogFragment newFragment = new DatePickerDialogFragment(date, spinnerDate);
					newFragment.show(getFragmentManager(), "datePicker");
					break;
				default:
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// Well, do nothing...
			}

		});
	}

	private void addListenerToSpinnerTime() {

		spinnerTime.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				spinnerTime = getSpinnerTime();
				return false;
			}
		});

		spinnerTime.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				spinnerTime = getSpinnerTime();
				return false;
			}
		});

		spinnerTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				switch (pos) {
				case 0:
					time = null;
					break;
				case 1:
					if (time == null)
						time = Calendar.getInstance();
					DialogFragment newFragment = new TimePickerDialogFragment(time, spinnerTime);
					newFragment.show(getFragmentManager(), "timePicker");
					break;
				default:
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// Well, do nothing...
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

	private void addListenerToSpinnerCategory() {
		spinnerCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// if user clicked "+ Category"
				if (pos == (spinnerCategory.getCount() - 1)) {
					DialogFragment newFragment = AddCategoryDialogFragment.newInstance(spinnerCategory);
					newFragment.show(getFragmentManager(), "" + R.string.dialog_addcategory_title);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
				// well... do nothing
			}
		});
	}

	/**
	 * Just an auxiliary method to create reminders from the UI data.
	 * 
	 * @return a new reminder with the ui values.
	 **/
	private void createReminder() {
		try {
			reminder.setText(edtReminder.getText().toString());
			reminder.setDetails(edtDetails.getText().toString());
			reminder.setDate(dateToString());
			reminder.setHour(timeToString());
			reminder.setPriority(selectedPriority);
			reminder.setCategory((Category) spinnerCategory.getSelectedItem());
			if (cbCalendar.isChecked()) {
				creator = new CalendarEventCreator();
				creator.addEventCalendar(reminder, getApplicationContext());
			}
		} catch (InvalidTextException e) {
			// This catch is handling both Reminder's Text and Details.
			Toast.makeText(getApplicationContext(), "Texto inválido.", Toast.LENGTH_SHORT).show();
		} catch (InvalidDateException e) {
			Toast.makeText(getApplicationContext(), "Data inválida.", Toast.LENGTH_SHORT).show();
		} catch (InvalidHourException e) {
			Toast.makeText(getApplicationContext(), "Hora inválida.", Toast.LENGTH_SHORT).show();
		} catch (CalendarNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Não foi encontrado um Google Calendar", Toast.LENGTH_SHORT).show();
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Transformação incorreta de data", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// this is an exceptional case that needs to be studied
			Toast.makeText(getApplicationContext(), "Erro grave.", Toast.LENGTH_SHORT).show();
		}
	}

	private String dateToString() {
		if (date == null)
			return null;
		String sDate;
		sDate = Integer.toString(date.get(Calendar.MONTH) + 1);
		if (date.get(Calendar.MONTH) + 1 < 10)
			sDate = "0" + sDate;
		sDate = Integer.toString(date.get(Calendar.DAY_OF_MONTH)) + "-" + sDate;
		if (date.get(Calendar.DAY_OF_MONTH) < 10)
			sDate = "0" + sDate;
		sDate += "-" + Integer.toString(date.get(Calendar.YEAR));
		return sDate;
	}

	private String timeToString() {
		if (time == null)
			return null;
		String sTime;
		sTime = Integer.toString(time.get(Calendar.MINUTE));
		if (time.get(Calendar.MINUTE) < 10)
			sTime = "0" + sTime;
		sTime = Integer.toString(time.get(Calendar.HOUR_OF_DAY)) + ":" + sTime;
		if (time.get(Calendar.HOUR_OF_DAY) < 10)
			sTime = "0" + sTime;
		return sTime;
	}

	protected void updateDateFromString(String sDate) {
		if (sDate == null || sDate.equals("")) {
			date = null;
			return;
		}
		char sDay[] = { sDate.charAt(0), sDate.charAt(1) };
		int day = Integer.parseInt(new String(sDay), 10);
		char sMonth[] = { sDate.charAt(3), sDate.charAt(4) };
		int month = Integer.parseInt(new String(sMonth), 10);
		char sYear[] = { sDate.charAt(6), sDate.charAt(7), sDate.charAt(8), sDate.charAt(9) };
		int year = Integer.parseInt(new String(sYear), 10);
		if (date == null)
			date = Calendar.getInstance();
		date.set(year, month - 1, day);
	}

	protected void updateTimeFromString(String sTime) {
		if (sTime == null || sTime.equals("")) {
			time = null;
			return;
		}
		char sHour[] = { sTime.charAt(0), sTime.charAt(1) };
		int hour = Integer.parseInt(new String(sHour), 10);
		char sMinute[] = { sTime.charAt(3), sTime.charAt(4) };
		int minute = Integer.parseInt(new String(sMinute), 10);
		if (time == null)
			time = Calendar.getInstance();
		time.set(Calendar.MINUTE, minute);
		time.set(Calendar.HOUR_OF_DAY, hour);
	}

	private Spinner getSpinnerCategory() throws Exception {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCategories);

		SpinnerAdapterGenerator<Category> adapterCategoryGenerator = new SpinnerAdapterGenerator<Category>();

		List<Category> categories = getCategories();

		ArrayAdapter<Category> adapter = adapterCategoryGenerator.getSpinnerAdapter(categories, this);

		// We do not persist this! It's just to show the add category dialog.
		Category temp = new Category();
		temp.setName("+ Categoria");
		adapter.add(temp);

		spinner.setAdapter(adapter);

		return spinner;
	}

	protected List<Category> getCategories() throws Exception {
		return Controller.instance(getApplicationContext()).listCategories();
	}

	/*
	 * Utility method which inserts a new date into the last position of the
	 * spinner.
	 */
	@SuppressWarnings("unchecked")
	protected void updateSpinnerDateHour(Spinner spinner, String dateOrHour) {
		if (dateOrHour == null)
			return;

		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();

		int count = adapter.getCount();
		if (count > 2) {
			for (int i = 2; i < count; ++i)
				// Date and Time Spinners can have max of 3 elements.
				adapter.remove(adapter.getItem(i));
		}
		adapter.add(dateOrHour);
		spinner.setSelection(2);
	}

	private Spinner getSpinnerDate() {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerDate);

		SpinnerAdapterGenerator<String> adapterDateGenerator = new SpinnerAdapterGenerator<String>();

		List<String> items = new ArrayList<String>();
		// TODO: Move these to XML.
		items.add("Sem data");
		items.add("+ Selecionar");

		spinner.setAdapter(adapterDateGenerator.getSpinnerAdapter(items, this));

		return spinner;
	}

	private Spinner getSpinnerTime() {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerTime);

		SpinnerAdapterGenerator<String> adapterTimeGenerator = new SpinnerAdapterGenerator<String>();

		List<String> items = new ArrayList<String>();
		// TODO: Move these to XML.
		items.add("Sem horário");
		items.add("+ Selecionar");

		spinner.setAdapter(adapterTimeGenerator.getSpinnerAdapter(items, this));

		return spinner;
	}

	protected Spinner getSpinnerCategory(List<Category> categories) throws Exception {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCategories);

		SpinnerAdapterGenerator<Category> adapterCategoryGenerator = new SpinnerAdapterGenerator<Category>();

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

	protected abstract void persist(Reminder reminder);

}
