package br.unb.cic.reminders.model;

import br.unb.cic.framework.persistence.DBTypes;
import br.unb.cic.framework.persistence.annotations.Column;
import br.unb.cic.framework.persistence.annotations.Entity;

/**
 * This class represents a categegory, an useful abstraction for classifying
 * reminders.
 * 
 * @author rbonifacio
 */
@Entity(table = "CATEGORY")
public class Category {
	@Column(column = "PK", type = DBTypes.LONG, primaryKey = true)
	private Long id;

	@Column(column = "NAME", type = DBTypes.TEXT)
	private String name;

	@Column(column = "LOCKED", type = DBTypes.INT)
	private int locked;

	public Category() {
	}

	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
		this.locked = 0;
	}

	public Category(Long id, String name, int locked) {
		this.id = id;
		this.name = name;
		this.locked = locked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.isEmpty() == true)
			throw new InvalidTextException(name);
		else
			this.name = name;
	}

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	@Override
	public String toString() {
		return name;
	}
}
