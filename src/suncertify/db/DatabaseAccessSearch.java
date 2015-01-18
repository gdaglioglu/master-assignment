package suncertify.db;

import suncertify.utilities.UrlyBirdApplicationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has the logic for handling the searching for records in the
 * database file.
 *
 * @author Luke GJ Potter
 * @since 27/03/2014
 */
class DatabaseAccessSearch {

    /**
     * Returns an array of record numbers that match the specified
     * {@code criteria}. Field {@code n} in the database file is described by
     * {@code criteria[n]}. A null value in {@code criteria[n]} matches any
     * field value. A non-null value in {@code criteria[n]} matches any field
     * value that begins with {@code criteria[n]}.
     * <p/>
     * For example, "Fred" matches "Fred" or "Freddy".
     *
     * @param criteria The search criteria to match against.
     * @return An array of record numbers that match the {@code criteria}.
     */
    public static long[] findByCriteria(String[] criteria) {

        long[] validRecordNumbers = getAllValidRecordNumbers();

        if (criteria[0] == null && criteria[1] == null) {
            return validRecordNumbers;
        } else if (!criteria[0].equals(
                UrlyBirdApplicationConstants.EMPTY_STRING)
                && criteria[1].equals(
                UrlyBirdApplicationConstants.EMPTY_STRING)) {
            return searchOnCriteria(criteria, 0, validRecordNumbers);
        } else if (criteria[0].equals(UrlyBirdApplicationConstants.EMPTY_STRING)
                && !criteria[1].equals(
                UrlyBirdApplicationConstants.EMPTY_STRING)) {
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
     * @param criteria           The criteria to match against.
     * @param positionInCriteria The position in the criteria String array to
     *                           perform the match against.
     * @param validRecordNumbers An array of the valid database record numbers
     *                           to search against.
     * @return A {@code long[]} containing the record numbers of the database
     * records that match {@code criteria[positionInStrings]}.
     */
    private static long[] searchOnCriteria(String[] criteria,
                                           int positionInCriteria,
                                           long[] validRecordNumbers) {

        List<Long> searchResults = new ArrayList<>();

        for (long recordNumber : validRecordNumbers) {

            try {
                String record =
                        DatabaseAccessCrudOperations
                                .readRecord(recordNumber)[positionInCriteria];

                if (record.toLowerCase().startsWith(
                        criteria[positionInCriteria].toLowerCase())) {
                    searchResults.add(recordNumber);
                }

            } catch (RecordNotFoundException ignored) {
            }
        }

        return longListToLongArray(searchResults);
    }

    /**
     * This method searches the database records that correspond to
     * {@code validRecordNumbers} for the values that start with the contents of
     * the {@code criteria} parameter and returns
     * their record numbers as an array.
     *
     * @param criteria           The criteria to match against.
     * @param validRecordNumbers An array of the valid database record numbers
     *                           to search against.
     * @return A {@code long[]} containing the record numbers of the database
     * records that fully match {@code criteria}.
     */
    private static long[] searchOnCriteria(String[] criteria,
                                           long[] validRecordNumbers) {

        List<Long> searchResults = new ArrayList<>();

        for (long recordNumber : validRecordNumbers) {

            try {
                boolean allMatch = true;
                String[] records =
                        DatabaseAccessCrudOperations.readRecord(recordNumber);

                for (int i = 0; i < criteria.length; i++) {

                    if (!records[i].toLowerCase().startsWith(
                            criteria[i].toLowerCase())) {
                        allMatch = false;
                        break;
                    }
                }

                if (allMatch) searchResults.add(recordNumber);

            } catch (RecordNotFoundException ignored) {
            }
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
        long recordsInDatabase =
                databaseFileUtils.getNumberOfRecordsInDatabase();
        List<Long> validRecordNumbers = new ArrayList<>();

        for (long recordNumber = 0; recordNumber < recordsInDatabase;
             recordNumber++) {

            try {
                DatabaseAccessCrudOperations.readRecord(recordNumber);
                validRecordNumbers.add(recordNumber);
            } catch (RecordNotFoundException ignored) {
            }
        }

        return longListToLongArray(validRecordNumbers);
    }

    /**
     * This method converts a {@code List<Long>} to an array of {@code long}.
     *
     * @param searchResults A list of the records numbers to be converted to an
     *                      array.
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
