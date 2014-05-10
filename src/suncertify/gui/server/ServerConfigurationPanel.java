package suncertify.gui.server;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationGuiConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.StringTokenizer;

/**
 * @author Luke GJ Potter
 *         Date: 29/04/2014
 */
class ServerConfigurationPanel {

    private final JPanel serverConfigurationPanel;
    private JLabel pathToDatabaseFileLabel, rmiHostnameLabel, rmiPortNumberLabel;
    private JTextField pathToDatabaseFileTextField, rmiHostnameTextField, rmiPortNumberTextField;

    public ServerConfigurationPanel() {

        serverConfigurationPanel = new JPanel();
        serverConfigurationPanel.setLayout(new GridBagLayout());

        initialiseServerConfigurationPanelLabelsAndTextFields();
        layoutServerConfigurationPanel();

    }

    public JPanel getServerConfigurationPanel() {
        return serverConfigurationPanel;
    }

    public String getPathToDatabaseFileFromTextField() {
        return pathToDatabaseFileTextField.getText().trim();
    }

    public String getRmiHostnameFromTextField() {
        return rmiHostnameTextField.getText().trim();
    }

    public String getRmiPortNumberFromTextField() {
        return rmiPortNumberTextField.getText().trim();
    }

    public boolean areTextFieldValuesValid() {

        String dbFilePath = getPathToDatabaseFileFromTextField();
        String rmiHostname = getRmiHostnameFromTextField();
        String rmiPortNumber = getRmiPortNumberFromTextField();
        String emptyString = URLyBirdApplicationConstants.EMPTY_STRING;

        boolean emptyFields = dbFilePath.equals(emptyString) || rmiHostname.equals(emptyString) || rmiPortNumber.equals(emptyString);

        return (!emptyFields) && new File(dbFilePath).exists() && isValidHostname(rmiHostname) && isValidPortNumber(rmiPortNumber);
    }

    public void disableAllFields() {

        pathToDatabaseFileTextField.setEnabled(false);
        rmiHostnameTextField.setEnabled(false);
        rmiPortNumberTextField.setEnabled(false);
    }

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
        // TODO: Implement JFileChooser

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

    private void initialiseServerConfigurationPanelLabelsAndTextFields() {

        pathToDatabaseFileLabel = new JLabel(URLyBirdApplicationGuiConstants.PATH_TO_DATABASE_FILE, JLabel.TRAILING);
        pathToDatabaseFileTextField = new JTextField(URLyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        pathToDatabaseFileTextField.setText(URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_PATH_TO_DATABASE_FILE));
        pathToDatabaseFileLabel.setLabelFor(pathToDatabaseFileTextField);

        rmiHostnameLabel = new JLabel(URLyBirdApplicationGuiConstants.RMI_HOSTNAME, JLabel.TRAILING);
        rmiHostnameTextField = new JTextField(URLyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        rmiHostnameTextField.setText(URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME));
        rmiHostnameLabel.setLabelFor(rmiHostnameTextField);

        rmiPortNumberLabel = new JLabel(URLyBirdApplicationGuiConstants.RMI_PORT_NUMBER, JLabel.TRAILING);
        rmiPortNumberTextField = new JTextField(URLyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        rmiPortNumberTextField.setText(URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER));
        rmiPortNumberLabel.setLabelFor(rmiPortNumberTextField);
    }

    private boolean isValidHostname(String rmiHostname) {

        return rmiHostname.equals("localhost") || isValidIpAddress(rmiHostname);
    }

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

    private boolean isValidPortNumber(String rmiPortNumber) {

        try {
            return (Integer.parseInt(rmiPortNumber) > 0);
        } catch (NumberFormatException ignored) { return false; }
    }
}
