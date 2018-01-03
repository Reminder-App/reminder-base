package br.unb.cic.reminders.model;

public class InvalidTextException extends InvalidFormatException {

	private static final long serialVersionUID = 7998188562654167391L;

	public InvalidTextException(Object o) {
		super("This text " + o);
	}

}
