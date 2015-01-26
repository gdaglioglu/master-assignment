package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import suncertify.db.io.DBParser;


public class Data implements DBMain{
	
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
			//App.showErrorAndExit("Cannot open database file for reading.");
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
	}


	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		//this.checkRecordNumber(recNo);
		final String[] hotelRoom = this.hotelRooms.get(recNo);

		return hotelRoom;
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		
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

}
