package suncertify.db;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.application.Utils;

/**
 * The LockManager class logically locks records.
 * This is necessary when an attempt is made to
 * update or delete a record to ensure a record
 * is being edited by only one thread at a time.
 * 
 * @author john
 *
 */
public class LockManager {
    
    /**
     * Mutex object used to synchronize on the 
     * HashMap that maps locked record numbers
     * to the cookie they are locked with.
     */
    private static final String MUTEX = "MUTEX";
    
    /**
     * Maps the numbers of records that are locked to
     * the cookie that they are locked with.
     */
    private static final Map<Integer, Long> LOCKMAP
            = new HashMap<Integer, Long>();
    
    /**
     * The number of cookies that have been generated.
     */
    private static long cookieCount = 0L;
    
    /**
     * Logger for the LockManager class.
     */
    private final Logger log = Logger.getLogger(LockManager.class.getName());
    
    /**
     * Class constructor.
     */
    public LockManager() {
        Utils.setLogLevel(log, Level.FINER);
    }
    
    /**
     * Locks a record by adding the record's number to the LOCKMAP
     * HashMap as key, with the generated cookie as value.  If a 
     * thread tries to obtain a lock on a record that is already
     * locked, the thread enters an inactive state consuming no
     * CPU cycles until it has been notified that a record has been
     * unlocked, at which point it will attempt to lock the record 
     * again.
     * @param recNo the number of the record to be locked.
     * @return the cookie that the record has been locked with.
     */
    public long lockRecord(int recNo) {
        String threadName = Thread.currentThread().getName();
        
        synchronized (MUTEX) {
            while (LOCKMAP.containsKey(recNo)) {
                log.info(threadName + ": Record number " + recNo + " is locked.  waiting...");
                try {
                    MUTEX.wait();
                } catch (InterruptedException e) {
                    log.throwing("LockManager.java", "lockRecord", e);
                    // NEED TO CLARIFY
                    Thread.currentThread().interrupt();
                }
            }
            long cookie = generateCookie();
            
            log.info("Generated cookie for recNo "  + recNo + ": thread: " 
                     + threadName + " cookie: " + cookie);
            
            LOCKMAP.put(recNo, cookie);
            log.info(threadName + ": Locked record number " + recNo);
            
            return cookie;
        }
    }
    
    /**
     * Unlocks a record by removing the record number from the 
     * LOCKMAP HashMap.  Other threads are notified that a record
     * has been unlocked.
     * @param recNo the number of the record to be unlocked.
     * @param cookie the cookie that the record was locked with.
     * @throws SecurityException if the value of cookie parameter
     * 			is not the same value as the cookie that the record was 
     * 			locked with.
     */
    public void unlockRecord(int recNo, long cookie) throws SecurityException {
        String threadName = Thread.currentThread().getName();
        
        synchronized (MUTEX) {
            if (LOCKMAP.get(recNo) == cookie) {
                log.info(threadName + ": Unlocking record number " + recNo);
                LOCKMAP.remove(recNo);
                MUTEX.notifyAll();
                log.info(threadName + ": Notifying threads that record number " + recNo + " is unlocked");
            }
            else {
                log.warning("An illegal attempt was made to unlock record number " + recNo);
                throw new SecurityException();
            }
        }
    }
    
    /**
     * Generates a lock cookie.
     * @return lock cookie.
     */
    private long generateCookie() {
        cookieCount += 1;
        return System.currentTimeMillis() + cookieCount;
    }
    
    /**
     * Gets the HashMap that maps locked record numbers
     * to the cookies they are locked with.
     * @return LOCKMAP
     */
    public Map<Integer, Long> getLockMap() {
        return LOCKMAP;
    }

}
