package br.unb.cic.reminders.model;

/**
 * Exception that might be used to report situations where an invalid format was
 * detected. For instance, we throw an instance of this exception when we try to
 * set a date as a string that does not match the expected date format.
 * 
 * @author rbonifacio
 */
public class InvalidFormatException extends RuntimeException {

	private static final long serialVersionUID = 2850584940310533216L;

	/**
	 * InvalidFormatException constructor.
	 * 
	 * @param o
	 *            An object that does not match an expected format.
	 */
	public InvalidFormatException(Object o) {
		super(o + " is in the wrong format");
	}
}
