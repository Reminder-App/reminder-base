package br.unb.cic.reminders.model.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.framework.persistence.DBInvalidEntityException;
import br.unb.cic.framework.persistence.GenericDAO;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.Priority;
import br.unb.cic.reminders.model.Reminder;

/**
 * A default implementation of the @see {@link ReminderDAO} interface.
 * 
 * @author positivo
 *
 */
public class DefaultReminderDAO extends GenericDAO<Reminder> implements ReminderDAO {

	public DefaultReminderDAO(Context c) {
		super(c);
	}

	/**
	 * @see ReminderDAO#saveReminder(Reminder reminder)
	 */
	public Long saveReminder(Reminder r) throws DBException {
		try {
			return persist(r);
		} catch (DBInvalidEntityException e) {
			throw new DBException();
		}
	}

	/**
	 * @see ReminderDAO#saveReminder(Reminder)
	 */
	public List<Reminder> listReminders() throws DBException {
		try {
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(DBConstants.SELECT_REMINDERS, null);

			return remindersFromCursor(cursor);
		} catch (Exception e) {
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		} finally {
			db.close();
			dbHelper.close();
		}
	}

	/**
	 * @see ReminderDAO#deleteReminder(Reminder reminder)
	 */
	public List<Reminder> listRemindersByCategory(Category category) throws DBException {
		try {
			db = dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(DBConstants.SELECT_REMINDERS_BY_CATEGORY,
					new String[] { category.getId().toString() });

			return remindersFromCursor(cursor);
		} catch (Exception e) {
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		} finally {
			db.close();
			dbHelper.close();
		}
	}

	/**
	 * TODO: I guess that this note does not make sense any more.
	 * 
	 * @see ReminderDAO#updateReminder(Reminder reminder) As it was asked on
	 *      iceScrum, the update use the persist method after removing the given
	 *      reminder. If this was what was asked then it is done, otherwise the
	 *      persist method can be upgraded to support the update functionality
	 *      (if it isn't already implemented. I guess it isn't) later by
	 *      refactoring it.
	 */
	public void updateReminder(Reminder reminder) throws DBException {
		try {
			persist(reminder);
		} catch (DBInvalidEntityException e) {
			throw new DBException();
		} finally {
			db.close();
			dbHelper.close();
		}
	}

	/**
	 * @see ReminderDAO#deleteReminder(Reminder reminder)
	 */
	public void deleteReminder(Reminder reminder) throws DBException {
		try {
			db = dbHelper.getWritableDatabase();
			db.delete(DBConstants.REMINDER_TABLE, DBConstants.REMINDER_PK_COLUMN + "=" + reminder.getId(), null);
		} catch (SQLiteException e) {
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		} finally {
			db.close();
			dbHelper.close();
		}
	}

	public void persistReminder(Reminder reminder) throws DBException {
		try {
			persist(reminder);
		} catch (DBInvalidEntityException e) {
			throw new DBException();
		} finally {
			db.close();
			dbHelper.close();
		}
	}

	/*
	 * Transform a cursor into a Reminder
	 */
	private Reminder cursorToReminder(Cursor cursor) throws DBException {
		Long pk = cursor.getLong(cursor.getColumnIndex(DBConstants.REMINDER_PK_COLUMN));
		String text = cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_TEXT_COLUMN));
		String details = cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_DETAILS_COLUMN));
		String date = cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_DATE_COLUMN));
		String hour = cursor.getString(cursor.getColumnIndex(DBConstants.REMINDER_HOUR_COLUMN));
		int priority = cursor.getInt(cursor.getColumnIndex(DBConstants.REMINDER_PRIORITY_COLUMN));
		int done = cursor.getInt(cursor.getColumnIndex(DBConstants.REMINDER_DONE_COLUMN));
		Long categoryId = cursor.getLong(cursor.getColumnIndex(DBConstants.REMINDER_FK_CATEGORY_COLUMN));

		Category category = DBFactory.factory(context).createCategoryDAO().findCategoryById(categoryId);

		Reminder reminder = new Reminder();

		reminder.setText(text);
		reminder.setDetails(details);
		reminder.setId(pk);
		reminder.setDate(date);
		reminder.setHour(hour);
		reminder.setPriority(Priority.fromCode(priority));
		reminder.setDone(done);
		reminder.setCategory(category);

		return reminder;
	}

	/*
	 * Iterates over a cursor in order to build a list of reminders.
	 */
	private List<Reminder> remindersFromCursor(Cursor cursor) throws DBException {
		List<Reminder> reminders = new ArrayList<Reminder>();

		if (cursor.moveToFirst()) {
			do {
				Reminder reminder = cursorToReminder(cursor);
				reminders.add(reminder);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return reminders;
	}

}
