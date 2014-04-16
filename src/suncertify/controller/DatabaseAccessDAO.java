package suncertify.controller;

import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 15/04/2014
 */
public interface DatabaseAccessDao {

    long createHotelRoom(HotelRoom newHotelRoom);

    HotelRoom retrieveHotelRoom(long recordNumber);

    boolean updateHotelRoom(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie);

    boolean deleteHotelRoom(long recordNumber, long lockCookie);

    ArrayList<HotelRoom> findHotelRooms(String... searchStrings);

    ArrayList<HotelRoom> retrieveAllHotelRooms();

    boolean bookHotelRoom(long recordNumber, String customerName, long lockCookie);

    boolean cancelHotelRoomBooking(long recordNumber, long lockCookie);
}
