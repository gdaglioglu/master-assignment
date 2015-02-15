package suncertify.db;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * This class implements the RemoteDBMain interface, it allows me to throw
 * RemoteExceptions on the methods that are in DBMain
 *
 * @author Gokhan Daglioglu
 */
public class RemoteDBMainImpl implements RemoteDBMain {

	/**
	 * Logger instance to pass messages through
	 */
	private Logger logger = Logger.getLogger("suncertify.db");
	/**
	 * Create instance of Data using the DBMain Interface
	 */
	private DBMain database = null;

	/**
	 * Constructor creates a new data instance and passes in the location of the
	 * database.
	 * 
	 * @param dbLocation
	 * @throws RemoteException
	 */
	public RemoteDBMainImpl(String dbLocation) throws RemoteException {

		database = new Data(dbLocation);

	}

	/**
	 * Reads a record from the file. Returns an array where each element is a
	 * record value.
	 * 
	 * @param recNo
	 * @return A string array containing the records
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException,
			RemoteException {
		return database.read(recNo);
	}

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n].
	 * 
	 * @param recNo
	 * @param data
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public void update(int recNo, String[] data)
			throws RecordNotFoundException, RemoteException {
		database.update(recNo, data);
	}

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse.
	 * 
	 * @param recNo
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException,
			RemoteException {
		database.delete(recNo);
	}

	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n]. (For example,
	 * "Fred" matches "Fred" or "Freddy".)
	 * 
	 * @param criteria
	 * @return An integer array containing the record numbers matching the
	 *         search criteria
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException,
			RemoteException {
		return database.find(criteria);
	}

	/**
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 * 
	 * @param data
	 * @return an integer signifying the record number of the newly created
	 *         record
	 * @throws DuplicateKeyException
	 * @throws RemoteException
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException,
			RemoteException {
		return database.create(data);
	}

	/**
	 * Locks a record so that it can only be updated or deleted by this client.
	 * If the specified record is already locked, the current thread gives up
	 * the CPU and consumes no CPU cycles until the record is unlocked.
	 * 
	 * @param recNo
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException, RemoteException {
		database.lock(recNo);
	}

	/**
	 * Releases the lock on a record.
	 * 
	 * @param recNo
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException,
			RemoteException {
		database.unlock(recNo);
	}

	/**
	 * Determines if a record is currently locked. Returns true if the record is
	 * locked, false otherwise.
	 * 
	 * @param recNo
	 * @return a boolean signifying if the record is locked or not
	 * @throws RecordNotFoundException
	 * @throws RemoteException
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException,
			RemoteException {
		return database.isLocked(recNo);
	}

}
