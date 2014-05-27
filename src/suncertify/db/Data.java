package suncertify.db;

/**
 * This is a Facade for the implementation of the {@link suncertify.db.DBAccess}
 * interface.
 *
 * @author Luke GJ Potter
 * @since 27/03/2014
 */
public class Data implements DBAccess {

    /**
     * Creates a new record in the database. Inserts the given data, and returns
     * the record number of the new record.
     *
     * @param data The string array representation of a database record.
     * @return The record number of the position that the record was created.
     * @throws DuplicateKeyException When trying to create a record that already
     *                               exists in the database.
     */
    @Override
    public long createRecord(String[] data) throws DuplicateKeyException {
        return DatabaseAccessCrudOperations.createRecord(data);
    }

    /**
     * Reads a record from the file. Returns an array where each element is a
     * record field.
     *
     * @param recNo The record number in the database to retrieve.
     * @return A String array representation of the database record.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     */
    @Override
    public String[] readRecord(long recNo) throws RecordNotFoundException {
        return DatabaseAccessCrudOperations.readRecord(recNo);
    }

    /**
     * Modifies the fields of a record. The new value for field n appears in
     * data[n]. Throws SecurityException if the record is locked with a cookie
     * other than lockCookie.
     *
     * @param recNo      The record number in the database to update.
     * @param data       The string array representation of a database record,
     *                   containing the updates.
     * @param lockCookie The cookie that the row is locked with.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     * @throws SecurityException       If the record is locked by a user other
     *                                 than the user trying to update the
     *                                 record.
     */
    @Override
    public void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException {
        DatabaseAccessCrudOperations.updateRecord(recNo, data, lockCookie);
    }

    /**
     * Deletes a record, making the record number and associated disk storage
     * unavailable for reuse by setting an "invalid" flag for the record. Throws
     * SecurityException if the record is locked with a cookie other than
     * lockCookie.
     *
     * @param recNo      The record number in the database to delete.
     * @param lockCookie The cookie that the row is locked with.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     * @throws SecurityException       If the record is locked by a user other
     *                                 than the user trying to update the
     *                                 record.
     */
    @Override
    public void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException, SecurityException {
        DatabaseAccessCrudOperations.deleteRecord(recNo, lockCookie);
    }

    /**
     * Returns an array of record numbers that match the specified
     * {@code criteria}. Field {@code n} in the database file is described by
     * {@code criteria[n]}. A null value in {@code criteria[n]} matches any
     * field value. A non-null value in {@code criteria[n]} matches any field
     * value that begins with {@code criteria[n]}.
     * <p/>
     * For example, "Fred" matches "Fred" or "Freddy".
     *
     * @param criteria The search criteria to match against.
     * @return An array of record numbers that match the {@code criteria}.
     */
    @Override
    public long[] findByCriteria(String[] criteria) {
        return DatabaseAccessSearch.findByCriteria(criteria);
    }

    /**
     * Locks a record so that it can only be updated or deleted by this client.
     * Returned value is a cookie that must be used when the record is unlocked,
     * updated, or deleted. If the specified record is already locked by a
     * different client, the current thread gives up the CPU and consumes no CPU
     * cycles until the record is unlocked.
     *
     * @param recNo the record number to lock
     * @return A long representing the lock's owner's id.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     */
    @Override
    public long lockRecord(long recNo) throws RecordNotFoundException {
        return DatabaseAccessLockManager.getInstance().lock(recNo);
    }

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when
     * the record was locked; otherwise throws {@code SecurityException}.
     *
     * @param recNo  the record number to unlock
     * @param cookie the cookie to unlock the record with
     * @throws SecurityException If the record is locked by a user other than
     *                           the user trying to update the record.
     */
    @Override
    public void unlock(long recNo, long cookie) throws SecurityException {
        DatabaseAccessLockManager.getInstance().unlock(recNo, cookie);
    }
}
