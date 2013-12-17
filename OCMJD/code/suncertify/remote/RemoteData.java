/*
 * RemoteData
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.*;

/**
 * An implementation of <code>RemoteDBAccess</code> that wraps the
 * <code>DBAccess</code> interface to connect to a local datafile
 * 
 * @author Eoin Mooney
 */
public class RemoteData extends UnicastRemoteObject implements RemoteDBAccess {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = 8217941285842635667L;

	/**
	 * The <code>DBAccess</code> instance used to interact with a local datafile
	 */
	private static DBAccess database = null;

	/**
	 * Instantiates a new <code>RemoteData</code> and creates the new
	 * <code>Data</code> instance to communicate with the datafile
	 * 
	 * @param dbLocation
	 *            The local location of the datafile
	 * @throws RemoteException
	 *             the remote exception
	 */
	public RemoteData(final String dbLocation) throws RemoteException {
		RemoteData.database = new Data(dbLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#readRecord(long)
	 */
	@Override
	public String[] readRecord(final long recNo)
			throws RecordNotFoundException, RemoteException {
		return RemoteData.database.readRecord(recNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#updateRecord(long,
	 * java.lang.String[], long)
	 */
	@Override
	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException, RemoteException {
		RemoteData.database.updateRecord(recNo, data, lockCookie);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#deleteRecord(long, long)
	 */
	@Override
	public void deleteRecord(final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException {
		RemoteData.database.deleteRecord(recNo, lockCookie);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#findByCriteria(java.lang.String[])
	 */
	@Override
	public long[] findByCriteria(final String[] criteria)
			throws RemoteException {
		return RemoteData.database.findByCriteria(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#createRecord(java.lang.String[])
	 */
	@Override
	public long createRecord(final String[] data) throws DuplicateKeyException,
			RemoteException {
		return RemoteData.database.createRecord(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#lockRecord(long)
	 */
	@Override
	public long lockRecord(final long recNo) throws RecordNotFoundException,
			RemoteException {
		return RemoteData.database.lockRecord(recNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.RemoteDBAccess#unlock(long, long)
	 */
	@Override
	public void unlock(final long recNo, final long cookie)
			throws SecurityException, RemoteException {
		RemoteData.database.unlock(recNo, cookie);
	}
}
