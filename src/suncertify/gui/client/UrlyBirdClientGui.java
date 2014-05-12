package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.controller.DatabaseAccessDaoLocal;
import suncertify.controller.DatabaseAccessDaoRemote;
import suncertify.utilities.URLyBirdApplicationGuiConstants;
import suncertify.utilities.URLyBirdApplicationMode;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

/**
 * This is the Client GUI for the URLyBird Application.
 *
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
public class UrlyBirdClientGui extends JFrame {

    public UrlyBirdClientGui(URLyBirdApplicationMode urlyBirdApplicationMode) {

        // ToDo: Check application configuration is correct.

        String csrNumber = null;
        try {
            csrNumber = receiveCsrNumber();
        } catch (Exception ignored) {
            System.exit(1);
        }

        DatabaseAccessDao databaseAccessDao = retrieveCorrectDao(urlyBirdApplicationMode);

        setTitle(URLyBirdApplicationGuiConstants.CLIENT_GUI_APPLICATION_TITLE + " - " + csrNumber);
        setSize(URLyBirdApplicationGuiConstants.CLIENT_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new SearchPanel(databaseAccessDao).getSearchPanel(), BorderLayout.NORTH);
        add(new TablePanel(databaseAccessDao).getTablePanel(), BorderLayout.CENTER);
        add(new BookingPanel(databaseAccessDao, csrNumber).getBookingPanel(), BorderLayout.SOUTH);
    }

    private String receiveCsrNumber() {

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

    private boolean isAllDigits(String csrNumber) {

        try {
            Integer.parseInt(csrNumber);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Returns a local or remote DatabaseAccessDao instance, depending on the
     * Application Mode.
     *
     * @param urlyBirdApplicationMode The mode of the application.
     * @return a {@code DatabaseAccessDaoRemote} instance, if the application is
     *         set to {@code NETWORKED_CLIENT}.
     *         a {@code DatabaseAccessDaoLocal} instance, if the application is
     *         set to {@code STANDALONE_CLIENT}.
     */
    private DatabaseAccessDao retrieveCorrectDao(URLyBirdApplicationMode urlyBirdApplicationMode) {

        if (urlyBirdApplicationMode == URLyBirdApplicationMode.NETWORKED_CLIENT) {
            try {
                return new DatabaseAccessDaoRemote();
            } catch (RemoteException ignored) {
                return null;
            }
        } else if (urlyBirdApplicationMode == URLyBirdApplicationMode.STANDALONE_CLIENT) {
            return new DatabaseAccessDaoLocal();
        } else {
            return null;
        }
    }
}
