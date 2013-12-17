/*
 * Database
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a caching mechanism for the application by storing the
 * records in a <code>List</code> of <code>RoomRecords</code>. It also provides
 * for dynamic reading of the datafile by retaining the schema in a
 * <code>List</code> of <code>RecordFieldInfo</code>
 * 
 * @author Eoin Mooney
 */
public class Database {

	/** The value of the magic cookie read from the data file is stored here */
	private long magicCookie;

	/**
	 * After reading how many fields are in a record from the datafile, that
	 * value is stored here
	 */
	private short fieldsPerRecord;

	/** After the size of a record is calculated, that value is stored here */
	private long sizeOfRecord;

	/**
	 * This <code>List</code> of <code>RecordFieldInfo</code> stores the schema
	 * for the datafile
	 */
	private final List<RecordFieldInfo> dbSchema = new ArrayList<RecordFieldInfo>();

	/**
	 * This <code>List</code> of <code>RoomRecords</code> provides the records
	 * cache for the application
	 */
	private final List<RoomRecord> recordsCache = new ArrayList<RoomRecord>();

	/**
	 * Instantiates a new database.
	 */
	public Database() {
	}

	/**
	 * Clears the cache of records
	 */
	public void clearRecordsCache() {
		this.recordsCache.clear();
	}

	/**
	 * Gets the magic cookie.
	 * 
	 * @return the magic cookie
	 */
	public long getMagicCookie() {
		return this.magicCookie;
	}

	/**
	 * Sets the magic cookie.
	 * 
	 * @param number
	 *            the new magic cookie
	 */
	public void setMagicCookie(final long number) {
		this.magicCookie = number;
	}

	/**
	 * Gets the fields per record.
	 * 
	 * @return the fields per record
	 */
	public short getFieldsPerRecord() {
		return this.fieldsPerRecord;
	}

	/**
	 * Sets the fields per record.
	 * 
	 * @param number
	 *            the new fields per record
	 */
	public void setFieldsPerRecord(final short number) {
		this.fieldsPerRecord = number;
	}

	/**
	 * Gets the size of a record.
	 * 
	 * @return the size of a record
	 */
	public long getSizeOfRecord() {
		return this.sizeOfRecord;
	}

	/**
	 * Sets the size of a record.
	 * 
	 * @param number
	 *            the new size of a record
	 */
	public void setSizeOfRecord(final long number) {
		this.sizeOfRecord = number;
	}

	/**
	 * Gets the field info for the for the field at the given index
	 * 
	 * @param index
	 *            The index of the field required
	 * @return The <code>RecordFieldInfo</code> containing the field info for
	 *         the requested field
	 */
	public RecordFieldInfo getRecordFieldInfo(final int index) {
		return this.dbSchema.get(index);
	}

	/**
	 * Adds the field info for a new record to the schema
	 * 
	 * @param recordFieldInfo
	 *            The <code>RecordFieldInfo</code> to be added
	 */
	public void addRecordFieldInfo(final RecordFieldInfo recordFieldInfo) {
		this.dbSchema.add(recordFieldInfo);
	}

	/**
	 * Gets the record with given record number from the cache
	 * 
	 * @param recNo
	 *            The record number of the record to be returned
	 * @return The <code>RoomRecord</code> for the record with given record
	 *         number
	 */
	private RoomRecord getRoomRecord(final long recNo) {
		return this.recordsCache.get((int) recNo);
	}

	/**
	 * Gets the contents of the record with given record from the cache
	 * 
	 * @param recNo
	 *            The record number of the record contents to be returned
	 * @return A <code>String[]</code> with each <code>String</code> holding a
	 *         field of the record
	 */
	public String[] getRoomRecordData(final long recNo) {
		return this.getRoomRecord(recNo).getAllFieldsData();
	}

	/**
	 * Adds a <code>RoomRecord</code> to the cache
	 * 
	 * @param roomRecord
	 *            The <code>RoomRecord</code> to be added to the cache
	 */
	private void addRoomRecord(final RoomRecord roomRecord) {
		this.recordsCache.add(roomRecord);
	}

	/**
	 * Creates a <code>RoomRecord</code> from the parameters and adds it to the
	 * cache
	 * 
	 * @param flag
	 *            The deleted flag for the record to be added to the cache
	 * @param data
	 *            A <code>String[]</code> with each <code>String</code> holding
	 *            a field of the record
	 */
	public void addRoomRecord(final byte flag, final String[] data) {
		this.addRoomRecord(new RoomRecord(flag, data));
	}

	/**
	 * Gets the number of room records in the cache
	 * 
	 * @return The size of the cache
	 */
	public int getNumberOfRoomRecords() {
		return this.recordsCache.size();
	}

	/**
	 * Checks if a record is marked as deleted.
	 * 
	 * @param recNo
	 *            The record number of the record to be checked
	 * @return true, if the record is deleted
	 */
	public boolean isRecordDeleted(final long recNo) {
		return this.recordsCache.get((int) recNo).isRoomRecordDeleted();
	}

	/**
	 * Checks if a record is already booked.
	 * 
	 * @param recNo
	 *            The record number of the record to be checked
	 * @return true, if the record is booked
	 */
	public boolean isRecordBooked(final long recNo) {
		final String[] test = this.recordsCache.get((int) recNo)
				.getAllFieldsData();

		if (test[6].equals("        ")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets the record numbers of all valid records
	 * 
	 * @return A <code>long[]</code> where each <code>long</code> is the record
	 *         number of a valid record
	 */
	public long[] getValidRoomRecordRecNos() {
		final List<Long> list = new ArrayList<Long>();

		for (int i = 0; i < this.recordsCache.size(); i++) {
			if (!this.isRecordDeleted(i)) {
				list.add((long) i);
			}
		}

		final long[] validRoomRecordRecNos = new long[list.size()];

		for (int i = 0; i < list.size(); i++) {
			validRoomRecordRecNos[i] = list.get(i);
		}
		return validRoomRecordRecNos;
	}

	/**
	 * Checks if a record matches the given criteria.
	 * 
	 * @param recNo
	 *            The record number of the record to check
	 * @param criteria
	 *            A <code>String[]</code> where each <code>String</code> is
	 *            checked against the same field in the record for a match
	 * @return true, if the record matches the given criteria
	 */
	public boolean isRecordMatch(final long recNo, final String[] criteria) {
		final String[] recordData = this.getRoomRecordData(recNo);

		for (int i = 0; i < this.fieldsPerRecord; i++) {
			if (criteria[i] == null) {
				continue;
			}
			if (recordData[i].contains(criteria[i])) {
				return true;
			}
		}

		return false;
	}
}
