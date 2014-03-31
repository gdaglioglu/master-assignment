package suncertify.controller;

import suncertify.db.DatabaseFileReader;
import suncertify.model.Hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukepotter on 06/12/2013.
 */
public class MainController {

    public static void main(String[] args) {

        DatabaseAccessDAO databaseAccessDAO = new DatabaseAccessDAO();
        ArrayList<Hotel> hotels = databaseAccessDAO.retrieveAllHotels();

        long recordNumber = 1L;
        for (Hotel hotel : hotels) {
            System.out.println(recordNumber + ": " + hotel.toString());
            recordNumber++;
        }
    }

    private void readFromDatabaseFileReader() {

        DatabaseFileReader databaseFileReader = new DatabaseFileReader();
        List<Hotel> hotels = new ArrayList<Hotel>();

        int magicCookie = databaseFileReader.getMagicCookie();
        hotels = databaseFileReader.readDatabaseFile();

        databaseFileReader.closeDatabaseFileInputStream();

        System.out.println("Magic Cookie is: " + databaseFileReader.getMagicCookie());
        System.out.println("Record Length is: " + databaseFileReader.getRecordLength());
        System.out.println("No. of Fields is: " + databaseFileReader.getNumberOfFields());
        System.out.println("Header Offset is: " + databaseFileReader.getHeaderOffset());

        System.out.println("The List of Hotels is :");
        for(Hotel hotel : hotels) {
            System.out.println(hotel.toString());
        }
    }
}
