package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import suncertify.db.io.DBParser;

public class Data implements DBMain {

	private List<ReentrantLock> locks;
	private final String dbLocation;
	private RandomAccessFile is;
	private List<String[]> hotelRooms;

	public Data(String dbLocation) {
		this.dbLocation = dbLocation;
		this.init();
	}

	/**
	 * Initialize the Data instance. This method will create and populate the
	 * cache.
	 */
	private void init() {
		try {
			this.is = new RandomAccessFile(this.dbLocation, "rw");
			final DBParser parser = new DBParser(this.is);
			this.buildCache(parser);
		} catch (final FileNotFoundException e) {
			// App.showErrorAndExit("Cannot open database file for reading.");
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

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		// this.checkRecordNumber(recNo);
		final String[] hotelRoom = this.hotelRooms.get(recNo);

		return hotelRoom;
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);

		// this.dbLocation.write(recNo, data);
		this.hotelRooms.set(recNo, data);

	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		final List<Integer> results = new ArrayList<Integer>();
		for (int n = 0; n < this.hotelRooms.size(); n++) {

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

		}

		final int[] intResults = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			intResults[i] = results.get(i);
		}

		return intResults;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
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
	 * This method checks if a recordis marked as deleted.
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
	private void checkRecordNumber(final int recNo)
			throws RecordNotFoundException {
		if (recNo < 0) {
			throw new IllegalArgumentException(
					"The record number cannot be negative.");
		}
		if (this.hotelRooms.size() <= recNo) {
			throw new RecordNotFoundException(
					"No record found for record number: " + recNo);
		}
		if (this.isRecordDeleted(recNo) && !this.isRecordLocked(recNo)) {
			throw new RecordNotFoundException("Record number " + recNo
					+ " has been deleted.");
		}
	}

}
