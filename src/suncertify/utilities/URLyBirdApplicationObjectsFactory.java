package suncertify.utilities;

import java.io.*;
import java.util.Properties;

/**
 * @author Luke GJ Potter
 * Date: 02/04/2014
 */
public class URLyBirdApplicationObjectsFactory {

    public static Properties getURLyBirdApplicationProperties() {

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(URLyBirdApplicationConstants.PROPERTY_FILE_NAME));
        }  catch (FileNotFoundException e) {
            System.out.println("Unable to find the properties file. Looking for file in: "
                    + System.getProperty("user.dir")
                    + "/"
                    + URLyBirdApplicationConstants.PROPERTY_FILE_NAME);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error loading the properties file. File may be corrupt. Looking for file in: "
                    + URLyBirdApplicationConstants.PROPERTY_FILE_NAME);
            e.printStackTrace();
        }

        return properties;
    }

    public static RandomAccessFile getDatabaseRandomAccessFile() {

        Properties properties = getURLyBirdApplicationProperties();

        try {
            File databaseFile = new File(properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            if (! databaseFile.exists()) {
                throw new FileNotFoundException();
            }
            return new RandomAccessFile(
                    databaseFile,
                    URLyBirdApplicationConstants.RANDOM_ACCESS_FILE_OPEN_MODE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find the database file. Looking for file in: "
                    + System.getProperty("user.dir")
                    + "/"
                    + properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            e.printStackTrace();
        }

        return null;
    }
}
