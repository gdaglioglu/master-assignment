package suncertify.controller;

import suncertify.db.DatabaseFileUtils;
import suncertify.model.HotelRoom;

import java.util.ArrayList;

/**
 * @author Luke GJ Potter
 * Date: 06/12/2013
 */
public class MainController {

    public static void main(String[] args) {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        printDatabaseFileUtilsToConsole(databaseFileUtils);
        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
        createNewHotelRoomInDatabase();
        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
        deleteHotelRoomFromDatabase(databaseFileUtils);
        printHotelRoomsToConsoleUsingDatabaseAccessDAO();

        databaseFileUtils.closeDatabaseRandomAccessFile();
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

    private static void createNewHotelRoomInDatabase() {

        HotelRoom hotelRoom = new HotelRoom("Casa de Luca", "Lukeville", 2, true, 150.00, "2014/04/12");
        DatabaseAccessDAO databaseAccessDAO = new DatabaseAccessDAO();

        System.out.println("\n\nInserting new record into the database.\n"
                + "Record is:" + hotelRoom.toString() + "\n\n");

        long positionInserted = databaseAccessDAO.createHotel(hotelRoom);
        System.out.println("Inserted record in position " + positionInserted);

        System.out.println("The hotel in position "
                + positionInserted
                + " is:\n"
                + databaseAccessDAO.retrieveHotel(positionInserted).toString()
                + "\nThe number of records in the database is now "
                + DatabaseFileUtils.getInstance().getNumberOfRecordsInDatabase());
    }

    private static void deleteHotelRoomFromDatabase(DatabaseFileUtils databaseFileUtils) {

        DatabaseAccessDAO databaseAccessDAO = new DatabaseAccessDAO();

        System.out.println("\n\nDeleting record from the database."
                + "\nNumber of records in database: "
                + databaseFileUtils.getNumberOfRecordsInDatabase() + ".");

        databaseAccessDAO.deleteHotel(databaseFileUtils.getNumberOfRecordsInDatabase() - 1, 0L);

        System.out.println("Record from the database."
                + "\nNumber of records in database: "
                + databaseFileUtils.getNumberOfRecordsInDatabase() + ".");
    }
}
