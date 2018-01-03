package br.unb.cic.framework.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.unb.cic.framework.persistence.annotations.Column;
import br.unb.cic.framework.persistence.annotations.Entity;
import br.unb.cic.framework.persistence.annotations.ForeignKey;
import br.unb.cic.reminders.model.db.DBHelper;
import br.unb.cic.reminders.model.db.DefaultCategoryDAO;

/**
 * A generic DAO class.
 * 
 * @author rbonifacio
 */
public class GenericDAO<T> {

	protected Context context;
	protected SQLiteDatabase db;
	protected DBHelper dbHelper;

	private static final String DATABASE_NAME = "ReminderDB";
	private static final int DATABASE_VERSION = 7;

	/**
	 * Constructor of GenericDAO.
	 * 
	 * @param c
	 *            the application context.
	 */
	public GenericDAO(Context c) {
		context = c;
		dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Save a specific entity into the database.
	 * 
	 * @param entity
	 *            that will be stored.
	 * 
	 * @return the Id of the entity in the database.
	 */
	protected Long persist(T entity) throws DBInvalidEntityException, DBException {
		try {
			db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();

			if (!entity.getClass().isAnnotationPresent(Entity.class)) {
				throw new DBInvalidEntityException(entity);
			}

			String tableName = entity.getClass().getAnnotation(Entity.class).table();

			boolean update = false;
			String updateWhereClause = null;

			for (Field f : entity.getClass().getDeclaredFields()) {
				if (f.isAnnotationPresent(Column.class)) {
					String column = f.getAnnotation(Column.class).column();
					boolean pk = f.getAnnotation(Column.class).primaryKey();

					String methodName = getMethod(f.getName());
					Class args[] = {};
					Method m = entity.getClass().getDeclaredMethod(methodName, args);

					Object value = m.invoke(entity, args);

					// in the cases where the field is a related object, mapped
					// by a FK, a specific
					// behavior is necessary, since we need to get the value of
					// the
					// primary key of the related object.
					if (f.isAnnotationPresent(ForeignKey.class)) {
						String fk = f.getAnnotation(ForeignKey.class).mappedBy();
						methodName = getMethod(fk);

						m = value.getClass().getDeclaredMethod(methodName, args);

						value = m.invoke(value, args);
					}
					if (value != null) {
						values.put(column, value.toString());
					} else {
						values.putNull(column);
					}
					if (pk && value != null) {
						update = true;
						updateWhereClause = column + " = " + value.toString();
					}
				}
			}
			Long id = null;
			db.beginTransaction();
			if (!update) {
				id = db.insert(tableName, null, values);
			} else {
				db.update(tableName, values, updateWhereClause, null);
			}
			db.setTransactionSuccessful();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(DefaultCategoryDAO.class.getCanonicalName(), e.getLocalizedMessage());
			throw new DBException();
		} finally {
			if (db.inTransaction()) {
				db.endTransaction();
			}
			db.close();
			dbHelper.close();
		}
	}

	public T cursorToEntity(Cursor cursor, Class<T> clasz) throws DBInvalidEntityException, Exception {
		if (!clasz.isAnnotationPresent(Entity.class)) {
			throw new DBInvalidEntityException(clasz);
		}

		T entity = clasz.newInstance();

		for (Field f : declaredFields(clasz)) {
			Class args[] = { f.getType() };

			if (!f.isAnnotationPresent(ForeignKey.class)) {
				Method m = entity.getClass().getDeclaredMethod(setMethod(f.getName()), args);

				m.invoke(entity, fieldValueFromCursor(cursor, f));
			} else {

			}
		}

		return entity;
	}

	private Object fieldValueFromCursor(Cursor cursor, Field f) {
		String column = f.getAnnotation(Column.class).column();
		DBTypes type = f.getAnnotation(Column.class).type();

		switch (type) {
		case INT:
			return cursor.getInt(cursor.getColumnIndex(column));
		case LONG:
			return cursor.getLong(cursor.getColumnIndex(column));
		case TEXT:
			return cursor.getString(cursor.getColumnIndex(column));
		default:
			return null;
		}
	}

	private List<Field> declaredFields(Class<T> clasz) {
		List<Field> fields = new ArrayList<Field>();
		for (Field f : clasz.getDeclaredFields()) {
			if (f.isAnnotationPresent(Column.class)) {
				fields.add(f);
			}
		}
		return fields;
	}

	private String setMethod(String field) {
		return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	private String getMethod(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
}
