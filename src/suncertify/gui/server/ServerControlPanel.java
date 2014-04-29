package suncertify.gui.server;

import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
public class ServerControlPanel {

    private JPanel serverControlPanel;

    public ServerControlPanel() {

        serverControlPanel = new JPanel();
        serverControlPanel.setLayout(new FlowLayout());

        JButton startServerButton = new JButton(URLyBirdApplicationGuiConstants.START_SERVER_BUTTON);
        startServerButton.addActionListener(new StartServer());

        serverControlPanel.add(startServerButton);
    }

    public JPanel getServerControlPanel() {
        return serverControlPanel;
    }

    private class StartServer implements ActionListener {

        @Override public void actionPerformed(ActionEvent actionEvent) {
            // TODO: Implement RMI
        }
    }
}
