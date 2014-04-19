package suncertify.db;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Luke GJ Potter
 * Date: 27/03/2014
 */
class DatabaseAccessCrudOperations {

    // ---------- Public Methods ----------
    /**
     * Creates a new record in the database (possibly reusing a deleted entry). Inserts the given data, and returns the
     * record number of the new record.
     *
     * @param data
     * @return
     * @throws DuplicateKeyException
     */
    public static synchronized long createRecord(String[] data) throws DuplicateKeyException {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        databaseFileUtils.updateNumberOfRecordsInDatabase();
        // TODO: Put in check for DuplicateRecordException, but for now just write to the end of the file.
        long positionToInsertRecord = databaseFileUtils.getNumberOfRecordsInDatabase();
        long lockCookie = 0;

        try {
            lockCookie = DatabaseAccessLockManager.getInstance().lockRecordWhenCreatingNewRecord(positionToInsertRecord);
        } catch (RecordNotFoundException ignored) {}

        try {
            RandomAccessFile databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(distanceToSeek(positionToInsertRecord, databaseFileUtils));
            databaseRandomAccessFile.writeByte(DatabaseFileSchema.VALID_RECORD_FLAG);
            databaseRandomAccessFile.write(stringArrayAsByteArray(data));

            databaseRandomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error when creating new record.");
            e.printStackTrace();

        } finally {

            databaseFileUtils.updateNumberOfRecordsInDatabase();
            DatabaseAccessLockManager.getInstance().unlockRecordWhenCreatingOrDeletingRecord(positionToInsertRecord, lockCookie);
        }

        return positionToInsertRecord;
    }

    /**
     * Reads a record from the file. Returns an array where each element is a record value.
     *
     * @param recNo
     * @return
     * @throws RecordNotFoundException
     */
    public static String[] readRecord(long recNo) throws RecordNotFoundException {

        long lockingCookie = DatabaseAccessLockManager.getInstance().lock(recNo);

        isValidRecordNumber(recNo);

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        String[] rowContentStrings = new String[databaseFileUtils.getNumberOfFields()];

        try {
            RandomAccessFile randomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            randomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));

            rowContentStrings = readStringArrayFromDatabaseFile(databaseFileUtils, randomAccessFile);
            randomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error reading from RandomAccessFile.");
            e.printStackTrace();

        } finally {

            DatabaseAccessLockManager.getInstance().unlock(recNo, lockingCookie);
        }

        return rowContentStrings;
    }

    /**
     * Modifies the fields of a record. The new value for field n appears in data[n]. Throws SecurityException if the
     * record is locked with a cookie other than lockCookie.
     *
     * @param recNo
     * @param data
     * @param lockCookie
     * @throws RecordNotFoundException
     * @throws SecurityException
     */
    public static void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException {

        DatabaseAccessLockManager databaseAccessLockManager = DatabaseAccessLockManager.getInstance();

        if (databaseAccessLockManager.isRecordLockedBySomeoneElse(recNo, lockCookie)) {
            throw new SecurityException("Record " + recNo + " locked by another user.");
        }

        try {
            databaseAccessLockManager.lock(recNo);

            DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
            databaseFileUtils.updateNumberOfRecordsInDatabase();

            RandomAccessFile databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();

            databaseRandomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));
            readStringArrayFromDatabaseFile(databaseFileUtils, databaseRandomAccessFile);

            databaseRandomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));
            databaseRandomAccessFile.writeByte(DatabaseFileSchema.VALID_RECORD_FLAG);
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
     * Deletes a record, making the record number and associated disk storage available for reuse. Throws
     * SecurityException if the record is locked with a cookie other than lockCookie.
     *
     * @param recNo
     * @param lockCookie
     * @throws RecordNotFoundException
     * @throws SecurityException
     */
    public static void deleteRecord(long recNo, long lockCookie)  throws RecordNotFoundException, SecurityException {

        DatabaseAccessLockManager databaseAccessLockManager = DatabaseAccessLockManager.getInstance();

        if (databaseAccessLockManager.isRecordLockedBySomeoneElse(recNo, lockCookie)) {
            throw new SecurityException("Record " + recNo + " locked by another user.");
        }

        databaseAccessLockManager.lock(recNo);
        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        try {
            databaseFileUtils.updateNumberOfRecordsInDatabase();
            isValidRecordNumber(recNo);

            RandomAccessFile databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));
            readStringArrayFromDatabaseFile(databaseFileUtils, databaseRandomAccessFile);
            databaseRandomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));
            databaseRandomAccessFile.write(DatabaseFileSchema.INVALID_RECORD_FLAG);

            databaseRandomAccessFile.close();

        } catch (IOException e) {

            System.out.println("Error when deleting record " + recNo);
            e.printStackTrace();

        } finally {

            databaseFileUtils.updateNumberOfRecordsInDatabase();
            databaseAccessLockManager.unlockRecordWhenCreatingOrDeletingRecord(recNo, lockCookie);
        }
    }

    /**
     * Checks the database file to see if the supplied {@code recordNumber}
     * corresponds to an existing record.
     *
     * @param recordNumber
     * @throws RecordNotFoundException
     */
    public static void isValidRecordNumber(long recordNumber) throws RecordNotFoundException {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        isExistingRecordNumber(recordNumber, databaseFileUtils);

        try {
            byte[] recordBytes = new byte[DatabaseFileSchema.RECORD_LENGTH];

            RandomAccessFile randomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            randomAccessFile.seek(distanceToSeek(recordNumber, databaseFileUtils));

            if (randomAccessFile.read(recordBytes) < DatabaseFileSchema.RECORD_LENGTH) {
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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            for (int i = 0, j = 0, k = 0; i < DatabaseFileUtils.getInstance().getNumberOfFields(); i++) {

                int fieldLength = DatabaseFileSchema.databaseFieldLengths.get(i);
                byte[] bytes = new byte[fieldLength];

                if (strings[i] == null) strings[i] = "";

                for (byte b : strings[i].getBytes()) {
                    bytes[j++] = b;
                }

                while (k < fieldLength) {
                    dataOutputStream.write(bytes[k++]);
                }
                // Reset the secondary counters each time round.
                j = 0; k = 0;
            }

            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {

            System.out.println("Error converting the StringArray to a ByteArray.");
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static void isExistingRecordNumber(long recordNumber, DatabaseFileUtils databaseFileUtils) throws RecordNotFoundException {

        databaseFileUtils.updateNumberOfRecordsInDatabase();

        if (recordNumber < 0 || recordNumber >= databaseFileUtils.getNumberOfRecordsInDatabase()) {
            throw new RecordNotFoundException("Record Number " + recordNumber + " is not in the right range.");
        }
    }

    private static long distanceToSeek(long recordNumber, DatabaseFileUtils databaseFileUtils) {

        return databaseFileUtils.getHeaderOffset() + (DatabaseFileSchema.RECORD_LENGTH * recordNumber);
    }

    private static String[] readStringArrayFromDatabaseFile(DatabaseFileUtils databaseFileUtils, RandomAccessFile randomAccessFile) throws IOException, RecordNotFoundException {

        byte[] recordBytes = new byte[DatabaseFileSchema.RECORD_LENGTH];

        if (randomAccessFile.read(recordBytes) < DatabaseFileSchema.RECORD_LENGTH) {
            throw new RecordNotFoundException("Record does not exist");
        }

        if (recordBytes[0] == DatabaseFileSchema.INVALID_RECORD_FLAG) {
            throw new RecordNotFoundException("Record already deleted.");
        }

        String record = new String(recordBytes, URLyBirdApplicationConstants.FILE_ENCODING);
        String[] strings = new String[databaseFileUtils.getNumberOfFields()];
        int flagIndentation = 1;

        for (int i = 0; i < databaseFileUtils.getNumberOfFields(); i++) {

            int fieldLength = DatabaseFileSchema.databaseFieldLengths.get(i);
            strings[i] = record.substring(flagIndentation, flagIndentation + fieldLength);
            flagIndentation = flagIndentation + fieldLength;
        }

        return strings;
    }
}
