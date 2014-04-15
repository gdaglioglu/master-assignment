package suncertify.controller;

import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 31/03/2014
 */
public class DatabaseAccessDaoImpl implements DatabaseAccessDao {

    private final Data databaseAccessFacade;

    public DatabaseAccessDaoImpl() {

         databaseAccessFacade = new Data();
    }

    @Override
    public long createHotel(HotelRoom newHotelRoom) {

        try {
            return databaseAccessFacade.createRecord(newHotelRoom.toStringArray());
        } catch (DuplicateKeyException e) {
            return -1;
        }
    }

    @Override
    public HotelRoom retrieveHotel(long recordNumber) {

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
    public boolean updateHotel(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie) {

        try {
            databaseAccessFacade.updateRecord(recordNumber, updatedHotelRoom.toStringArray(), lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean deleteHotel(long recordNumber, long lockCookie) {

        try {
            databaseAccessFacade.deleteRecord(recordNumber, lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    @Override
    public ArrayList<HotelRoom> findHotels(String... searchStrings) {

        long[] recordNumbers = databaseAccessFacade.findByCriteria(searchStrings);
        ArrayList<HotelRoom> hotelRooms = null;

        for(long recordNumber : recordNumbers) {

            HotelRoom hotelRoom = retrieveHotel(recordNumber);

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
    public ArrayList<HotelRoom> retrieveAllHotels() {

        ArrayList<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();
        HotelRoom tempHotelRoom;

        for (long recordNumber = 0; (tempHotelRoom = retrieveHotel(recordNumber)) != null; recordNumber++) {
            hotelRooms.add(tempHotelRoom);
        }

        return hotelRooms;
    }

    @Override
    public boolean bookHotel(long recordNumber, String customerName, long lockCookie) {

        HotelRoom hotelRoomToBook;

        if ((hotelRoomToBook = retrieveHotel(recordNumber)) == null) {
            return false;
        }

        hotelRoomToBook.setOwnerName(customerName);

        return updateHotel(recordNumber, hotelRoomToBook, lockCookie);
    }

    @Override
    public boolean cancelHotelBooking(long recordNumber, long lockCookie) {

        HotelRoom hotelRoomBookingToCancel;

        if ((hotelRoomBookingToCancel = retrieveHotel(recordNumber)) == null) {
            return false;
        }

        hotelRoomBookingToCancel.setOwnerName(null);

        return updateHotel(recordNumber, hotelRoomBookingToCancel, lockCookie);
    }
}
