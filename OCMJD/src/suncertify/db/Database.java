/*
 * 
 */
package suncertify.db;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Database.
 */
public class Database {

	/** The magic cookie. */
	private long magicCookie;

	/** The fields per record. */
	private short fieldsPerRecord;

	/** The size of record. */
	private long sizeOfRecord;

	/** The db schema. */
	private final List<RecordFieldInfo> dbSchema = new ArrayList<RecordFieldInfo>();

	/** The room records. */
	private final List<RoomRecord> roomRecords = new ArrayList<RoomRecord>();

	/**
	 * Instantiates a new database.
	 */
	public Database() {
	}

	/**
	 * Clear records cache.
	 */
	public void clearRecordsCache() {
		this.roomRecords.clear();
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
	 * @param number the new magic cookie
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
	 * @param number the new fields per record
	 */
	public void setFieldsPerRecord(final short number) {
		this.fieldsPerRecord = number;
	}

	/**
	 * Gets the size of record.
	 *
	 * @return the size of record
	 */
	public long getSizeOfRecord() {
		return this.sizeOfRecord;
	}

	/**
	 * Sets the size of record.
	 *
	 * @param number the new size of record
	 */
	public void setSizeOfRecord(final long number) {
		this.sizeOfRecord = number;
	}

	/**
	 * Gets the record field info at index.
	 *
	 * @param index the index
	 * @return the record field info at index
	 */
	public RecordFieldInfo getRecordFieldInfoAtIndex(final int index) {
		return this.dbSchema.get(index);
	}

	/**
	 * Adds the record field info.
	 *
	 * @param recordFieldInfo the record field info
	 */
	public void addRecordFieldInfo(final RecordFieldInfo recordFieldInfo) {
		this.dbSchema.add(recordFieldInfo);
	}

	/**
	 * Gets the field names.
	 *
	 * @return the field names
	 */
	public String[] getFieldNames() {
		String[] fieldNames;
		final List<String> tempArray = new ArrayList<String>();

		for (int index = 0; index < this.dbSchema.size(); index++) {
			tempArray.add(this.getRecordFieldInfoAtIndex(index).getFieldName());
		}

		fieldNames = new String[tempArray.size()];
		for (int index = 0; index < tempArray.size(); index++) {
			fieldNames[index] = tempArray.get(index);
		}

		return fieldNames;
	}

	/**
	 * Gets the room record.
	 *
	 * @param recNo the rec no
	 * @return the room record
	 */
	public RoomRecord getRoomRecord(final long recNo) {
		return this.roomRecords.get((int) recNo);
	}

	/**
	 * Gets the room record data.
	 *
	 * @param recNo the rec no
	 * @return the room record data
	 */
	public String[] getRoomRecordData(final long recNo) {
		return this.getRoomRecord(recNo).getAllFieldsData();
	}

	/**
	 * Adds the room record.
	 *
	 * @param roomRecord the room record
	 */
	public void addRoomRecord(final RoomRecord roomRecord) {
		this.roomRecords.add(roomRecord);
	}

	/**
	 * Adds the room record.
	 *
	 * @param flag the flag
	 * @param data the data
	 */
	public void addRoomRecord(final byte flag, final String[] data) {
		this.addRoomRecord(new RoomRecord(flag, data));
	}

	/**
	 * Gets the number of room records.
	 *
	 * @return the number of room records
	 */
	public int getNumberOfRoomRecords() {
		return this.roomRecords.size();
	}

	/**
	 * Checks if is record deleted.
	 *
	 * @param recNo the rec no
	 * @return true, if is record deleted
	 */
	public boolean isRecordDeleted(final long recNo) {
		return this.roomRecords.get((int) recNo).isRoomRecordDeleted();
	}

	/**
	 * Checks if is record booked.
	 *
	 * @param recNo the rec no
	 * @return true, if is record booked
	 */
	public boolean isRecordBooked(final long recNo) {
		final String[] test = this.roomRecords.get((int) recNo)
				.getAllFieldsData();

		if (test[6].equals("        ")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets the valid room record rec nos.
	 *
	 * @return the valid room record rec nos
	 */
	public long[] getValidRoomRecordRecNos() {
		final List<Long> list = new ArrayList<Long>();

		for (int i = 0; i < this.roomRecords.size(); i++) {
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
	 * Gets the valid room records.
	 *
	 * @return the valid room records
	 */
	public List<RoomRecord> getValidRoomRecords() {
		final List<RoomRecord> validRoomRecords = new ArrayList<RoomRecord>();

		final long[] validRoomRecordRecNos = this.getValidRoomRecordRecNos();

		for (final long validRecNo : validRoomRecordRecNos) {
			validRoomRecords.add(this.roomRecords.get((int) validRecNo));
		}

		return validRoomRecords;
	}

	/**
	 * Checks if is record match.
	 *
	 * @param recNo the rec no
	 * @param criteria the criteria
	 * @return true, if is record match
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
