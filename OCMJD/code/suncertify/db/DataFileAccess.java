/*
 * DataFileAccess
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.util.ApplicationConstants;

/**
 * This class is a worker class called by the <code>Data</code> facade class for
 * all interactions with the datafile It also interacts with the cache provided
 * by the <code>Database</code> class
 * 
 * @author Eoin Mooney
 */
public class DataFileAccess {

	/**
	 * This attribute is used to store the location of the first record in the
	 * datafile
	 */
	private long startOfDataOffset;

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.db</code>
	 */
	private final Logger log = Logger.getLogger("suncertify.db");

	/**
	 * The database instance is used for all read and write operations on the
	 * datafile
	 */
	private RandomAccessFile database = null;

	/**
	 * This <code>String</code> holds the location of the datafile on the system
	 */
	private String dbLocation = null;

	/**
	 * This class provides a caching mechanism
	 */
	private final Database cache = new Database();

	/**
	 * This constructor takes the datafile location as a parameter and checks
	 * that this file can be accessed
	 * 
	 * @param providedDbLocation
	 *            A <code>String</code> holding the provided location for the
	 *            datafile
	 */
	public DataFileAccess(final String providedDbLocation) {

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
	}

	/**
	 * This method opens the datafile for read or read/write operations and logs
	 * any exceptions encountered as <code>SEVERE</code>
	 * 
	 * @param location
	 *            A <code>String</code> containing the location of the datafile
	 * @param mode
	 *            Used to open the file in read mode (use "r") or read/write
	 *            mode (use "rw")
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
	 * This method closes the datafile and logs any IO exceptions encountered as
	 * <code>SEVERE</code>
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
	 * This method dynamically reads the schema information for a field from the
	 * database and stores this information in a <code>RecordFieldInfo</code>
	 * object in the cache
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readFieldInfo() throws IOException {
		int fieldNameLengthTemp;
		fieldNameLengthTemp = this.database.readByte();

		final byte[] fieldNameTemp = new byte[fieldNameLengthTemp];
		this.database.read(fieldNameTemp, 0, fieldNameLengthTemp);

		int fieldLength;
		fieldLength = this.database.readByte();

		try {
			// Build RecordFieldInfo object
			final RecordFieldInfo fieldInfoTemp = new RecordFieldInfo(
					fieldNameLengthTemp, fieldNameTemp, fieldLength);

			// Store RecordFieldInfo object in the cache
			this.cache.addRecordFieldInfo(fieldInfoTemp);

		} catch (final UnsupportedEncodingException uee) {
			this.log.log(Level.SEVERE, uee.getMessage(), uee);
			System.err.println("Unable to decode field info: ");
			uee.printStackTrace();
		}
	}

	/**
	 * This method reads all the schema information from the datafile and stores
	 * it in the cache
	 * 
	 * @return The location in the datafile after the schema info where first
	 *         data record begins.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private long readDatabaseSchema() throws IOException {
		long offset = -1;
		long sizeOfRecord = 1;

		this.openDatabase(this.dbLocation, "r");

		final int magicCookie = this.database.readInt();

		if (magicCookie != ApplicationConstants.MAGIC_COOKIE) {
			this.log.severe("Magic Cookie does not match expected value for datafile. Exiting Application");
			System.exit(0);
		}

		this.cache.setMagicCookie(magicCookie);
		this.cache.setFieldsPerRecord(this.database.readShort());

		for (int i = 0; i < this.cache.getFieldsPerRecord(); i++) {
			this.readFieldInfo();
			sizeOfRecord += this.cache.getRecordFieldInfo(i).getBytesInField();
		}

		offset = this.database.getFilePointer();
		this.cache.setSizeOfRecord(sizeOfRecord);

		this.closeDatabase();

		this.log.info("Schema successfully read to cache");

		return offset;
	}

	/**
	 * Reads a 1 <code>byte</code> flag at the start of each record that
	 * differentiates between a valid or deleted record
	 * 
	 * @return The deleted flag
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private byte readRecordFlag() throws IOException {
		final byte recordFlag = this.database.readByte();

		return recordFlag;
	}

	/**
	 * Reads a record from the datafile, using the schema info from the cache to
	 * accurately read each field
	 * 
	 * @return An array of <String>, each containing one field from the record
	 *         padded with spaces
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String[] readRecordData() throws IOException {
		final String[] recordContents = new String[this.cache
				.getFieldsPerRecord()];

		for (int index = 0; index < this.cache.getFieldsPerRecord(); index++) {
			// read n bytes, where n is based on the schema info in the cache
			final byte[] temp = new byte[this.cache.getRecordFieldInfo(index)
					.getBytesInField()];
			this.database.read(temp, 0, this.cache.getRecordFieldInfo(index)
					.getBytesInField());

			try {
				// convert byte array to String
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
	 * Writes the 1 byte flag at the start of each record that differentiates
	 * between a valid or deleted record
	 * 
	 * @param recNo
	 *            Identifies the record number where the flag will be written
	 * @param deleted
	 *            If true the flag will indicate a deleted record
	 */
	private void writeRecordFlag(final long recNo, final boolean deleted) {
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
	}

	/**
	 * Writes the fields of a record to the datafile.
	 * 
	 * @param recNo
	 *            Identifies record number of the record to be written
	 * @param data
	 *            An array of <code>String</code> containing the fields of the
	 *            record to be written
	 */
	private void writeRecordData(final long recNo, final String[] data) {

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
	}

	/**
	 * Clears the existing cache and re-populates it by reading all records from
	 * the datafile
	 */
	private void updateCache() {
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
	}

	/**
	 * Reads a single record from the cache
	 * 
	 * @param recNo
	 *            The record number of the record to read
	 * @return A <code>String[]</code> with each <code>String</code> holding a
	 *         field of the record
	 * @throws RecordNotFoundException
	 *             Signals that no valid record exists with given record number
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
	 * Updates a record in the datafile and refreshes the cache
	 * 
	 * @param recNo
	 *            The record number of the record to be updated
	 * @param data
	 *            A <code>String[]</code> with each <code>String</code> holding
	 *            a field of the record that will be written to the datafile
	 * @param lockCookie
	 *            A <code>lockCookie</code> provided by the
	 *            <code>DataLockManager</code>, used to ensure only one user can
	 *            update a record at a time
	 * @throws RecordNotFoundException
	 *             Signals that no valid record exists with given record number
	 * @throws SecurityException
	 *             Signals that user is attempting to update record with an
	 *             invalid <code>lockCookie</code>
	 */
	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {
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
	}

	/**
	 * Marks a record as deleted in the datafile and refreshes the cache
	 * 
	 * @param recNo
	 *            The record number of the record to be deleted
	 * @param lockCookie
	 *            A <code>lockCookie</code> provided by the
	 *            <code>DataLockManager</code>, used to ensure only one user can
	 *            update a record at a time
	 * @throws RecordNotFoundException
	 *             Signals that no valid record exists with given record number
	 * @throws SecurityException
	 *             Signals that user is attempting to update record with an
	 *             invalid <code>lockCookie</code>
	 */
	public void deleteRecord(final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
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
	}

	/**
	 * Case sensitive search that will return record number of all records that
	 * match the given criteria for each field.
	 * 
	 * @param criteria
	 *            A <code>String[]</code> containing the <code>String</code> to
	 *            match each field on
	 * @return The list of record numbers that match the given criteria, if
	 *         criteria is <code>null</code> returns all valid records
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
	 * Writes a new record to the datafile, overwriting a deleted record if
	 * available.
	 * 
	 * @param data
	 *            A <code>String[]</code> with each <code>String</code> holding
	 *            a field of the record that will be written to the datafile
	 * @return The record number of the newly created record
	 * @throws DuplicateKeyException
	 *             the duplicate key exception
	 */
	public long createRecord(final String[] data) {
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
