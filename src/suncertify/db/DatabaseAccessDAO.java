package suncertify.db;

import suncertify.exceptions.DuplicateKeyException;
import suncertify.exceptions.RecordNotFoundException;

/**
 * Created by lukepotter on 27/03/2014.
 */
public class DatabaseAccessDAO implements DBAccess {

    @Override
    public String[] readRecord(long recNo) throws RecordNotFoundException {
        return DatabaseAccessCRUD.readRecord(recNo);
    }

    @Override
    public void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException {
        DatabaseAccessCRUD.updateRecord(recNo, data, lockCookie);
    }

    @Override
    public void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException, SecurityException {
        DatabaseAccessCRUD.deleteRecord(recNo, lockCookie);
    }

    @Override
    public long[] findByCriteria(String[] criteria) {
        return DatabaseAccessSearch.findByCriteria(criteria);
    }

    @Override
    public long createRecord(String[] data) throws DuplicateKeyException {
        return DatabaseAccessCRUD.createRecord(data);
    }

    @Override
    public long lockRecord(long recNo) throws RecordNotFoundException {
        return DatabaseAccessLockManager.lock(recNo);
    }

    @Override
    public void unlock(long recNo, long cookie) throws SecurityException {
        DatabaseAccessLockManager.unlock(recNo, cookie);
    }
}
