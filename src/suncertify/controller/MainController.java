package suncertify.controller;

import suncertify.db.DatabaseFileUtils;
import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * Created by lukepotter on 06/12/2013.
 */
public class MainController {

    public static void main(String[] args) {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        printDatabaseFileUtilsToConsole(databaseFileUtils);
        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
    }

    private static void printDatabaseFileUtilsToConsole(DatabaseFileUtils databaseFileUtils) {

        System.out.println("Magic Cookie is:   " + databaseFileUtils.getMagicCookie());
        System.out.println("Record Length is:  " + databaseFileUtils.getRecordLength());
        System.out.println("No. of Fields is:  " + databaseFileUtils.getNumberOfFields());
        System.out.println("Header Offset is:  " + databaseFileUtils.getHeaderOffset());
        System.out.println("No. of Records is: " + databaseFileUtils.getNumberOfRecordsInDatabase());
    }

    private static void printHotelRoomsToConsoleUsingDatabaseAccessDAO() {

        DatabaseAccessDAO databaseAccessDAO = new DatabaseAccessDAO();
        ArrayList<HotelRoom> hotelRooms = databaseAccessDAO.retrieveAllHotels();

        System.out.println("The Hotels in the database are:");
        long recordNumber = 1L;
        for (HotelRoom hotelRoom : hotelRooms) {
            System.out.println(recordNumber + ": " + hotelRoom.toString());
            recordNumber++;
        }
    }
}
