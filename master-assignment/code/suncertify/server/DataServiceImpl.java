package suncertify.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.DAOFactory;
import suncertify.db.DBMain;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.HotelRoom;

/**
 * This class is the main implementation of the Server interface.
 * 
 * @author Gokhan Daglioglu
 */
public class DataServiceImpl implements DataService {

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.server</code>.
	 */
	private Logger logger = Logger.getLogger(DataServiceImpl.class.getPackage().getName());

	/**
	 * The instance to {@link DBMain}.
	 */
	private final DBMain data;

	/**
	 * Construct a new {@link DataService} object that acts as an adapter for
	 * {@link DBMain}.
	 */
	public DataServiceImpl() {
		this.data = DAOFactory.getDataService();
		logger.log(Level.FINE, "Initialized DataServiceImpl");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HotelRoom read(final int recNo) throws RecordNotFoundException {
		this.data.lock(recNo);
		final HotelRoom record = new HotelRoom(this.data.read(recNo));
		this.data.unlock(recNo);
		logger.log(Level.FINER, "Read the hotel room object with recNo: " + recNo);
		return record;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void update(final int recNo, final HotelRoom data) throws RecordNotFoundException {
		this.data.lock(recNo);
		this.data.update(recNo, data.toArray());
		this.data.unlock(recNo);
		logger.log(Level.FINER, "Updated the hotel room object with recNo: " + recNo
				+ " with data: " + data.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		this.data.lock(recNo);
		this.data.delete(recNo);
		this.data.unlock(recNo);
		logger.log(Level.FINER, "Deleted the hotel room object with recNo: " + recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<HotelRoom> find(final String[] criteria, final boolean findExactMatches) {
		final List<HotelRoom> finalResults = new ArrayList<HotelRoom>();
		int[] rawResults = new int[0];
		try {
			rawResults = this.data.find(criteria);
		} catch (final RecordNotFoundException e) {
			// cannot happen
		}

		for (final int recNo : rawResults) {
			try {
				final HotelRoom record = this.read(recNo);
				if (!findExactMatches || this.isExactMatch(record, criteria)) {
					finalResults.add(record);
				}
			} catch (final RecordNotFoundException e) {
				logger.log(Level.FINER, "Record has been deleted, ignoring it");
			}
		}
		return finalResults;
	}

	/**
	 * This method is used to determine if a {@link HotelRoom} exactly matches
	 * search criteria.
	 * 
	 * @param record
	 *            The {@link HotelRoom} to check.
	 * @param criteria
	 *            The search criteria used to check the {@link HotelRoom}.
	 * @return true if the {@link HotelRoom} is an exact match otherwise false.
	 */
	private boolean isExactMatch(final HotelRoom record, final String[] criteria) {
		final String[] dataArray = record.toArray();
		for (int i = 0; i < criteria.length; i++) {
			if ((criteria[i] != null) && !criteria[i].equals("")
					&& !dataArray[i].equals(criteria[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int create(final String[] data) throws DuplicateKeyException {
		final int recNo = this.data.create(data);
		logger.log(Level.FINER, "Created the hotel room object with recNo: " + recNo
				+ " with data: " + data.toString());
		return recNo;
	}
}