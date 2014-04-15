package suncertify.controller;

import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 15/04/2014
 */
public interface DatabaseAccessDao {

    long createHotel(HotelRoom newHotelRoom);

    HotelRoom retrieveHotel(long recordNumber);

    boolean updateHotel(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie);

    boolean deleteHotel(long recordNumber, long lockCookie);

    ArrayList<HotelRoom> findHotels(String... searchStrings);

    ArrayList<HotelRoom> retrieveAllHotels();

    boolean bookHotel(long recordNumber, String customerName, long lockCookie);

    boolean cancelHotelBooking(long recordNumber, long lockCookie);
}
