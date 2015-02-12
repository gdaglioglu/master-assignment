package suncertify.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import suncertify.app.App;
import suncertify.app.ApplicationRunner;
import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.HotelRoom;
import suncertify.remote.DBRemote;

/**
 * Controller for the {@link HotelRoomView} GUI class. Handles all interactions
 * between the GUI layer and the data layer.
 *
 * @author Gokhan Daglioglu
 * @see DBMain
 * @see DBRemote
 * @see GuiControllerException
 */

public class HotelRoomController {

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.client.ui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.client.ui");

	/**
	 * The model for the client MVC.
	 */
	private HotelRoomTableModel hotelRoomModel;

	/**
	 * The view for the client MVC.
	 */
	private HotelRoomView hotelRoomView;

	/**
	 * The mode the application is currently running in.
	 */
	private final ApplicationMode applicationMode;

	/**
	 * The reference to a local datafile.
	 */
	private DBMain localConnection;

	/**
	 * The reference to a remote datafile.
	 */
	private DBRemote remoteConnection;

	/**
	 * The search listener, attached to the search button in the
	 * {@link SearchPanel} instance.
	 */
	private ActionListener searchListener;

	/**
	 * The booking listener, attached to the book button in the
	 * {@link HotelRoomsPanel} instance.
	 */
	private ActionListener bookingListener;

	/**
	 * This exact match listener, attached to the exact match check box in the
	 * {@link SearchPanel} instance.
	 */
	private ActionListener exactMatchListener;

	/**
	 * The criteria for last search executed.
	 */
	private String[] lastSearchCriteria;

	/**
	 * Creates a <code>HotelRoomController</code> instance with a specified
	 * connection type.
	 *
	 * @param applicationMode
	 *            The mode the application is currently running in
	 */
	public HotelRoomController(ApplicationMode applicationMode) {
		this.applicationMode = applicationMode;

		// find out where our database is
		DatabaseLocationDialog dbLocationDialog = new DatabaseLocationDialog(
				applicationMode);

		if (dbLocationDialog.userCanceled()) {
			System.exit(0);
		}

		try {
			switch (dbLocationDialog.getNetworkType()) {
			case DIRECT:
				localConnection = suncertify.direct.HotelConnector
						.getLocal(dbLocationDialog.getLocation());
				break;
			case RMI:
				remoteConnection = suncertify.remote.HotelConnector.getRemote(
						dbLocationDialog.getLocation(),
						dbLocationDialog.getPort());
				break;
			default:
				throw new IllegalArgumentException(
						"Invalid connection type specified");
			}
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			// throw new GuiControllerException(e);
		} catch (java.rmi.RemoteException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			// throw new GuiControllerException(e);
		} catch (java.io.IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			// throw new GuiControllerException(e);
		}

		// Start ClientWindow so we can pop-up a ConfigDialog
		if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
			this.hotelRoomView = new HotelRoomView(
					"URLyBird Discounted Hotel Rooms - Standalone Mode");
		} else if (applicationMode == ApplicationMode.NETWORK_CLIENT) {
			this.hotelRoomView = new HotelRoomView(
					"URLyBird Discounted Hotel Rooms - Network Mode");
		} else {
			this.logger
					.severe("Client started with incorrect Application Mode. Exiting application");
			App.handleException("Client started with incorrect Application Mode. Exiting application");

		}

		try {
			this.hotelRoomModel = this.getAllHotelRooms();
		} catch (GuiControllerException e) {
			App.handleException("Failed to acquire an initial hotel room list."
					+ "\nPlease check the DB connection.");
		}
		this.hotelRoomView.updateTable(this.hotelRoomModel);
	}

	private HotelRoomTableModel getAllHotelRooms()
			throws GuiControllerException {
		lastSearchCriteria = getSearchCriteria();
		final boolean exactMatch = Boolean.valueOf(PropertyManager
				.getParameter(PropertyManager.EXACT_MATCH));
		return this.getHotelRoomsByCriteria(lastSearchCriteria, exactMatch);
	}

	private String[] getSearchCriteria() {
		return hotelRoomView.getSearchCriteria();
	}

	/**
	 * Retrieves a specific record or records depending on criteria passed into
	 * the method.
	 * 
	 * @param exactMatch
	 *            The <code>boolean</code> representing the state of exact match
	 *            check box.
	 * @param hotelName
	 *            The <code>String</code> representing the hotel name.
	 * @return A {@link HotelRoomModel} containing all found hotel room records.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	private HotelRoomTableModel getHotelRoomsByCriteria(
			String[] searchCriteria, boolean exactMatch)
			throws GuiControllerException {
		HotelRoomTableModel hotelRoomModel = new HotelRoomTableModel();
		int[] rawResults;
		try {
			if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
				rawResults = this.localConnection.find(searchCriteria);

				for (final int recNo : rawResults) {
					String[] hotelRoom = this.localConnection.read(recNo);
					if (!exactMatch
							|| this.isExactMatch(hotelRoom, searchCriteria)) {
						hotelRoomModel.addHotelRoomRecord(hotelRoom);
					}

				}
			} else {
				rawResults = this.remoteConnection.find(searchCriteria);
				for (final int recNo : rawResults) {
					String[] hotelRoom = this.remoteConnection.read(recNo);
					if (!exactMatch
							|| this.isExactMatch(hotelRoom, searchCriteria)) {
						hotelRoomModel.addHotelRoomRecord(hotelRoom);
					}
				}
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new GuiControllerException(e);
		}
		return hotelRoomModel;
	}

	/**
	 * This method is used to determine if a hotel room exactly matches search
	 * criteria.
	 * 
	 * @param hotelRoom
	 *            The hotel room to check.
	 * @param searchCriteria
	 *            The search criteria used to check the {@link HotelRoom}.
	 * @return true if the hotel room is an exact match otherwise false.
	 */
	private boolean isExactMatch(final String[] hotelRoom,
			final String[] searchCriteria) {
		for (int i = 0; i < searchCriteria.length; i++) {
			if ((searchCriteria[i] != null) && !searchCriteria[i].equals("")
					&& !hotelRoom[i].equals(searchCriteria[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method is called by the {@link ApplicationRunner}after instantiating
	 * a <code>HotelRoomController</code>. It creates
	 * <code>ActionListener</code> instances for searching and booking and adds
	 * them to the relevant <code>JButtons</code> in the
	 * <code>ClientWindow</code>
	 */
	public void control() {

		this.searchListener = new ActionListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HotelRoomController.this.lastSearchCriteria = HotelRoomController.this
							.getSearchCriteria();
					final boolean exactMatch = Boolean.valueOf(PropertyManager
							.getParameter(PropertyManager.EXACT_MATCH));
					hotelRoomView
							.updateTable(HotelRoomController.this
									.getHotelRoomsByCriteria(
											HotelRoomController.this.lastSearchCriteria,
											exactMatch));
				} catch (GuiControllerException gce) {
					// Inspect the exception chain
					Throwable rootException = gce.getCause();
					String msg = "Search operation failed.";
					// If a syntax error occurred, get the message
					if (rootException instanceof PatternSyntaxException) {
						msg += ("\n" + rootException.getMessage());
					}
					App.handleException(msg);
				}

			}

		};
		this.exactMatchListener = new ActionListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				PropertyManager.setParameter(PropertyManager.EXACT_MATCH,
						HotelRoomController.this.hotelRoomView
								.isExactMatchSelected());
			}
		};

		this.bookingListener = new ActionListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				final String ALREADY_BOOKED_MSG = "Unable to book - room is already booked.";

				JTable mainTable = hotelRoomView.getMainTable();
				int hotelRoomRecordNo = mainTable.getSelectedRow();
				if ((hotelRoomRecordNo >= 0)) {
					final String customerId = JOptionPane.showInputDialog(null,
							"Enter Customer ID", "Book Room",
							JOptionPane.QUESTION_MESSAGE);
					if (isValidCustomerId(customerId)) {
						try {
							boolean booked = HotelRoomController.this.bookRoom(
									customerId, hotelRoomRecordNo);
							if (booked == false) {
								App.handleException(ALREADY_BOOKED_MSG);
							}
							final boolean exactMatch = Boolean
									.valueOf(PropertyManager
											.getParameter(PropertyManager.EXACT_MATCH));
							hotelRoomView
									.updateTable(HotelRoomController.this
											.getHotelRoomsByCriteria(
													HotelRoomController.this.lastSearchCriteria,
													exactMatch));
						} catch (GuiControllerException gce) {
							App.handleException("Booking operation failed.");
						}
					}
				} else {
					App.showWarning("Please select a row");
					JOptionPane.showMessageDialog(
							HotelRoomController.this.hotelRoomView,
							"Please select a row");
				}
			}

			private boolean isValidCustomerId(String id) {
				if (id == null) {
					return false;
				} else if (!id.matches("^\\d{8}$")) {
					JOptionPane.showMessageDialog(
							HotelRoomController.this.hotelRoomView,
							"Please enter a valid Customer Id");
					return false;
				} else
					return true;
			}

		};

		this.hotelRoomView.getSearchButton().addActionListener(
				this.searchListener);
		this.hotelRoomView.getBookButton().addActionListener(
				this.bookingListener);
		this.hotelRoomView.getExactMatch().addActionListener(
				this.exactMatchListener);
	}

	/**
	 * This method attempts to lock the record, update the record with the
	 * customer ID and unlock the record
	 * 
	 * @param customerId
	 *            The customer ID the user wishes to book the record for
	 * @param hotelRoomRecordNo
	 *            The record number of the record the user wishes to book
	 * @return A <code>boolean</code> indicating if the rent operation was
	 *         successful.
	 */
	private boolean bookRoom(String customerId, int hotelRoomRecordNo)
			throws GuiControllerException {

		logger.entering("HotelRoomController", "bookRoom",
				"Record number to be booked: " + hotelRoomRecordNo);
		String[] hotelRoom = null;
		try {
			if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
				hotelRoom = this.localConnection.read(hotelRoomRecordNo);
			} else {
				hotelRoom = this.remoteConnection.read(hotelRoomRecordNo);
			}
			if (hotelRoom[6] != null && hotelRoom[6].trim().length() > 0) {
				return false;
			}

		} catch (RecordNotFoundException rnfex) {
			logger.log(Level.SEVERE, rnfex.getMessage(), rnfex);
			System.err.println("Error reading record at position : "
					+ rnfex.getMessage());
		} catch (Exception ex) {
			if (ex instanceof RemoteException) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
				System.err.println("Problem with remote connection : "
						+ ex.getMessage());
			}
		}
		HotelRoom room = new HotelRoom(hotelRoom);
		room.setOwner(customerId);
		try {
			if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
				this.localConnection.lock(hotelRoomRecordNo);
				this.localConnection.update(hotelRoomRecordNo, room.toArray());
			} else {
				this.remoteConnection.lock(hotelRoomRecordNo);
				this.remoteConnection.update(hotelRoomRecordNo, room.toArray());
			}
		} catch (RecordNotFoundException rnfex) {
			logger.log(Level.SEVERE, rnfex.getMessage(), rnfex);
			System.err.println("Error attempting to update : "
					+ rnfex.getMessage());
		} catch (Exception ex) {
			if (ex instanceof RemoteException) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
				System.err.println("Problem with remote connection : "
						+ ex.getMessage());
			}

		} finally {
			try {
				this.localConnection.unlock(hotelRoomRecordNo);
			} catch (RecordNotFoundException rnfex) {
				logger.log(Level.SEVERE, rnfex.getMessage(), rnfex);
			} catch (Exception ex) {
				if (ex instanceof RemoteException) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
					System.err.println("Problem with remote connection : "
							+ ex.getMessage());
				}
			}

		}
		logger.exiting("HotelRoomController", "bookRoom");
		return true;
	}
}