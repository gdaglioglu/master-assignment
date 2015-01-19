/*
 * RemoteDBAccess
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

/**
 * A remote interface that mirrors the <code>DBAccess</code> interface and
 * extends <code>Remote</code>
 * 
 * @author Eoin Mooney
 */
public interface RemoteDBAccess extends Remote {

	/**
	 * Reads a record from the file. Returns an array where each element is a
	 * record value.
	 * 
	 * @param recNo
	 *            The record number of the record to be read
	 * @return A <code>String[]</code> with each <code>String</code> holding a
	 *         field of the record
	 * @throws RecordNotFoundException
	 *             Signals that no valid record exists with given record number
	 * @throws RemoteException
	 *             the remote exception
	 */
	public String[] readRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n]. Throws SecurityException if the record is locked with a cookie
	 * other than lockCookie.
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
	 * @throws RemoteException
	 *             the remote exception
	 */
	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException;

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse. Throws SecurityException if the record is locked
	 * with a cookie other than lockCookie.
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
	 * @throws RemoteException
	 *             the remote exception
	 */
	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException;

	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n]. (For example,
	 * "Fred" matches "Fred" or "Freddy".)
	 * 
	 * @param criteria
	 *            A <code>String[]</code> containing the <code>String</code> to
	 *            match each field on
	 * @return The list of record numbers that match the given criteria, if
	 *         criteria is <code>null</code> returns all valid records
	 * @throws RemoteException
	 *             the remote exception
	 */
	public long[] findByCriteria(String[] criteria) throws RemoteException;

	/**
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 * 
	 * @param data
	 *            A <code>String[]</code> with each <code>String</code> holding
	 *            a field of the record that will be written to the datafile
	 * @return The record number of the newly created record
	 * @throws DuplicateKeyException
	 *             the duplicate key exception
	 * @throws RemoteException
	 *             the remote exception
	 */
	public long createRecord(String[] data) throws DuplicateKeyException,
			RemoteException;

	/**
	 * Locks a record so that it can only be updated or deleted by this client.
	 * Returned value is a cookie that must be used when the record is unlocked,
	 * updated, or deleted. If the specified record is already locked by a
	 * different client, the current thread gives up the CPU and consumes no CPU
	 * cycles until the record is unlocked.
	 * 
	 * @param recNo
	 *            The record number of the record to be locked
	 * @return A <code>lockCookie</code> to ensure only one user can update a
	 *         record at a time
	 * @throws RecordNotFoundException
	 *             Signals that no valid record exists with given record number
	 * @throws RemoteException
	 *             the remote exception
	 */
	public long lockRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * Releases the lock on a record. Cookie must be the cookie returned when
	 * the record was locked; otherwise throws SecurityException.
	 * 
	 * @param recNo
	 *            The record number of the record to be unlocked
	 * @param cookie
	 *            A <code>lockCookie</code> previously provided by this class to
	 *            ensure only one user can update a record at a time
	 * @throws SecurityException
	 *             Signals that user is attempting to unlock a record with an
	 *             invalid <code>lockCookie</code>
	 * @throws RemoteException
	 *             the remote exception
	 */
	public void unlock(long recNo, long cookie) throws SecurityException,
			RemoteException;
}