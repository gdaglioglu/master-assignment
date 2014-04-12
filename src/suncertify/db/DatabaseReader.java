/*
 * @(#)DBFileReader.java    1.0 05/13/2009
 *
 * Candidate: Roberto Perillo
 * Prometric ID: Your Prometric ID here
 * Candidate ID: Your candidate ID here
 *
 * Sun Certified Developer for Java 2 Platform, Standard Edition Programming Assignment (CX-310-252A)
 *
 * This class is part of the Programming Assignment of the Sun Certified Developer for Java 2 Platform, Standard Edition
 * certification program, must not be used out of this context and must be used exclusively by Oracle and Sun
 * Microsystems, Inc.
 */
package suncertify.db;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import java.io.*;

/**
 * <p>
 * The <code>DBFileReader</code> reads the .db file that is provided to each SCJD candidate. Essentially, it displays on
 * the console the content of the file, starting by the "magic cookie", total overall length (in bytes) of each record
 * and the number of fields in each record. Finally, it displays the content of each record, in the format "Field name:
 * value". It also informs the status of each record, that is, if it is either valid or deleted. Note that the content
 * is displayed exactly as it appears in the .db file, which means that no transformations are done (such as formatting
 * the date).
 * </p>
 * <p>
 * This class was first made with the information provided in the <code>instructions.html</code> provided in the
 * URLyBird 1.3.1 assignment, therefore, it is likely that it will require a few changes in order to correctly read the
 * content of the .db files provided in other assignments.
 * </p>
 *
 * @author Roberto Perillo
 * @version 1.0 05/13/2009
 */
public class DatabaseReader {

    // The bytes that store the "magic cookie" value.
    private static final int MAGIC_COOKIE_BYTES = 4;
    // The bytes that store the total overall length of each record.
    private static final int RECORD_LENGTH_BYTES = 4;
    // The bytes that store the number of fields in each record.
    private static final int NUMBER_OF_FIELDS_BYTES = 2;
    // The bytes that store the length of each field name.
    private static final int FIELD_NAME_BYTES = 2;
    // The bytes that store the fields length.
    private static final int FIELD_LENGTH_BYTES = 2;
    // The bytes that store the flag of each record.
    private static final int RECORD_FLAG_BYTES = 1;
    // The value that identifies a record as being valid.
    private static final int VALID = 0;

    /**
     * Reads the .db file. The values are read according to the information provided in the class-level fields (such as
     * MAGIC_COOKIE_BYTES) and to values read at runtime (such as field name length). Each relevant value is displayed
     * on the console in the form "Field: value". For instance:
     * <p/>
     * <pre>
     * Magic cookie: 888
     * Record length: 888
     * Number of fields in each record: 8
     * </pre>
     * <p/>
     * The content of each record is displayed until EOF is reached.
     *
     * @param args The arguments provided in the command line when this application is run.
     */
    public static void main(final String[] args) {

        try {
            final RandomAccessFile database = URLyBirdApplicationObjectsFactory.getDatabaseRandomAccessFile();

            final byte[] magicCookieByteArray = new byte[MAGIC_COOKIE_BYTES];
            final byte[] recordLengthByteArray = new byte[RECORD_LENGTH_BYTES];
            final byte[] numberOfFieldsByteArray = new byte[NUMBER_OF_FIELDS_BYTES];

            database.read(magicCookieByteArray);
            database.read(recordLengthByteArray);
            database.read(numberOfFieldsByteArray);

            final int magicCookie = getValue(magicCookieByteArray);
            final int recordLength = getValue(recordLengthByteArray);
            final int numberOfFields = getValue(numberOfFieldsByteArray);

            System.out.println("Magic cookie: " + magicCookie);
            System.out.println("Record length: " + recordLength);
            System.out.println("Number of fields in each record: " + numberOfFields);

            final String[] fieldNames = new String[numberOfFields];
            final int[] fieldLengths = new int[numberOfFields];

            /*
             * At this point, the information of each field is provided, therefore, the main point of this "for" loop is
             * to get the name and length of each field.
             */
            for (int i = 0; i < numberOfFields; i++) {

                final byte nameLengthByteArray[] = new byte[FIELD_NAME_BYTES];
                database.read(nameLengthByteArray);
                final int nameLength = getValue(nameLengthByteArray);

                final byte[] fieldNameByteArray = new byte[nameLength];
                database.read(fieldNameByteArray);
                fieldNames[i] = new String(fieldNameByteArray, URLyBirdApplicationConstants.FILE_ENCODING);

                final byte[] fieldLength = new byte[FIELD_LENGTH_BYTES];
                database.read(fieldLength);
                fieldLengths[i] = getValue(fieldLength);
            }

            System.out.println("Header offset is: " + database.getFilePointer());
            System.out.println();

            int record = 1;
            // The actual content of each record is read here. The file is read until EOF is reached.
            while (true) {

                final byte[] flagByteArray = new byte[RECORD_FLAG_BYTES];
                final int eof = database.read(flagByteArray);

                if (eof == -1) break;

                final int flag = getValue(flagByteArray);

                System.out.println("*********** RECORD " + record + " ***********");
                for (int i = 0; i < numberOfFields; i++) {

                    final byte[] buffer = new byte[fieldLengths[i]];
                    database.read(buffer);
                    System.out.println(fieldNames[i] + ": " + new String(buffer, URLyBirdApplicationConstants.FILE_ENCODING));
                }

                if (flag == VALID) System.out.println("Status: valid record\n");
                else System.out.println("Status: deleted record\n");

                record++;
            }

            database.close();

        } catch (final FileNotFoundException exception) {
            throw new RuntimeException("The given file does not exist.", exception);
        } catch (final IOException exception) {
            throw new RuntimeException("The following error occurred while trying to read the database file: "
                    + exception.getMessage(), exception);
        }
    }

    /**
     * Converts to <code>int</code> the content of a given <code>byte</code> array.
     *
     * @param byteArray The <code>byte</code> array that contains the number to be converted.
     * @return An <code>int</code> that represents the content of the <code>byte</code> array, provided as argument.
     */
    private static int getValue(final byte[] byteArray) {

        int value = 0;
        final int byteArrayLength = byteArray.length;

        for (int i = 0; i < byteArrayLength; i++) {
            final int shift = (byteArrayLength - 1 - i) * 8;
            value += (byteArray[i] & 0x000000FF) << shift;
        }
        return value;
    }
}
