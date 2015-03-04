package suncertify.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.HotelRoom;

/**
 * This is the main interface of the server side. It specifies how clients can
 * interact with the server.
 * 
 * @author Gokhan Daglioglu
 */
public interface DataService extends Remote {

	/**
	 * This method reads a {@link HotelRoom} object from the database.
	 * 
	 * @param recNo
	 *            The record number of the {@link HotelRoom}.
	 * @return The {@link HotelRoom} object.
	 * @throws RecordNotFoundException
	 *             When no {@link HotelRoom} was found with the specified record
	 *             number.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	HotelRoom read(final int recNo) throws RecordNotFoundException, RemoteException;

	/**
	 * This method will update the {@link HotelRoom} in the database.
	 * 
	 * @param recNo
	 *            The record to be updated.
	 * @param record
	 *            The {@link HotelRoom} object to update.
	 * @throws RecordNotFoundException
	 *             When no {@link HotelRoom} was found that matches the record
	 *             number of the specified {@link HotelRoom} object.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	void update(final int recNo, final HotelRoom record) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method deletes a {@link HotelRoom} from the database.
	 * 
	 * @param recNo
	 *            The record to be deleted.
	 * @throws RecordNotFoundException
	 *             When no {@link HotelRoom} was found that matches the record
	 *             number of the specified {@link HotelRoom} object.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	void delete(int recNo) throws RecordNotFoundException, RemoteException;

	/**
	 * This method searches the database for records that match the given search
	 * criteria.
	 * 
	 * @param criteria
	 *            An array with the search criteria for each field of a
	 *            {@link HotelRoom}.
	 * @param exactMatch
	 *            If the results should only contain {@link HotelRoom} whose
	 *            fields exactly match the search criteria.
	 * @return All the {@link HotelRoom} objects that match the criteria.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	List<HotelRoom> find(final String[] criteria, boolean exactMatch) throws RemoteException;

	/**
	 * This method will create a new {@link HotelRoom} in the database.
	 * 
	 * @param data
	 *            An array containing the fields used to create the
	 *            {@link HotelRoom}.
	 * @return The record number of the new {@link HotelRoom}.
	 * @throws DuplicateKeyException
	 *             If this {@link HotelRoom} already exists.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	int create(final String[] data) throws DuplicateKeyException, RemoteException;
}