package suncertify.db;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
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
class DatabaseAccessCRUD {

    // ---------- Public Methods ----------
    public static long createRecord(String[] data) {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        databaseFileUtils.updateNumberOfRecordsInDatabase();
        // TODO: Put in check for DuplicateRecordException, but for now just write to the end of the file.
        long positionToInsertRecord = databaseFileUtils.getNumberOfRecordsInDatabase();

        try {

            RandomAccessFile databaseRandomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            databaseRandomAccessFile.seek(distanceToSeek(positionToInsertRecord, databaseFileUtils));
            databaseRandomAccessFile.writeByte(DatabaseFileSchema.IS_VALID_RECORD_FLAG);
            databaseRandomAccessFile.write(stringArrayAsByteArray(data));

            databaseRandomAccessFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseFileUtils.updateNumberOfRecordsInDatabase();
        databaseFileUtils.closeDatabaseRandomAccessFile();
        return positionToInsertRecord;
    }

    public static String[] readRecord(long recNo) throws RecordNotFoundException {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        isExistingRecordNumber(recNo, databaseFileUtils);

        String[] rowContentStrings = new String[databaseFileUtils.getNumberOfFields()];

        try {
            RandomAccessFile randomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            randomAccessFile.seek(distanceToSeek(recNo, databaseFileUtils));

            for (int i = 0; i < databaseFileUtils.getNumberOfFields(); i++) {

                byte[] fieldBytes = new byte[DatabaseFileSchema.databaseFieldLengths.get(i)];
                randomAccessFile.read(fieldBytes);
                rowContentStrings[i] = new String(fieldBytes, URLyBirdApplicationConstants.FILE_ENCODING);
            }
            randomAccessFile.close();

        } catch (IOException e) {
            System.out.println("Error reading from RandomAccessFile.");
            e.printStackTrace();
        }
        databaseFileUtils.closeDatabaseRandomAccessFile();
        return rowContentStrings;
    }

    public static void updateRecord(long recNo, String[] data, long lockCookie) {

    }

    public static void deleteRecord(long recNo, long lockCookie) {

    }

    // ---------- Private Methods ----------
    private static byte[] stringArrayAsByteArray(String[] data) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            for (int i = 0, j = 0, k = 0; i < DatabaseFileUtils.getInstance().getNumberOfFields(); i++) {

                int fieldLength = DatabaseFileSchema.databaseFieldLengths.get(i);
                byte[] bytes = new byte[fieldLength];

                if (data[i] == null) data[i] = "";

                for (byte b : data[i].getBytes()) {
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

        if (recordNumber < 0 || recordNumber >= databaseFileUtils.getNumberOfRecordsInDatabase()) {
            throw new RecordNotFoundException("Record Number " + recordNumber + " is not in the right range.");
        }
    }

    private static long distanceToSeek(long recNo, DatabaseFileUtils databaseFileUtils) {
        return databaseFileUtils.getHeaderOffset() + (DatabaseFileSchema.BYTES_RECORD_FLAG + recNo) + (DatabaseFileSchema.RECORD_LENGTH * recNo);
    }
}
