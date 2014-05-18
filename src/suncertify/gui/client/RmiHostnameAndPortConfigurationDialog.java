package suncertify.gui.client;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import javax.swing.*;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * @author Luke GJ Potter
 *         Date: 18/05/2014
 */
class RmiHostnameAndPortConfigurationDialog extends JFrame {

    private final JPanel rmiHostnameAndPortConfigurationDialogPanel;
    private JLabel rmiHostnameLabel, rmiPortNumberLabel;
    private JTextField rmiHostnameTextField, rmiPortNumberTextField;
    private JButton confirmButton;

    public RmiHostnameAndPortConfigurationDialog () {

        rmiHostnameAndPortConfigurationDialogPanel = new JPanel();
        rmiHostnameAndPortConfigurationDialogPanel.setLayout(new GridBagLayout());

        initialiseRmiHostnameAndPortConfigurationDialogPanelLabelsAndTextFields();
        layoutRmiHostnameAndPortConfigurationDialogPanel();
    }

    public String getRmiHostnameFromTextField() {
        return rmiHostnameTextField.getText().trim();
    }

    public String getRmiPortNumberFromTextField() {
        return rmiPortNumberTextField.getText().trim();
    }

    public boolean areTextFieldValuesValid() {

        String rmiHostname = getRmiHostnameFromTextField();
        String rmiPortNumber = getRmiPortNumberFromTextField();
        String emptyString = UrlyBirdApplicationConstants.EMPTY_STRING;

        boolean emptyFields = rmiHostname.equals(emptyString) || rmiPortNumber.equals(emptyString);

        return (!emptyFields) && isValidHostname(rmiHostname) && isValidPortNumber(rmiPortNumber);
    }

    private void layoutRmiHostnameAndPortConfigurationDialogPanel() {

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        rmiHostnameAndPortConfigurationDialogPanel.add(rmiHostnameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        rmiHostnameAndPortConfigurationDialogPanel.add(rmiHostnameTextField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        rmiHostnameAndPortConfigurationDialogPanel.add(rmiPortNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        rmiHostnameAndPortConfigurationDialogPanel.add(rmiPortNumberTextField, gridBagConstraints);
    }

    private void initialiseRmiHostnameAndPortConfigurationDialogPanelLabelsAndTextFields() {

        rmiHostnameLabel = new JLabel(UrlyBirdApplicationGuiConstants.RMI_HOSTNAME, JLabel.TRAILING);
        rmiHostnameTextField = new JTextField(UrlyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        rmiHostnameTextField.setText(UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_HOSTNAME));
        rmiHostnameLabel.setLabelFor(rmiHostnameTextField);

        rmiPortNumberLabel = new JLabel(UrlyBirdApplicationGuiConstants.RMI_PORT_NUMBER, JLabel.TRAILING);
        rmiPortNumberTextField = new JTextField(UrlyBirdApplicationGuiConstants.SERVER_CONFIG_TEXT_FIELD_LENGTH);
        rmiPortNumberTextField.setText(UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER));
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
