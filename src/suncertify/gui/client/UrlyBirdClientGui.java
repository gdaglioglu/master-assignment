package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;
import suncertify.utilities.UrlyBirdApplicationMode;

import javax.swing.*;
import java.awt.*;

/**
 * This is the Client GUI for the URLyBird Application. It only contains the
 * logic for displaying the GUI components.
 *
 * @author Luke GJ Potter
 * @since  22/04/2014
 */
public class UrlyBirdClientGui extends JFrame {

    /**
     * The constructor for the Client GUI. It sets up the JFrame and the panels.
     *
     * @param urlyBirdApplicationMode The mode that the application is running
     *                                in.
     */
    public UrlyBirdClientGui(UrlyBirdApplicationMode urlyBirdApplicationMode) {

        UrlyBirdClientGuiUtils clientUtils = UrlyBirdClientGuiUtils.getInstance();
        clientUtils.ensurePropertiesAreValid(urlyBirdApplicationMode);
        DatabaseAccessDao databaseAccessDao = clientUtils.retrieveCorrectDao(urlyBirdApplicationMode);

        String csrNumber = null;
        try {
            csrNumber = clientUtils.receiveCsrNumber();
        } catch (NullPointerException ignored) { System.exit(1); }

        setTitle(UrlyBirdApplicationGuiConstants.CLIENT_GUI_APPLICATION_TITLE + " - " + csrNumber);
        setSize(UrlyBirdApplicationGuiConstants.CLIENT_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(new SearchPanel(databaseAccessDao).getSearchPanel(), BorderLayout.NORTH);
        add(new TablePanel(databaseAccessDao).getTablePanel(), BorderLayout.CENTER);
        add(new BookingPanel(databaseAccessDao, csrNumber).getBookingPanel(), BorderLayout.SOUTH);
    }
}
