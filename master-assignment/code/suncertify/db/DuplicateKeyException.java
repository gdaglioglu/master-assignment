package suncertify.db;

/**
 * Exception thrown when an attempting to create a new record with a key that
 * already exists.
 * 
 * @author Gokhan Daglioglu
 */
public class DuplicateKeyException extends Exception {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = -6689165809485807888L;

	/**
	 * Default zero argument constructor.
	 */
	public DuplicateKeyException() {
	}

	/**
	 * Constructs a DuplicateKeyException with the specified detail message.
	 * 
	 * @param message
	 *            the exception detail.
	 */
	public DuplicateKeyException(String message) {
		super(message);
	}
}