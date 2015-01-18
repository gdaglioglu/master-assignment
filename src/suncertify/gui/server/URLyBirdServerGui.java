package suncertify.gui.server;

import suncertify.gui.common.CommonGuiUtils;
import suncertify.rmi.RmiServerManager;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The Server GUI, it contains the UI components and the logic for Starting the
 * server.
 *
 * @author Luke GJ Potter
 * @since 29/04/2014
 */
public class UrlyBirdServerGui extends JFrame {

    private final ServerConfigurationPanel serverConfigurationPanel;
    private JButton startServerButton;
    private JLabel serverStatusLabel;

    /**
     * The constructor for the UrlyBirdServerGui class. It initialises the GUI
     * components.
     */
    public UrlyBirdServerGui() {

        setTitle(UrlyBirdApplicationGuiConstants.SERVER_GUI_APPLICATION_TITLE);
        setSize(UrlyBirdApplicationGuiConstants.SERVER_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        serverConfigurationPanel = new ServerConfigurationPanel();

        add(serverConfigurationPanel.getServerConfigurationPanel(),
                BorderLayout.CENTER);
        add(getServerControlPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates a instance of a JPanel with the start server button.
     *
     * @return a JPanel with the button to start the server.
     */
    private JPanel getServerControlPanel() {

        JPanel serverControlPanel = new JPanel(new FlowLayout());
        startServerButton = new JButton(
                UrlyBirdApplicationGuiConstants.START_SERVER_BUTTON);

        startServerButton.setEnabled(true);
        startServerButton.addActionListener(new StartServer());

        serverControlPanel.add(startServerButton);

        serverStatusLabel = new JLabel(
                UrlyBirdApplicationGuiConstants.SERVER_STOPPED);
        serverControlPanel.add(serverStatusLabel);

        return serverControlPanel;
    }

    /**
     * The ActionListener for the Start Server JButton.
     */
    private class StartServer implements ActionListener {

        /**
         * Handles starting the server. It checks that the JTextFields are
         * correct. Then it uses RMI to start the server and make it available
         * for incoming client connections.
         *
         * @param actionEvent The event performed.
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            if (!serverConfigurationPanel.areTextFieldValuesValid()) {
                CommonGuiUtils.showErrorMessageDialog(
                        "Please enter valid values in the Text Fields.\n\n"
                                + "Does the Database File exist?\n"
                                + "Are the RMI Hostname and Port Number"
                                + "correctly formatted?");
                return;
            }

            try {
                CommonGuiUtils.updatePropertiesToReflectGui(
                        serverConfigurationPanel
                                .getPathToDatabaseFileFromTextField(),
                        serverConfigurationPanel.getRmiHostnameFromTextField(),
                        serverConfigurationPanel
                                .getRmiPortNumberFromTextField());
            } catch (FileNotFoundException ignored) {
                CommonGuiUtils.showErrorMessageDialog(
                        "The Application's Properties File does not exist.");
                return;
            } catch (IOException ignored) {
                CommonGuiUtils.showErrorMessageDialog(
                        "There was an error updating the Application's"
                                + " Properties File.");
                return;
            }

            RmiServerManager.startRmiServer();

            if (RmiServerManager.isRmiServerRunning()) {
                serverConfigurationPanel.disableAllFields();
                startServerButton.setEnabled(false);
                serverStatusLabel.setText(
                        UrlyBirdApplicationGuiConstants.SERVER_STARTED);
            }
        }
    }
}
