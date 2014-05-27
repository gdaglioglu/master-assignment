package suncertify.db;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The utilities for the database file. This is a singleton. It offers
 * information on the database file, such as number of records in the database
 * file.
 *
 * @author Luke GJ Potter
 * @since 31/03/2014
 */
public class DatabaseFileUtils {

    private static DatabaseFileUtils databaseFileUtils;
    private final RandomAccessFile databaseRandomAccessFile;

    private int magicCookie, recordLength, numberOfFields;
    private long numberOfRecordsInDatabase, headerOffset;

    private DatabaseFileUtils() {

        databaseRandomAccessFile = UrlyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();

        readHeaderValues();
        readColumnsHeaders();
        calculateHeaderOffset();
        updateNumberOfRecordsInDatabase();
    }

    /**
     * Get an instance of the {@code DatabaseFileUtils} class.
     *
     * @return A
     */
    public static DatabaseFileUtils getInstance() {

        if (databaseFileUtils == null) {
            databaseFileUtils = new DatabaseFileUtils();
        }

        return databaseFileUtils;
    }

    // ---------- Getters and Setters ----------

    /**
     * Gets the value of the magic cookie at the beginning of the database file.
     *
     * @return The magic cookie value.
     */
    public int getMagicCookie() {
        return magicCookie;
    }

    private void setMagicCookie(int magicCookie) {
        this.magicCookie = magicCookie;
    }

    /**
     * Gets the number of a bytes in a record.
     *
     * @return The number of bytes in a record.
     */
    public int getRecordLength() {
        return recordLength;
    }

    private void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    /**
     * Gets the number of fields in a record.
     *
     * @return The number of fields in a record.
     */
    public int getNumberOfFields() {
        return numberOfFields;
    }

    private void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    /**
     * Gets the number of records in the database, including deleted records.
     *
     * @return The number of records in the database, including the deleted
     * records.
     */
    public long getNumberOfRecordsInDatabase() {
        return numberOfRecordsInDatabase;
    }

    private void setNumberOfRecordsInDatabase(long numberOfRecordsInDatabase) {
        this.numberOfRecordsInDatabase = numberOfRecordsInDatabase;
    }

    /**
     * Gets the offset, in terms of bytes, from where the database file's header
     * information and where the records start.
     *
     * @return The number of bytes from where the file's header information
     * ends.
     */
    public long getHeaderOffset() {
        return headerOffset;
    }

    private void setHeaderOffset(long headerOffset) {
        this.headerOffset = headerOffset;
    }

    // ---------- Public Methods ----------

    /**
     * Get the current number of records in the database including deleted
     * records.
     */
    public synchronized void updateNumberOfRecordsInDatabase() {

        try {
            setNumberOfRecordsInDatabase((databaseRandomAccessFile.length() - getHeaderOffset()) / getRecordLength());
        } catch (IOException e) {
            System.out.println("Error getting the length of the database file.");
            e.printStackTrace();
        }
    }

    /**
     * A helper method to close the {@code databaseRandomAccessFile}.
     */
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
                DatabaseFileSchema.databaseFieldNames.add(i, new String(fieldNameBytes, UrlyBirdApplicationConstants.FILE_ENCODING));

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
     * @param bytes The {@code byte} array that contains the number to be
     *              converted to an {@code int}.
     * @return An {@code int} that represents the content of the {@code byte}
     * array, provided as an argument.
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
