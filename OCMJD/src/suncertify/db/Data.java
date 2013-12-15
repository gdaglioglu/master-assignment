package suncertify.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyFileManager;

public class Data implements DBAccess {

	private static DataFileAccess database = null;
	private static DataLockManager locks = DataLockManager.getInstance();
	private static PropertyFileManager properties = PropertyFileManager
			.getInstance();

	public Data() throws FileNotFoundException, IOException {
		this(properties.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH));
	}

	public Data(String dbLocation) throws FileNotFoundException, IOException {
		database = new DataFileAccess(dbLocation);
	}

	public String[] readRecord(long recNo) throws RecordNotFoundException {
		return database.readRecord(recNo);
	}

	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		database.updateRecord(recNo, data, lockCookie);
	}

	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		database.deleteRecord(recNo, lockCookie);
	}

	public long[] findByCriteria(String[] criteria) {
		if (criteria == null) {
			return database.getValidRecNos();
		} else {
			return database.findByCriteria(criteria);
		}
	}

	public long createRecord(String[] data) throws DuplicateKeyException {
		return database.createRecord(data);
	}

	public long lockRecord(long recNo) throws RecordNotFoundException {
		return locks.lockRecord(recNo);
	}

	public void unlock(long recNo, long cookie) throws SecurityException {
		locks.unlock(recNo, cookie);
	}
}
