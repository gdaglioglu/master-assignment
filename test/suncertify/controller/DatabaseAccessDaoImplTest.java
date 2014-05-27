package suncertify.controller;

import suncertify.db.DatabaseFileUtils;
import suncertify.model.HotelRoom;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
class DatabaseAccessDaoImplTest {

    public static void main(String args[]) {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        printDatabaseFileUtilsToConsole(databaseFileUtils);

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

        DatabaseAccessDao databaseAccessDAO = new DatabaseAccessDaoLocal();
        ArrayList<HotelRoom> hotelRooms = databaseAccessDAO.retrieveAllHotelRooms();

        System.out.println("The Hotels in the database are:");
        long recordNumber = 1L;
        for (HotelRoom hotelRoom : hotelRooms) {
            System.out.println(recordNumber + ": " + hotelRoom.toString());
            recordNumber++;
        }
    }

    private static void searchForHotelRoom() {

        DatabaseAccessDao databaseAccessDao = new DatabaseAccessDaoLocal();
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
