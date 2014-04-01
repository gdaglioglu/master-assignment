package suncertify.db;

import suncertify.model.HotelRoom;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    // The database file location.
    // TODO: make this relative, or read from a properties file.
    private final String PATH_TO_DATABASE_FILE = "/Users/lukepotter/Eclipse-Workspace/URLyBird/src/db-1x1.db";
    // The bytes that store the "magic cookie" value.
    private final int BYTES_MAGIC_COOKIE = 4;
    // The bytes that store the total overall length of each record.
    private final int BYTES_RECORD_LENGTH = 4;
    // The bytes that store the number of fields in each record.
    private final int BYTES_NUMBER_OF_FIELDS = 2;
    // The bytes that store the length of each field name.
    private final int BYTES_FIELD_NAME = 2;
    // The bytes that store the fields length.
    private final int BYTES_FIELD_LENGTH = 2;
    // The bytes that store the flag of each record.
    private final int BYTES_RECORD_FLAG = 1;
    // The value that identifies a record as being valid.
    private final boolean IS_VALID_RECORD = false;
    // The character encoding of the file.
    private static final String FILE_ENCODING = "US-ASCII";
    // The string representation for rooms that allow smoking.
    private final String SMOKING_ALLOWED = "Y";

    // --------------- Instance Variables ---------------- //
    private RandomAccessFile databaseRandomAccessFile;
    private int magicCookie, recordLength, numberOfFields, headerOffset, numberOfRecords;
    private final Logger log = Logger.getLogger("suncertify.db.DatabaseFileReader");

    public DatabaseFileReader() {

        try {
            databaseRandomAccessFile = new RandomAccessFile(PATH_TO_DATABASE_FILE, "rw");
            databaseRandomAccessFile.seek(0);

            // Set the magic cookie value, by reading it from the database file's header.
            readMagicCookie();

            // Set the record length, by reading it from the database file's header.
            readRecordLength();

            // Set the number of fields, by reading it from the database file's header.
            readNumberOfFields();

            headerOffset = (int) databaseRandomAccessFile.getFilePointer();

        } catch (FileNotFoundException e) {

            System.out.println("There was an FileNotFoundException. Intended file path is: " + PATH_TO_DATABASE_FILE + "\n");
            e.printStackTrace();

        } catch (IOException e) {

            System.out.println("There was an IOException.\n");
            e.printStackTrace();
        }
    }

    public List<HotelRoom> readDatabaseFile() {

        List<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();

        for(int i = 0; i < numberOfFields; i++) {

            HotelRoom hotelRoom = new HotelRoom();

            hotelRoom.setName(readHotelName());
            hotelRoom.setLocation(readHotelLocation());
            hotelRoom.setRoomSize(readHotelRoomSize());
            hotelRoom.setSmoking(readHotelIsSmoking());
            hotelRoom.setRate(readHotelRate());
            hotelRoom.setDate(readHotelDate());
            hotelRoom.setOwnerName(readHotelOwner());

            hotelRooms.add(hotelRoom);
        }

        return hotelRooms;
    }

    public int getMagicCookie() {
        return magicCookie;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public int getHeaderOffset() {
        return headerOffset;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    private void setNumberOfRecords() {
        try {
            numberOfRecords = (int)(databaseRandomAccessFile.length() - getHeaderOffset()) / BYTES_RECORD_LENGTH;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabaseFileInputStream() {
        try {
            databaseRandomAccessFile.close();
            log.fine("Database closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readNumberOfFields() throws IOException {

        final byte[] numberOfFieldsBytes = new byte[BYTES_NUMBER_OF_FIELDS];
        databaseRandomAccessFile.read(numberOfFieldsBytes);
        numberOfFields = retrieveIntegerFromDatabaseFile(numberOfFieldsBytes);
    }

    private void readRecordLength() throws IOException {

        final byte[] recordLengthBytes = new byte[BYTES_RECORD_LENGTH];
        databaseRandomAccessFile.read(recordLengthBytes);
        recordLength = retrieveIntegerFromDatabaseFile(recordLengthBytes);
    }

    private void readMagicCookie() throws IOException {

        final byte[] magicCookieBytes = new byte[BYTES_MAGIC_COOKIE];
        databaseRandomAccessFile.read(magicCookieBytes);
        magicCookie = retrieveIntegerFromDatabaseFile(magicCookieBytes);
    }

    private String readHotelName() {

        final byte[] hotelNameBytes = new byte[FIELD_LENGTH_HOTEL];
        try{
            databaseRandomAccessFile.read(hotelNameBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Name");
        }
        return retrieveStringFromDatabaseFile(hotelNameBytes);
    }

    private String readHotelLocation() {

        final byte[] hotelLocationBytes = new byte[FIELD_LENGTH_CITY];
        try{
            databaseRandomAccessFile.read(hotelLocationBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Location");
        }
        return retrieveStringFromDatabaseFile(hotelLocationBytes);
    }

    private int readHotelRoomSize() {

        final byte[] hotelSizeBytes = new byte[FIELD_LENGTH_SIZE];
        try{
            databaseRandomAccessFile.read(hotelSizeBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Size");
        }
        return retrieveIntegerFromDatabaseFile(hotelSizeBytes);
    }

    private boolean readHotelIsSmoking() {

        final byte[] hotelSmokingBytes = new byte[FIELD_LENGTH_SMOKING];
        try{
            databaseRandomAccessFile.read(hotelSmokingBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Smoking");
        }

        if(retrieveStringFromDatabaseFile(hotelSmokingBytes).equalsIgnoreCase(SMOKING_ALLOWED)) {
            return true;
        }

        return false;
    }

    private double readHotelRate() {

        final byte[] hotelRateBytes = new byte[FIELD_LENGTH_RATE];
        try{
            databaseRandomAccessFile.read(hotelRateBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Rate");
        }
        return retrieveDoubleFromDatabaseFile(hotelRateBytes);
    }

    private String readHotelDate() {

        final byte[] hotelDateBytes = new byte[FIELD_LENGTH_DATE];
        try{
            databaseRandomAccessFile.read(hotelDateBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Date");
        }
        return retrieveStringFromDatabaseFile(hotelDateBytes);
    }

    private String readHotelOwner() {

        final byte[] hotelOwnerBytes = new byte[FIELD_LENGTH_OWNER];
        try{
            databaseRandomAccessFile.read(hotelOwnerBytes);
        } catch (IOException e) {
            System.out.println("Error Reading HotelRoom Owner");
        }
        return retrieveStringFromDatabaseFile(hotelOwnerBytes);
    }

    private int retrieveIntegerFromDatabaseFile(final byte[] bytes) {

        int retrievedValue = 0;
        final int bytesLength = bytes.length;

        for (int i = 0; i < bytesLength; i++) {

            retrievedValue += (bytes[i] & 0x000000FF) << ((bytesLength - 1 - i) * 8);
        }

        return retrievedValue;
    }

    private double retrieveDoubleFromDatabaseFile(final byte[] bytes) {

        double retrievedValue = 0;
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
