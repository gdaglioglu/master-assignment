package suncertify.rmi;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

/**
 * @author Luke GJ Potter
 *         Date: 07/05/2014
 */
class RmiUtils {

    public static String formRmiUrl() {

        return "rmi://" + attainRmiHostnameFromPropertiesFile() + ":" + attainRmiPortNumberFromPropertiesFile() + UrlyBirdApplicationConstants.RMI_APPLICATION_PATH;
    }

    private static String attainRmiHostnameFromPropertiesFile() {
        return UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME);
    }

    private static String attainRmiPortNumberFromPropertiesFile() {
        return UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER);
    }
}
