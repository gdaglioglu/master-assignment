package suncertify.controller;

import suncertify.db.RecordNotFoundException;
import suncertify.model.HotelRoom;
import suncertify.rmi.DatabaseAccessRemote;
import suncertify.rmi.RmiClientManager;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The Remote DAO will handle the requests across the network.
 *
 * @author Luke GJ Potter
 *         Date: 08/05/2014
 */
public class DatabaseAccessDaoRemote implements DatabaseAccessDao {

    /**
     * The reference to the class that directly manipulates the String Arrays in
     * the database file, via the network.
     */
    private final DatabaseAccessRemote databaseAccessRemote;

    /**
     * The default constructor for the {@code DatabaseAccessDaoRemote} class.
     */
    public DatabaseAccessDaoRemote() throws RemoteException {

        databaseAccessRemote = RmiClientManager.connectToRemoteServerViaRmi();
    }

    /**
     * Finds the {@code HotelRoom} records that match the arguments.
     *
     * @param searchStrings The criteria to compare the database records to.
     * @return An ArrayList of the {@code HotelRoom} object that match the
     *         criteria.
     */
    @Override public ArrayList<HotelRoom> findHotelRooms(String... searchStrings) {

        try {
            long[] recordNumbers = databaseAccessRemote.findByCriteria(searchStrings);
            ArrayList<HotelRoom> hotelRooms = null;

            for(long recordNumber : recordNumbers) {

                HotelRoom hotelRoom = retrieveHotelRoom(recordNumber);

                if (hotelRoom != null) {

                    // Initialise the hotelRooms ArrayList the first time we find a hotelRoom to add to the list.
                    if (hotelRooms == null) {
                        hotelRooms = new ArrayList<HotelRoom>();
                    }

                    hotelRooms.add(hotelRoom);
                }
            }

            return hotelRooms;

        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Gets all the {@code HotelRoom} objects in the database.
     *
     * @return An ArrayList of the all the {@code HotelRoom} objects in the
     *         database.
     */
    @Override public ArrayList<HotelRoom> retrieveAllHotelRooms() {

        ArrayList<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();
        HotelRoom tempHotelRoom;

        for (long recordNumber = 0; (tempHotelRoom = retrieveHotelRoom(recordNumber)) != null; recordNumber++) {
            hotelRooms.add(tempHotelRoom);
        }

        return hotelRooms;
    }

    /**
     * This method allows the booking of a {@code HotelRoom}.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @param customerName The CSR number of the booker.
     * @param endDate The string representation of the date when the booking is due to expire.
     * @param lockCookie The key to lock the record with.
     * @return True, if the booking was successful.
     *         False, if the booking was not successful.
     */
    @Override public boolean bookHotelRoom(long recordNumber, String customerName, String endDate, long lockCookie) {

        HotelRoom hotelRoomToBook;

        if ((hotelRoomToBook = retrieveHotelRoom(recordNumber)) == null) {
            return false;
        }

        hotelRoomToBook.setOwnerName(customerName);
        hotelRoomToBook.setDate(endDate);

        return updateHotelRoom(recordNumber, hotelRoomToBook, lockCookie);
    }

    // ----- Private Methods -----
    /**
     * Retrieves a {@code HotelRoom} object from the database.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @return The {@code HotelRoom} object that corresponds to the
     *         {@code recordNumber}. Or NULL if there was a problem retrieving
     *         the record from the database.
     */
    private HotelRoom retrieveHotelRoom(long recordNumber) {

        try {
            String[] hotelRoomFieldsStrings = databaseAccessRemote.readRecord(recordNumber);
            if (hotelRoomFieldsStrings == null) {
                return null;
            }
            return new HotelRoom(hotelRoomFieldsStrings);
        } catch (RecordNotFoundException e) {
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Updates a {@code HotelRoom} record in the database.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @param updatedHotelRoom The {@code HotelRoom} pojo to Overwrite the
     *                         existing record.
     * @param lockCookie The key to lock the record with.
     * @return True, if the update was successful.
     *         False, if the update was not successful.
     */
    private boolean updateHotelRoom(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie) {

        try {
            databaseAccessRemote.updateRecord(recordNumber, updatedHotelRoom.toStringArray(), lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }
}
