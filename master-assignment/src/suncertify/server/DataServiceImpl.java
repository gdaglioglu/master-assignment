package suncertify.server;

import java.util.ArrayList;
import java.util.List;

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

	private final DBMain data;

	/**
	 * Construct a new {@link DataService} object that acts as an adapter for
	 * {@link DBMain}.
	 */
	public DataServiceImpl() {
		this.data = DAOFactory.getDataService();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HotelRoom read(final int recNo) throws RecordNotFoundException {
		this.data.lock(recNo);
		final HotelRoom record = new HotelRoom(this.data.read(recNo));
		this.data.unlock(recNo);

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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		this.data.lock(recNo);
		this.data.delete(recNo);
		this.data.unlock(recNo);
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
				// record has been deleted, ignore it
			}
		}
		return finalResults;
	}

	/**
	 * This method is used to determine if a Contractor exactly matches search
	 * criteria.
	 * 
	 * @param record
	 *            The Contractor to check.
	 * @param criteria
	 *            The search criteria used to check the Contractor.
	 * @return true if the Contractor is an exact match otherwise false.
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
		return recNo;
	}
}