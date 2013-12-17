/*
 * 
 */
package suncertify.db;

// TODO: Auto-generated Javadoc
/**
 * The Class Data.
 */
public class Data implements DBAccess {

	/** The database. */
	private static DataFileAccess database = null;

	/** The locks. */
	private static DataLockManager locks = DataLockManager.getInstance();

	/**
	 * Instantiates a new data.
	 *
	 * @param dbLocation the db location
	 */
	public Data(final String dbLocation) {
		Data.database = new DataFileAccess(dbLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#readRecord(long)
	 */
	@Override
	public String[] readRecord(final long recNo) throws RecordNotFoundException {
		return Data.database.readRecord(recNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#updateRecord(long, java.lang.String[], long)
	 */
	@Override
	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {

		Data.database.updateRecord(recNo, data, lockCookie);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#deleteRecord(long, long)
	 */
	@Override
	public void deleteRecord(final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {

		Data.database.deleteRecord(recNo, lockCookie);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#findByCriteria(java.lang.String[])
	 */
	@Override
	public long[] findByCriteria(final String[] criteria) {
		return Data.database.findByCriteria(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#createRecord(java.lang.String[])
	 */
	@Override
	public long createRecord(final String[] data) throws DuplicateKeyException {
		return Data.database.createRecord(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#lockRecord(long)
	 */
	@Override
	public long lockRecord(final long recNo) throws RecordNotFoundException {
		return Data.locks.lockRecord(recNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.db.DBAccess#unlock(long, long)
	 */
	@Override
	public void unlock(final long recNo, final long cookie)
			throws SecurityException {
		Data.locks.unlock(recNo, cookie);
	}
}
