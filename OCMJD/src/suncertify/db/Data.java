package suncertify.db;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Data implements DBAccess {

	private static DatabaseFileAccess database = null;

	public Data() throws FileNotFoundException, IOException {
		this(System.getProperty("user.dir"));
	}

	public Data(String dbLocation) throws FileNotFoundException, IOException {
		database = new DatabaseFileAccess(dbLocation);
	}

	public void printDatabase() throws IOException {
		database.printDatabaseSchema();
		database.printDatabase();
	}

	@Override
	public String[] readRecord(long recNo) throws RecordNotFoundException {
		return database.readRecord(recNo);
	}

	@Override
	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException {

		database.updateRecord(recNo, data, lockCookie);
	}

	@Override
	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		database.deleteRecord(recNo, lockCookie);
	}

	@Override
	public long[] findByCriteria(String[] criteria) {
		if (criteria == null) {
			return database.getAllRecordIDs();
		} else {
			return database.findByCriteria(criteria);
		}
	}

	@Override
	public long createRecord(String[] data) throws DuplicateKeyException {
		return database.createRecord(data);
	}

	@Override
	public long lockRecord(long recNo) throws RecordNotFoundException {
		return database.lockRecord(recNo);
	}

	@Override
	public void unlock(long recNo, long cookie) throws SecurityException {
	}

}
