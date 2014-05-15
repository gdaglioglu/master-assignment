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
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
public class UrlyBirdServerGui extends JFrame {

    private final ServerConfigurationPanel serverConfigurationPanel;
    private JButton startServerButton;
    private JLabel serverStatusLabel;

    public UrlyBirdServerGui() {

        setTitle(UrlyBirdApplicationGuiConstants.SERVER_GUI_APPLICATION_TITLE);
        setSize(UrlyBirdApplicationGuiConstants.SERVER_GUI_DIMENSION);

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        serverConfigurationPanel = new ServerConfigurationPanel();

        add(serverConfigurationPanel.getServerConfigurationPanel(), BorderLayout.CENTER);
        add(getServerControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel getServerControlPanel() {

        JPanel serverControlPanel = new JPanel(new FlowLayout());
        startServerButton = new JButton(UrlyBirdApplicationGuiConstants.START_SERVER_BUTTON);

        startServerButton.setEnabled(true);
        startServerButton.addActionListener(new StartServer());

        serverControlPanel.add(startServerButton);

        serverStatusLabel = new JLabel(UrlyBirdApplicationGuiConstants.SERVER_STOPPED);
        serverControlPanel.add(serverStatusLabel);

        return serverControlPanel;
    }

    private class StartServer implements ActionListener {

        @Override public void actionPerformed(ActionEvent actionEvent) {


            if (!serverConfigurationPanel.areTextFieldValuesValid()) {
                CommonGuiUtils.showErrorMessageDialog("Please enter valid values in the Text Fields.");
                return;
            }

            try {
                updatePropertiesToReflectServerGui();
            } catch (FileNotFoundException ignored) {
                CommonGuiUtils.showErrorMessageDialog("The Application's Properties File does not exist.");
                return;
            } catch (IOException ignored) {
                CommonGuiUtils.showErrorMessageDialog("There was an error updating the Application's Properties File.");
                return;
            }

            RmiServerManager.startRmiServer();

            if (RmiServerManager.isRmiServerRunning()) {
                serverConfigurationPanel.disableAllFields();
                startServerButton.setEnabled(false);
                serverStatusLabel.setText(UrlyBirdApplicationGuiConstants.SERVER_STARTED);
            }
        }

        private void updatePropertiesToReflectServerGui() throws IOException {

            Properties properties = UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties();

            if (hasDatabasePathPropertyChanged(properties)) {

                properties.setProperty(
                        UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE,
                        serverConfigurationPanel.getPathToDatabaseFileFromTextField());

                properties.setProperty(
                        UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME,
                        serverConfigurationPanel.getRmiHostnameFromTextField());

                properties.setProperty(
                        UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER,
                        serverConfigurationPanel.getRmiPortNumberFromTextField());

                properties.store(new FileOutputStream(UrlyBirdApplicationConstants.PROPERTY_FILE_NAME), null);
            }
        }

        private boolean hasDatabasePathPropertyChanged(Properties properties) {

            return !(properties.getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE)
                    .equals(serverConfigurationPanel.getPathToDatabaseFileFromTextField())

                    && properties.getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME)
                    .equals(serverConfigurationPanel.getRmiHostnameFromTextField())

                    && properties.getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER)
                    .equals(serverConfigurationPanel.getRmiPortNumberFromTextField()));
        }
    }
}
