package suncertify.rmi;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

/**
 * A class containing utility methods for the RMI package.
 *
 * @author Luke GJ Potter
 * @since  07/05/2014
 */
class RmiUtils {

    // ----- Public Methods -----
    /**
     * Forms the RMI URL from the values in the suncertify.properties file.
     *
     * @return The RMI URL where the server is listening.
     */
    public static String formRmiUrl() {

        return "rmi://" + attainRmiHostnameFromPropertiesFile() + ":" + attainRmiPortNumberFromPropertiesFile() + UrlyBirdApplicationConstants.RMI_APPLICATION_PATH;
    }

    // ----- Private Methods -----
    private static String attainRmiHostnameFromPropertiesFile() {
        return UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME);
    }

    private static String attainRmiPortNumberFromPropertiesFile() {
        return UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER);
    }
}
