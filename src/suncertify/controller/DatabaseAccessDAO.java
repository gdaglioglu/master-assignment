package suncertify.controller;

import suncertify.db.Data;
import suncertify.db.DatabaseFileUtils;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 31/03/2014
 */
public class DatabaseAccessDAO {

    private final Data databaseAccessFacade;

    public DatabaseAccessDAO() {

         databaseAccessFacade = new Data();
    }

    public boolean createHotel(HotelRoom newHotelRoom) {

        try {
            databaseAccessFacade.createRecord(newHotelRoom.toStringArray());
            DatabaseFileUtils.getInstance().updateNumberOfRecordsInDatabase();
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

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

    public boolean updateHotel(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie) {

        String[] updatedHotelStrings = updatedHotelRoom.toStringArray();

        try {
            databaseAccessFacade.updateRecord(recordNumber, updatedHotelStrings, lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    public boolean deleteHotel(long recordNumber, long lockCookie) {

        try {
            databaseAccessFacade.deleteRecord(recordNumber, lockCookie);
            DatabaseFileUtils.getInstance().updateNumberOfRecordsInDatabase();
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    public ArrayList<HotelRoom> findHotels(String ... searchStrings) {

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

    public ArrayList<HotelRoom> retrieveAllHotels() {

        ArrayList<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();
        HotelRoom tempHotelRoom;

        for (long recordNumber = 0; (tempHotelRoom = retrieveHotel(recordNumber)) != null; recordNumber++) {
            hotelRooms.add(tempHotelRoom);
        }

        return hotelRooms;
    }

    public boolean bookHotel(long recordNumber, String customerName, long lockCookie) {

        HotelRoom hotelRoomToBook;

        if ((hotelRoomToBook = retrieveHotel(recordNumber)) == null) {
            return false;
        }

        hotelRoomToBook.setOwnerName(customerName);

        return updateHotel(recordNumber, hotelRoomToBook, lockCookie);
    }

    public boolean cancelHotelBooking(long recordNumber, long lockCookie) {

        HotelRoom hotelRoomBookingToCancel;

        if ((hotelRoomBookingToCancel = retrieveHotel(recordNumber)) == null) {
            return false;
        }

        hotelRoomBookingToCancel.setOwnerName(null);

        return updateHotel(recordNumber, hotelRoomBookingToCancel, lockCookie);
    }
}
