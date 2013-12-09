package suncertify.remote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.*;

public class RemoteData extends UnicastRemoteObject implements RemoteDBAccess {

	// TODO serialVersionUID
	private static final long serialVersionUID = 8217941285842635667L;
	
	private static DBAccess database = null;

	public RemoteData(String dbLocation) throws RemoteException {
		try {
			database = new Data(dbLocation);
		} catch (FileNotFoundException e) {
			throw new RemoteException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
	public String[] readRecord(long recNo) throws RecordNotFoundException,
			RemoteException {
		return database.readRecord(recNo);
	}

	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException {
		database.updateRecord(recNo, data, lockCookie);
	}

	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException {
		database.deleteRecord(recNo, lockCookie);
	}

	public long[] findByCriteria(String[] criteria) throws RemoteException {
		return database.findByCriteria(criteria);
	}

	public long createRecord(String[] data) throws DuplicateKeyException,
			RemoteException {
		return database.createRecord(data);
	}

	public long lockRecord(long recNo) throws RecordNotFoundException,
			RemoteException {
		return database.lockRecord(recNo);
	}

	public void unlock(long recNo, long cookie) throws SecurityException,
			RemoteException {
		database.unlock(recNo, cookie);
	}

}
