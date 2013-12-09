package suncertify.remote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import suncertify.db.*;

public class RemoteData extends UnicastRemoteObject implements RemoteDBAccess {

	// TODO serialVersionUID
	private static final long serialVersionUID = 8217941285842635667L;

	private static DBAccess database = null;

	private Logger log = Logger.getLogger("suncertify.remote");

	public RemoteData(String dbLocation) throws RemoteException {
		log.entering("suncertify.remote.RemoteData", "RemoteData()");

		try {
			database = new Data(dbLocation);
		} catch (FileNotFoundException e) {
			throw new RemoteException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}

		log.exiting("suncertify.remote.RemoteData", "RemoteData()");
	}

	public String[] readRecord(long recNo) throws RecordNotFoundException,
			RemoteException {
		log.entering("suncertify.remote.RemoteData", "readRecord()", recNo);
		log.exiting("suncertify.remote.RemoteData", "readRecord()");

		return database.readRecord(recNo);
	}

	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException {
		log.entering("suncertify.remote.RemoteData", "updateRecord()");

		database.updateRecord(recNo, data, lockCookie);

		log.exiting("suncertify.remote.RemoteData", "updateRecord()");
	}

	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException {
		log.entering("suncertify.remote.RemoteData", "deleteRecord()");

		database.deleteRecord(recNo, lockCookie);

		log.exiting("suncertify.remote.RemoteData", "deleteRecord()");
	}

	public long[] findByCriteria(String[] criteria) throws RemoteException {
		log.entering("suncertify.remote.RemoteData", "findByCriteria()",
				criteria);
		log.exiting("suncertify.remote.RemoteData", "findByCriteria()");

		return database.findByCriteria(criteria);
	}

	public long createRecord(String[] data) throws DuplicateKeyException,
			RemoteException {
		log.entering("suncertify.remote.RemoteData", "createRecord()", data);
		log.exiting("suncertify.remote.RemoteData", "createRecord()");

		return database.createRecord(data);
	}

	public long lockRecord(long recNo) throws RecordNotFoundException,
			RemoteException {
		log.entering("suncertify.remote.RemoteData", "lockRecord()", recNo);
		log.exiting("suncertify.remote.RemoteData", "lockRecord()");

		return database.lockRecord(recNo);
	}

	public void unlock(long recNo, long cookie) throws SecurityException,
			RemoteException {
		log.entering("suncertify.remote.RemoteData", "unlock()");

		database.unlock(recNo, cookie);

		log.exiting("suncertify.remote.RemoteData", "unlock()");
	}

}
