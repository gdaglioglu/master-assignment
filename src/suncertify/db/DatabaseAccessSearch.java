package suncertify.db;

import suncertify.utilities.URLyBirdApplicationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luke GJ Potter
 * Date: 27/03/2014
 */
class DatabaseAccessSearch {

    /**
     * Returns an array of record numbers that match the specified
     * {@code criteria}. Field {@code n} in the database file is described by
     * {@code criteria[n]}. A null value in {@code criteria[n]} matches any
     * field value. A non-null value in {@code criteria[n]} matches any field
     * value that begins with {@code criteria[n]}.
     *
     * For example, "Fred" matches "Fred" or "Freddy".
     *
     * @param criteria
     * @return An array of record numbers that match the {@code criteria}.
     */
    public static long[] findByCriteria(String[] criteria) {

        long[] validRecordNumbers = getAllValidRecordNumbers();

        if (criteria[0] == null && criteria[1] == null) {
            return validRecordNumbers;
        } else if (!criteria[0].equals(URLyBirdApplicationConstants.EMPTY_STRING) && criteria[1].equals(URLyBirdApplicationConstants.EMPTY_STRING)) {
            return searchOnCriteria(criteria, 0, validRecordNumbers);
        } else if (criteria[0].equals(URLyBirdApplicationConstants.EMPTY_STRING) && !criteria[1].equals(URLyBirdApplicationConstants.EMPTY_STRING)) {
            return searchOnCriteria(criteria, 1, validRecordNumbers);
        } else {
            return searchOnCriteria(criteria, validRecordNumbers);
        }
    }

    // ---------- Private Methods ----------
    /**
     * This method searches the database records that correspond to
     * {@code validRecordNumbers} for values in the {@code positionInCriteria}
     * position that start with {@code criteria[positionInCriteria]} and returns
     * their record numbers as an array.
     *
     * @param criteria
     * @param positionInCriteria
     * @param validRecordNumbers
     * @return A {@code long[]} containing the record numbers of the database
     *         records that match {@code criteria[positionInStrings]}.
     */
    private static long[] searchOnCriteria(String[] criteria, int positionInCriteria, long[] validRecordNumbers) {

        List<Long> searchResults = new ArrayList<Long>();

        for (long recordNumber : validRecordNumbers) {

            try {
                String record = DatabaseAccessCrudOperations.readRecord(recordNumber)[positionInCriteria];

                if (record.toLowerCase().startsWith(criteria[positionInCriteria].toLowerCase())) {
                    searchResults.add(recordNumber);
                }

            } catch (RecordNotFoundException ignored) { }
        }

        return longListToLongArray(searchResults);
    }

    /**
     * This method searches the database records that correspond to
     * {@code validRecordNumbers} for the values that start with the contents of
     * the {@code criteria} parameter and returns
     * their record numbers as an array.
     *
     * @param criteria
     * @param validRecordNumbers
     * @return
     */
    private static long[] searchOnCriteria(String[] criteria, long[] validRecordNumbers) {

        List<Long> searchResults = new ArrayList<Long>();

        for (long recordNumber : validRecordNumbers) {

            try {
                boolean allMatch = true;
                String[] records = DatabaseAccessCrudOperations.readRecord(recordNumber);

                for (int i = 0; i < criteria.length; i++) {

                    if (!records[i].toLowerCase().startsWith(criteria[i].toLowerCase())) {
                        allMatch = false;
                        break;
                    }
                }

                if (allMatch) searchResults.add(recordNumber);

            } catch (RecordNotFoundException ignored) { }
        }

        return longListToLongArray(searchResults);
    }

    /**
     * Method to get all the records numbers for all the valid records in the
     * Database File.
     *
     * @return A {@code long[]} that contains all valid record numbers.
     */
    private static long[] getAllValidRecordNumbers() {

        DatabaseFileUtils databaseFileUtils = DatabaseFileUtils.getInstance();
        databaseFileUtils.updateNumberOfRecordsInDatabase();
        long recordsInDatabase = databaseFileUtils.getNumberOfRecordsInDatabase();
        List<Long> validRecordNumbers = new ArrayList<Long>();

        for (long recordNumber = 0; recordNumber < recordsInDatabase; recordNumber++) {

            try {
                DatabaseAccessCrudOperations.readRecord(recordNumber);
                validRecordNumbers.add(recordNumber);
            } catch (RecordNotFoundException ignored) { }
        }

        return longListToLongArray(validRecordNumbers);
    }

    /**
     * This method converts a {@code List<Long>} to an array of {@code long}.
     *
     * @param searchResults
     * @return A {@code long[]} containing contents of {@code searchResults}.
     */
    private static long[] longListToLongArray(List<Long> searchResults) {

        long[] longs = new long[searchResults.size()];

        int i = 0;
        for (Long l : searchResults) {

            longs[i] = l;
            i++;
        }

        return longs;
    }
}
