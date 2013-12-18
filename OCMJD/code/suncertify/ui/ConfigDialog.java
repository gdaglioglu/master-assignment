/*
 * ConfigDialog
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import suncertify.util.*;

/**
 * This class is used to display the configuration parameters to the user and store their selected values using the <code>PropertyManager</code> instance.
 * 
 * @author Eoin Mooney
 */
public class ConfigDialog extends JDialog implements ActionListener {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = -7286902966873123608L;

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.ui</code>
	 */
	private final Logger log = Logger.getLogger("suncertify.ui");

	/** The <code>JPanel</code> for datafile location. */
	private JPanel dbPanel;

	/** The <code>JLabel</code> used in dbPanel. */
	private JLabel dbLabel;

	/** The <code>JTextField</code> where the user enters a datafile location. */
	private JTextField dbField;

	/** The <code>JButton</code> that can be used to open a file browser. */
	private JButton dbButton;

	/** The <code>JPanel</code> for network information. */
	private JPanel netPanel;

	/** The <code>JLabel</code> used to identify the host text field. */
	private JLabel netHostLabel;

	/** The <code>JLabel</code> used to identify the port text field. */
	private JLabel netPortLabel;

	/** The <code>JTextField</code> where the user enters a host. */
	private JTextField netHostField;

	/** The <code>JTextField</code> where the user enters a port. */
	private JTextField netPortField;

	/** The <code>JPanel</code> for confirmation/cancellation buttons. */
	private JPanel controlPanel;

	/** The <code>JButton</code> used to confirm the parameters. */
	private JButton controlConfirmButton;

	/** The <code>JButton</code> used to cancel and exit the application. */
	private JButton controlCancelButton;

	/** The Constant BROWSE_TEXT for the text shown on dbButton. */
	private static final String BROWSE_TEXT = "Browse";

	/** The Constant CONFIRM_TEXT for the text shown on controlConfirmButton. */
	private static final String CONFIRM_TEXT = "OK";

	/** The Constant CANCEL_TEXT for the text shown on controlCancelButton. */
	private static final String CANCEL_TEXT = "Exit";

	/** The db file path. */
	private String dbFilePath = null;

	/** The network host. */
	private String netHost = null;

	/** The network port. */
	private String netPort = null;

	/** The PropertyManager instance */
	private PropertyManager properties = PropertyManager.getInstance();

	/** The mode the application is running in. */
	private final ApplicationMode runningMode;

	/** The db flag. */
	private boolean dbFlag;

	/** The network host flag. */
	private boolean netHostFlag;

	/** The network port flag. */
	private boolean netPortFlag;

	/**
	 * Instantiates a new config dialog and calls methods to build the relevant <cod>JPanel</code>s based on the application mode
	 *
	 * @param mode The mode the application is running in
	 */
	public ConfigDialog(final ApplicationMode mode) {
		this.runningMode = mode;

		this.setTitle("URLyBird 1.0 - Configuration");
		this.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setSize(500, 130);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				ConfigDialog.this.getCustomEvent(ConfigDialog.CANCEL_TEXT);
			}
		});

		switch (this.runningMode) {
		case STANDALONE_CLIENT:
			this.dbPanel = this.buildDBPanel();
			this.add(this.dbPanel, BorderLayout.CENTER);
			this.controlPanel = this.buildControlPanel();
			this.add(this.controlPanel, BorderLayout.SOUTH);
			break;

		case NETWORKED_CLIENT:
			this.netPanel = this.buildNetPanel();
			this.add(this.netPanel, BorderLayout.CENTER);
			this.controlPanel = this.buildControlPanel();
			this.add(this.controlPanel, BorderLayout.SOUTH);
			break;

		case SERVER:
			this.dbPanel = this.buildDBPanel();
			this.add(this.dbPanel, BorderLayout.NORTH);
			this.netPanel = this.buildNetPanel();
			this.add(this.netPanel, BorderLayout.CENTER);
			this.controlPanel = this.buildControlPanel();
			this.add(this.controlPanel, BorderLayout.SOUTH);
			break;

		default:
			this.log.severe("Config dialog started with incorrect Application Mode. Exiting application");
			System.exit(0);
		}

		this.initaliseValues();
	}

	/**
	 * Builds the <code>JPanel</code> for datafile location parameters.
	 *
	 * @return the <code>JPanel</code> for datafile location
	 */
	private JPanel buildDBPanel() {
		this.dbLabel = new JLabel("Location:");

		this.dbField = new JTextField(30);

		this.dbButton = new JButton(ConfigDialog.BROWSE_TEXT + "...");
		this.dbButton.setActionCommand(ConfigDialog.BROWSE_TEXT);
		this.dbButton.addActionListener(this);

		this.dbPanel = new JPanel();
		this.dbPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.dbPanel.add(this.dbLabel);
		this.dbPanel.add(this.dbField);
		this.dbPanel.add(this.dbButton);

		return this.dbPanel;
	}

	/**
	 * Builds the <code>JPanel</code> for network parameters.
	 *
	 * @return the <code>JPanel</code> for network parameters
	 */
	private JPanel buildNetPanel() {
		this.netHostLabel = new JLabel("Server Address:");
		this.netPortLabel = new JLabel("Server Port:");

		this.netHostField = new JTextField(22);
		this.netPortField = new JTextField(5);

		this.netPanel = new JPanel();
		this.netPanel.add(this.netHostLabel);
		this.netPanel.add(this.netHostField);
		this.netPanel.add(this.netPortLabel);
		this.netPanel.add(this.netPortField);

		return this.netPanel;
	}

	/**
	 * Builds the <code>JPanel</code> for confirm/canel buttons.
	 *
	 * @return the <code>JPanel</code> for confirm/canel buttons
	 */
	private JPanel buildControlPanel() {
		this.controlConfirmButton = new JButton(ConfigDialog.CONFIRM_TEXT);
		this.controlConfirmButton.setActionCommand(ConfigDialog.CONFIRM_TEXT);
		this.controlConfirmButton.addActionListener(this);

		this.controlCancelButton = new JButton(ConfigDialog.CANCEL_TEXT);
		this.controlCancelButton.setActionCommand(ConfigDialog.CANCEL_TEXT);
		this.controlCancelButton.addActionListener(this);

		this.controlPanel = new JPanel();
		this.controlPanel.add(this.controlConfirmButton);
		this.controlPanel.add(this.controlCancelButton);

		return this.controlPanel;
	}

	/**
	 * Initalise values to values from properties file, if set
	 */
	private void initaliseValues() {
		this.dbFlag = false;
		this.netHostFlag = false;
		this.netPortFlag = false;

		this.dbFilePath = this.properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH);
		this.netHost = this.properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_HOST);
		this.netPort = this.properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT);

		switch (this.runningMode) {
		case STANDALONE_CLIENT:
			if (!this.dbFilePath.equals("")) {
				this.dbField.setText(this.dbFilePath);
			}
			break;

		case NETWORKED_CLIENT:
			this.netHostField.setText(this.netHost);
			this.netPortField.setText(this.netPort);
			break;

		case SERVER:
			this.netHostField.setEnabled(false);

			if (!this.dbFilePath.equals("")) {
				this.dbField.setText(this.dbFilePath);
			}
			this.netHostField.setText("localhost");
			this.netPortField.setText(this.netPort);
			break;
		default:
			this.log.severe("Config dialog started with incorrect Application Mode. Exiting application");
			System.exit(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		this.getCustomEvent(event.getActionCommand());
	}

	/**
	 * Gets the custom event that triggers data validation, launches a file browser or exits the application
	 *
	 * @param event the event
	 * @return the custom event
	 */
	private void getCustomEvent(final String event) {
		if (ConfigDialog.CONFIRM_TEXT.equals(event)) {
			this.verifyValues();
		} else if (ConfigDialog.BROWSE_TEXT.equals(event)) {
			this.browseFiles();
		} else {
			this.properties = null;
			System.exit(0);
		}
	}

	/**
	 * Launch a file browser and bring the selected location back to <code>ConfigDialog</code>
	 */
	private void browseFiles() {
		final JFileChooser fileChooser = new JFileChooser();

		final int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.dbField.setText(fileChooser.getSelectedFile().toString());
		}
	}

	/**
	 * Verify that the values make sense
	 */
	private void verifyValues() {
		switch (this.runningMode) {
		case STANDALONE_CLIENT:
			this.dbFlag = this.verifyDBFilePath();
			this.netPortFlag = true;
			this.netHostFlag = true;
			break;
		case NETWORKED_CLIENT:
			this.dbFlag = true;
			this.netHostFlag = this.verifyNetHost();
			this.netPortFlag = this.verifyNetPort();
			break;
		case SERVER:
			this.dbFlag = this.verifyDBFilePath();
			this.netHostFlag = this.verifyNetHost();
			this.netPortFlag = this.verifyNetPort();
			break;
		default:
			throw new UnsupportedOperationException(
					"Invalid application startup mode");
		}

		if (this.dbFlag && this.netHostFlag && this.netPortFlag) {
			this.dispose();
		}

	}

	/**
	 * Verify datafile location.
	 *
	 * @return true, if file exists and can be read
	 */
	private boolean verifyDBFilePath() {
		boolean tempFlag = false;

		if (!this.dbField.getText().equals("")) {
			this.dbFilePath = this.dbField.getText();

			final File file = new File(this.dbFilePath);
			if (file.exists() && file.canRead()) {
				tempFlag = true;
				this.properties.setProperty("dbPath", this.dbFilePath);
			} else {
				JOptionPane.showMessageDialog(this.controlPanel,
						"Path entered is invalid");
			}
		} else {
			JOptionPane.showMessageDialog(this.controlPanel,
					"Please enter a path to the local database");
		}

		return tempFlag;
	}

	/**
	 * Verify network host.
	 *
	 * @return true, if network host is a valid network address
	 */
	private boolean verifyNetHost() {
		boolean tempFlag = false;

		if (!this.netHostField.getText().equals("")) {
			this.netHost = this.netHostField.getText();
			try {
				InetAddress.getByName(this.netHost);
				tempFlag = true;
				this.properties.setProperty(
						ApplicationConstants.KEY_PROPERTY_NETWORK_HOST,
						this.netHost);
			} catch (final UnknownHostException uhe) {
				this.log.log(Level.INFO, uhe.getMessage(), uhe);
				System.err.println("Unknown Host: " + uhe.getMessage());
				uhe.printStackTrace();

				tempFlag = false;
				JOptionPane.showMessageDialog(this.controlPanel,
						"Host name supplied is not a recognised host");
				this.netHostField.setText("");
			}
		} else {
			JOptionPane.showMessageDialog(this.controlPanel,
					"Please enter a hostname");
		}

		return tempFlag;
	}

	/**
	 * Verify network port.
	 *
	 * @return true, if network port is a number between 0 - 65535
	 */
	private boolean verifyNetPort() {
		boolean tempFlag = false;

		if (!this.netPortField.getText().equals("")) {

			try {
				final int port = Integer.parseInt(this.netPortField.getText());
				if (port >= 0 && port < 65536) {
					tempFlag = true;
					this.netPort = this.netPortField.getText();
					this.properties.setProperty(
							ApplicationConstants.KEY_PROPERTY_NETWORK_PORT,
							this.netPort);
				} else {
					JOptionPane
							.showMessageDialog(this.controlPanel,
									"Port not in range, port must be between 0 and 65535");
				}
			} catch (final NumberFormatException nfe) {
				this.log.log(Level.INFO, nfe.getMessage(), nfe);
				System.err.println("Not a number: " + nfe.getMessage());
				nfe.printStackTrace();

				JOptionPane.showMessageDialog(this.controlPanel,
						"Port number supplied is not a recognised number");
			}
		} else {
			JOptionPane.showMessageDialog(this.controlPanel,
					"Please enter a port number");
		}

		return tempFlag;
	}
}
