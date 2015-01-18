package suncertify.db;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class handles the CRUD operations on the database file. With the methods
 * in this class it it possible to create a record, retrieve a record, update a
 * record and delete a record.
 *
 * @author Luke GJ Potter
 * @since 27/03/2014
 */
class DatabaseAccessCrudOperations {

    // ---------- Public Methods ----------

    /**
     * Creates a new record in the database. Inserts the given data, and returns
     * the record number of the new record.
     *
     * @param data The string array representation of a database record.
     * @return The record number of the position that the record was created.
     * @throws DuplicateKeyException When trying to create a record that already
     *                               exists in the database.
     */
    public static synchronized long createRecord(String[] data)
            throws DuplicateKeyException {

        // Search on the name and location fields of the data object.
        if (DatabaseAccessSearch.findByCriteria(
                new String[]{data[0], data[1]}).length != 0) {
            String record = "";
            for (String field : data) record += field + ", ";

            throw new DuplicateKeyException("The record "
                    + record.substring(0, record.length() - 2)
                    + " already exists in the database.");
        }

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        databaseFileUtils.updateNumberOfRecordsInDatabase();
        long positionToInsertRecord =
                databaseFileUtils.getNumberOfRecordsInDatabase();
        long lockCookie = 0;

        try {
            lockCookie = DatabaseAccessLockManager
                    .getInstance()
                    .lockRecordWhenCreatingNewRecord(positionToInsertRecord);
        } catch (RecordNotFoundException ignored) {
        }

        try {
            RandomAccessFile databaseRandomAccessFile =
                    UrlyBirdApplicationObjectsFactory
                            .getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(
                    distanceToSeek(positionToInsertRecord, databaseFileUtils));
            databaseRandomAccessFile.writeByte(
                    DatabaseFileSchema.VALID_RECORD_FLAG);
            databaseRandomAccessFile.write(stringArrayAsByteArray(data));

            databaseRandomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error when creating new record.");
            e.printStackTrace();

        } finally {

            databaseFileUtils.updateNumberOfRecordsInDatabase();
            DatabaseAccessLockManager
                    .getInstance()
                    .unlockRecordWhenCreatingOrDeletingRecord(
                            positionToInsertRecord, lockCookie);
        }

        return positionToInsertRecord;
    }

    /**
     * Reads a record from the file. Returns an array where each element is a
     * record field.
     *
     * @param recNo The record number in the database to retrieve.
     * @return A String array representation of the database record.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     */
    public static String[] readRecord(long recNo)
            throws RecordNotFoundException {

        long lockingCookie = DatabaseAccessLockManager
                .getInstance().lock(recNo);

        isValidRecordNumber(recNo);

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        String[] rowContentStrings =
                new String[databaseFileUtils.getNumberOfFields()];

        try {
            RandomAccessFile randomAccessFile =
                    UrlyBirdApplicationObjectsFactory
                            .getDatabaseRandomAccessFile();
            randomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));

            rowContentStrings =
                    readStringArrayFromDatabaseFile(databaseFileUtils,
                            randomAccessFile);
            randomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error reading from RandomAccessFile.");
            e.printStackTrace();

        } finally {

            DatabaseAccessLockManager
                    .getInstance().unlock(recNo, lockingCookie);
        }

        return rowContentStrings;
    }

    /**
     * Modifies the fields of a record. The new value for field n appears in
     * data[n]. Throws SecurityException if the record is locked with a cookie
     * other than lockCookie.
     *
     * @param recNo      The record number in the database to update.
     * @param data       The string array representation of a database record,
     *                   containing the updates.
     * @param lockCookie The cookie that the row is locked with.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     * @throws SecurityException       If the record is locked by a user other
     *                                 than the user trying to update the
     *                                 record.
     */
    public static void updateRecord(long recNo, String[] data, long lockCookie)
            throws RecordNotFoundException, SecurityException {

        DatabaseAccessLockManager databaseAccessLockManager =
                DatabaseAccessLockManager.getInstance();

        if (databaseAccessLockManager
                .isRecordLockedBySomeoneElse(recNo, lockCookie)) {
            throw new SecurityException("Record "
                    + recNo
                    + " locked by another user.");
        }

        try {
            lockCookie = databaseAccessLockManager.lock(recNo);

            DatabaseFileUtils databaseFileUtils =
                    DatabaseFileUtils.getInstance();
            databaseFileUtils.updateNumberOfRecordsInDatabase();

            RandomAccessFile databaseRandomAccessFile =
                    UrlyBirdApplicationObjectsFactory
                            .getDatabaseRandomAccessFile();

            databaseRandomAccessFile.seek(
                    distanceToSeek(recNo, databaseFileUtils));
            readStringArrayFromDatabaseFile(
                    databaseFileUtils, databaseRandomAccessFile);

            databaseRandomAccessFile.seek(distanceToSeek(
                    recNo, databaseFileUtils));
            databaseRandomAccessFile.writeByte(
                    DatabaseFileSchema.VALID_RECORD_FLAG);
            databaseRandomAccessFile.write(stringArrayAsByteArray(data));

            databaseRandomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error when updating record " + recNo);
            e.printStackTrace();

        } finally {

            databaseAccessLockManager.unlock(recNo, lockCookie);
        }
    }

    /**
     * Deletes a record, making the record number and associated disk storage
     * unavailable for reuse by setting an "invalid" flag for the record. Throws
     * SecurityException if the record is locked with a cookie other than
     * lockCookie.
     *
     * @param recNo      The record number in the database to delete.
     * @param lockCookie The cookie that the row is locked with.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     * @throws SecurityException       If the record is locked by a user other
     *                                 than the user trying to update the
     *                                 record.
     */
    public static void deleteRecord(long recNo, long lockCookie)
            throws RecordNotFoundException, SecurityException {

        DatabaseAccessLockManager databaseAccessLockManager =
                DatabaseAccessLockManager.getInstance();

        if (databaseAccessLockManager
                .isRecordLockedBySomeoneElse(recNo, lockCookie)) {
            throw new SecurityException("Record "
                    + recNo
                    + " locked by another user.");
        }

        lockCookie = databaseAccessLockManager.lock(recNo);
        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        try {
            databaseFileUtils.updateNumberOfRecordsInDatabase();
            isValidRecordNumber(recNo);

            RandomAccessFile databaseRandomAccessFile =
                    UrlyBirdApplicationObjectsFactory
                            .getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(
                    distanceToSeek(recNo, databaseFileUtils));
            readStringArrayFromDatabaseFile(
                    databaseFileUtils, databaseRandomAccessFile);
            databaseRandomAccessFile.seek(
                    distanceToSeek(recNo, databaseFileUtils));
            databaseRandomAccessFile.write(
                    DatabaseFileSchema.INVALID_RECORD_FLAG);

            databaseRandomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error when deleting record " + recNo);
            e.printStackTrace();

        } finally {

            databaseFileUtils.updateNumberOfRecordsInDatabase();
            databaseAccessLockManager
                    .unlockRecordWhenCreatingOrDeletingRecord(
                            recNo, lockCookie);
        }
    }

    /**
     * Checks the database file to see if the supplied {@code recordNumber}
     * corresponds to an existing valid record.
     *
     * @param recordNumber The record number in the database to retrieve.
     * @throws RecordNotFoundException When locating a record that does not
     *                                 exist, or had been previously deleted.
     */
    public static void isValidRecordNumber(long recordNumber)
            throws RecordNotFoundException {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        isExistingRecordNumber(recordNumber, databaseFileUtils);

        try {
            byte[] recordBytes = new byte[DatabaseFileSchema.RECORD_LENGTH];

            RandomAccessFile randomAccessFile =
                    UrlyBirdApplicationObjectsFactory
                            .getDatabaseRandomAccessFile();
            randomAccessFile.seek(
                    distanceToSeek(recordNumber, databaseFileUtils));

            if (randomAccessFile.read(recordBytes)
                    < DatabaseFileSchema.RECORD_LENGTH) {
                throw new RecordNotFoundException("Record does not exist");
            }

            if (recordBytes[0] == DatabaseFileSchema.INVALID_RECORD_FLAG) {
                throw new RecordNotFoundException("Record already deleted.");
            }

        } catch (IOException e) {

            System.out.println("Error reading from file.");
            e.printStackTrace();
        }
    }

    // ---------- Private Methods ----------
    private static byte[] stringArrayAsByteArray(String[] strings) {

        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        DataOutputStream dataOutputStream =
                new DataOutputStream(byteArrayOutputStream);

        try {
            for (int i = 0, j = 0, k = 0;
                 i < DatabaseFileUtils.getInstance().getNumberOfFields(); i++) {

                int fieldLength =
                        DatabaseFileSchema.databaseFieldLengths.get(i);
                byte[] bytes = new byte[fieldLength];

                if (strings[i] == null) strings[i] = "";

                for (byte b : strings[i].getBytes()) {
                    bytes[j++] = b;
                }

                while (k < fieldLength) {
                    dataOutputStream.write(bytes[k++]);
                }
                // Reset the secondary counters each time round.
                j = 0;
                k = 0;
            }

            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {

            System.out.println(
                    "Error converting the StringArray to a ByteArray.");
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static void isExistingRecordNumber(
            long recordNumber, DatabaseFileUtils databaseFileUtils)
            throws RecordNotFoundException {

        databaseFileUtils.updateNumberOfRecordsInDatabase();

        if (recordNumber < 0 ||
                recordNumber >=
                        databaseFileUtils.getNumberOfRecordsInDatabase()) {
            throw new RecordNotFoundException("Record Number "
                    + recordNumber
                    + " is not in the right range.");
        }
    }

    private static long distanceToSeek(long recordNumber,
                                       DatabaseFileUtils databaseFileUtils) {

        return databaseFileUtils.getHeaderOffset()
                + (DatabaseFileSchema.RECORD_LENGTH * recordNumber);
    }

    private static String[] readStringArrayFromDatabaseFile(
            DatabaseFileUtils databaseFileUtils,
            RandomAccessFile randomAccessFile)
            throws IOException, RecordNotFoundException {

        byte[] recordBytes = new byte[DatabaseFileSchema.RECORD_LENGTH];

        if (randomAccessFile.read(recordBytes)
                < DatabaseFileSchema.RECORD_LENGTH) {
            throw new RecordNotFoundException("Record does not exist");
        }

        if (recordBytes[0] == DatabaseFileSchema.INVALID_RECORD_FLAG) {
            throw new RecordNotFoundException("Record already deleted.");
        }

        String record = new String(recordBytes,
                UrlyBirdApplicationConstants.FILE_ENCODING);
        String[] strings = new String[databaseFileUtils.getNumberOfFields()];
        int flagIndentation = 1;

        for (int i = 0; i < databaseFileUtils.getNumberOfFields(); i++) {

            int fieldLength = DatabaseFileSchema.databaseFieldLengths.get(i);
            strings[i] = record.substring(flagIndentation,
                    flagIndentation + fieldLength);
            flagIndentation = flagIndentation + fieldLength;
        }

        return strings;
    }
}
