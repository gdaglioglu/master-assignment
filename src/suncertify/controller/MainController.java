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

        DatabaseFileReader databaseFileReader = new DatabaseFileReader();
        List<Hotel> hotels = new ArrayList<Hotel>();

        int magicCookie = databaseFileReader.getMagicCookie();
        hotels = databaseFileReader.readDatabaseFile();

        databaseFileReader.closeDatabaseFileInputStream();

        System.out.println("The List of Hotels is :");
        for(Hotel hotel : hotels) {
            System.out.println(hotel.toString());
        }
    }
}
