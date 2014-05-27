package suncertify.controller;

import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * The interface of the DAO pattern implementation for the URLyBird Application.
 *
 * @author Luke GJ Potter
 * @since 15/04/2014
 */
public interface DatabaseAccessDao {

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
     * @param endDate      The string representation of the date when the
     *                     booking is due to expire.
     * @param lockCookie   The key to lock the record with.
     * @return True, if the booking was successful.
     *         False, if the booking was not successful.
     */
    boolean bookHotelRoom(long recordNumber, String customerName, String endDate, long lockCookie);
}
