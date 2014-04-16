package suncertify.db;

/**
 * @author Luke GJ Potter
 * Date: 27/03/2014
 */
class DatabaseAccessLockManager {

    /**
     * Locks a record so that it can only be updated or deleted by this client. Returned value is a cookie that must be
     * used when the record is unlocked, updated, or deleted. If the specified record is already locked by a different
     * client, the current thread gives up the CPU and consumes no CPU cycles until the record is unlocked.
     *
     * @param recNo
     * @return
     * @throws RecordNotFoundException
     */
    public static long lock(long recNo) throws RecordNotFoundException {
        return 0;
    }

    /**
     * Releases the lock on a record. Cookie must be the cookie returned when the record was locked; otherwise throws
     * SecurityException.
     *
     * @param recNo
     * @param cookie
     * @throws SecurityException
     */
    public static void unlock(long recNo, long cookie) throws SecurityException {

    }
}
