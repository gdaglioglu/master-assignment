package suncertify.db;

import java.util.ArrayList;

/**
 * Holds the information on teh schema of the database file.
 *
 * @author Gokhan Daglioglu
 * @since 18/01/2015
 */
class DatabaseFileSchema {

    /**
     * The bytes that store the "magic cookie" value.
     */
    public static final int MAGIC_COOKIE_BYTES = 4;
    /**
     * The bytes that store the number of fields in each record.
     */
    public static final int NUMBER_OF_FIELDS_BYTES = 2;
    /**
     * The bytes that store the length of each field name.
     */
    public static final int FIELD_NAME_BYTES = 1;
    /**
     * The bytes that store the fields length.
     */
    public static final int FIELD_LENGTH_BYTES = 1;
    /**
     * The bytes that store the flag of each record.
     */
    public static final int RECORD_FLAG_BYTES = 1;
    /**
     * The value that identifies a record as being valid.
     */
    public static final byte VALID_RECORD_FLAG = 0x0;
    /**
     * The value that identifies a record as being invalid, or deleted.
     */
    public static final byte INVALID_RECORD_FLAG = 0x1;
    /**
     * A list to store the lengths of the fields in the Database.
     */
    public static final ArrayList<Integer> databaseFieldLengths =
            new ArrayList<>();
    /**
     * A list to store the names of the fields in the Database.
     */
    public static final ArrayList<String> databaseFieldNames =
            new ArrayList<>();
    /**
     * Field descriptive name: Name of the hotel.
     * Database field name:    name
     * Field length:           64
     * Detailed description:   The name of the hotel this vacancy record relates
     * to.
     */
    private static final int HOTEL_FIELD_LENGTH = 64;
    /**
     * Field descriptive name: City.
     * Database field name:    location
     * Field length:           64
     * Detailed description:   The location of this hotel.
     */
    private static final int CITY_FIELD_LENGTH = 64;
    /**
     * Field descriptive name: Maximum occupancy of this room.
     * Database field name:    size
     * Field length:           4
     * Detailed description:   The maximum number of people permitted in this
     * room, not including infants.
     */
    private static final int SIZE_FIELD_LENGTH = 4;
    /**
     * Field descriptive name: Is the room smoking or non-smoking.
     * Database field name:    smoking
     * Field length:           1
     * Detailed description:   Flag indicating if smoking is permitted. Valid
     * values are "Y" indicating a smoking room, and "N" indicating a
     * non-smoking room.
     */
    private static final int SMOKING_FIELD_LENGTH = 1;
    /**
     * Field descriptive name: Price per night.
     * Database field name:    rate
     * Field length:           8
     * Detailed description:   Charge per night for the room. This field
     * includes the currency symbol.
     */
    private static final int RATE_FIELD_LENGTH = 8;
    /**
     * Field descriptive name: Date available.
     * Database field name:    date
     * Field length:           10
     * Detailed description:   The single night to which this record relates,
     * format is yyyy/mm/dd.
     */
    private static final int DATE_FIELD_LENGTH = 10;
    /**
     * Field descriptive name: Customer holding this record.
     * Database field name:    owner
     * Field length:           8
     * Detailed description:   The customer id (an 8 digit number). Note that
     * for this application, you should assume that both the customers and the
     * CSRs know the customer id. However the booking must always be made by CSR
     * by entering the customer id against the room reservation.
     */
    private static final int OWNER_FIELD_LENGTH = 8;
    /**
     * The length of the record.
     */
    public static final int RECORD_LENGTH = RECORD_FLAG_BYTES
            + HOTEL_FIELD_LENGTH + CITY_FIELD_LENGTH + SIZE_FIELD_LENGTH
            + SMOKING_FIELD_LENGTH + RATE_FIELD_LENGTH + DATE_FIELD_LENGTH
            + OWNER_FIELD_LENGTH;
}
