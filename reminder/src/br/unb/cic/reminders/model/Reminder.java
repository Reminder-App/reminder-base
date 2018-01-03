package br.unb.cic.reminders.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Patterns;
import br.unb.cic.framework.persistence.DBTypes;
import br.unb.cic.framework.persistence.annotations.Column;
import br.unb.cic.framework.persistence.annotations.Entity;
import br.unb.cic.framework.persistence.annotations.ForeignKey;
import br.unb.cic.reminders.view.InvalidHourException;

/**
 * This class provides basic representation of a reminder.
 * 
 * @author rbonifacio
 */
@Entity(table = "REMINDER")
public class Reminder {

	@Column(column = "PK", primaryKey = true, type = DBTypes.LONG)
	private Long id;

	@Column(column = "TEXT", type = DBTypes.TEXT)
	private String text;

	@Column(column = "DETAILS", type = DBTypes.TEXT)
	private String details;

	@Column(column = "FK_CATEGORY", type = DBTypes.LONG)
	@ForeignKey(mappedBy = "id")
	private Category category;

	@Column(column = "DATE", type = DBTypes.TEXT)
	private String date; // it must be in the YYYY-MM-DD format

	@Column(column = "HOUR", type = DBTypes.TEXT)
	private String hour; // it must be in the HH:mm format

	@Column(column = "PRIORITY", type = DBTypes.INT)
	private Priority priority;

	@Column(column = "DONE", type = DBTypes.INT)
	private boolean done;

	public Reminder() {
	}

	public Reminder(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null || text.trim().equals("")) {
			throw new InvalidTextException(text);
		}
		this.text = text;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		// Details are optional.
		if (details == null || details.trim().equals("")) {
			this.details = null;
		} else {
			this.details = details;
		}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		// We allow reminders to have no date associated.
		if (!(date == null || date.equals("")) && !checkFormat(date, Patterns.DATE_PATTERN)) {
			throw new InvalidDateException(date);
		}
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		// We allow reminders to have no hour associated.
		if (!(hour == null || hour.equals("")) && !checkFormat(hour, Patterns.HOUR_PATTERN)) {
			throw new InvalidHourException(hour);
		}
		this.hour = hour;
	}

	private boolean checkFormat(String date, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(date);
		return m.matches();
	}

	public int getPriority() {
		return priority.getCode();
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public boolean isValid() {
		return (text != null && category != null && date != null && hour != null && priority != null);
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	/* DB getter & setter */
	public int getDone() {
		return done ? 1 : 0;
	}

	public void setDone(int done) {
		this.done = (done == 0 ? false : true);
	}

}
