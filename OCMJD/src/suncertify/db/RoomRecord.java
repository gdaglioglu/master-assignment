/*
 * RoomRecord
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class RoomRecord.
 */
public class RoomRecord {

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.db");

	/** The deleted. */
	private boolean deleted;

	/** The fields. */
	private final String[] fields;

	/**
	 * Instantiates a new room record.
	 *
	 * @param flag the flag
	 * @param content the content
	 */
	public RoomRecord(final byte flag, final String[] content) {
		if (flag == 0x00) {
			this.deleted = false;
		} else if (flag == 0xFF) {
			this.deleted = true;
		} else {
			this.log.warning("Incorrect value for flag ... assuming deleted = false");
			this.deleted = false;
		}

		this.fields = content;
	}

	/**
	 * Checks if is room record deleted.
	 *
	 * @return true, if is room record deleted
	 */
	public boolean isRoomRecordDeleted() {
		return this.deleted;
	}

	/**
	 * Gets the field data.
	 *
	 * @param number the number
	 * @return the field data
	 */
	public String getFieldData(final int number) {
		return this.fields[number];
	}

	/**
	 * Gets the all fields data.
	 *
	 * @return the all fields data
	 */
	public String[] getAllFieldsData() {
		return this.fields;
	}
}
