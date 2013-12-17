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

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigDialog.
 */
public class ConfigDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7286902966873123608L;

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.ui");

	/** The db panel. */
	private JPanel dbPanel;

	/** The db label. */
	private JLabel dbLabel;

	/** The db field. */
	private JTextField dbField;

	/** The db button. */
	private JButton dbButton;

	/** The net panel. */
	private JPanel netPanel;

	/** The net host label. */
	private JLabel netHostLabel;

	/** The net port label. */
	private JLabel netPortLabel;

	/** The net host field. */
	private JTextField netHostField;

	/** The net port field. */
	private JTextField netPortField;

	/** The control panel. */
	private JPanel controlPanel;

	/** The control confirm button. */
	private JButton controlConfirmButton;

	/** The control cancel button. */
	private JButton controlCancelButton;

	/** The Constant BROWSE_TEXT. */
	private static final String BROWSE_TEXT = "Browse";

	/** The Constant CONFIRM_TEXT. */
	private static final String CONFIRM_TEXT = "OK";

	/** The Constant CANCEL_TEXT. */
	private static final String CANCEL_TEXT = "Exit";

	/** The db file path. */
	private String dbFilePath = null;

	/** The net host. */
	private String netHost = null;

	/** The net port. */
	private String netPort = null;

	/** The properties. */
	private PropertyFileManager properties = PropertyFileManager.getInstance();

	/** The running mode. */
	private final ApplicationMode runningMode;

	/** The db flag. */
	private boolean dbFlag;

	/** The net host flag. */
	private boolean netHostFlag;

	/** The net port flag. */
	private boolean netPortFlag;

	/**
	 * Instantiates a new config dialog.
	 *
	 * @param mode the mode
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
	 * Builds the db panel.
	 *
	 * @return the j panel
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
	 * Builds the net panel.
	 *
	 * @return the j panel
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
	 * Builds the control panel.
	 *
	 * @return the j panel
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
	 * Initalise values.
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
	 * Gets the custom event.
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
	 * Browse files.
	 */
	private void browseFiles() {
		final JFileChooser fileChooser = new JFileChooser();

		final int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.dbField.setText(fileChooser.getSelectedFile().toString());
		}
	}

	/**
	 * Verify values.
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
	 * Verify db file path.
	 *
	 * @return true, if successful
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
	 * Verify net host.
	 *
	 * @return true, if successful
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
	 * Verify net port.
	 *
	 * @return true, if successful
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
