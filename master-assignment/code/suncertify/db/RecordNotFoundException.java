package suncertify.db;

/**
 * Exception thrown when a specified record cannot be found. When an attempt to
 * find a particular record and the record does not exist or is marked as
 * deleted then a RecordNotFoundException is thrown.
 * 
 * @author Gokhan Daglioglu
 */
public class RecordNotFoundException extends Exception {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 9172845648588845215L;

	/**
	 * Default zero argument constructor.
	 */
	public RecordNotFoundException() {
	}

	/**
	 * Constructs a <code>RecordNotFoundException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the exception detail.
	 */
	public RecordNotFoundException(String message) {
		super(message);
	}
}