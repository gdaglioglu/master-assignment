/*
 * RecordNotFoundException
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

/**
 * This is the exception class used when a record cannot be found
 * 
 * @author Eoin Mooney
 */
public class RecordNotFoundException extends Exception {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = 9172845648588845215L;

	/**
	 * Instantiates a new record not found exception.
	 */
	public RecordNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new record not found exception with a specific message.
	 * 
	 * @param s
	 *            the message
	 */
	public RecordNotFoundException(final String s) {
		super(s);
	}
}
