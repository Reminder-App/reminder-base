package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {
	/*
	 * Static function that formats the database readable date into an object of
	 * type Date
	 */
	public static Date dateFormater(String dateUnformated) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
		return sdf.parse(dateUnformated);
	}
	
}
