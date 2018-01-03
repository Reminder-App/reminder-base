package br.unb.cic.reminders.view;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private Calendar time;

	private Spinner spinnerTime;

	private int pHour;
	private int pMinute;

	public TimePickerDialogFragment(Calendar date, Spinner spinnerTime) {
		this.time = date;
		this.spinnerTime = spinnerTime;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int minute = time.get(Calendar.MINUTE);

		// Create a new instance of DatePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDismiss(DialogInterface dialog) {
		time.set(Calendar.MINUTE, pMinute);
		time.set(Calendar.HOUR_OF_DAY, pHour);
		String sTime = Integer.toString(pMinute);
		if (pMinute < 10)
			sTime = "0" + sTime;
		sTime = Integer.toString(pHour) + ":" + sTime;

		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerTime.getAdapter();
		adapter.add(sTime);
		spinnerTime.setSelection(2);

		super.onDismiss(dialog);
	}

	public void onTimeSet(TimePicker view, int hour, int minute) {
		pHour = hour;
		pMinute = minute;
	}

}