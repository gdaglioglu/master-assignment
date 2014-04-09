package suncertify.db;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by lukepotter on 27/03/2014.
 */
public class DatabaseAccessCRUD {


    public static long createRecord(String[] data) {
        return 0;
    }

    public static String[] readRecord(long recNo) throws RecordNotFoundException {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();

        isExistingRecordNumber(recNo, databaseFileUtils);

        String[] rowContentStrings = new String[databaseFileUtils.getNumberOfFields()];

        try {
            RandomAccessFile randomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            randomAccessFile.seek((DatabaseFileSchema.BYTES_RECORD_FLAG + recNo) + databaseFileUtils.getHeaderOffset() + (databaseFileUtils.getRecordLength() * recNo));

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

        return rowContentStrings;
    }

    public static void updateRecord(long recNo, String[] data, long lockCookie) {

    }

    public static void deleteRecord(long recNo, long lockCookie) {

    }

    private static void isExistingRecordNumber(long recordNumber, DatabaseFileUtils databaseFileUtils) throws RecordNotFoundException {

        if (recordNumber < 0 || recordNumber >= databaseFileUtils.getNumberOfRecordsInDatabase()) {
            throw new RecordNotFoundException("Record Number " + recordNumber + " is not in the right range.");
        }
    }
}
