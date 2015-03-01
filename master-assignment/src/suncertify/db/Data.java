package suncertify.db;

import static suncertify.app.util.App.showErrorAndExit;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.io.DBParser;
import suncertify.db.io.DBSchema;
import suncertify.db.io.DBWriter;

/**
 * This class implements the provided interface <code>DBMain</code> This class
 * is a facade and delegates all functionality to two worker classes
 * 
 * @author Gokhan Daglioglu
 * 
 */
public class Data implements DBMain {

	private Logger logger = Logger.getLogger("suncertify.db");

	private List<ReentrantLock> locks;
	private ReentrantLock createLock;
	private final String dbLocation;
	private RandomAccessFile is;
	private List<String[]> hotelRooms;
	private DBWriter dbWriter;

	/**
	 * Construct a new instance connected to the passed file.
	 * 
	 * @param dbLocation
	 *            The location of the database file.
	 */
	public Data(String dbLocation) {
		this.dbLocation = dbLocation;
		this.init();
		logger.log(Level.FINE, "Initialized Data");
	}

	/**
	 * Initialize the Data instance. This method will create and populate the
	 * cache.
	 */
	private void init() {
		try {
			this.is = new RandomAccessFile(this.dbLocation, "rw");
			final DBParser parser = new DBParser(this.is);
			this.dbWriter = new DBWriter(this.is);
			this.createLock = new ReentrantLock();
			this.buildCache(parser);
		} catch (final FileNotFoundException e) {
			showErrorAndExit("Cannot open database file for reading.");
		}
	}

	/**
	 * This method will read all the records from the database and populate the
	 * cache with them.
	 * 
	 * @param parser
	 *            The database parser used to read the database records.
	 */
	private void buildCache(final DBParser parser) {
		this.hotelRooms = parser.getAllRecords();
		this.locks = new ArrayList<ReentrantLock>(this.hotelRooms.size());
		for (int i = 0; i < this.hotelRooms.size(); i++) {
			this.locks.add(new ReentrantLock());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		final String[] hotelRoom = this.hotelRooms.get(recNo);

		return Arrays.copyOf(hotelRoom, hotelRoom.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.dbWriter.write(recNo, data);
		this.hotelRooms.set(recNo, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.dbWriter.delete(recNo);
		this.hotelRooms.set(recNo, new String[DBSchema.NUMBER_OF_FIELDS]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		final List<Integer> results = new ArrayList<Integer>();
		for (int n = 0; n < this.hotelRooms.size(); n++) {
			if (!this.isRecordDeleted(n)) {
				this.lock(n);

				boolean match = true;
				for (int i = 0; i < criteria.length; i++) {
					if (criteria[i] != null) {
						String field = this.hotelRooms.get(n)[i];
						if (field != null) {
							field = field.toLowerCase();
							final String critField = criteria[i].toLowerCase();
							if (!field.contains(critField)) {
								match = false;
							}
						} else {
							match = false;
						}
					}
				}

				if (match) {
					results.add(n);
				}

				this.unlock(n);
			}
		}

		final int[] intResults = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			intResults[i] = results.get(i);
		}

		return intResults;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException {

		this.createLock.lock();

		this.checkCreateData(data);

		final int deletedPos = this.dbWriter.create(data);
		int recNo = deletedPos;

		if (deletedPos != -1) {
			this.hotelRooms.set(deletedPos, data);
		} else {
			this.hotelRooms.add(data);
			this.locks.add(new ReentrantLock());
			recNo = this.hotelRooms.size() - 1;
		}
		this.createLock.unlock();
		return recNo;
	}

	/**
	 * This method is used to validate that the data passed to
	 * {@link #create(String[])} is correct. It checks the primary key of this
	 * new record isn't already used.
	 * 
	 * @param data
	 *            The new record to be created.
	 * @return true if this record can be created, otherwise false.
	 * @throws DuplicateKeyException
	 *             If a record with the primary key of name & location already
	 *             exists.
	 */
	private boolean checkCreateData(final String[] data) throws DuplicateKeyException {
		if ((data == null) || (data.length < 2) || (data[0] == null) || (data[1] == null)
				|| data[0].equals("") || data[1].equals("")) {
			this.createLock.unlock();
			throw new IllegalArgumentException("The Name & Address cannot be empty!");
		}

		for (int i = 0; i < this.hotelRooms.size(); i++) {
			if (!this.isRecordDeleted(i)) {
				final String[] record = this.hotelRooms.get(i);
				if (record[0].equals(data[0]) && record[1].equals(data[1])) {
					this.createLock.unlock();
					throw new DuplicateKeyException(
							"A record with this Name & Address already exists.");
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.locks.get(recNo).lock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.locks.get(recNo).unlock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		return this.isRecordLocked(recNo);
	}

	/**
	 * This method checks if a record is already locked.
	 * 
	 * @param recNo
	 *            The record number to check.
	 * @return true if the record is locked, otherwise false.
	 */
	private boolean isRecordLocked(final int recNo) {
		return this.locks.get(recNo).isLocked();
	}

	/**
	 * This method checks if a record is marked as deleted.
	 * 
	 * @param recNo
	 *            The record number to check.
	 * @return true if the record has been deleted, otherwise false.
	 */
	private boolean isRecordDeleted(final int recNo) {
		return this.hotelRooms.get(recNo)[0] == null;
	}

	/**
	 * This method checks is a record exists and is not deleted.
	 * 
	 * @param recNo
	 *            The record number to check.
	 * @throws RecordNotFoundException
	 *             If the record can't be found in the cache.
	 */
	private void checkRecordNumber(final int recNo) throws RecordNotFoundException {
		if (recNo < 0) {
			throw new IllegalArgumentException("The record number cannot be negative.");
		}
		if (this.hotelRooms.size() <= recNo) {
			throw new RecordNotFoundException("No record found for record number: " + recNo);
		}
		if (this.isRecordDeleted(recNo) && !this.isRecordLocked(recNo)) {
			throw new RecordNotFoundException("Record number " + recNo + " has been deleted.");
		}
	}
}