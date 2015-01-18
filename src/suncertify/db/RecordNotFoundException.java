package suncertify.db;

/**
 * @author Luke GJ Potter
 * @since 06/12/2013
 */
public class RecordNotFoundException extends Exception {

    /**
     * Default Constructor.
     */
    public RecordNotFoundException() {
    }

    /**
     * Constructor that accepts a parameter.
     *
     * @param message The message to be printed with the stack trace.
     */
    public RecordNotFoundException(String message) {

        super(message);
    }
}
