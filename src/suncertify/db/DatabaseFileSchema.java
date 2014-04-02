package suncertify.db;

/**
 * Created by lukepotter on 01/04/2014.
 */
public class DatabaseFileSchema {

    /**
     * Field descriptive name: Name of the hotel.
     * Database field name:    name
     * Field length:           64
     * Detailed description:   The name of the hotel this vacancy record relates to.
     */
    public final int FIELD_LENGTH_HOTEL = 64;

    /**
     * Field descriptive name: City.
     * Database field name:    location
     * Field length:           64
     * Detailed description:   The location of this hotel.
     */
    public final int FIELD_LENGTH_CITY = 64;

    /**
     * Field descriptive name: Maximum occupancy of this room.
     * Database field name:    size
     * Field length:           4
     * Detailed description:   The maximum number of people permitted in this room, not including infants.
     */
    public final int FIELD_LENGTH_SIZE = 4;

    /**
     * Field descriptive name: Is the room smoking or non-smoking.
     * Database field name:    smoking
     * Field length:           1
     * Detailed description:   Flag indicating if smoking is permitted. Valid values are "Y" indicating a smoking room,
     *                         and "N" indicating a non-smoking room.
     */
    public final int FIELD_LENGTH_SMOKING = 1;

    /**
     * Field descriptive name: Price per night.
     * Database field name:    rate
     * Field length:           8
     * Detailed description:   Charge per night for the room. This field includes the currency symbol.
     */
    public final int FIELD_LENGTH_RATE = 6;

    /**
     * Field descriptive name: Date available.
     * Database field name:    date
     * Field length:           10
     * Detailed description:   The single night to which this record relates, format is yyyy/mm/dd.
     */
    public final int FIELD_LENGTH_DATE = 10;

    /**
     * Field descriptive name: Customer holding this record.
     * Database field name:    owner
     * Field length:           8
     * Detailed description:   The customer id (an 8 digit number) . Note that for this application, you should assume
     *                         that both the customers and the CSRs know the customer id. However the booking must
     *                         always be made by CSR by entering the customer id against the room reservation.
     */
    public final int FIELD_LENGTH_OWNER = 8;

    // The bytes that store the "magic cookie" value.
    public static final int BYTES_MAGIC_COOKIE = 4;
    // The bytes that store the total overall length of each record.
    public static final int BYTES_RECORD_LENGTH = 4;
    // The bytes that store the number of fields in each record.
    public static final int BYTES_NUMBER_OF_FIELDS = 2;
    // The bytes that store the length of each field name.
    public static final int BYTES_FIELD_NAME = 2;
    // The bytes that store the fields length.
    public static final int BYTES_FIELD_LENGTH = 2;
    // The bytes that store the flag of each record.
    public static final int BYTES_RECORD_FLAG = 1;
    // The value that identifies a record as being valid.
    public static final boolean IS_VALID_RECORD = false;
    // The string representation for rooms that allow smoking.
    public static final String SMOKING_ALLOWED = "Y";
}
