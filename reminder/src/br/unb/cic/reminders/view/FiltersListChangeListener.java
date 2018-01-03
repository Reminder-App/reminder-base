package br.unb.cic.reminders.view;

import br.unb.cic.reminders.controller.ReminderFilter;

/**
 * Interface used to receive a category list changing notificaton.
 * 
 * @author rbonifacio
 */
public interface FiltersListChangeListener {

	public void onSelectedFilterChanged(ReminderFilter filter);

}