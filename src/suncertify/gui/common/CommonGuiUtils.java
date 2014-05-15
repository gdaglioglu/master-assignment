package suncertify.gui.common;

import javax.swing.*;

/**
 * @author Luke GJ Potter
 *         Date: 15/05/2014
 */
public class CommonGuiUtils {

    public static void showErrorMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
