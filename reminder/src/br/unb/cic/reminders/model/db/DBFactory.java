package br.unb.cic.reminders.model.db;

import android.content.Context;

/**
 * An abstract factory class to instantiate DAOs.
 * 
 * @author rbonifacio
 */
public abstract class DBFactory {
	protected Context context;

	public static DBFactory factory(Context context) {
		return new DefaultDBFactory(context);
	}

	/**
	 * DBFactory constructor
	 * 
	 * @param context
	 *            the application context.
	 */
	public DBFactory(Context context) {
		this.context = context;
	}

	/**
	 * Returns an instance of CategoryDAO.
	 * 
	 * @return an instance of CategoryDAO.
	 */
	public abstract CategoryDAO createCategoryDAO();

	/**
	 * Returns an instance of ReminderDAO.
	 * 
	 * @return an instance of ReminderDAO
	 */
	public abstract ReminderDAO createReminderDAO();
}
