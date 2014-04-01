package suncertify.db;

/**
 * Created by lukepotter on 06/12/2013.
 */
public class RecordNotFoundException extends Exception {

    /**
     * Default Constructor.
     */
    public RecordNotFoundException() {}

    /**
     * Constructor that accepts a parameter.
     *
     * @param message
     */
    public RecordNotFoundException(String message) {

        super(message);
    }
}
