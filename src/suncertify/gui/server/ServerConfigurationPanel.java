package suncertify.gui.server;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.StringTokenizer;

/**
 * The JPanel that contains the JTextFields for the database location, RMI
 * hostname and RMI port which are used by the server GUI.
 *
 * @author Luke GJ Potter
 * @since  29/04/2014
 */
class ServerConfigurationPanel {

    private final JPanel serverConfigurationPanel;
    private JLabel pathToDatabaseFileLabel, rmiHostnameLabel, rmiPortNumberLabel;
    private JTextField pathToDatabaseFileTextField, rmiHostnameTextField, rmiPortNumberTextField;

    /**
     * The constructor for the ServerConfigurationPanel. It initialises and lays
     * out the GUI components.
     */
    public ServerConfigurationPanel() {

        serverConfigurationPanel = new JPanel();
        serverConfigurationPanel.setLayout(new GridBagLayout());

        initialiseServerConfigurationPanelLabelsAndTextFields();
        layoutServerConfigurationPanel();

    }

    /**
     * Gets the ServerConfigurationPanel.
     *
     * @return the ServerConfigurationPanel.
     */
    public JPanel getServerConfigurationPanel() {
        return serverConfigurationPanel;
    }

    /**
     * Get the text in the "database path" JTextField.
     *
     * @return The text in the "database path" JTextField.
     */
    public String getPathToDatabaseFileFromTextField() {
        return pathToDatabaseFileTextField.getText().trim();
    }

    /**
     * Get the text in the "rmi hostname" JTextField.
     *
     * @return The text in the "rmi hostname" JTextField.
     */
    public String getRmiHostnameFromTextField() {
        return rmiHostnameTextField.getText().trim();
    }

    /**
     * Get the text in the "rmi port" JTextField.
     *
     * @return The text in the "rmi port" JTextField.
     */
    public String getRmiPortNumberFromTextField() {
        return rmiPortNumberTextField.getText().trim();
    }

    /**
     * Ensures that the values in the ServerConfigurationPanel's JTextFields are
     * valid values. This method examines if the database file exists in the
     * location specified.
     *
     * @return True, if the values are valid.
     *         False, if the values are invalid.
     */
    public boolean areTextFieldValuesValid() {

        String dbFilePath = getPathToDatabaseFileFromTextField();
        String rmiHostname = getRmiHostnameFromTextField();
        String rmiPortNumber = getRmiPortNumberFromTextField();
        String emptyString = UrlyBirdApplicationConstants.EMPTY_STRING;

        boolean emptyFields = dbFilePath.equals(emptyString) || rmiHostname.equals(emptyString) || rmiPortNumber.equals(emptyString);

        return (!emptyFields) && new File(dbFilePath).exists() && isValidHostname(rmiHostname) && isValidPortNumber(rmiPortNumber);
    }

    /**
     * Disables all the ServerConfigurationPanel's JTextFields, so they cannot
     * be edited.
     */
    public void disableAllFields() {

        pathToDatabaseFileTextField.setEnabled(false);
        rmiHostnameTextField.setEnabled(false);
        rmiPortNumberTextField.setEnabled(false);
    }

    // ----- Private Methods -----

    /**
     * Lays out the ServerConfigurationPanel using a GridBagLayout.
     */
    private void layoutServerConfigurationPanel() {

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        serverConfigurationPanel.add(pathToDatabaseFileLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        serverConfigurationPanel.add(pathToDatabaseFileTextField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        serverConfigurationPanel.add(rmiHostnameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        serverConfigurationPanel.add(rmiHostnameTextField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        serverConfigurationPanel.add(rmiPortNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        serverConfigurationPanel.add(rmiPortNumberTextField, gridBagConstraints);
    }

    /**
     * Initialises the ServerConfigurationPanel UI components.
     */
    private void initialiseServerConfigurationPanelLabelsAndTextFields() {

        pathToDatabaseFileLabel = new JLabel(UrlyBirdApplicationGuiConstants.PATH_TO_DATABASE_FILE, JLabel.TRAILING);
        pathToDatabaseFileTextField = new JTextField(UrlyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        pathToDatabaseFileTextField.setText(UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
        pathToDatabaseFileLabel.setLabelFor(pathToDatabaseFileTextField);

        rmiHostnameLabel = new JLabel(UrlyBirdApplicationGuiConstants.RMI_HOSTNAME, JLabel.TRAILING);
        rmiHostnameTextField = new JTextField(UrlyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        rmiHostnameTextField.setText(UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME));
        rmiHostnameLabel.setLabelFor(rmiHostnameTextField);

        rmiPortNumberLabel = new JLabel(UrlyBirdApplicationGuiConstants.RMI_PORT_NUMBER, JLabel.TRAILING);
        rmiPortNumberTextField = new JTextField(UrlyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        rmiPortNumberTextField.setText(UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER));
        rmiPortNumberLabel.setLabelFor(rmiPortNumberTextField);
    }

    /**
     * Examines if the rmi hostname is valid.
     *
     * @param rmiHostname The RMI hostname to validate.
     * @return True, if the hostname is valid.
     *         False, if the hostname is invalid.
     */
    private boolean isValidHostname(String rmiHostname) {

        return rmiHostname.equals("localhost") || isValidIpAddress(rmiHostname);
    }

    /**
     * Examines the ip address of the rmi hostname.
     *
     * @param rmiHostname The hostname for the RMI server in the dotted decimal
     *                    format of an IP address.
     * @return True, if the hostname is a valid IP address.
     *         False, if the hostname is an invalid IP address.
     */
    private boolean isValidIpAddress(String rmiHostname) {

        StringTokenizer stringTokenizer = new StringTokenizer(rmiHostname, ".");

        if (stringTokenizer.countTokens() != 4) return false;

        while (stringTokenizer.hasMoreTokens()) {

            int ipBlock;

            try {
                ipBlock = Integer.parseInt(stringTokenizer.nextToken(), 10);
            } catch (NumberFormatException ignored) { return false; }

            if (ipBlock < 0 || ipBlock > 255) return false;
        }

        return true;
    }

    /**
     * Checks that the RMI Port is a valid port number.
     *
     * @param rmiPortNumber The port number to validate.
     * @return True, if the port is valid.
     *         False, if the port is invalid.
     */
    private boolean isValidPortNumber(String rmiPortNumber) {

        try {
            return (Integer.parseInt(rmiPortNumber) > 0);
        } catch (NumberFormatException ignored) { return false; }
    }
}
