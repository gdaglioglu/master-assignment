package suncertify.controller;

import suncertify.db.DatabaseFileUtils;
import suncertify.model.HotelRoom;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
public class DatabaseAccessDaoImplTest {

    public static void main(String args[]) {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        printDatabaseFileUtilsToConsole(databaseFileUtils);

        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
        createNewHotelRoomInDatabase();

        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
        deleteHotelRoomFromDatabase(databaseFileUtils);

        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
        updateHotelRoomInDatabase();

        printHotelRoomsToConsoleUsingDatabaseAccessDAO();
        searchForHotelRoom();

        databaseFileUtils.closeDatabaseRandomAccessFile();
    }

    // ---------- Private Methods ----------
    private static void printDatabaseFileUtilsToConsole(DatabaseFileUtils databaseFileUtils) {

        System.out.println("Magic Cookie is:   " + databaseFileUtils.getMagicCookie());
        System.out.println("Record Length is:  " + databaseFileUtils.getRecordLength());
        System.out.println("No. of Fields is:  " + databaseFileUtils.getNumberOfFields());
        System.out.println("Header Offset is:  " + databaseFileUtils.getHeaderOffset());
        System.out.println("No. of Records is: " + databaseFileUtils.getNumberOfRecordsInDatabase());
        System.out.println("\n\n");
    }

    private static void printHotelRoomsToConsoleUsingDatabaseAccessDAO() {

        DatabaseAccessDao databaseAccessDAO = new DatabaseAccessDaoImpl();
        ArrayList<HotelRoom> hotelRooms = databaseAccessDAO.retrieveAllHotelRooms();

        System.out.println("The Hotels in the database are:");
        long recordNumber = 1L;
        for (HotelRoom hotelRoom : hotelRooms) {
            System.out.println(recordNumber + ": " + hotelRoom.toString());
            recordNumber++;
        }
    }

    private static void createNewHotelRoomInDatabase() {

        HotelRoom hotelRoom = new HotelRoom("Casa de Luca", "Lukeville", 2, true, 150.00, "2014/04/12");
        DatabaseAccessDao databaseAccessDAO = new DatabaseAccessDaoImpl();

        System.out.println("\n\nInserting new record into the database. "
                + "Record is:" + hotelRoom.toString());

        long positionInserted = databaseAccessDAO.createHotelRoom(hotelRoom);
        System.out.println("Inserted record in position " + (positionInserted + 1));

        System.out.println("The hotel in position "
                + (positionInserted + 1)
                + " is: "
                + databaseAccessDAO.retrieveHotelRoom(positionInserted).toString()
                + "\nThe number of records in the database is now "
                + DatabaseFileUtils.getInstance().getNumberOfRecordsInDatabase()
                + "\n\n");
    }

    private static void deleteHotelRoomFromDatabase(DatabaseFileUtils databaseFileUtils) {

        DatabaseAccessDao databaseAccessDAO = new DatabaseAccessDaoImpl();

        System.out.println("\n\nDeleting record from the database."
                + "\nNumber of records in database: "
                + databaseFileUtils.getNumberOfRecordsInDatabase() + ".");

        databaseAccessDAO.deleteHotelRoom(databaseFileUtils.getNumberOfRecordsInDatabase() - 1, Thread.currentThread().getId());

        System.out.println("Deleted record from the database."
                + "\nNumber of records in database: "
                + databaseFileUtils.getNumberOfRecordsInDatabase() + ".\n\n");
    }

    private static void updateHotelRoomInDatabase() {

        long recordNumber = 30;
        DatabaseAccessDao databaseAccessDao = new DatabaseAccessDaoImpl();

        System.out.println("\n\nUpdating record " + (recordNumber + 1) + " in Database.");

        HotelRoom updatedHotelRoom = databaseAccessDao.retrieveHotelRoom(recordNumber);
        System.out.print("Old Record: " + updatedHotelRoom.toString());
        updatedHotelRoom.setName("Updated");
        updatedHotelRoom.setLocation("Newtown");
        System.out.println(" | New Record: " + updatedHotelRoom.toString());

        databaseAccessDao.updateHotelRoom(recordNumber, updatedHotelRoom, Thread.currentThread().getId());

        System.out.println("Database record "
                + (recordNumber + 1)
                + " is "
                + databaseAccessDao.retrieveHotelRoom(recordNumber).toString()
                + "\n\n");
    }

    private static void searchForHotelRoom() {

        DatabaseAccessDao databaseAccessDao = new DatabaseAccessDaoImpl();
        String[] criteria = new String[] { null, null };
        System.out.println("\n\nSearching for " + Arrays.asList(criteria) + " Found " +databaseAccessDao.findHotelRooms(criteria));

        String[] criteria2 = new String[] { "Palace", "" };
        System.out.println("Searching for " + Arrays.asList(criteria2) + " Found " +databaseAccessDao.findHotelRooms(criteria2));

        String[] criteria3 = new String[] { "", "Whoville" };
        System.out.println("Searching for " + Arrays.asList(criteria3) + " Found " +databaseAccessDao.findHotelRooms(criteria3));

        String[] criteria4 = new String[] { "Palace", "Whoville" };
        System.out.println("Searching for " + Arrays.asList(criteria4) + " Found " +databaseAccessDao.findHotelRooms(criteria4));

        String[] criteria5 = new String[] { "Wrong", "Place" };
        System.out.println("Searching for " + Arrays.asList(criteria5) + " Found " +databaseAccessDao.findHotelRooms(criteria5));
    }
}
