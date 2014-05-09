package suncertify.rmi;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

/**
 * @author Luke GJ Potter
 *         Date: 07/05/2014
 */
class RmiUtils {

    public static String formRmiUrl() {

        return "rmi://" + attainRmiHostnameFromPropertiesFile() + ":" + attainRmiPortNumberFromPropertiesFile() + URLyBirdApplicationConstants.RMI_APPLICATION_PATH;
    }

    private static String attainRmiHostnameFromPropertiesFile() {
        return URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME);
    }

    private static String attainRmiPortNumberFromPropertiesFile() {
        return URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER);
    }
}
