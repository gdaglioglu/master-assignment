package suncertify.gui.server;

import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;

/**
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
public class UrlyBirdServerGui extends JFrame {

    public UrlyBirdServerGui() {

        setTitle(URLyBirdApplicationGuiConstants.SERVER_GUI_APPLICATION_TITLE);
        setSize(URLyBirdApplicationGuiConstants.SERVER_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new ServerConfigurationPanel().getServerConfigurationPanel(), BorderLayout.CENTER);
        add(new ServerControlPanel().getServerControlPanel(), BorderLayout.SOUTH);
    }
}
