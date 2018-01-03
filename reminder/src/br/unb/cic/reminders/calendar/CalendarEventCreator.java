package br.unb.cic.reminders.calendar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import util.DateFormat;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import br.unb.cic.reminders.model.CalendarNotFoundException;
import br.unb.cic.reminders.model.Reminder;

public class CalendarEventCreator {
	private String userAccount, timeZone, accountName, displayName;
	private long calendarID;
	private boolean haveMainCalendar;
	/*
	 * Columns of the Calendar's table
	 */
	private static final String[] COLUMNS = new String[] { Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME, // 2
			Calendars.OWNER_ACCOUNT, // 3
			Calendars.CALENDAR_TIME_ZONE // 4
	};

	private void initializer() {
		haveMainCalendar = false;
		userAccount = null;
		accountName = null;
		displayName = null;
		timeZone = null;
		calendarID = -1;
	}

	/*
	 * Search for the first gmail account TODO:support multiple google accounts
	 * on the same device
	 */
	private void getUserMainCalendar(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		initializer();

		Cursor cur = null;
		cur = cr.query(Calendars.CONTENT_URI, COLUMNS, null, null, null);
		while (cur.moveToNext()) {
			if (cur.getString(3).contains("@gmail.com")) {
				haveMainCalendar = true;
				calendarID = cur.getLong(0);
				accountName = cur.getString(1);
				displayName = cur.getString(2);
				userAccount = cur.getString(3);
				timeZone = cur.getString(4);
				break;
			}
		}
	}

	/*
	 * Receive a reminder and a context. With those two, create a new entry in
	 * google calendar TODO: correctly input the start time of the event TODO:
	 * We need a better title for the reminder
	 */
	public void addEventCalendar(Reminder reminder, Context ctx) throws CalendarNotFoundException, ParseException {
		long startMilliseconds, endmilliseconds;
		Calendar calEnd;
		startMilliseconds = endmilliseconds = 0;

		getUserMainCalendar(ctx);

		if (haveMainCalendar) {
			ContentResolver cr = ctx.getContentResolver();
			calEnd = Calendar.getInstance();
			Date date = DateFormat.dateFormater(reminder.getDate() + " " + reminder.getHour());
			calEnd.setTime(date);

			startMilliseconds = calEnd.getTimeInMillis() - 100000;
			endmilliseconds = calEnd.getTimeInMillis();

			ContentValues values = new ContentValues();

			values.put(Events.DTSTART, startMilliseconds);
			values.put(Events.DTEND, endmilliseconds);
			values.put(Events.TITLE, reminder.getText());
			values.put(Events.DESCRIPTION, reminder.getCategory().getName() + " - PositivoApp");
			values.put(Events.CALENDAR_ID, calendarID);
			values.put(Events.EVENT_TIMEZONE, timeZone);

			cr.insert(Events.CONTENT_URI, values);
		} else
			throw new CalendarNotFoundException();
	}
}
