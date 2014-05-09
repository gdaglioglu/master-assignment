package suncertify.rmi;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Luke GJ Potter
 *         Date: 07/05/2014
 */
public interface DatabaseAccessRemote extends Remote {

    /**
     * Reads a record from the file. Returns an array where each element is a
     * record value.
     *
     * @param recNo the record number
     * @return a string array of the records fields
     * @throws RecordNotFoundException
     * @throws RemoteException
     */
    public String[] readRecord(long recNo) throws RecordNotFoundException, RemoteException;

    /**
     * Modifies the fields of a record. The new value for field n appears in
     * data[n]. Throws SecurityException if the record is locked with a cookie
     * other than lockCookie.
     *
     * @param recNo the record number
     * @param data a string array of the fields of the record
     * @param lockCookie the cookie that the record is locked with
     * @throws RecordNotFoundException
     * @throws SecurityException
     * @throws RemoteException
     */
    public void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException;

    /**
     * Deletes a record, making the record number and associated disk storage
     * available for reuse. Throws SecurityException if the record is locked
     * with a cookie other than lockCookie.
     *
     * @param recNo the record number
     * @param lockCookie the cookie that the record is locked with
     * @throws RecordNotFoundException
     * @throws SecurityException
     * @throws RemoteException
     */
    public void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException;

    /**
     * Returns an array of record numbers that match the specified criteria.
     * Field n in the database file is described by criteria[n]. A null value in
     * criteria[n] matches any field value. A non-null  value in criteria[n]
     * matches any field value that begins with criteria[n]. (For example,
     * "Fred" matches "Fred" or "Freddy".)
     *
     * @param criteria a string array of criteria to match against the database
     * @return an array of record numbers that match the criteria
     * @throws RemoteException
     */
    public long[] findByCriteria(String[] criteria) throws RemoteException;

    /**
     * Creates a new record in the database (possibly reusing a deleted entry).
     * Inserts the given data, and returns the record number of the new record.
     *
     * @param data a string array of the fields of the record
     * @return the record number of the created record
     * @throws DuplicateKeyException
     * @throws RemoteException
     */
    public long createRecord(String[] data) throws DuplicateKeyException, RemoteException;

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated, or deleted. If the specified record is already locked by a
     * different client, the current thread gives up the CPU and consumes no CPU
     * cycles until the record is unlocked.
     *
     * @param recNo the record number
     * @return the cookie that the row was locked with
     * @throws RecordNotFoundException
     * @throws RemoteException
     */
    public long lockRecord(long recNo) throws RecordNotFoundException, RemoteException;

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when
     * the record was locked; otherwise throws SecurityException.
     *
     * @param recNo the record number
     * @param cookie the cookie that the record is locked with
     * @throws SecurityException
     * @throws RemoteException
     */
    public void unlock(long recNo, long cookie) throws SecurityException, RemoteException;
}
