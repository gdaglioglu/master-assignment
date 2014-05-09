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

    private final DatabaseAccessRemote databaseAccessRemote;

    public DatabaseAccessDaoRemote() throws RemoteException {

        databaseAccessRemote = RmiClientManager.connectToRemoteServerViaRmi();
    }

    @Override
    public long createHotelRoom(HotelRoom newHotelRoom) {

        try {
            return databaseAccessRemote.createRecord(newHotelRoom.toStringArray());
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public HotelRoom retrieveHotelRoom(long recordNumber) {

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

    @Override
    public boolean updateHotelRoom(long recordNumber, HotelRoom updatedHotelRoom, long lockCookie) {

        try {
            databaseAccessRemote.updateRecord(recordNumber, updatedHotelRoom.toStringArray(), lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override
    public boolean deleteHotelRoom(long recordNumber, long lockCookie) {

        try {
            databaseAccessRemote.deleteRecord(recordNumber, lockCookie);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override
    public ArrayList<HotelRoom> findHotelRooms(String... searchStrings) {

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
