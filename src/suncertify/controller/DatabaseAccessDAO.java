package suncertify.controller;

import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 15/04/2014
 */
public interface DatabaseAccessDao {

    /**
     * Creates a new {@code HotelRoom} object in the database.
     *
     * @param newHotelRoom The {@code HotelRoom} to create in the database.
     * @return The record number of the newly created record. Or NULL if there
     *         was a problem creating the record in the database.
     */
    long createHotelRoom(HotelRoom newHotelRoom);

    /**
     * Retrieves a {@code HotelRoom} object from the database.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @return The {@code HotelRoom} object that corresponds to the
     *         {@code recordNumber}. Or NULL if there was a problem retrieving
     *         the record from the database.
     */
    HotelRoom retrieveHotelRoom(long recordNumber);

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
    boolean updateHotelRoom(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie);

    /**
     * Deletes a {@code HotelRoom} record in the database.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @param lockCookie The key to lock the record with.
     * @return True, if the deletion was successful.
     *         False, if the deletion was not successful.
     */
    boolean deleteHotelRoom(long recordNumber, long lockCookie);

    /**
     * Finds the {@code HotelRoom} records that match the arguments.
     *
     * @param searchStrings The criteria to compare the database records to.
     * @return An ArrayList of the {@code HotelRoom} object that match the
     *         criteria.
     */
    ArrayList<HotelRoom> findHotelRooms(String... searchStrings);

    /**
     * Gets all the {@code HotelRoom} objects in the database.
     *
     * @return An ArrayList of the all the {@code HotelRoom} objects in the
     *         database.
     */
    ArrayList<HotelRoom> retrieveAllHotelRooms();

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
    boolean bookHotelRoom(long recordNumber, String customerName, String endDate, long lockCookie);

    /**
     * This method allows the cancellation of a {@code HotelRoom} booking.
     *
     * @param recordNumber The record number of the desired {@code HotelRoom}.
     * @param lockCookie The key to lock the record with.
     * @return True, if the cancellation of the booking was successful.
     *         False, if the cancellation of the booking was not successful.
     */
    boolean cancelHotelRoomBooking(long recordNumber, long lockCookie);
}
