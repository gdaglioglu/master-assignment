/*
 * 
 */
package suncertify.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DataFileAccess.
 */
public class DataFileAccess {

	/** The start of data offset. */
	private long startOfDataOffset;

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.db");

	/** The database. */
	private RandomAccessFile database = null;

	/** The db location. */
	private String dbLocation = null;

	/** The cache. */
	private final Database cache = new Database();

	/**
	 * Instantiates a new data file access.
	 *
	 * @param providedDbLocation the provided db location
	 */
	public DataFileAccess(final String providedDbLocation) {
		this.log.entering("suncertify.db.DataFileAccess", "DataFileAccess",
				providedDbLocation);

		if (this.database == null) {
			this.openDatabase(providedDbLocation, "r");
			this.dbLocation = providedDbLocation;
			this.closeDatabase();

			try {
				this.startOfDataOffset = this.readDatabaseSchema();
				this.updateCache();
			} catch (final IOException ioe) {
				this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
				System.err
						.println("I/O problem encountered when attempting to read data file: "
								+ ioe.getMessage());
				ioe.printStackTrace();
			}

		} else if (this.dbLocation != providedDbLocation) {
			this.log.warning("Only one database location can be specified. "
					+ "Current location: " + this.dbLocation + " "
					+ "Ignoring provided path: " + providedDbLocation);
		}

		this.log.exiting("suncertify.db.DataFileAccess", "DataFileAccess");
	}

	/**
	 * Open database.
	 *
	 * @param location the location
	 * @param mode the mode
	 */
	private void openDatabase(final String location, final String mode) {
		try {
			if (mode.equals("r")) {
				this.database = new RandomAccessFile(location, mode);
				this.log.fine("Database opened");
			} else if (mode.equals("rw")) {
				this.database = new RandomAccessFile(location, mode);
				this.log.fine("Database opened for writing");
			} else {
				this.log.fine("Invalid mode for openDatabase");
			}
		} catch (final FileNotFoundException fnfe) {
			this.log.log(Level.SEVERE, fnfe.getMessage(), fnfe);
			System.err.println("Unable to open data file at " + location + ": "
					+ fnfe.getMessage());
			fnfe.printStackTrace();
		}
	}

	/**
	 * Close database.
	 */
	private void closeDatabase() {
		try {
			this.database.close();
			this.log.fine("Database closed");
		} catch (final IOException ioe) {
			this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
			System.err
					.println("I/O problem encountered when attempting to close data file: "
							+ ioe.getMessage());
			ioe.printStackTrace();
		}

	}

	/**
	 * Read field info.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readFieldInfo() throws IOException {
		int fieldNameLengthTemp;
		fieldNameLengthTemp = this.database.readByte();

		final byte[] fieldNameTemp = new byte[fieldNameLengthTemp];
		this.database.read(fieldNameTemp, 0, fieldNameLengthTemp);

		int fieldLength;
		fieldLength = this.database.readByte();

		try {
			final RecordFieldInfo fieldInfoTemp = new RecordFieldInfo(
					fieldNameLengthTemp, fieldNameTemp, fieldLength);
			this.cache.addRecordFieldInfo(fieldInfoTemp);
		} catch (final UnsupportedEncodingException uee) {
			this.log.log(Level.SEVERE, uee.getMessage(), uee);
			System.err.println("Unable to decode field info: ");
			uee.printStackTrace();
		}
	}

	/**
	 * Read database schema.
	 *
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public long readDatabaseSchema() throws IOException {
		this.log.entering("suncertify.db.DataFileAccess", "readDatabaseSchema");
		long offset = -1;
		long sizeOfRecord = 1;

		this.openDatabase(this.dbLocation, "r");

		final int magicCookie = this.database.readInt();

		this.cache.setMagicCookie(magicCookie);
		this.cache.setFieldsPerRecord(this.database.readShort());

		for (int i = 0; i < this.cache.getFieldsPerRecord(); i++) {
			this.readFieldInfo();
			sizeOfRecord += this.cache.getRecordFieldInfoAtIndex(i)
					.getBytesInField();
		}

		offset = this.database.getFilePointer();
		this.cache.setSizeOfRecord(sizeOfRecord);

		this.closeDatabase();

		this.log.exiting("suncertify.db.DataFileAccess", "readDatabaseSchema");

		this.log.fine("Schema successfully read to cache");

		return offset;
	}

	/**
	 * Read record flag.
	 *
	 * @return the byte
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private byte readRecordFlag() throws IOException {
		final byte recordFlag = this.database.readByte();

		return recordFlag;
	}

	/**
	 * Read record data.
	 *
	 * @return the string[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String[] readRecordData() throws IOException {
		final String[] recordContents = new String[this.cache
				.getFieldsPerRecord()];

		for (int index = 0; index < this.cache.getFieldsPerRecord(); index++) {
			final byte[] temp = new byte[this.cache.getRecordFieldInfoAtIndex(
					index).getBytesInField()];
			this.database.read(temp, 0,
					this.cache.getRecordFieldInfoAtIndex(index)
							.getBytesInField());

			try {
				recordContents[index] = new String(temp, 0, temp.length,
						"US-ASCII");
			} catch (final UnsupportedEncodingException uee) {
				this.log.log(Level.SEVERE, uee.getMessage(), uee);
				System.err.println("Unable to decode record data: ");
				uee.printStackTrace();
			}
		}
		return recordContents;
	}

	/**
	 * Write record flag.
	 *
	 * @param recNo the rec no
	 * @param deleted the deleted
	 */
	private void writeRecordFlag(final long recNo, final boolean deleted) {
		this.log.entering("suncertify.db.DataFileAccess", "writeRecordFlag");

		final long startOfRecord = this.startOfDataOffset + recNo
				* this.cache.getSizeOfRecord();

		this.openDatabase(this.dbLocation, "rw");

		try {
			this.database.seek(startOfRecord);
			this.log.fine("Pointer moved to start of Record");

			if (deleted) {
				this.database.writeByte(0xFF);
			} else {
				this.database.writeByte(0x00);
			}
		} catch (final IOException ioe) {
			this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
			System.err
					.println("I/O problem encountered when attempting to write record flag: "
							+ ioe.getMessage());
			ioe.printStackTrace();
		} finally {
			this.closeDatabase();
		}

		this.log.exiting("suncertify.db.DataFileAccess", "writeRecordFlag");
	}

	/**
	 * Write record data.
	 *
	 * @param recNo the rec no
	 * @param data the data
	 */
	private void writeRecordData(final long recNo, final String[] data) {
		this.log.entering("suncertify.db.DataFileAccess", "writeRecordData");

		final long startOfRecord = this.startOfDataOffset + recNo
				* this.cache.getSizeOfRecord();

		this.openDatabase(this.dbLocation, "rw");
		try {
			this.database.seek(startOfRecord + 1);
			this.log.fine("Pointer moved to start of Record Data");

			for (final String field : data) {
				final byte[] fieldBytes = field.getBytes("US-ASCII");
				this.database.write(fieldBytes);
			}

		} catch (final IOException ioe) {
			this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
			System.err
					.println("I/O problem encountered when attempting to write record data: "
							+ ioe.getMessage());
			ioe.printStackTrace();
		} finally {
			this.closeDatabase();
		}

		this.log.exiting("suncertify.db.DataFileAccess", "writeRecordData");
	}

	/**
	 * Update cache.
	 */
	public void updateCache() {
		this.log.entering("suncertify.db.DataFileAccess", "updateCache");

		this.cache.clearRecordsCache();
		this.log.fine("Cache cleared");

		try {
			this.openDatabase(this.dbLocation, "r");

			this.database.seek(this.startOfDataOffset);
			this.log.fine("Pointer moved to start of Data");

			while (this.database.getFilePointer() < this.database.length()) {
				final byte recordFlag = this.readRecordFlag();
				final String[] recordContents = this.readRecordData();

				this.cache.addRoomRecord(recordFlag, recordContents);
			}

		} catch (final IOException ioe) {
			this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
			System.err
					.println("I/O problem found when attempting to read all records: "
							+ ioe.getMessage());
			ioe.printStackTrace();
		} finally {
			this.closeDatabase();
		}

		this.log.fine("Database loaded into memory");

		this.log.exiting("suncertify.db.DataFileAccess", "updateCache");
	}

	/**
	 * Read record.
	 *
	 * @param recNo the rec no
	 * @return the string[]
	 * @throws RecordNotFoundException the record not found exception
	 */
	public String[] readRecord(final long recNo) throws RecordNotFoundException {

		if (recNo < 0 || recNo >= this.cache.getNumberOfRoomRecords()) {
			throw new RecordNotFoundException("Cannot find record with recNo: "
					+ recNo);
		}

		if (this.cache.isRecordDeleted(recNo)) {
			return null;
		} else {
			return this.cache.getRoomRecordData(recNo);
		}
	}

	/**
	 * Update record.
	 *
	 * @param recNo the rec no
	 * @param data the data
	 * @param lockCookie the lock cookie
	 * @throws RecordNotFoundException the record not found exception
	 * @throws SecurityException the security exception
	 */
	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {
		this.log.entering("suncertify.db.DataFileAccess", "updateRecord");
		final long userLockCookie = Thread.currentThread().getId();

		final DataLockManager locks = DataLockManager.getInstance();
		final Long ownerLockCookie = locks.getOwner(recNo);

		if (recNo < 0 || recNo >= this.cache.getNumberOfRoomRecords()) {
			throw new RecordNotFoundException("Cannot find record with recNo: "
					+ recNo);
		} else if (!locks.isLocked(recNo)) {
			throw new SecurityException(
					"User updating an unlocked record, with recNo: " + recNo);
		} else if (ownerLockCookie != userLockCookie) {
			throw new SecurityException("User does not own lock on recNo: "
					+ recNo);
		} else if (lockCookie == userLockCookie) {
			this.updateCache();

			if (this.cache.isRecordDeleted(recNo)
					|| this.cache.isRecordBooked(recNo)) {
				throw new RecordNotFoundException(
						"Table content is out of date, cannot update record with recNo: "
								+ recNo + ". Please refresh table content");
			} else {
				this.writeRecordFlag(recNo, false);
				this.writeRecordData(recNo, data);
			}
		} else {
			throw new SecurityException(
					"Provided lock cookie does not match user generated lock cookie");
		}

		this.updateCache();

		this.log.exiting("suncertify.db.DataFileAccess", "updateRecord");
	}

	/**
	 * Delete record.
	 *
	 * @param recNo the rec no
	 * @param lockCookie the lock cookie
	 * @throws RecordNotFoundException the record not found exception
	 * @throws SecurityException the security exception
	 */
	public void deleteRecord(final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
		this.log.entering("suncertify.db.DataFileAccess", "deleteRecord");

		final long userLockCookie = Thread.currentThread().getId();

		final DataLockManager locks = DataLockManager.getInstance();
		final Long ownerLockCookie = locks.getOwner(recNo);

		if (recNo < 0 || recNo >= this.cache.getNumberOfRoomRecords()) {
			throw new RecordNotFoundException("Cannot find record with recNo: "
					+ recNo);
		} else if (!locks.isLocked(recNo)) {
			throw new SecurityException(
					"User deleting an unlocked record, with recNo: " + recNo);
		} else if (ownerLockCookie != userLockCookie) {
			throw new SecurityException("User does not own lock on recNo: "
					+ recNo);
		} else if (lockCookie == userLockCookie) {

			if (this.cache.isRecordDeleted(recNo)) {
				throw new RecordNotFoundException(
						"Table content is out of date, cannot delete record with recNo: "
								+ recNo + ". Please refresh table content");
			} else {
				this.writeRecordFlag(recNo, true);
			}
		} else {
			throw new SecurityException(
					"Provided lock cookie does not match user generated lock cookie");
		}

		this.updateCache();

		this.log.exiting("suncertify.db.DataFileAccess", "deleteRecord");
	}

	/**
	 * Find by criteria.
	 *
	 * @param criteria the criteria
	 * @return the long[]
	 */
	public long[] findByCriteria(final String[] criteria) {

		this.updateCache();

		final long[] validRecNos = this.cache.getValidRoomRecordRecNos();

		if (criteria == null) {
			return validRecNos;
		}

		final List<Long> tempArray = new ArrayList<Long>();

		for (final long recNo : validRecNos) {
			if (this.cache.isRecordMatch(recNo, criteria)) {
				tempArray.add(recNo);
			}
		}

		final long[] matchedRecNos = new long[tempArray.size()];

		for (int i = 0; i < tempArray.size(); i++) {
			matchedRecNos[i] = tempArray.get(i);
		}

		return matchedRecNos;
	}

	/**
	 * Creates the record.
	 *
	 * @param data the data
	 * @return the long
	 * @throws DuplicateKeyException the duplicate key exception
	 */
	public long createRecord(final String[] data) throws DuplicateKeyException {
		long recNo;

		for (recNo = 0; recNo < this.cache.getNumberOfRoomRecords(); recNo++) {
			if (this.cache.isRecordDeleted(recNo)) {
				this.writeRecordFlag(recNo, false);
				this.writeRecordData(recNo, data);

				this.updateCache();

				return recNo;
			}
		}
		this.writeRecordFlag(recNo, false);
		this.writeRecordData(recNo, data);

		this.updateCache();

		return recNo;
	}
}
