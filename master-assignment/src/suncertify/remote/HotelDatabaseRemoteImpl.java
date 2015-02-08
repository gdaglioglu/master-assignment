package suncertify.remote;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.RemoteDBMain;
import suncertify.db.RemoteDBMainImpl;

/**
 * A HotelDatabaseImpl object is the implementation of the HotelDatabaseRemote
 * interface. This class is an RMI implementation.
 *
 * A HotelDatabaseImpl object contains a reference to the db. This class acts
 * primarily as a wrapper for the database.
 *
 * @author Gokhan Daglioglu
 * @see suncertify.remote.HotelDatabaseRemote
 */
public class HotelDatabaseRemoteImpl extends UnicastRemoteObject implements
		HotelDatabaseRemote {
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 5165L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.remote</code>.
	 */
	private static Logger logger = Logger.getLogger("suncertify.remote");

	/**
	 * The database handle.
	 */
	private RemoteDBMain db = null;

	/**
	 * HotelDatabaseImpl default constructor.
	 *
	 * @param dbLocation
	 *            the location of the database.
	 * @throws RemoteException
	 *             Thrown if a <code>HotelDatabaseImpl</code> instance cannot be
	 *             created.
	 */
	public HotelDatabaseRemoteImpl(String dbLocation) throws RemoteException {

		try {
			db = new RemoteDBMainImpl(dbLocation);
		} catch (IOException iex) {
			logger.log(Level.SEVERE, "I/O problem with file at: " + dbLocation
					+ "Exception is: " + iex.getMessage());
		}

	}

	/**
	 * Reads a record from the file. Returns an array where each element is a
	 * record value.
	 * 
	 * @param recNo
	 * @return a <code>String[]</code> holding the field values of a record
	 * @throws RecordNotFoundException
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		try {
			return db.read(recNo);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on read, Exception is: "
					+ rex.getMessage());
			return new String[0];
		}
	}

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n].
	 * 
	 * @param recNo
	 * @param data
	 * @throws RecordNotFoundException
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		try {
			db.update(recNo, data);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on update, Exception is: "
					+ rex.getMessage());
		}
	}

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse.
	 * 
	 * @param recNo
	 * @throws RecordNotFoundException
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		try {
			db.delete(recNo);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on delete, Exception is: "
					+ rex.getMessage());
		}
	}

	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n]. (For example,
	 * "Fred" matches "Fred" or "Freddy".)
	 * 
	 * @param criteria
	 * @return an <code>int[]</code> holding record numbers that correspond to
	 *         the records in the database
	 * @throws RecordNotFoundException
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		try {
			return db.find(criteria);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on find, Exception is: "
					+ rex.getMessage());
			return new int[0];
		}
	}

	/**
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 * 
	 * @param data
	 * @return the record number of the newly created record
	 * @throws DuplicateKeyException
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException {
		try {
			return db.create(data);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on create, Exception is: "
					+ rex.getMessage());
			return 0;
		}
	}

	/**
	 * Locks a record so that it can only be updated or deleted by this client.
	 * If the specified record is already locked, the current thread gives up
	 * the CPU and consumes no CPU cycles until the record is unlocked.
	 * 
	 * @param recNo
	 * @throws RecordNotFoundException
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		try {
			db.lock(recNo);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on lock, Exception is: "
					+ rex.getMessage());
		}
	}

	/**
	 * Releases the lock on a record.
	 * 
	 * @param recNo
	 * @throws RecordNotFoundException
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		try {
			db.unlock(recNo);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on unlock, Exception is: "
					+ rex.getMessage());
		}
	}

	/**
	 * Determines if a record is currently locked. Returns true if the record is
	 * locked, false otherwise.
	 * 
	 * @param recNo
	 * @return boolean signifying record lock status
	 * @throws RecordNotFoundException
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		try {
			return db.isLocked(recNo);
		} catch (RemoteException rex) {
			logger.log(Level.SEVERE, "I/O problem on create, Exception is: "
					+ rex.getMessage());
			return true;
		}
	}
}
