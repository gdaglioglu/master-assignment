package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

public interface RemoteDBAccess extends Remote {
	public String[] readRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException;

	public void deleteRecord(long recNo, long lockCookie)
			throws RecordNotFoundException, SecurityException, RemoteException;

	public long[] findByCriteria(String[] criteria) throws RemoteException;

	public long createRecord(String[] data) throws DuplicateKeyException,
			RemoteException;

	public long lockRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	public void unlock(long recNo, long cookie) throws SecurityException,
			RemoteException;

}