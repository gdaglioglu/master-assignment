package suncertify.gui.server;

import suncertify.gui.common.CommonGuiUtils;
import suncertify.rmi.RmiServerManager;
import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
                        "Please enter valid values in the Text Fields.");
                return;
            }

            try {
                updatePropertiesToReflectServerGui();
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

        /**
         * Updates the {@code suncertify.properties} file with the latest
         * properties.
         *
         * @throws IOException If there's a problem with accessing the
         *                     {@code suncertify.properties} file.
         */
        private void updatePropertiesToReflectServerGui() throws IOException {

            Properties properties =
                    UrlyBirdApplicationObjectsFactory
                            .getURLyBirdApplicationProperties();

            if (hasDatabasePathPropertyChanged(properties)) {

                properties.setProperty(
                        UrlyBirdApplicationConstants
                                .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE,
                        serverConfigurationPanel
                                .getPathToDatabaseFileFromTextField());

                properties.setProperty(
                        UrlyBirdApplicationConstants
                                .PROPERTY_FILE_KEY_RMI_HOSTNAME,
                        serverConfigurationPanel.getRmiHostnameFromTextField());

                properties.setProperty(
                        UrlyBirdApplicationConstants
                                .PROPERTY_FILE_KEY_RMI_PORT_NUMBER,
                        serverConfigurationPanel
                                .getRmiPortNumberFromTextField());

                properties.store(new FileOutputStream(
                        UrlyBirdApplicationConstants.PROPERTY_FILE_NAME), null);
            }
        }

        /**
         * Compares the URLyBird Application's properties to the text in the
         * JTextFields on the {@code ServerConfigurationPanel}.
         *
         * @param properties The current properties of the URLyBird Application.
         * @return True, if the properties have been changed.
         * False, if the properties have not been changed.
         */
        private boolean hasDatabasePathPropertyChanged(Properties properties) {

            return !(properties.getProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE)
                    .equals(serverConfigurationPanel
                            .getPathToDatabaseFileFromTextField())

                    && properties.getProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_RMI_HOSTNAME)
                    .equals(serverConfigurationPanel
                            .getRmiHostnameFromTextField())

                    && properties.getProperty(
                    UrlyBirdApplicationConstants
                            .PROPERTY_FILE_KEY_RMI_PORT_NUMBER)
                    .equals(serverConfigurationPanel
                            .getRmiPortNumberFromTextField()));
        }
    }
}
