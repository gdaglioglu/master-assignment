package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.controller.DatabaseAccessDaoLocal;
import suncertify.controller.DatabaseAccessDaoRemote;
import suncertify.gui.common.CommonGuiUtils;
import suncertify.rmi.RmiClientManager;
import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationMode;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This singleton class contains the utility methods for the client GUI. It
 * allows the {@link suncertify.gui.client.UrlyBirdClientGui} to only contain
 * code for the displaying of the UI Components.
 *
 * @author Luke GJ Potter
 * @since 15/05/2014
 */
public class UrlyBirdClientGuiUtils {

    private static final UrlyBirdClientGuiUtils instance =
            new UrlyBirdClientGuiUtils();

    private UrlyBirdClientGuiUtils() {
    }

    /**
     * The singleton constructor.
     *
     * @return An instance of {@code UrlyBirdClientGuiUtils}.
     */
    public static UrlyBirdClientGuiUtils getInstance() {
        return instance;
    }

    /**
     * Ensures that the properties in the properties file are correct for the
     * GUI to function in the current application mode.
     * <p>If the application is started in standalone mode, it checks that the
     * database file, supplied in the properties file, exists. If the database
     * file does not exist, the User is asked for it's location.</p>
     * <p>If the application is started in networked mode, it checks that the
     * RMI host and port are correct, by attempting to connect to the server.
     * Should the connection attempt fail, the client aborts.</p>
     *
     * @param urlyBirdApplicationMode The mode that the application is running
     *                                in.
     */
    public void ensurePropertiesAreValid(
            UrlyBirdApplicationMode urlyBirdApplicationMode) {

        Properties properties =
                UrlyBirdApplicationObjectsFactory
                        .getUrlyBirdApplicationProperties();

        if (urlyBirdApplicationMode
                == UrlyBirdApplicationMode.STANDALONE_CLIENT) {

            File databaseFile = new File(properties.getProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));

            while (!databaseFile.exists()) {

                properties.setProperty(
                        UrlyBirdApplicationConstants
                                .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE,
                        JOptionPane.showInputDialog(
                                null,
                                "Enter the location of the database file"));
                try {
                    properties.store(
                            new FileOutputStream(
                                    UrlyBirdApplicationConstants
                                            .PROPERTY_FILE_NAME), null);
                } catch (IOException e) {
                    CommonGuiUtils
                            .showErrorMessageDialog(
                                    "The Application's Properties File does not"
                                            + " exist.");
                }

                databaseFile = new File(properties.getProperty(
                        UrlyBirdApplicationConstants
                                .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            }
        } else if (urlyBirdApplicationMode
                == UrlyBirdApplicationMode.NETWORKED_CLIENT) {
            while (RmiClientManager.connectToRemoteServerViaRmi() == null) {
                CommonGuiUtils.showErrorMessageDialog(
                        "Could not connect to Server.\n\n"
                                + "Troubleshooting Tips:\n"
                                + "Is the server running?\n"
                                + "Is the server URL correct?");
                askForRmiHostnameAndPort(properties);
            }
        }
    }

    /**
     * Asks the GUI User for their CSR Number by launching a
     * {@code JOptionPane.showInputDialog} window. It performs checks on the CSR
     * number that is entered to ensure that the CSR number is 8 digits.
     *
     * @return The correctly formatted CSR number.
     */
    public String receiveCsrNumber() {

        String csrNumber = null;
        boolean correctCsrNumberEntered = false;

        while (!correctCsrNumberEntered) {

            csrNumber = JOptionPane.showInputDialog(null,
                    "Login with your CSR number (8 digits)",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE).trim();

            if (csrNumber.length() != 8) {
                JOptionPane.showMessageDialog(null,
                        "CSR length must be 8 digits, you have entered "
                                + csrNumber.length()
                                + " digits.");
            } else if (!isAllDigits(csrNumber)) {
                JOptionPane.showMessageDialog(null,
                        "CSR length must be all digits");
            } else {
                correctCsrNumberEntered = true;
            }
        }

        return csrNumber;
    }

    /**
     * Returns a local or remote {@link suncertify.controller.DatabaseAccessDao}
     * instance, depending on the Application Mode.
     *
     * @param urlyBirdApplicationMode The mode of the application.
     * @return A {@link suncertify.controller.DatabaseAccessDaoRemote} instance,
     * if the application is set to {@code NETWORKED_CLIENT}.
     * A {@link suncertify.controller.DatabaseAccessDaoLocal} instance,
     * if the application is set to {@code STANDALONE_CLIENT}.
     */
    public DatabaseAccessDao retrieveCorrectDao(
            UrlyBirdApplicationMode urlyBirdApplicationMode) {

        if (urlyBirdApplicationMode
                == UrlyBirdApplicationMode.NETWORKED_CLIENT) {
            return new DatabaseAccessDaoRemote();
        } else if (urlyBirdApplicationMode
                == UrlyBirdApplicationMode.STANDALONE_CLIENT) {
            return new DatabaseAccessDaoLocal();
        } else {
            return null;
        }
    }

    // ----- Private Methods -----

    /**
     * Checks that the CSR Number string that has been passed contains only
     * digits.
     *
     * @param csrNumber The CSR Number of the GUI User.
     * @return True, if the CSR Number is all digits.
     * False, if the CSR Number is not all digits.
     */
    private boolean isAllDigits(String csrNumber) {

        try {
            Integer.parseInt(csrNumber);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Asks the user for the RMI hostname and RMI port number by using
     * JOptionPane Input Dialogs. This method also verifies the input.
     *
     * @param properties The application's properties object.
     */
    private void askForRmiHostnameAndPort(Properties properties) {

        String rmiHost = "", rmiPort = "";

        try {
            while (!CommonGuiUtils.isValidHostname(rmiHost)) {
                rmiHost = JOptionPane.showInputDialog(null,
                        "Enter the RMI Hostname");
            }

            while (!CommonGuiUtils.isValidPortNumber(rmiPort)) {
                rmiPort = JOptionPane.showInputDialog(null,
                        "Enter the RMI Port");
            }

            CommonGuiUtils.updatePropertiesToReflectGui(
                    properties.getProperty(
                            UrlyBirdApplicationConstants
                                    .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE),
                    rmiHost, rmiPort);

        } catch (NullPointerException ignored) {
            JOptionPane.showMessageDialog(null,
                    "Exiting Networked Client GUI because RMI URL parameters "
                            + "were not set.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error writing to Properties file.");
            e.printStackTrace();
        }
    }
}
