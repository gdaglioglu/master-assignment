package suncertify.gui.server;

import suncertify.rmi.RmiServerManager;
import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationGuiConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
public class UrlyBirdServerGui extends JFrame {

    private final ServerConfigurationPanel serverConfigurationPanel;
    private JButton startServerButton;
    private JLabel serverStatusLabel;

    public UrlyBirdServerGui() {

        setTitle(URLyBirdApplicationGuiConstants.SERVER_GUI_APPLICATION_TITLE);
        setSize(URLyBirdApplicationGuiConstants.SERVER_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        serverConfigurationPanel = new ServerConfigurationPanel();

        add(serverConfigurationPanel.getServerConfigurationPanel(), BorderLayout.CENTER);
        add(getServerControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel getServerControlPanel() {

        JPanel serverControlPanel = new JPanel(new FlowLayout());
        startServerButton = new JButton(URLyBirdApplicationGuiConstants.START_SERVER_BUTTON);

        startServerButton.setEnabled(true);
        startServerButton.addActionListener(new StartServer());

        serverControlPanel.add(startServerButton);

        serverStatusLabel = new JLabel(URLyBirdApplicationGuiConstants.SERVER_STOPPED);
        serverControlPanel.add(serverStatusLabel);

        return serverControlPanel;
    }

    private class StartServer implements ActionListener {

        @Override public void actionPerformed(ActionEvent actionEvent) {


            if (!serverConfigurationPanel.areTextFieldValuesValid()) {
                showErrorMessageDialog("Please enter valid values in the Text Fields.");
                return;
            }

            try {
                updatePropertiesToReflectServerGui();
            } catch (FileNotFoundException ignored) {
                showErrorMessageDialog("The Application's Properties File does not exist.");
                return;
            } catch (IOException ignored) {
                showErrorMessageDialog("There was an error updating the Application's Properties File.");
                return;
            }

            RmiServerManager.startRmiServer();

            if (RmiServerManager.isRmiServerRunning()) {
                serverConfigurationPanel.disableAllFields();
                startServerButton.setEnabled(false);
                serverStatusLabel.setText(URLyBirdApplicationGuiConstants.SERVER_STARTED);
            }
        }

        private void showErrorMessageDialog(String message) {
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        private void updatePropertiesToReflectServerGui() throws IOException {

            Properties properties = URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties();

            if (hasDatabasePathPropertyChanged(properties)) {

                properties.setProperty(
                        URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE,
                        serverConfigurationPanel.getPathToDatabaseFileFromTextField());

                properties.setProperty(
                        URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME,
                        serverConfigurationPanel.getRmiHostnameFromTextField());

                properties.setProperty(
                        URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER,
                        serverConfigurationPanel.getRmiPortNumberFromTextField());

                properties.store(new FileOutputStream(URLyBirdApplicationConstants.PROPERTY_FILE_NAME), null);
            }
        }

        private boolean hasDatabasePathPropertyChanged(Properties properties) {

            return !(properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE)
                    .equals(serverConfigurationPanel.getPathToDatabaseFileFromTextField())

                    && properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME)
                    .equals(serverConfigurationPanel.getRmiHostnameFromTextField())

                    && properties.getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER)
                    .equals(serverConfigurationPanel.getRmiPortNumberFromTextField()));
        }
    }
}
