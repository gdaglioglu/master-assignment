package suncertify.db;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DataLockManager {

	private Logger log = Logger.getLogger("suncertify.db");

	private Map<Long, Long> lockedRecords;

	private static DataLockManager instance = new DataLockManager();

	public static DataLockManager getInstance() {
		return instance;
	}

	private DataLockManager() {
		lockedRecords = new HashMap<Long, Long>();
	}

	public synchronized long lockRecord(long recNo) throws RecordNotFoundException {
		log.entering("suncertify.db.DataLockManager", "lockRecord()");

		long lockOwnerCookie = Thread.currentThread().getId();
		try {

			while (lockedRecords.containsKey(recNo)
					&& (lockedRecords.get(recNo) != lockOwnerCookie)) {
				wait();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lockedRecords.put(recNo, lockOwnerCookie);

		log.exiting("suncertify.db.DataLockManager", "lockRecord()");

		return lockOwnerCookie;
	}

	public synchronized void unlock(long recNo, long cookie) throws SecurityException {
		log.entering("suncertify.db.DataLockManager", "unlock()");

		long lockOwnerCookie = Thread.currentThread().getId();

		if (lockedRecords.get(recNo) == null) {
			// TODO Throw SecurityException
		} else if (lockedRecords.get(recNo) != lockOwnerCookie) {
			// TODO Throw SecurityException
		} else if (lockedRecords.containsKey(recNo)) {
			lockedRecords.remove(recNo);
			notifyAll();
		}

		log.exiting("suncertify.db.DataLockManager", "unlock()");
	}
}
