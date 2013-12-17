/*
 * RecordNotFoundException
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

// TODO: Auto-generated Javadoc
/**
 * The Class RecordNotFoundException.
 */
public class RecordNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9172845648588845215L;

	/**
	 * Instantiates a new record not found exception.
	 */
	public RecordNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new record not found exception.
	 *
	 * @param s the s
	 */
	public RecordNotFoundException(final String s) {
		super(s);
	}
}
