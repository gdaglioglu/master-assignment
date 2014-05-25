package suncertify.utilities;

/**
 * This class holds the constants, not unique to the GUI classes, used in the
 * URLyBird Application.
 *
 * @author Luke Potter
 * @since  01/04/2014
 */
public class UrlyBirdApplicationConstants {

    /** Used as filename for the properties file, {@code suncertify.properties},
     * which was asked for in the assignment. */
    public static String PROPERTY_FILE_NAME = System.getProperty("user.dir") + "/suncertify.properties";
    /** Used to load the path to the database file key in the
     * {@code suncertify.properties} file. */
    public static final String PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE = "pathToDatabaseFile";
    /** Used to RMI hostname key in the {@code suncertify.properties} file. */
    public static final String PROPERTY_FILE_KEY_RMI_HOSTNAME = "rmiHostname";
    /** Used to RMI port key in the {@code suncertify.properties} file. */
    public static final String PROPERTY_FILE_KEY_RMI_PORT_NUMBER = "rmiPortNumber";
    /** Used to specify the mode to open the database file across the
     * application. */
    public static final String RANDOM_ACCESS_FILE_OPEN_MODE = "rw";
    /** The character encoding of the database file. */
    public static final String FILE_ENCODING = "US-ASCII";
    /** The currency prefix for the monetary value in the Database. */
    public static final String CURRENCY_PREFIX = "$";
    /** The string representation for rooms that allow smoking. */
    public static final String SMOKING_ALLOWED = "Y";
    /** The string representation for rooms that don't allow smoking. */
    public static final String SMOKING_NOT_ALLOWED = "N";
    /** The string representation of the empty string. */
    public static final String EMPTY_STRING = "";
    /** The format of the date used throughout the application. */
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    /** RMI Application Path. */
    public static final String RMI_APPLICATION_PATH = "/URLyBird";
    /** The CommandLine Argument to specify that the Client GUI should be
     * launched and connect to the local database. */
    public static final String CLI_ARG_STANDALONE_MODE = "alone";
    /** The CommandLine Argument to specify that the Server GUI should be
     * launched. */
    public static final String CLI_ARG_SERVER_MODE = "server";
}
