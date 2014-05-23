package suncertify.db;

/**
 * @author Luke GJ Potter
 * @since  06/12/2013
 */
public class DuplicateKeyException extends Exception {

    /**
     * Default Constructor.
     */
    public DuplicateKeyException() {}

    /**
     * Constructor that accepts a parameter.
     *
     * @param message The message to be printed with the stack trace.
     */
    public DuplicateKeyException(String message) {

        super(message);
    }
}
