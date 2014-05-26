package suncertify.gui.common;

import javax.swing.*;

/**
 * A utilities class to be used across all the GUIs
 *
 * @author Luke GJ Potter
 * @since  15/05/2014
 */
public class CommonGuiUtils {

    /**
     * Displays a error message dialog using the
     * {@code JOptionPane.showMessageDialog} window.
     *
     * @param message The error message to be displayed.
     */
    public static void showErrorMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
