package br.unb.cic.reminders.model.db;

import android.content.Context;

/**
 * A default implementation of the abstract class DBFactory.
 * 
 * @author rbonifacio
 */
public class DefaultDBFactory extends DBFactory {

	public DefaultDBFactory(Context context) {
		super(context);
	}

	@Override
	public CategoryDAO createCategoryDAO() {
		return new DefaultCategoryDAO(context);
	}

	@Override
	public ReminderDAO createReminderDAO() {
		return new DefaultReminderDAO(context);
	}
}
