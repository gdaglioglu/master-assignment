package suncertify.gui.client;

import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.BorderLayout;

/**
 * This is the main GUI for the URLyBird Application.
 *
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
public class UrlyBirdClientGui extends JFrame {

    public UrlyBirdClientGui(String[] args) {

        setTitle(URLyBirdApplicationGuiConstants.CLIENT_GUI_APPLICATION_TITLE);
        setSize(URLyBirdApplicationGuiConstants.CLIENT_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new SearchPanel().getSearchPanel(), BorderLayout.NORTH);
        add(new TablePanel().getTablePanel(), BorderLayout.CENTER);
        add(new BookingPanel().getBookingPanel(), BorderLayout.SOUTH);
    }
}
