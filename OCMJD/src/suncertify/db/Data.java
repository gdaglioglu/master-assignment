package suncertify.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyFileManager;

public class Data implements DBAccess {

	private static DataFileAccess database = null;
	private static DataLockManager locks = DataLockManager.getInstance();
	private static PropertyFileManager properties = PropertyFileManager
			.getInstance();

	private Logger log = Logger.getLogger("suncertify.db");

	public Data() throws FileNotFoundException, IOException {
		this(properties.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH));
	}

	public Data(String dbLocation) throws FileNotFoundException, IOException {
		log.entering("suncertify,db", "Data()", dbLocation);

		database = new DataFileAccess(dbLocation);

		log.exiting("suncertify,db", "Data()");
	}

	public String[] readRecord(long recNo) throws RecordNotFoundException {
		log.entering("suncertify,db", "readRecord()", recNo);
		log.exiting("suncertify,db", "readRecord()");

		return database.readRecord(recNo);
	}

	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		log.entering("suncertify,db", "updateRecord()");

		database.updateRecord(recNo, data, lockCookie);

		log.exiting("suncertify,db", "updateRecord()");
	}

	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException {
		database.deleteRecord(recNo, lockCookie);
	}

	public long[] findByCriteria(String[] criteria) {
		log.entering("suncertify,db", "findByCriteria()", criteria);

		if (criteria == null) {
			log.exiting("suncertify,db", "findByCriteria()");

			return database.getValidRecNos();
		} else {
			log.exiting("suncertify,db", "findByCriteria()");

			return database.findByCriteria(criteria);
		}
	}

	public long createRecord(String[] data) throws DuplicateKeyException {
		log.entering("suncertify,db", "createRecord()", data);
		log.exiting("suncertify,db", "createRecord()");

		return database.createRecord(data);
	}

	public long lockRecord(long recNo) throws RecordNotFoundException {
		log.entering("suncertify,db", "lockRecord()", recNo);
		log.exiting("suncertify,db", "lockRecord()");

		return locks.lockRecord(recNo);
	}

	public void unlock(long recNo, long cookie) throws SecurityException {
		log.entering("suncertify,db", "unlock()");

		locks.unlock(recNo, cookie);

		log.exiting("suncertify,db", "unlock()");
	}
}
