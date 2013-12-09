package suncertify.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.swing.*;

import suncertify.util.*;

public class ConfigDialog extends JDialog implements ActionListener {

	// TODO serialVersionUID
	private static final long serialVersionUID = -2130127546941012498L;

	private Logger log = Logger.getLogger("suncertify.ui");

	private JPanel dbPanel;
	private JLabel dbLabel;
	private JTextField dbField;
	private JButton dbButton;

	private JPanel netPanel;
	private JLabel netHostLabel;
	private JLabel netPortLabel;
	private JTextField netHostField;
	private JTextField netPortField;

	private JPanel controlPanel;
	private JButton controlConfirmButton;
	private JButton controlCancelButton;

	private static final String BROWSE_TEXT = "Browse";
	private static final String CONFIRM_TEXT = "OK";
	private static final String CANCEL_TEXT = "Exit";
	private String dbFilePath = null;
	private String netHost = null;
	private String netPort = null;

	private PropertyFileManager properties = PropertyFileManager.getInstance();
	private ApplicationMode runningMode;

	private boolean dbFlag;
	private boolean netHostFlag;
	private boolean netPortFlag;

	public ConfigDialog(ApplicationMode mode) {
		runningMode = mode;

		setTitle("URLyBird 1.0 - Configuration");
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setSize(500, 130);
		setResizable(false);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				getCustomEvent(CANCEL_TEXT);
			}
		});

		switch (runningMode) {
		case STANDALONE_CLIENT:
			dbPanel = buildDBPanel();
			add(dbPanel, BorderLayout.CENTER);
			controlPanel = buildControlPanel();
			add(controlPanel, BorderLayout.SOUTH);
			break;

		case NETWORKED_CLIENT:
			netPanel = buildNetPanel();
			add(netPanel, BorderLayout.CENTER);
			controlPanel = buildControlPanel();
			add(controlPanel, BorderLayout.SOUTH);
			break;

		case SERVER:
			dbPanel = buildDBPanel();
			add(dbPanel, BorderLayout.NORTH);
			netPanel = buildNetPanel();
			add(netPanel, BorderLayout.CENTER);
			controlPanel = buildControlPanel();
			add(controlPanel, BorderLayout.SOUTH);
			break;
		default:
			// TODO Massive problems
		}

		initaliseValues();
	}

	private JPanel buildDBPanel() {
		dbLabel = new JLabel("Location:");

		dbField = new JTextField(30);

		dbButton = new JButton(BROWSE_TEXT + "...");
		dbButton.setActionCommand(BROWSE_TEXT);
		dbButton.addActionListener(this);

		dbPanel = new JPanel();
		dbPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		dbPanel.add(dbLabel);
		dbPanel.add(dbField);
		dbPanel.add(dbButton);

		return dbPanel;
	}

	private JPanel buildNetPanel() {
		netHostLabel = new JLabel("Server Address:");
		netPortLabel = new JLabel("Server Port:");

		netHostField = new JTextField(22);
		netPortField = new JTextField(5);

		netPanel = new JPanel();
		netPanel.add(netHostLabel);
		netPanel.add(netHostField);
		netPanel.add(netPortLabel);
		netPanel.add(netPortField);

		return netPanel;
	}

	private JPanel buildControlPanel() {
		controlConfirmButton = new JButton(CONFIRM_TEXT);
		controlConfirmButton.setActionCommand(CONFIRM_TEXT);
		controlConfirmButton.addActionListener(this);

		controlCancelButton = new JButton(CANCEL_TEXT);
		controlCancelButton.setActionCommand(CANCEL_TEXT);
		controlCancelButton.addActionListener(this);

		controlPanel = new JPanel();
		controlPanel.add(controlConfirmButton);
		controlPanel.add(controlCancelButton);

		return controlPanel;
	}

	private void initaliseValues() {
		dbFlag = false;
		netHostFlag = false;
		netPortFlag = false;

		dbFilePath = properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH);
		netHost = properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_HOST);
		netPort = properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT);

		switch (runningMode) {
		case STANDALONE_CLIENT:
			if (!dbFilePath.equals("")) {
				dbField.setText(dbFilePath);
			}
			break;

		case NETWORKED_CLIENT:
			netHostField.setText(netHost);
			netPortField.setText(netPort);
			break;

		case SERVER:
			netHostField.setEnabled(false);

			if (!dbFilePath.equals("")) {
				dbField.setText(dbFilePath);
			}
			netHostField.setText("localhost");
			netPortField.setText(netPort);
			break;
		default:
			// TODO Massive problems
		}
	}

	public void actionPerformed(ActionEvent event) {
		getCustomEvent(event.getActionCommand());
	}

	private void getCustomEvent(String event) {
		if (CONFIRM_TEXT.equals(event)) {
			verifyValues();
		} else if (BROWSE_TEXT.equals(event)) {
			browseFiles();
		} else {
			properties = null;
			System.exit(0);
		}
	}

	private void browseFiles() {
		JFileChooser fileChooser = new JFileChooser();

		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dbField.setText(fileChooser.getSelectedFile().toString());
		}
	}

	private void verifyValues() {
		switch (runningMode) {
		case STANDALONE_CLIENT:
			dbFlag = verifyDBFilePath();
			netPortFlag = true;
			netHostFlag = true;
			break;
		case NETWORKED_CLIENT:
			dbFlag = true;
			netHostFlag = verifyNetHost();
			netPortFlag = verifyNetPort();
			break;
		case SERVER:
			dbFlag = verifyDBFilePath();
			netHostFlag = verifyNetHost();
			netPortFlag = verifyNetPort();
			break;
		default:
			throw new UnsupportedOperationException(
					"Invalid application startup mode");
		}

		if (dbFlag && netHostFlag && netPortFlag) {
			dispose();
		}

	}

	private boolean verifyDBFilePath() {
		boolean tempFlag = false;

		if (!dbField.getText().equals("")) {
			dbFilePath = dbField.getText();

			File file = new File(dbFilePath);
			if (file.exists() && file.canRead()) {
				tempFlag = true;
				properties.setProperty("dbPath", dbFilePath);
			} else {
				JOptionPane.showMessageDialog(controlPanel,
						"Path entered is invalid");
			}
		} else {
			JOptionPane.showMessageDialog(controlPanel,
					"Please enter a path to the local database");
		}

		return tempFlag;
	}

	private boolean verifyNetHost() {
		boolean tempFlag = false;

		if (!netHostField.getText().equals("")) {
			netHost = netHostField.getText();
			try {
				InetAddress.getByName(netHost);
				tempFlag = true;
				properties
						.setProperty(
								ApplicationConstants.KEY_PROPERTY_NETWORK_HOST,
								netHost);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				tempFlag = false;
				JOptionPane.showMessageDialog(controlPanel,
						"Host name supplied is not a recognised host");
				netHostField.setText("");
			}
		} else {
			JOptionPane.showMessageDialog(controlPanel,
					"Please enter a hostname");
		}

		return tempFlag;
	}

	private boolean verifyNetPort() {
		boolean tempFlag = false;

		if (!netPortField.getText().equals("")) {

			try {
				int port = Integer.parseInt(netPortField.getText());
				if ((port >= 0) && (port < 65536)) {
					tempFlag = true;
					netPort = netPortField.getText();
					properties.setProperty(
							ApplicationConstants.KEY_PROPERTY_NETWORK_PORT,
							netPort);
				} else {
					JOptionPane
							.showMessageDialog(controlPanel,
									"Port not in range, port must be between 0 and 65535");
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				JOptionPane.showMessageDialog(controlPanel,
						"Port number supplied is not a recognised number");
			}
		} else {
			JOptionPane.showMessageDialog(controlPanel,
					"Please enter a port number");
		}

		return tempFlag;
	}
}
