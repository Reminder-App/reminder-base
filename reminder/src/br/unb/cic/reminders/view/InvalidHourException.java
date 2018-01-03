package br.unb.cic.reminders.view;

import br.unb.cic.reminders.model.InvalidFormatException;

public class InvalidHourException extends InvalidFormatException {

	private static final long serialVersionUID = 8341230773647025695L;

	public InvalidHourException(Object o) {
		super("This hour " + o);
	}

}