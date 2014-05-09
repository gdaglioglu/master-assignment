package suncertify.rmi;

import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Luke GJ Potter
 *         Date: 07/05/2014
 */
public class DatabaseAccessRemoteImpl extends UnicastRemoteObject implements DatabaseAccessRemote {

    DatabaseAccessRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public String[] readRecord(long recNo) throws RecordNotFoundException, RemoteException {
        return new Data().readRecord(recNo);
    }

    @Override
    public void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException {
        new Data().updateRecord(recNo, data, lockCookie);
    }

    @Override
    public void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException {
        new Data().deleteRecord(recNo, lockCookie);
    }

    @Override
    public long[] findByCriteria(String[] criteria) throws RemoteException {
        return new Data().findByCriteria(criteria);
    }

    @Override
    public long createRecord(String[] data) throws DuplicateKeyException, RemoteException {
        return new Data().createRecord(data);
    }

    @Override
    public long lockRecord(long recNo) throws RecordNotFoundException, RemoteException {
        return new Data().lockRecord(recNo);
    }

    @Override
    public void unlock(long recNo, long cookie) throws SecurityException, RemoteException {
        new Data().unlock(recNo, cookie);
    }
}
