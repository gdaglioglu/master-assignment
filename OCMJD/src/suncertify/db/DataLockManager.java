/*
 * DataLockManager
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.db;

import java.util.HashMap;
import java.util.Map;

/**
 * This singleton class is a worker class called by the <code>Data</code> facade class to provide a locking mechanism for the application
 * 
 * @author Eoin Mooney
 */
public class DataLockManager {

	/**
	 * Hash Map containing records and their locks
	 */
	private final Map<Long, Long> lockedRecords = new HashMap<Long, Long>();

	/**
	 * Creates a singleton instance of the class
	 */
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
	 * Private constructor for this singleton class
	 */
	private DataLockManager() {
	}

	/**
	 * Locks a record so that it can only be updated by this user. If record is
	 * already locked, user's thread will give up CPU until the record is
	 * becomes unlocked
	 * 
	 * @param recNo
	 *            The record number of the record to be locked
	 * @return A <code>lockCookie</code> to ensure only one user can update a
	 *         record at a time
	 * @throws Signals
	 *             that no valid record exists with given record number
	 */
	public synchronized long lockRecord(final long recNo)
			throws RecordNotFoundException {
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

		return lockOwnerCookie;
	}

	/**
	 * Unlocks a previously locked record.
	 * 
	 * @param recNo
	 *            The record number of the record to be unlocked
	 * @param cookie
	 *            A <code>lockCookie</code> previously provided by this class to
	 *            ensure only one user can update a record at a time
	 * @throws SecurityException
	 *             Signals that user is attempting to unlock a record with an
	 *             invalid <code>lockCookie</code>
	 */
	public synchronized void unlock(final long recNo, final long cookie)
			throws SecurityException {
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
	}

	/**
	 * Checks if a lock exists for a given record.
	 * 
	 * @param recNo
	 *            The record number of the record to be checked
	 * @return true, if record is locked
	 */
	public boolean isLocked(final long recNo) {
		return this.lockedRecords.containsKey(recNo);
	}

	/**
	 * Gets the lockCookie associated with a record
	 * 
	 * @param recNo
	 *            The record number of the record to be checked
	 * @return The <code>lockCookie</code> of the record if it is locked,
	 *         otherwise <code>null</code>
	 */
	public Long getOwner(final long recNo) {
		return this.lockedRecords.get(recNo);
	}
}
