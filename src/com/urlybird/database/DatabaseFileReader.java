package com.urlybird.database;

import com.urlybird.model.Hotel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukepotter on 06/12/2013.
 */
public class DatabaseFileReader {

    /**
     * Field descriptive name: Hotel Name
     * Database field name:    name
     * Field length:           64
     * Detailed description:   The name of the hotel this vacancy record relates to.
     */
    private final int FIELD_LENGTH_HOTEL = 64;

    /**
     * Field descriptive name: City.
     * Database field name:    location
     * Field length:           64
     * Detailed description:   The location of this hotel.
     */
    private final int FIELD_LENGTH_CITY = 64;

    /**
     * Field descriptive name: Maximum occupancy of this room.
     * Database field name:    size
     * Field length:           4
     * Detailed description:   The maximum number of people permitted in this room, not including infants.
     */
    private final int FIELD_LENGTH_SIZE = 4;

    /**
     * Field descriptive name: Is the room smoking or non-smoking.
     * Database field name:    smoking
     * Field length:           1
     * Detailed description:   Flag indicating if smoking is permitted. Valid values are "Y" indicating a smoking room,
     *                         and "N" indicating a non-smoking room.
     */
    private final int FIELD_LENGTH_SMOKING = 1;

    /**
     * Field descriptive name: Price per night.
     * Database field name:    rate
     * Field length:           8
     * Detailed description:   Charge per night for the room. This field includes the currency symbol.
     */
    private final int FIELD_LENGTH_RATE = 6;

    /**
     * Field descriptive name: Date available.
     * Database field name:    date
     * Field length:           10
     * Detailed description:   The single night to which this record relates, format is yyyy/mm/dd.
     */
    private final int FIELD_LENGTH_DATE = 10;

    /**
     * Field descriptive name: Customer holding this record.
     * Database field name:    owner
     * Field length:           8
     * Detailed description:   The customer id (an 8 digit number) . Note that for this application, you should assume
     *                         that both the customers and the CSRs know the customer id. However the booking must
     *                         always be made by CSR by entering the customer id against the room reservation.
     */
    private final int FIELD_LENGTH_OWNER = 8;

    // --------------- Constants ---------------- //
    // The database file location. TODO: make this relative.
    private final String PATH_TO_DATABASE_FILE = "/Users/lukepotter/Eclipse-Workspace/URLyBird/src/db-1x1.db";
    // The bytes that store the "magic cookie" value.
    private final int BYTES_MAGIC_COOKIE = 4;
    // The bytes that store the total overall length of each record.
    private final int BYTES_RECORD_LENGTH = 4;
    // The bytes that store the number of fields in each record.
    private final int BYTES_NUMBER_OF_FIELDS = 2;

    // --------------- Instance Variables ---------------- //
    private InputStream databaseFileInputStream;
    private int magicCookie, numberOfFields;

    public DatabaseFileReader() {

        try {
            databaseFileInputStream = new FileInputStream(PATH_TO_DATABASE_FILE);

            // Set the magic cookie value, by reading it from the database file's header.
            readMagicCookie();

            // Read the record length, by reading it from the database file's header.
            readRecordLength();

            // Set the number of fields, by reading it from the database file's header.
            readNumberOfFields();

        } catch (FileNotFoundException e) {

            System.out.println("There was an FileNotFoundException. Intended file path is: " + PATH_TO_DATABASE_FILE + "\n");
            e.printStackTrace();

        } catch (IOException e) {

            System.out.println("There was an IOException.\n");
            e.printStackTrace();
        }
    }

    public List<Hotel> readDatabaseFile() {

        List<Hotel> hotels = new ArrayList<Hotel>();

        for(int i = 0; i < numberOfFields; i++) {

            Hotel hotel = new Hotel();

            hotel.setName(readHotelName());
            hotel.setLocation(readHotelLocation());
            hotel.setRoomSize(readHotelRoomSize());
            hotel.setSmoking(readHotelIsSmoking());
            hotel.setRate(readHotelRate());
            hotel.setDate(readHotelDate());
            hotel.setOwnerName(readHotelOwner());

            hotels.add(hotel);
        }

        return hotels;
    }

    public int getMagicCookie() {
        return magicCookie;
    }

    public void closeDatabaseFileInputStream() {
        try {
            databaseFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readNumberOfFields() throws IOException {

        final byte[] numberOfFieldsBytes = new byte[BYTES_NUMBER_OF_FIELDS];
        databaseFileInputStream.read(numberOfFieldsBytes);
        numberOfFields = retrieveIntegerFromDatabaseFile(numberOfFieldsBytes);
    }

    private void readRecordLength() throws IOException {

        final byte[] recordLengthBytes = new byte[BYTES_RECORD_LENGTH];
        databaseFileInputStream.read(recordLengthBytes);
        // int recordLength = retrieveIntegerFromDatabaseFile(recordLengthBytes);
    }

    private void readMagicCookie() throws IOException {

        final byte[] magicCookieBytes = new byte[BYTES_MAGIC_COOKIE];
        databaseFileInputStream.read(magicCookieBytes);
        magicCookie = retrieveIntegerFromDatabaseFile(magicCookieBytes);
    }

    private int retrieveIntegerFromDatabaseFile(final byte[] bytes) {

        int retrievedValue = 0;
        final int bytesLength = bytes.length;

        for (int i = 0; i < bytesLength; i++) {

            retrievedValue += (bytes[i] & 0x000000FF) << ((bytesLength - 1 - i) * 8);
        }

        return retrievedValue;
    }

    private String retrieveStringFromDatabaseFile(final byte[] bytes) {

        StringBuilder retrievedValue = new StringBuilder();
        final int bytesLength = bytes.length;

        for (int i = 0; i < bytesLength; i++) {

            retrievedValue.append((bytes[i] & 0x000000FF) << ((bytesLength - 1 - i) * 8));
        }

        return retrievedValue.toString();
    }
}
