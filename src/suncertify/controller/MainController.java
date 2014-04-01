package suncertify.controller;

import suncertify.db.DatabaseFileReader;
import suncertify.model.HotelRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukepotter on 06/12/2013.
 */
public class MainController {

    public static void main(String[] args) {

        DatabaseAccessDAO databaseAccessDAO = new DatabaseAccessDAO();
        ArrayList<HotelRoom> hotelRooms = databaseAccessDAO.retrieveAllHotels();

        long recordNumber = 1L;
        for (HotelRoom hotelRoom : hotelRooms) {
            System.out.println(recordNumber + ": " + hotelRoom.toString());
            recordNumber++;
        }
    }

    private void readFromDatabaseFileReader() {

        DatabaseFileReader databaseFileReader = new DatabaseFileReader();
        List<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();

        int magicCookie = databaseFileReader.getMagicCookie();
        hotelRooms = databaseFileReader.readDatabaseFile();

        databaseFileReader.closeDatabaseFileInputStream();

        System.out.println("Magic Cookie is: " + databaseFileReader.getMagicCookie());
        System.out.println("Record Length is: " + databaseFileReader.getRecordLength());
        System.out.println("No. of Fields is: " + databaseFileReader.getNumberOfFields());
        System.out.println("Header Offset is: " + databaseFileReader.getHeaderOffset());

        System.out.println("The List of Hotels is :");
        for(HotelRoom hotelRoom : hotelRooms) {
            System.out.println(hotelRoom.toString());
        }
    }
}
