package suncertify.db;

/**
 * @author Luke GJ Potter
 * Date: 06/12/2013
 */
public class DuplicateKeyException extends Exception {

    /**
     * Default Constructor.
     */
    public DuplicateKeyException() {}

    /**
     * Constructor that accepts a parameter.
     *
     * @param message
     */
    public DuplicateKeyException(String message) {

        super(message);
    }
}
