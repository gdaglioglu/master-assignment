package suncertify.db;

import suncertify.utilities.URLyBirdApplicationConstants;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lukepotter on 31/03/2014.
 */
public class DatabaseFileUtils {

    public static DatabaseFileUtils databaseFileUtils;
    private RandomAccessFile databaseRandomAccessFile;
    private Properties properties;

    private int magicCookie, recordLength, numberOfFields;
    private long numberOfRecordsInDatabase, headerOffset;

    private static Logger logger = Logger.getLogger("suncertify.db.DatabaseFileUtils");

    private DatabaseFileUtils() {

        initialiseProperties();
        initialiseDatabaseRandomAccessFile();

        deriveHeaderValues();
        seekPastColumnsHeaders();
        deriveHeaderOffset();
        updateNumberOfRecordsInDatabase();

        closeDatabaseRandomAccessFile();
    }

    // ---------- Getters and Setters ----------
    public int getMagicCookie() {
        return magicCookie;
    }

    private void setMagicCookie(int magicCookie) {
        this.magicCookie = magicCookie;
    }

    public int getRecordLength() {
        return recordLength;
    }

    private void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    private void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public long getNumberOfRecordsInDatabase() {
        return numberOfRecordsInDatabase;
    }

    private void setNumberOfRecordsInDatabase(long numberOfRecordsInDatabase) {
        this.numberOfRecordsInDatabase = numberOfRecordsInDatabase;
    }

    public long getHeaderOffset() {
        return headerOffset;
    }

    public void setHeaderOffset(long headerOffset) {
        this.headerOffset = headerOffset;
    }

    // ---------- Public Methods ----------
    public static DatabaseFileUtils getInstance() {

        if (databaseFileUtils != null) {
            logger.finest("Returning existing databaseFileUtils");
            return databaseFileUtils;
        } else {
            logger.finest("Creating new databaseFileUtils");
            return new DatabaseFileUtils();
        }
    }

    public void updateNumberOfRecordsInDatabase() {

        try {
            setNumberOfRecordsInDatabase((databaseRandomAccessFile.length() - getHeaderOffset()) / getRecordLength());
        } catch (IOException e) {
            System.out.println("Error getting the length of the database file");
            e.printStackTrace();
        }
    }

    // ---------- Private Methods ----------
    private void initialiseProperties() {

        logger.finest("Entering initialiseProperties method.");

        try {
            properties = new Properties();
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

        logger.finest("Entering initialiseProperties method.");
    }

    private void initialiseDatabaseRandomAccessFile() {

        try {
            File databaseFile = new File(properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            if (! databaseFile.exists()) {
                throw new FileNotFoundException();
            }
            databaseRandomAccessFile = new RandomAccessFile(
                    databaseFile,
                    URLyBirdApplicationConstants.RANDOM_ACCESS_FILE_OPEN_MODE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find the database file. Looking for file in: "
                    + System.getProperty("user.dir")
                    + "/"
                    + properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            e.printStackTrace();
        }
    }

    private void closeDatabaseRandomAccessFile() {
        try {
            databaseRandomAccessFile.close();
        } catch (IOException e) {
            System.out.println("Error closing Database File.");
            e.printStackTrace();
        }
    }

    private void deriveHeaderValues() {

        try {
            final byte[] magicCookieBytes = new byte[DatabaseFileSchema.BYTES_MAGIC_COOKIE];
            databaseRandomAccessFile.read(magicCookieBytes);
            setMagicCookie(getValueFromByteArray(magicCookieBytes));
        } catch (IOException e) {
            System.out.println("Error reading Magic Cookie from database file");
            e.printStackTrace();
        }

        try {
            final byte[] recordLengthBytes = new byte[DatabaseFileSchema.BYTES_RECORD_LENGTH];
            databaseRandomAccessFile.read(recordLengthBytes);
            setRecordLength(getValueFromByteArray(recordLengthBytes));
        } catch (IOException e) {
            System.out.println("Error reading Record Length from database file");
            e.printStackTrace();
        }

        try {
            final byte[] numberOfFieldsBytes = new byte[DatabaseFileSchema.BYTES_NUMBER_OF_FIELDS];
            databaseRandomAccessFile.read(numberOfFieldsBytes);
            setNumberOfFields(getValueFromByteArray(numberOfFieldsBytes));
        } catch (IOException e) {
            System.out.println("Error reading Number of Fields from database file");
            e.printStackTrace();
        }
    }

    private void seekPastColumnsHeaders() {

        for (int i = 0; i < numberOfFields; i++) {

            try {
                databaseRandomAccessFile.seek(new byte[DatabaseFileSchema.BYTES_FIELD_NAME].length);
            } catch (IOException e) {
                System.out.println("Error seeking past the column headers.");
                e.printStackTrace();
            }
        }
    }

    private void deriveHeaderOffset() {

        try {
            setHeaderOffset(databaseRandomAccessFile.getFilePointer());
        } catch (IOException e) {
            System.out.println("Error getting the File Pointer for the HeaderOffset.");
            e.printStackTrace();
        }
    }

    /**
     * Converts the content of a given <code>byte</code> array to an <code>int</code>.
     *
     * @param bytes The <code>byte</code> array that contains the number to be converted to an <code>int</code>.
     * @return An <code>int</code> that represents the content of the <code>byte</code> array, provided as an argument.
     */
    private int getValueFromByteArray(final byte[] bytes) {

        int valueInByteArray = 0;
        final int bytesLength = bytes.length;

        for (int i = 0; i < bytesLength; i++) {
            valueInByteArray += (bytes[i] & 0x000000FF) << ((bytesLength - 1 - i) * 8);
        }

        return valueInByteArray;
    }
}
