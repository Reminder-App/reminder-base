package br.unb.cic.reminders.view;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private Calendar date;
	private Spinner spinnerDate;

	private int pYear;
	private int pDay;
	private int pMonth;

	public DatePickerDialogFragment(Calendar date, Spinner spinnerDate) {
		this.date = date;
		this.spinnerDate = spinnerDate;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDismiss(DialogInterface dialog) {
		date.set(pYear, pMonth, pDay);
		String sDate = Integer.toString(pDay) + "-" + Integer.toString(pMonth + 1) + "-" + Integer.toString(pYear);

		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerDate.getAdapter();

		/* DO NOT DELETE THESE COMMENTS! (Alexandre) */
		/* Maybe we need it next Sprint. */
		// if(adapter.getCount() > 2) {
		// adapter.remove(adapter.getItem(2));
		// }
		adapter.add(sDate);
		spinnerDate.setSelection(2);

		super.onDismiss(dialog);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		pYear = year;
		pDay = day;
		pMonth = month;
	}

}