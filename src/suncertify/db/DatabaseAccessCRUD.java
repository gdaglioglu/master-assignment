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

        if (recNo < 0 || recNo > databaseFileUtils.getNumberOfRecordsInDatabase()) {
            throw new RecordNotFoundException("Record Number " + recNo + " is not in the right range.");
        }

        String[] rowContentStrings = new String[databaseFileUtils.getNumberOfFields()];

        try {
            RandomAccessFile randomAccessFile = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();
            randomAccessFile.seek(databaseFileUtils.getHeaderOffset() + (databaseFileUtils.getRecordLength() * recNo));

            for (int i = 0; i < databaseFileUtils.getNumberOfFields(); i++) {

                final byte[] fieldBytes = new byte[DatabaseFileSchema.databaseFieldsLengths[i]];
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
}
