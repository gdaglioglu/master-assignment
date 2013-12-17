/*
 * RoomRecord
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.util.logging.Logger;

/**
 * This class is used to store a record from the datafile. A <code>List</code>
 * of these objects is used by <code>Database</code> to provide a caching
 * mechanism for the application
 * 
 * @author Eoin Mooney
 */
public class RoomRecord {

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.db</code>
	 */
	private final Logger log = Logger.getLogger("suncertify.db");

	/** If true, this record has been marked as deleted */
	private Boolean deleted;

	/**
	 * A <code>String[]</code> with each <code>String</code> storing the
	 * contents of a field of the record
	 */
	private final String[] fields;

	/**
	 * Instantiates a new record.
	 * 
	 * @param flag
	 *            a byte indicating whether the field is marked as deleted or
	 *            not. 0x00 indicates valid, 0xFF indicates deleted.
	 * @param content
	 *            the content
	 */
	public RoomRecord(final byte flag, final String[] content) {

		if (flag == 0x00) {
			this.deleted = false;
			this.fields = content;
		} else if (flag == 0xFF) {
			this.deleted = true;
			this.fields = content;
		} else {
			this.log.warning("Incorrect value for flag ... setting all attributes to null");
			this.deleted = null;
			this.fields = null;
		}
	}

	/**
	 * Checks if the record is marked for deletion.
	 * 
	 * @return true, if the record is deleted
	 */
	public boolean isRoomRecordDeleted() {
		return this.deleted;
	}

	/**
	 * Gets the contents of the fields in this record.
	 * 
	 * @return A <code>String[]</code> with each <code>String</code> holding the
	 *         contents of a field of the record
	 */
	public String[] getAllFieldsData() {
		return this.fields;
	}
}
