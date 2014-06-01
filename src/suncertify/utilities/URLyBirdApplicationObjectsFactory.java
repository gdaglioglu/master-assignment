package suncertify.utilities;

import java.io.*;
import java.util.Properties;

/**
 * Contains objects that are frequently used in the URLyBird Application.
 * Retrieving the object using this factory cuts down on the code that needs to
 * be written and maintained.
 *
 * @author Luke GJ Potter
 * @since 02/04/2014
 */
public class UrlyBirdApplicationObjectsFactory {

    /**
     * Gets the initialised properties object for the URLyBird Application.
     *
     * @return The initialised properties object for the URLyBird Application.
     */
    public static Properties getUrlyBirdApplicationProperties() {

        Properties properties = new Properties();
        File propertiesFile = new File(
                UrlyBirdApplicationConstants.PROPERTY_FILE_NAME);

        if (! (propertiesFile.exists() && propertiesFile.canRead()
                && propertiesFile.canWrite())) {
            createNewPropertiesFile(propertiesFile);
        }

        try {
            properties.load(new FileInputStream(
                    propertiesFile));
        } catch (IOException e) {
            System.err.println(
                    "Unable to find the properties file. Looking for file in: "
                    + UrlyBirdApplicationConstants.PROPERTY_FILE_NAME);
        }

        return properties;
    }

    /**
     * Gets the RandomAccessFile to access the database file in the URLyBird
     * Application.
     *
     * @return The RandomAccessFile to access the database file in the URLyBird
     * Application.
     */
    public static RandomAccessFile getDatabaseRandomAccessFile() {

        Properties properties = getUrlyBirdApplicationProperties();

        try {
            File databaseFile = new File(properties.getProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            if (!databaseFile.exists()) {
                throw new FileNotFoundException();
            }
            return new RandomAccessFile(
                    databaseFile,
                    UrlyBirdApplicationConstants.RANDOM_ACCESS_FILE_OPEN_MODE);
        } catch (FileNotFoundException e) {
            System.out.println(
                    "Unable to find the database file. Looking for file in: "
                            + System.getProperty("user.dir")
                            + "/"
                            + properties.getProperty(
                            UrlyBirdApplicationConstants
                                    .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a new properties file and initialises it with default values.
     *
     * @param propertiesFile The File object intended for storing properties.
     */
    private static void createNewPropertiesFile(File propertiesFile) {
        try {
            propertiesFile.createNewFile();
            BufferedWriter output = new BufferedWriter(
                    new FileWriter(propertiesFile));
            output.write(
                    "pathToDatabaseFile=db-1x1.db\n"
                    + "rmiHostname=localhost\n"
                    + "rmiPortNumber=1234");
            output.close();
        } catch (IOException e) {
            System.err.println("Problem creating properties file, "
                    + UrlyBirdApplicationConstants.PROPERTY_FILE_NAME
                    + ".");
            e.printStackTrace();
        }
    }
}
