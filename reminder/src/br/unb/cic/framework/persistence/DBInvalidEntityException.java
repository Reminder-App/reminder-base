package br.unb.cic.framework.persistence;

/**
 * Exception that is thrown when an invalid Entity is identified by the
 * persistence framework.
 * 
 * @author rbonifacio
 */
public class DBInvalidEntityException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param e
	 *            the invalid entity
	 */
	public DBInvalidEntityException(Object e) {
		super(e.getClass().getCanonicalName() + "is not a valid entity according to the persistence framework.");
	}
}
