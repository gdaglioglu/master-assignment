package suncertify.controller;

import suncertify.db.DatabaseAccessFacade;
import suncertify.exceptions.DuplicateKeyException;
import suncertify.exceptions.RecordNotFoundException;
import suncertify.model.Hotel;

import java.util.ArrayList;

/**
 * Created by lukepotter on 31/03/2014.
 */
public class DatabaseAccessDAO {

    private DatabaseAccessFacade databaseAccessFacade;

    public DatabaseAccessDAO() {

         databaseAccessFacade = new DatabaseAccessFacade();
    }

    public boolean createHotel(Hotel newHotel) {

        try {
            databaseAccessFacade.createRecord(DatabaseAccessDAOUtils.parseHotelPojoIntoStringArray(newHotel));
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    public Hotel retrieveHotel(long recordNumber) {

        try {
            return DatabaseAccessDAOUtils.parseStringArrayIntoHotelPojo(databaseAccessFacade.readRecord(recordNumber));
        } catch (RecordNotFoundException e) {
            return null;
        }
    }

    public boolean updateHotel(long recordNumber, Hotel updatedHotel, long lockCookie) {

        String[] updatedHotelStrings = DatabaseAccessDAOUtils.parseHotelPojoIntoStringArray(updatedHotel);

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
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    public ArrayList<Hotel> findHotels(String ... searchStrings) {

        long[] recordNumbers = databaseAccessFacade.findByCriteria(searchStrings);
        ArrayList<Hotel> hotels = null;

        for(long recordNumber : recordNumbers) {

            Hotel hotel = retrieveHotel(recordNumber);

            if (hotel != null) {

                // Initialise the hotels ArrayList the first time we find a hotel to add to the list.
                if (hotels == null) {
                    hotels = new ArrayList<Hotel>();
                }

                hotels.add(hotel);
            }
        }

        return hotels;
    }

    public ArrayList<Hotel> retrieveAllHotels() {

        ArrayList<Hotel> hotels = new ArrayList<Hotel>();
        Hotel tempHotel;

        for (long recordNumber = 1; (tempHotel = retrieveHotel(recordNumber)) != null; recordNumber++) {
            hotels.add(tempHotel);
        }

        return hotels;
    }

    public boolean bookHotel(long recordNumber, String customerName, long lockCookie) {

        Hotel hotelToBook;

        if ((hotelToBook = retrieveHotel(recordNumber)) == null) {
            return false;
        }

        hotelToBook.setOwnerName(customerName);

        return updateHotel(recordNumber, hotelToBook, lockCookie);
    }

    public boolean cancelHotelBooking(long recordNumber, long lockCookie) {

        Hotel hotelBookingToCancel;

        if ((hotelBookingToCancel = retrieveHotel(recordNumber)) == null) {
            return false;
        }

        hotelBookingToCancel.setOwnerName(null);

        return updateHotel(recordNumber, hotelBookingToCancel, lockCookie);
    }
}
