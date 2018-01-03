package br.unb.cic.reminders.controller;

import android.content.Context;
import br.unb.cic.reminders.model.Reminder;

public class AllRemindersFilter extends ReminderFilter {

	private final String name = "Todos";

	public AllRemindersFilter(Context context) {
		super(context);
	}

	@Override
	protected boolean selectReminder(Reminder r) {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

}
