package com.urlybird.controller;

import com.urlybird.database.DatabaseFileReader;
import com.urlybird.model.Hotel;

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
    }
}
