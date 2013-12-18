/*
 * DuplicateKeyException
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

/**
 * This is the exception class used when a duplicate key is found.
 * 
 * @author Eoin Mooney
 */
public class DuplicateKeyException extends Exception {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = -6689165809485807888L;

	/**
	 * Instantiates a new duplicate key exception.
	 */
	public DuplicateKeyException() {
		super();
	}

	/**
	 * Instantiates a new duplicate key exception with a specific message.
	 * 
	 * @param s
	 *            the message
	 */
	public DuplicateKeyException(final String s) {
		super(s);
	}
}
