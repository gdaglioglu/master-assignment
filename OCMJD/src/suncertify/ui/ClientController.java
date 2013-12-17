/*
 * 
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

// TODO: Auto-generated Javadoc
/**
 * The Class ClientController.
 */
public class ClientController {

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.ui");

	/** The application mode. */
	private final ApplicationMode applicationMode;

	/** The database. */
	private DBAccess database;

	/** The remote database. */
	private RemoteDBAccess remoteDatabase;

	/** The model. */
	private ClientModel model;

	/** The client ui. */
	private ClientWindow clientUI;

	/** The booking listener. */
	private ActionListener searchListener, bookingListener;

	/** The last search. */
	private String lastSearch;

	/** The dialog. */
	private final ConfigDialog dialog;

	/** The properties. */
	private final PropertyFileManager properties = PropertyFileManager
			.getInstance();

	/**
	 * Instantiates a new client controller.
	 *
	 * @param applicationMode the application mode
	 */
	public ClientController(final ApplicationMode applicationMode) {
		this.applicationMode = applicationMode;

		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			this.clientUI = new ClientWindow("URLyBird 1.0 - Standalone Mode");
		} else if (applicationMode == ApplicationMode.NETWORKED_CLIENT) {
			this.clientUI = new ClientWindow("URLyBird 1.0 - Network Mode");
		} else {
			this.log.severe("Client started with incorrect Application Mode. Exiting application");
			System.exit(0);
		}

		this.dialog = new ConfigDialog(applicationMode);
		this.dialog.setVisible(true);

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
				System.err
						.println("Remote Exception encountered when starting networked client "
								+ re.getMessage());
				re.printStackTrace();
			}
		} else {
			this.log.severe("Client started with incorrect Application Mode. Exiting application");
			System.exit(0);
		}

		this.model = this.getRecords("");
		this.clientUI.startClientView(this.model);
	}

	/**
	 * Control.
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
	 * @return the search text
	 */
	private String getSearchText() {
		return this.clientUI.getSearchField();
	}

	/**
	 * Gets the reserve rec no.
	 *
	 * @return the reserve rec no
	 */
	private long getReserveRecNo() {
		final int selectedRow = this.clientUI.getSelectedRowNo();
		return this.model.getRecNo(selectedRow);
	}

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	protected String getCustomerID() {
		return this.clientUI.getCustomerID();
	}

	/**
	 * Book room.
	 *
	 * @param recNo the rec no
	 * @param owner the owner
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
				System.err
						.println("Remote Exception encountered when booking a room "
								+ re.getMessage());
				re.printStackTrace();
			} catch (final RecordNotFoundException rnfe) {
				this.log.log(Level.WARNING, rnfe.getMessage(), rnfe);
				System.err.println("Cannot book record: " + rnfe.getMessage());
				rnfe.printStackTrace();
			}
		} else {
			this.log.severe("Client controller started with incorrect Application Mode. Exiting application");
			System.exit(0);
		}
	}

	/**
	 * Gets the records.
	 *
	 * @param searchText the search text
	 * @return the records
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
					System.err
							.println("Remote Exception encountered when retrieving search results"
									+ re.getMessage());
					re.printStackTrace();
				} catch (final RecordNotFoundException rnfe) {
					this.log.log(Level.WARNING, rnfe.getMessage(), rnfe);
					System.err.println("Cannot retrieve record: "
							+ rnfe.getMessage());
					rnfe.printStackTrace();
				}
			}
		} else {
			this.log.severe("Client controller started with incorrect Application Mode. Exiting application");
			System.exit(0);
		}

		return this.model;
	}
}
