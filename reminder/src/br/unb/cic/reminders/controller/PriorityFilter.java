package br.unb.cic.reminders.controller;

import android.content.Context;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders.model.Reminder;

public class PriorityFilter extends ReminderFilter {

	private Priority priority;

	public PriorityFilter(Priority priority, Context context) {
		super(context);
		this.priority = priority;
	}

	@Override
	protected boolean selectReminder(Reminder r) {
		return r.getPriority() == priority.getCode();
	}

	@Override
	public String getName() {
		return priority.toString();
	}

}
