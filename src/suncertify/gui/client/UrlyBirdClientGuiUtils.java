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
import java.rmi.RemoteException;
import java.util.Properties;

/**
 * @author Luke GJ Potter
 *         Date: 15/05/2014
 */
public class UrlyBirdClientGuiUtils {

    private static UrlyBirdClientGuiUtils ourInstance = new UrlyBirdClientGuiUtils();

    public static UrlyBirdClientGuiUtils getInstance() {
        return ourInstance;
    }

    private UrlyBirdClientGuiUtils() {
    }

    public void ensurePropertiesAreValid(UrlyBirdApplicationMode urlyBirdApplicationMode) {

        Properties properties = UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties();

        if (urlyBirdApplicationMode == UrlyBirdApplicationMode.STANDALONE_CLIENT) {

            File databaseFile = new File(properties.getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));

            while (! databaseFile.exists()) {

                properties.setProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE, JOptionPane.showInputDialog(null, "Enter the location of the database file"));
                try {
                    properties.store(new FileOutputStream(UrlyBirdApplicationConstants.PROPERTY_FILE_NAME), null);
                } catch (IOException e) {
                    CommonGuiUtils.showErrorMessageDialog("The Application's Properties File does not exist.");
                }

                databaseFile = new File(properties.getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
            }
        } else if (urlyBirdApplicationMode == UrlyBirdApplicationMode.NETWORKED_CLIENT) {
            if (RmiClientManager.connectToRemoteServerViaRmi() == null) {
                CommonGuiUtils.showErrorMessageDialog("Could not connect to Server.\n\n" +
                        "Troubleshooting Tips:\n" +
                        "Is the server running?\n" +
                        "Is the server URL correct?");
                System.exit(1);
            }
        }
    }



    public String receiveCsrNumber() {

        String csrNumber = null;
        boolean correctCsrNumberEntered = false;

        while (!correctCsrNumberEntered) {

            csrNumber =  JOptionPane.showInputDialog(null, "Login with your CSR number (8 digits)", "Login", JOptionPane.INFORMATION_MESSAGE).trim();

            if (csrNumber.length() != 8) {
                JOptionPane.showMessageDialog(null, "CSR length must be 8 digits, you have entered " + csrNumber.length() + " digits.");
            } else if (!isAllDigits(csrNumber)) {
                JOptionPane.showMessageDialog(null, "CSR length must be all digits");
            } else {
                correctCsrNumberEntered = true;
            }
        }

        return csrNumber;
    }

    /**
     * Returns a local or remote DatabaseAccessDao instance, depending on the
     * Application Mode.
     *
     * @param urlyBirdApplicationMode The mode of the application.
     * @return A {@code DatabaseAccessDaoRemote} instance, if the application is
     *         set to {@code NETWORKED_CLIENT}.
     *         A {@code DatabaseAccessDaoLocal} instance, if the application is
     *         set to {@code STANDALONE_CLIENT}.
     */
    public DatabaseAccessDao retrieveCorrectDao(UrlyBirdApplicationMode urlyBirdApplicationMode) {

        if (urlyBirdApplicationMode == UrlyBirdApplicationMode.NETWORKED_CLIENT) {
            try {
                return new DatabaseAccessDaoRemote();
            } catch (RemoteException ignored) {
                return null;
            }
        } else if (urlyBirdApplicationMode == UrlyBirdApplicationMode.STANDALONE_CLIENT) {
            return new DatabaseAccessDaoLocal();
        } else {
            return null;
        }
    }

    private boolean isAllDigits(String csrNumber) {

        try {
            Integer.parseInt(csrNumber);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
