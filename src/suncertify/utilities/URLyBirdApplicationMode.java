package suncertify.utilities;

/**
 * This Enumeration will hold the state to run the application.
 *
 * @author Luke GJ Potter
 * @since  08/05/2014
 */
public enum UrlyBirdApplicationMode {

    /**
     * Network mode; only display the
     * {@code suncertify.gui.client.URLyBirdClientGui} and use network access.
     *
     * This is the default application mode; to run the application in this mode
     * the user will not specify any Command Line Arguments.
     */
    NETWORKED_CLIENT,

    /**
     * Standalone mode; only display the
     * {@code suncertify.gui.client.URLyBirdClientGui} and not use network
     * access.
     */
    STANDALONE_CLIENT
}
