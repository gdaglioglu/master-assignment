/*
 * 
 */
package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

// TODO: Auto-generated Javadoc
/**
 * The Interface RemoteDBAccess.
 */
public interface RemoteDBAccess extends Remote {

	/**
	 * Read record.
	 *
	 * @param recNo the rec no
	 * @return the string[]
	 * @throws RecordNotFoundException the record not found exception
	 * @throws RemoteException the remote exception
	 */
	public String[] readRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * Update record.
	 *
	 * @param recNo the rec no
	 * @param data the data
	 * @param lockCookie the lock cookie
	 * @throws RecordNotFoundException the record not found exception
	 * @throws SecurityException the security exception
	 * @throws RemoteException the remote exception
	 */
	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException;

	/**
	 * Delete record.
	 *
	 * @param recNo the rec no
	 * @param lockCookie the lock cookie
	 * @throws RecordNotFoundException the record not found exception
	 * @throws SecurityException the security exception
	 * @throws RemoteException the remote exception
	 */
	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException;

	/**
	 * Find by criteria.
	 *
	 * @param criteria the criteria
	 * @return the long[]
	 * @throws RemoteException the remote exception
	 */
	public long[] findByCriteria(String[] criteria) throws RemoteException;

	/**
	 * Creates the record.
	 *
	 * @param data the data
	 * @return the long
	 * @throws DuplicateKeyException the duplicate key exception
	 * @throws RemoteException the remote exception
	 */
	public long createRecord(String[] data) throws DuplicateKeyException,
			RemoteException;

	/**
	 * Lock record.
	 *
	 * @param recNo the rec no
	 * @return the long
	 * @throws RecordNotFoundException the record not found exception
	 * @throws RemoteException the remote exception
	 */
	public long lockRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * Unlock.
	 *
	 * @param recNo the rec no
	 * @param cookie the cookie
	 * @throws SecurityException the security exception
	 * @throws RemoteException the remote exception
	 */
	public void unlock(long recNo, long cookie) throws SecurityException,
			RemoteException;
}