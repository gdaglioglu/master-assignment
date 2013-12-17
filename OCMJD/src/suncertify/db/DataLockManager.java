/*
 * 
 */
package suncertify.db;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DataLockManager.
 */
public class DataLockManager {

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.db");

	/** The locked records. */
	private final Map<Long, Long> lockedRecords = new HashMap<Long, Long>();

	/** The Constant instance. */
	private static final DataLockManager instance = new DataLockManager();

	/**
	 * Gets the single instance of DataLockManager.
	 *
	 * @return single instance of DataLockManager
	 */
	public static DataLockManager getInstance() {
		return DataLockManager.instance;
	}

	/**
	 * Instantiates a new data lock manager.
	 */
	private DataLockManager() {
	}

	/**
	 * Lock record.
	 *
	 * @param recNo the rec no
	 * @return the long
	 * @throws RecordNotFoundException the record not found exception
	 */
	public synchronized long lockRecord(final long recNo)
			throws RecordNotFoundException {
		this.log.entering("suncertify.db.DataLockManager", "lockRecord()");

		final long lockOwnerCookie = Thread.currentThread().getId();
		try {

			while (this.isLocked(recNo)
					&& this.getOwner(recNo) != lockOwnerCookie) {
				this.wait();
			}

		} catch (final InterruptedException ie) {
			throw new RecordNotFoundException(
					"Problem encountered when attempting"
							+ " to lock record, recNo: " + recNo + ". "
							+ ie.getMessage());
		}
		this.lockedRecords.put(recNo, lockOwnerCookie);

		this.log.exiting("suncertify.db.DataLockManager", "lockRecord()");

		return lockOwnerCookie;
	}

	/**
	 * Unlock.
	 *
	 * @param recNo the rec no
	 * @param cookie the cookie
	 * @throws SecurityException the security exception
	 */
	public synchronized void unlock(final long recNo, final long cookie)
			throws SecurityException {
		this.log.entering("suncertify.db.DataLockManager", "unlock()");

		final long lockOwnerCookie = Thread.currentThread().getId();

		if (!this.isLocked(recNo)) {
			throw new SecurityException(
					"User trying to remove lock on already unlocked record, recNo: "
							+ recNo);
		} else if (this.getOwner(recNo) != lockOwnerCookie) {
			throw new SecurityException(
					"User trying to another user's lock, recNo: " + recNo);
		} else if (lockOwnerCookie == cookie) {
			this.lockedRecords.remove(recNo);
			this.notifyAll();
		} else {
			throw new SecurityException(
					"Provided lock cookie does not match user generated lock cookie");
		}
		this.log.exiting("suncertify.db.DataLockManager", "unlock()");
	}

	/**
	 * Checks if is locked.
	 *
	 * @param recNo the rec no
	 * @return true, if is locked
	 */
	public boolean isLocked(final long recNo) {
		return this.lockedRecords.containsKey(recNo);
	}

	/**
	 * Gets the owner.
	 *
	 * @param recNo the rec no
	 * @return the owner
	 */
	public Long getOwner(final long recNo) {
		return this.lockedRecords.get(recNo);
	}
}
