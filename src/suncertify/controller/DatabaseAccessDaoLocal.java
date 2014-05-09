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

    private final Data databaseAccessFacade;

    public DatabaseAccessDaoLocal() {

         databaseAccessFacade = new Data();
    }

    @Override
    public long createHotelRoom(HotelRoom newHotelRoom) {

        try {
            return databaseAccessFacade.createRecord(newHotelRoom.toStringArray());
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public HotelRoom retrieveHotelRoom(long recordNumber) {

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

    @Override
    public boolean updateHotelRoom(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie) {

        try {
            databaseAccessFacade.updateRecord(recordNumber, updatedHotelRoom.toStringArray(), lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean deleteHotelRoom(long recordNumber, long lockCookie) {

        try {
            databaseAccessFacade.deleteRecord(recordNumber, lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    @Override
    public ArrayList<HotelRoom> findHotelRooms(String... searchStrings) {

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

    @Override
    public ArrayList<HotelRoom> retrieveAllHotelRooms() {

        ArrayList<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();
        HotelRoom tempHotelRoom;

        for (long recordNumber = 0; (tempHotelRoom = retrieveHotelRoom(recordNumber)) != null; recordNumber++) {
            hotelRooms.add(tempHotelRoom);
        }

        return hotelRooms;
    }

    @Override
    public boolean bookHotelRoom(long recordNumber, String customerName, long lockCookie) {

        HotelRoom hotelRoomToBook;

        if ((hotelRoomToBook = retrieveHotelRoom(recordNumber)) == null) {
            return false;
        }

        hotelRoomToBook.setOwnerName(customerName);

        return updateHotelRoom(recordNumber, hotelRoomToBook, lockCookie);
    }

    @Override
    public boolean cancelHotelRoomBooking(long recordNumber, long lockCookie) {

        HotelRoom hotelRoomBookingToCancel;

        if ((hotelRoomBookingToCancel = retrieveHotelRoom(recordNumber)) == null) {
            return false;
        }

        hotelRoomBookingToCancel.setOwnerName(null);

        return updateHotelRoom(recordNumber, hotelRoomBookingToCancel, lockCookie);
    }
}
