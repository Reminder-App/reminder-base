package br.unb.cic.reminders.controller;

import android.content.Context;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Reminder;

public class CategoryFilter extends ReminderFilter {

	private Category category;

	public CategoryFilter(Category category, Context context) {
		super(context);
		this.category = category;
	}

	@Override
	protected boolean selectReminder(Reminder r) {
		return r.getCategory().getName().equals(category.getName());
	}

	public Category getCategory() {
		return category;
	}

	@Override
	public String getName() {
		return category.getName();
	}

}
