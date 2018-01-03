package br.unb.cic.reminders.model.db;

import java.util.List;

import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Reminder;

/**
 * A Data Access Object for handling the persistence mechanism of reminders.
 * 
 * @author rbonifacio
 */
public interface ReminderDAO {

	/**
	 * Save a reminder in the reminders database.
	 * 
	 * @param reminder
	 *            - the reminder that will be stored.
	 * @return the reminder id.
	 */
	public Long saveReminder(Reminder r) throws DBException;

	/**
	 * List all reminders of the Reminders database.
	 * 
	 * @return list of registered reminders.
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public List<Reminder> listReminders() throws DBException;

	/**
	 * List all reminders of the Reminders database, but using as criteria a
	 * given category
	 * 
	 * @param category
	 *            the category used as the query criteria
	 * @return list of registered reminders.
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public List<Reminder> listRemindersByCategory(Category category) throws DBException;

	/**
	 * Update a given reminder from the database
	 * 
	 * @param reminder
	 *            the reminder that will be updated from the database. it
	 *            contains all fields with the new values and the id of the
	 *            reminder to be updated.
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public void updateReminder(Reminder reminder) throws DBException;

	/**
	 * Delete a given reminder from the database
	 * 
	 * @param reminder
	 *            the reminder that will be dropped from the database
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public void deleteReminder(Reminder reminder) throws DBException;

	/**
	 * Persist a given reminder from the database
	 * 
	 * @param reminder
	 *            the reminder that will be persisted in the database
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public void persistReminder(Reminder reminder) throws DBException;
}
