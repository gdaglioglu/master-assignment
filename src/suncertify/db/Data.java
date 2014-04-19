package suncertify.db;

/**
 * @author Luke GJ Potter
 * Date: 27/03/2014
 */
public class Data implements DBAccess {

    @Override
    public String[] readRecord(long recNo) throws RecordNotFoundException {
        return DatabaseAccessCrudOperations.readRecord(recNo);
    }

    @Override
    public void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException {
        DatabaseAccessCrudOperations.updateRecord(recNo, data, lockCookie);
    }

    @Override
    public void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException, SecurityException {
        DatabaseAccessCrudOperations.deleteRecord(recNo, lockCookie);
    }

    @Override
    public long[] findByCriteria(String[] criteria) {
        return DatabaseAccessSearch.findByCriteria(criteria);
    }

    @Override
    public long createRecord(String[] data) throws DuplicateKeyException {
        return DatabaseAccessCrudOperations.createRecord(data);
    }

    @Override
    public long lockRecord(long recNo) throws RecordNotFoundException {
        return DatabaseAccessLockManager.getInstance().lock(recNo);
    }

    @Override
    public void unlock(long recNo, long cookie) throws SecurityException {
        DatabaseAccessLockManager.getInstance().unlock(recNo, cookie);
    }
}
