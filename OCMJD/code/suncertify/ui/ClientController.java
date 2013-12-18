/*
 * ClientController
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.DBAccess;
import suncertify.db.RecordNotFoundException;
import suncertify.remote.RemoteDBAccess;
import suncertify.util.*;

/**
 * This class is the controller in the client model-view-controller pattern.
 * Using the <code>DataConnector</code> it can connect to either a local
 * datafile or a remote datafile. It also has references to both the
 * <code>ClientModel</code> and <code>ClientWindow</code> instances, but neither
 * of these instances have direct references to each other or this controller
 * 
 * @suthor Eoin Mooney
 */
public class ClientController {

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.ui</code>
	 */
	private final Logger log = Logger.getLogger("suncertify.ui");

	/** The mode the application is currently running in. */
	private final ApplicationMode applicationMode;

	/** The reference to a local datafile. */
	private DBAccess database;

	/** The reference to a remote datafile. */
	private RemoteDBAccess remoteDatabase;

	/** The model for the client MVC. */
	private ClientModel model;

	/** The view for the client MVC. */
	private ClientWindow clientUI;

	/**
	 * The search listener, attached to the search button in the
	 * <code>ClientWindow</code> instance.
	 */
	private ActionListener searchListener;

	/**
	 * The booking listener, attached to the book button in the
	 * <code>ClientWindow</code> instance.
	 */
	private ActionListener bookingListener;

	/** The criteria for last search executed. */
	private String lastSearch;

	/**
	 * The <code>ConfigDialog</code> used to store users configuration
	 * parameters in the <code>PropertyManager</code>.
	 */
	private final ConfigDialog config;

	/**
	 * The <code>PropertyManager</code> instance used to get and set property
	 * values
	 */
	private final PropertyManager properties = PropertyManager.getInstance();

	/**
	 * Instantiates a new client controller.
	 * 
	 * @param applicationMode
	 *            The mode the application is currently running in
	 */
	public ClientController(final ApplicationMode applicationMode) {
		this.applicationMode = applicationMode;

		// Start ClientWindow so we can pop-up a ConfigDialog
		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			this.clientUI = new ClientWindow("URLyBird 1.0 - Standalone Mode");
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			this.clientUI = new ClientWindow("URLyBird 1.0 - Network Mode");
		} else {
			this.log.severe("Client started with incorrect Application Mode. Exiting application");
			this.clientUI
					.showError(
							"Client started with incorrect Application Mode. Exiting application",
							"Error", Level.SEVERE);

			System.exit(0);
		}

		// Pop-up the ConfigDialog
		this.config = new ConfigDialog(applicationMode);
		this.config.setVisible(true);

		// Connect to datafile based on applicationMode and properties set in
		// the ConfigDialog
		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			this.database = DataConnector.getLocal(this.properties
					.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH));
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			try {
				this.remoteDatabase = DataConnector
						.getRemote(
								this.properties
										.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_HOST),
								this.properties
										.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));
			} catch (final RemoteException re) {
				this.log.log(Level.SEVERE, re.getMessage(), re);

				this.clientUI
						.showError(
								"Remote Exception encountered when starting networked client.",
								"Error", Level.SEVERE);

				System.err
						.println("Remote Exception encountered when starting networked client "
								+ re.getMessage());
				re.printStackTrace();
			}
		} else {
			this.log.severe("Client started with incorrect Application Mode. Exiting application");
			this.clientUI
					.showError(
							"Client started with incorrect Application Mode. Exiting application",
							"Error", Level.SEVERE);

			System.exit(0);
		}

		// Perform an empty search to retrieve all records, start the ClientView
		this.model = this.getRecords("");
		this.clientUI.startClientView(this.model);
	}

	/**
	 * This method is called by the <code>Runner</code> after instantiating a
	 * <code>ClientController</code>. It creates <code>ActionListener</code>
	 * instances for searching and booking and adds them to the relevant
	 * <code>JButtons</code> in the <code>ClientWindow</code>
	 */
	public void control() {
		this.searchListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent actionEvent) {
				ClientController.this.lastSearch = ClientController.this
						.getSearchText();
				ClientController.this.clientUI
						.updateTable(ClientController.this
								.getRecords(ClientController.this.lastSearch));
			}
		};
		this.bookingListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent actionEvent) {
				final long selectedRecNo = ClientController.this
						.getReserveRecNo();

				if (selectedRecNo != -1) {
					final String owner = ClientController.this.getCustomerID();

					if (owner != null) {
						ClientController.this.bookRoom(selectedRecNo, owner);
						ClientController.this.clientUI
								.updateTable(ClientController.this
										.getRecords(ClientController.this.lastSearch));
					}
				}
			}
		};

		this.clientUI.getSearchButton().addActionListener(this.searchListener);
		this.clientUI.getBookingButton()
				.addActionListener(this.bookingListener);
	}

	/**
	 * Gets the search text.
	 * 
	 * @return the text in the search field
	 */
	private String getSearchText() {
		return this.clientUI.getSearchField();
	}

	/**
	 * Gets the record number from the row selected for booking
	 * 
	 * @return The record number for the selected row
	 */
	private long getReserveRecNo() {
		final int selectedRow = this.clientUI.getSelectedRowNo();
		return this.model.getRecNo(selectedRow);
	}

	/**
	 * Calls a method in the <code>ClientWindow</code> that prompts the user for
	 * a customer ID
	 * 
	 * @return The validated 8 digit customer ID
	 */
	protected String getCustomerID() {
		return this.clientUI.getCustomerID();
	}

	/**
	 * This method attempts to lock the record, update the record with the
	 * customer ID and unlock the record
	 * 
	 * @param recNo
	 *            The record number of the record the user wishes to book
	 * @param owner
	 *            The customer ID the user wishes to book the record for
	 */
	private void bookRoom(final long recNo, final String owner) {
		if (this.applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			try {
				final long lock = this.database.lockRecord(recNo);
				final String[] record = this.database.readRecord(recNo);
				record[6] = owner;
				this.database.updateRecord(recNo, record, lock);
				this.database.unlock(recNo, lock);
			} catch (final RecordNotFoundException rnfe) {
				this.log.log(Level.WARNING, rnfe.getMessage(), rnfe);

				this.clientUI.showError(
						"Cannot book record: " + rnfe.getMessage(), "Warning",
						Level.WARNING);

				System.err.println("Cannot book record: " + rnfe.getMessage());
				rnfe.printStackTrace();
			}
		} else if (this.applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			try {
				final long lock = this.remoteDatabase.lockRecord(recNo);
				final String[] record = this.remoteDatabase.readRecord(recNo);
				record[6] = owner;
				this.remoteDatabase.updateRecord(recNo, record, lock);
				this.remoteDatabase.unlock(recNo, lock);
			} catch (final RemoteException re) {
				this.log.log(Level.SEVERE, re.getMessage(), re);

				this.clientUI.showError(
						"Remote Exception encountered when booking a room: "
								+ re.getMessage() + re.getMessage(), "Error",
						Level.SEVERE);

				System.err
						.println("Remote Exception encountered when booking a room "
								+ re.getMessage());
				re.printStackTrace();
			} catch (final RecordNotFoundException rnfe) {
				this.log.log(Level.WARNING, rnfe.getMessage(), rnfe);

				this.clientUI.showError(
						"Cannot book record: " + rnfe.getMessage(), "Warning",
						Level.WARNING);

				System.err.println("Cannot book record: " + rnfe.getMessage());
				rnfe.printStackTrace();
			}
		} else {
			this.log.severe("Client controller started with incorrect Application Mode. Exiting application");
			this.clientUI
					.showError(
							"Client controller started with incorrect Application Mode. Exiting application",
							"Error", Level.SEVERE);

			System.exit(0);
		}
	}

	/**
	 * This method searches both "name" and "location" fields and returns all
	 * records that match the search text. If search text is empty, this method
	 * will return all valid records
	 * 
	 * @param searchText
	 *            The text entered in the search box
	 * @return A <code>ClientModel</code> populated with the matched records
	 */
	private ClientModel getRecords(final String searchText) {
		this.model = new ClientModel();
		long[] recNoArray = null;

		if (this.applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			if (searchText == null || searchText.equals("")) {
				recNoArray = this.database.findByCriteria(null);
			} else {
				final String[] search = new String[this.model.getColumnCount()];
				search[this.model.findColumn("Name")] = searchText;
				search[this.model.findColumn("Location")] = searchText;
				recNoArray = this.database.findByCriteria(search);
			}

			for (final long recNo : recNoArray) {
				try {
					this.model
							.addRecord(this.database.readRecord(recNo), recNo);
				} catch (final RecordNotFoundException rnfe) {
					this.log.log(Level.WARNING, rnfe.getMessage(), rnfe);

					this.clientUI.showError(
							"Cannot retrieve record: " + rnfe.getMessage(),
							"Warning", Level.WARNING);

					System.err.println("Cannot retrieve record: "
							+ rnfe.getMessage());
					rnfe.printStackTrace();
				}
			}
		} else if (this.applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			if (searchText == null || searchText.equals("")) {
				try {
					recNoArray = this.remoteDatabase.findByCriteria(null);
				} catch (final RemoteException re) {
					this.log.log(Level.SEVERE, re.getMessage(), re);

					this.clientUI.showError(
							"Remote Exception encountered when retrieving search results"
									+ re.getMessage(), "Error", Level.SEVERE);

					System.err
							.println("Remote Exception encountered when retrieving search results"
									+ re.getMessage());
					re.printStackTrace();
				}
			} else {
				final String[] search = new String[this.model.getColumnCount()];
				search[this.model.findColumn("Name")] = searchText;
				search[this.model.findColumn("Location")] = searchText;
				try {
					recNoArray = this.remoteDatabase.findByCriteria(search);
				} catch (final RemoteException re) {
					this.log.log(Level.SEVERE, re.getMessage(), re);

					this.clientUI.showError(
							"Remote Exception encountered when retrieving search results"
									+ re.getMessage(), "Error", Level.SEVERE);

					System.err
							.println("Remote Exception encountered when retrieving search results"
									+ re.getMessage());
					re.printStackTrace();
				}
			}

			for (final long recNo : recNoArray) {
				try {
					this.model.addRecord(this.remoteDatabase.readRecord(recNo),
							recNo);
				} catch (final RemoteException re) {
					this.log.log(Level.SEVERE, re.getMessage(), re);

					this.clientUI.showError(
							"Remote Exception encountered when retrieving search results"
									+ re.getMessage(), "Error", Level.SEVERE);

					System.err
							.println("Remote Exception encountered when retrieving search results"
									+ re.getMessage());
					re.printStackTrace();
				} catch (final RecordNotFoundException rnfe) {
					this.log.log(Level.WARNING, rnfe.getMessage(), rnfe);

					this.clientUI.showError(
							"Cannot retrieve record: " + rnfe.getMessage(),
							"Warning", Level.WARNING);

					System.err.println("Cannot retrieve record: "
							+ rnfe.getMessage());
					rnfe.printStackTrace();
				}
			}
		} else {
			this.log.severe("Client controller started with incorrect Application Mode. Exiting application");
			this.clientUI
					.showError(
							"Client controller started with incorrect Application Mode. Exiting application",
							"Error", Level.SEVERE);

			System.exit(0);
		}

		return this.model;
	}
}
