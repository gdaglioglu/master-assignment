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
    public static long createRecord(String[] data) {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        databaseFileUtils.updateNumberOfRecordsInDatabase();
        // TODO: Put in check for DuplicateRecordException, but for now just write to the end of the file.
        long positionToInsertRecord = databaseFileUtils.getNumberOfRecordsInDatabase();

        try {
            RandomAccessFile databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(distanceToSeek(positionToInsertRecord, databaseFileUtils));
            databaseRandomAccessFile.writeByte(DatabaseFileSchema.VALID_RECORD_FLAG);
            databaseRandomAccessFile.write(stringArrayAsByteArray(data));

            databaseRandomAccessFile.close();

        } catch (IOException e) {
            System.out.println("Error when creating new record.");
            e.printStackTrace();
        }

        databaseFileUtils.updateNumberOfRecordsInDatabase();
        return positionToInsertRecord;
    }

    public static String[] readRecord(long recNo) throws RecordNotFoundException {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        isExistingRecordNumber(recNo, databaseFileUtils);

        String[] rowContentStrings = new String[databaseFileUtils.getNumberOfFields()];

        try {
            RandomAccessFile randomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            randomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));

            rowContentStrings = readStringArrayFromDatabaseFile(databaseFileUtils, randomAccessFile);
            randomAccessFile.close();

        } catch (IOException e) {
            System.out.println("Error reading from RandomAccessFile.");
            e.printStackTrace();
        }
        return rowContentStrings;
    }

    public static void updateRecord(long recNo, String[] data, long lockCookie) throws RecordNotFoundException {

        // TODO: Implement the locking logic for the lockCookie.
        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        databaseFileUtils.updateNumberOfRecordsInDatabase();

        try {
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
        }
    }

    public static void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException {

        // TODO: Implement the lockCookie to throw the SecurityException.
        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        isExistingRecordNumber(recNo, databaseFileUtils);

        try {
            RandomAccessFile databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));
            readStringArrayFromDatabaseFile(databaseFileUtils, databaseRandomAccessFile);
            databaseRandomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));
            databaseRandomAccessFile.write(DatabaseFileSchema.INVALID_RECORD_FLAG);

            databaseRandomAccessFile.close();

        } catch (IOException e) {
            System.out.println("Error when creating new record.");
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
