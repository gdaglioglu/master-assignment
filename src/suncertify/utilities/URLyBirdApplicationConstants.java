package suncertify.utilities;

/**
 * This class holds the constants used in the URLyBird Application.
 *
 * @author Luke Potter
 *
 * Created by lukepotter on 01/04/2014.
 */
public class URLyBirdApplicationConstants {

    /**
     * The Constant <code>PROPERTY_FILE_NAME</code>.
     * Used as filename for the properties file, <code>suncertify.properties</code>, which was asked for in the assignment.
     */
    public static final String PROPERTY_FILE_NAME = "src/suncertify.properties";

    /**
     * The Constant <code>KEY_PROPERTY_DB_PATH</code>.
     * Used to load the path to the database file key in the <code>suncertify.properties</code> file.
     */
    public static final String PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE = "pathToDatabaseFile";

    /**
     * The Constant <code>RANDOM_ACCESS_FILE_OPEN_MODE</code>.
     * Used to specify the mode to open the database file across the application.
     */
    public static final String RANDOM_ACCESS_FILE_OPEN_MODE = "rw";

    // The character encoding of the database file.
    private static final String FILE_ENCODING = "US-ASCII";
}
