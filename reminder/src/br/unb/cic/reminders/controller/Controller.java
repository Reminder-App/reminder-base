package br.unb.cic.reminders.controller;

import java.util.List;

import android.content.Context;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Reminder;
import br.unb.cic.reminders.model.db.DBFactory;

/**
 * Class that is responsible for handling user inputs and processing the
 * business rules of the system. Nevertheless, it is important to note that the
 * business rules in this system are really, really simple. For this reason, a
 * single controller might be enough.
 * 
 * @author rbonifacio
 *
 */
public class Controller {

	private Context context;

	private static Controller instance;

	/*
	 * Private constructor, according to the singleton design pattern.
	 */
	private Controller(Context c) {
		this.context = c;
	}

	/**
	 * The single interface to obtain an instance of the Controller class,
	 * according to the singleton pattern.
	 */
	public static final Controller instance(Context c) {
		if (instance == null) {
			instance = new Controller(c);
		}

		return instance;
	}

	/**
	 * Return the list of categories
	 */
	public List<Category> listCategories() throws Exception {
		try {
			return DBFactory.factory(context).createCategoryDAO().listCategories();
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	public Category findCategory(Long id) throws Exception {
		return DBFactory.factory(context).createCategoryDAO().findCategoryById(id);
	}

	public Category findCategory(String name) throws Exception {
		return DBFactory.factory(context).createCategoryDAO().findCategory(name);
	}

	/**
	 * Return all reminders of a given category
	 * 
	 * @param category
	 *            the criteria used to filter the reminders.
	 */
	public List<Reminder> listRemindersByCategory(Category category) throws Exception {
		try {
			return DBFactory.factory(context).createReminderDAO().listRemindersByCategory(category);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Return all reminders
	 */
	public List<Reminder> listReminders() throws Exception {
		try {
			return DBFactory.factory(context).createReminderDAO().listReminders();
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Add a new category to the list of existing categories
	 */
	public void addCategory(Category category) throws Exception {
		try {
			DBFactory.factory(context).createCategoryDAO().saveCategory(category);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Add a new reminder to the list of existing reminders
	 */
	public void addReminder(Reminder reminder) throws DBException {
		try {
			DBFactory.factory(context).createReminderDAO().saveReminder(reminder);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Update a given category given a Category object with an id
	 */
	public void updateCategory(Category category) throws DBException {
		try {
			DBFactory.factory(context).createCategoryDAO().updateCategory(category);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Add a new reminder to the list of existing reminders
	 */
	public void updateReminder(Reminder reminder) throws DBException {
		try {
			DBFactory.factory(context).createReminderDAO().updateReminder(reminder);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Remove a category given a Category object with an id
	 */
	public void deleteCategory(Category category) throws DBException {
		try {
			DBFactory.factory(context).createCategoryDAO().deleteCategory(category);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Remove a reminder given a Reminder object
	 */
	public void deleteReminder(Reminder reminder) throws DBException {
		try {
			DBFactory.factory(context).createReminderDAO().deleteReminder(reminder);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/**
	 * Persist a reminder given a Reminder object
	 */
	public void persistReminder(Reminder reminder) throws DBException {
		try {
			DBFactory.factory(context).createReminderDAO().persistReminder(reminder);
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}

	/*
	 * throws a RunTimeException
	 * 
	 * TODO: remove this method after all other public methods would have been
	 * implemented.
	 */
	private void notImplementedYet() {
		throw new RuntimeException("not implemented yet");
	}

	public Category getCategory(String name) throws DBException {
		try {
			List<Category> categories = DBFactory.factory(context).createCategoryDAO().listCategories();
			for (Category c : categories) {
				if (c.getName().equals(name))
					return c;
			}
			return null;
		} catch (DBException e) {
			// TODO: we need to define what to do in these situations.
			throw e;
		}
	}
}
