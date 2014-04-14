package suncertify.db;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

/**
 * @author Luke GJ Potter
 * Date: 31/03/2014
 */
public class DatabaseFileUtils {

    private static DatabaseFileUtils databaseFileUtils;
    private final RandomAccessFile databaseRandomAccessFile;

    private int magicCookie, recordLength, numberOfFields;
    private long numberOfRecordsInDatabase, headerOffset;

    private static final Logger logger = Logger.getLogger("suncertify.db.DatabaseFileUtils");

    private DatabaseFileUtils() {

        databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();

        readHeaderValues();
        readColumnsHeaders();
        calculateHeaderOffset();
        updateNumberOfRecordsInDatabase();
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

    private void setHeaderOffset(long headerOffset) {
        this.headerOffset = headerOffset;
    }

    // ---------- Public Methods ----------
    public static DatabaseFileUtils getInstance() {

        if (databaseFileUtils == null) {
            databaseFileUtils = new DatabaseFileUtils();
        }

        return databaseFileUtils;
    }

    public void updateNumberOfRecordsInDatabase() {

        try {
            setNumberOfRecordsInDatabase((databaseRandomAccessFile.length() - getHeaderOffset()) / getRecordLength());
        } catch (IOException e) {
            System.out.println("Error getting the length of the database file.");
            e.printStackTrace();
        }
    }

    public void closeDatabaseRandomAccessFile() {
        try {
            databaseRandomAccessFile.close();
        } catch (IOException e) {
            System.out.println("Error closing Database File.");
            e.printStackTrace();
        }
    }

    // ---------- Private Methods ----------
    private void readHeaderValues() {

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

    private void readColumnsHeaders() {

        for (int i = 0; i < numberOfFields; i++) {

            try {
                byte[] nameLengthBytes = new byte[DatabaseFileSchema.BYTES_FIELD_NAME];
                databaseRandomAccessFile.read(nameLengthBytes);
                int nameLength = getValueFromByteArray(nameLengthBytes);

                byte[] fieldNameBytes = new byte[nameLength];
                databaseRandomAccessFile.read(fieldNameBytes);
                DatabaseFileSchema.databaseFieldNames.add(i, new String(fieldNameBytes, URLyBirdApplicationConstants.FILE_ENCODING));

                byte[] fieldLength = new byte[DatabaseFileSchema.BYTES_FIELD_LENGTH];
                databaseRandomAccessFile.read(fieldLength);
                DatabaseFileSchema.databaseFieldLengths.add(i, getValueFromByteArray(fieldLength));

            } catch (IOException e) {
                System.out.println("Error seeking past the column headers.");
                e.printStackTrace();
            }
        }
    }

    private void calculateHeaderOffset() {

        try {
            setHeaderOffset(databaseRandomAccessFile.getFilePointer());

        } catch (IOException e) {
            System.out.println("Error getting the File Pointer for the HeaderOffset.");
            e.printStackTrace();
        }
    }

    /**
     * Converts the content of a given {@code byte} array to an {@code int}.
     *
     * @param bytes The {@code byte} array that contains the number to be converted to an {@code int}.
     * @return An {@code int} that represents the content of the {@code byte} array, provided as an argument.
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
