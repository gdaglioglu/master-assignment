package suncertify.controller;

import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 31/03/2014
 */
public class DatabaseAccessDaoLocal implements DatabaseAccessDao {

    /*
     * The reference to the class that directly manipulates the String Arrays in
     * the database file.
     */
    private final Data databaseAccessFacade;

    /**
     * The default constructor for the {@code DatabaseAccessDaoLocal} class.
     */
    public DatabaseAccessDaoLocal() {

         databaseAccessFacade = new Data();
    }

    /**
     * Creates a new {@code HotelRoom} object in the database.
     *
     * @param newHotelRoom The {@code HotelRoom} to create in the database.
     * @return The record number of the newly created record. Or NULL if there
     *         was a problem creating the record in the database.
     */
    @Override public long createHotelRoom(HotelRoom newHotelRoom) {

        try {
            return databaseAccessFacade.createRecord(newHotelRoom.toStringArray());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retrieves a {@code HotelRoom} object from the database.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @return The {@code HotelRoom} object that corresponds to the
     *         {@code recordNumber}. Or NULL if there was a problem retrieving
     *         the record from the database.
     */
    @Override public HotelRoom retrieveHotelRoom(long recordNumber) {

        try {
            String[] hotelRoomFieldsStrings = databaseAccessFacade.readRecord(recordNumber);
            if (hotelRoomFieldsStrings == null) {
                return null;
            }
            return new HotelRoom(hotelRoomFieldsStrings);
        } catch (RecordNotFoundException e) {
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
    @Override public boolean updateHotelRoom(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie) {

        try {
            databaseAccessFacade.updateRecord(recordNumber, updatedHotelRoom.toStringArray(), lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    /**
     * Deletes a {@code HotelRoom} record in the database.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @param lockCookie The key to lock the record with.
     * @return True, if the deletion was successful.
     *         False, if the deletion was not successful.
     */
    @Override public boolean deleteHotelRoom(long recordNumber, long lockCookie) {

        try {
            databaseAccessFacade.deleteRecord(recordNumber, lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    /**
     * Finds the {@code HotelRoom} records that match the arguments.
     *
     * @param searchStrings The criteria to compare the database records to.
     * @return An ArrayList of the {@code HotelRoom} object that match the
     *         criteria.
     */
    @Override public ArrayList<HotelRoom> findHotelRooms(String... searchStrings) {

        long[] recordNumbers = databaseAccessFacade.findByCriteria(searchStrings);
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
     * @param startDate
     *@param endDate
     * @param lockCookie The key to lock the record with.  @return True, if the booking was successful.
     *         False, if the booking was not successful.
     */
    @Override public boolean bookHotelRoom(long recordNumber, String customerName, String startDate, String endDate, long lockCookie) {

        HotelRoom hotelRoomToBook;

        if ((hotelRoomToBook = retrieveHotelRoom(recordNumber)) == null) {
            return false;
        }

        hotelRoomToBook.setOwnerName(customerName);

        return updateHotelRoom(recordNumber, hotelRoomToBook, lockCookie);
    }

    /**
     * This method allows the cancellation of a {@code HotelRoom} booking.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @param lockCookie The key to lock the record with.
     * @return True, if the cancellation of the booking was successful.
     *         False, if the cancellation of the booking was not successful.
     */
    @Override public boolean cancelHotelRoomBooking(long recordNumber, long lockCookie) {

        HotelRoom hotelRoomBookingToCancel;

        if ((hotelRoomBookingToCancel = retrieveHotelRoom(recordNumber)) == null) {
            return false;
        }

        hotelRoomBookingToCancel.setOwnerName(null);

        return updateHotelRoom(recordNumber, hotelRoomBookingToCancel, lockCookie);
    }
}
