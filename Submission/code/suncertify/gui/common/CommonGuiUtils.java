package suncertify.gui.common;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * A utilities class to be used across all the GUIs
 *
 * @author Luke GJ Potter
 * @since 15/05/2014
 */
public class CommonGuiUtils {

    /**
     * Displays a error message dialog using the
     * {@code JOptionPane.showMessageDialog} window.
     *
     * @param message The error message to be displayed.
     */
    public static void showErrorMessageDialog(String message) {
        JOptionPane.showMessageDialog(
                null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Examines if the RMI Hostname is valid.
     *
     * @param rmiHostname The RMI hostname to validate.
     * @return True, if the hostname is valid.
     * False, if the hostname is invalid.
     */
    public static boolean isValidHostname(String rmiHostname) {

        return rmiHostname.equals("localhost") || isValidIpAddress(rmiHostname);
    }

    /**
     * Checks that the RMI Port is a valid port number.
     *
     * @param rmiPortNumber The port number to validate.
     * @return True, if the port is valid.
     * False, if the port is invalid.
     */
    public static boolean isValidPortNumber(String rmiPortNumber) {

        try {
            return (Integer.parseInt(rmiPortNumber) > 0);
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Updates the {@code suncertify.properties} file with the latest
     * properties.
     *
     * @param dbPath  The path to the database file.
     * @param rmiHost The RMI hostname.
     * @param rmiPort The RMI Port Number.
     * @throws IOException If there's a problem with accessing the
     *                     {@code suncertify.properties} file.
     */
    public static void updatePropertiesToReflectGui(String dbPath,
                                                    String rmiHost,
                                                    String rmiPort)
            throws IOException {

        Properties properties =
                UrlyBirdApplicationObjectsFactory
                        .getUrlyBirdApplicationProperties();

        if (havePropertiesChanged(
                properties, dbPath, rmiHost, rmiPort)) {

            properties.setProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE, dbPath);

            properties.setProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_RMI_HOSTNAME, rmiHost);

            properties.setProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_RMI_PORT_NUMBER, rmiPort);

            properties.store(new FileOutputStream(
                    UrlyBirdApplicationConstants.PROPERTY_FILE_NAME), null);
        }
    }

    /**
     * Compares the URLyBird Application's properties to the text in the
     * values passed as arguments.
     *
     * @param properties The current properties of the URLyBird Application.
     * @param dbPath     The path to the database file.
     * @param rmiHost    The RMI hostname.
     * @param rmiPort    The RMI Port Number.
     * @return True, if the properties have been changed.
     * False, if the properties have not been changed.
     */
    private static boolean havePropertiesChanged(
            Properties properties, String dbPath, String rmiHost,
            String rmiPort) {

        return !(properties.getProperty(
                UrlyBirdApplicationConstants
                        .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE)
                .equals(dbPath)

                && properties.getProperty(
                UrlyBirdApplicationConstants
                        .PROPERTY_FILE_KEY_RMI_HOSTNAME)
                .equals(rmiHost)

                && properties.getProperty(
                UrlyBirdApplicationConstants
                        .PROPERTY_FILE_KEY_RMI_PORT_NUMBER)
                .equals(rmiPort));
    }

    /**
     * Examines the ip address of the rmi hostname.
     *
     * @param rmiHostname The hostname for the RMI server in the dotted decimal
     *                    format of an IP address.
     * @return True, if the hostname is a valid IP address.
     * False, if the hostname is an invalid IP address.
     */
    private static boolean isValidIpAddress(String rmiHostname) {

        StringTokenizer stringTokenizer = new StringTokenizer(rmiHostname, ".");

        if (stringTokenizer.countTokens() != 4) return false;

        while (stringTokenizer.hasMoreTokens()) {

            int ipBlock;

            try {
                ipBlock = Integer.parseInt(stringTokenizer.nextToken(), 10);
            } catch (NumberFormatException ignored) {
                return false;
            }

            if (ipBlock < 0 || ipBlock > 255) return false;
        }

        return true;
    }
}
