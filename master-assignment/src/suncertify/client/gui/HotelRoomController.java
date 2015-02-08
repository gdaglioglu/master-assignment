package suncertify.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import suncertify.app.ApplicationRunner;
import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.HotelRoom;
import suncertify.remote.HotelDatabaseRemote;

/**
 * Handles all interactions between the GUI layer and the data layer.
 *
 * @author Gokhan Daglioglu
 * @version 2.0
 * @see DBMain
 * @see GuiControllerException
 */
public class HotelRoomController {

	/**
	 * Application mode
	 */
	ApplicationMode applicationMode;

	/**
	 * Create instance of Data using the DBMain Interface for local connections
	 */
	private DBMain localConnection;

	/**
	 * Create instance of Data using the DBMain Interface for remote connections
	 */
	private HotelDatabaseRemote remoteConnection;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.client.gui");

	/**
	 * The search listener, attached to the search button in the
	 * <code>SearchPanel</code> instance.
	 */
	private ActionListener searchListener;

	/**
	 * The booking listener, attached to the book button in the
	 * <code>SearchPanel</code> instance.
	 */
	private ActionListener bookingListener;

	/**
	 * This listener persists the state of the exact match checkbox when it's
	 * state changes.
	 */
	private ActionListener exactMatchListener;

	/** The model for the client MVC. */
	private HotelRoomModel hotelRoomModel;

	/** The view for the client MVC. */
	private HotelRoomView hotelRoomView;

	/** The criteria for last search executed. */
	private String[] lastSearchCriteria;

	/**
	 * Creates a <code>GuiController</code> instance with a specified connection
	 * type.
	 *
	 * @param connectionType
	 *            the method of connecting the client and database.
	 * @param dbLocation
	 *            the path to the data file, or the network address of the
	 *            server hosting the data file.
	 * @param port
	 *            the port the network server is listening on.
	 * @throws GuiControllerException
	 *             on communication error with database.
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
			ApplicationRunner
					.handleException("Client started with incorrect Application Mode. Exiting application");

		}

		try {
			this.hotelRoomModel = this.getAllHotelRooms();
		} catch (GuiControllerException e) {
			ApplicationRunner
					.handleException("Failed to acquire an initial hotel room list."
							+ "\nPlease check the DB connection.");
		}
		this.hotelRoomView.updateTable(this.hotelRoomModel);
	}

	/**
	 * Locates a Hotel Room record by hotel name.
	 * 
	 * @param exactMatch
	 *
	 * @param hotelName
	 *            A String representing the hotel name.
	 * @return A HotelRoomModel containing all found hotel room records.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	public HotelRoomModel findRoom(String[] searchCriteria, boolean exactMatch)
			throws GuiControllerException {
		HotelRoomModel hotelRoomModel = new HotelRoomModel();
		int[] rawResults;

		try {
			if (applicationMode == ApplicationMode.STANDALONE_CLIENT) {
				rawResults = this.localConnection.find(searchCriteria);

				for (final int recNo : rawResults) {
					String[] hotelRoom = this.localConnection.read(recNo);
					if (!exactMatch
							|| this.isExactMatch(hotelRoom, searchCriteria)) {
						hotelRoomModel.addHotelRecord(hotelRoom[0],
								hotelRoom[1], hotelRoom[2], hotelRoom[3],
								hotelRoom[4], hotelRoom[5], hotelRoom[6]);
					}

				}
			} else {
				rawResults = this.remoteConnection.find(searchCriteria);
				for (final int recNo : rawResults) {
					String[] hotelRoom = this.remoteConnection.read(recNo);
					if (!exactMatch
							|| this.isExactMatch(hotelRoom, searchCriteria)) {
						hotelRoomModel.addHotelRecord(hotelRoom[0],
								hotelRoom[1], hotelRoom[2], hotelRoom[3],
								hotelRoom[4], hotelRoom[5], hotelRoom[6]);
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
	 * Reserve a room in the database
	 * 
	 * @param customerId
	 * @param hotelRoomRecordNo
	 */
	public boolean bookRoom(String customerId, int hotelRoomRecordNo)
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

	public void init() {

		this.searchListener = new ActionListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HotelRoomController.this.lastSearchCriteria = HotelRoomController.this
							.getSearchCriteria();
					final boolean exactMatch = Boolean
							.valueOf(SavedConfiguration.getSavedConfiguration()
									.getParameter(
											SavedConfiguration.EXACT_MATCH));
					hotelRoomView
							.updateTable(HotelRoomController.this
									.findRoom(
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
					ApplicationRunner.handleException(msg);
				}

			}

		};
		this.exactMatchListener = new ActionListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				SavedConfiguration.getSavedConfiguration().setParameter(
						SavedConfiguration.EXACT_MATCH,
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
								ApplicationRunner
										.handleException(ALREADY_BOOKED_MSG);
							}
							final boolean exactMatch = Boolean
									.valueOf(SavedConfiguration
											.getSavedConfiguration()
											.getParameter(
													SavedConfiguration.EXACT_MATCH));
							hotelRoomView
									.updateTable(HotelRoomController.this
											.findRoom(
													HotelRoomController.this.lastSearchCriteria,
													exactMatch));
						} catch (GuiControllerException gce) {
							ApplicationRunner
									.handleException("Booking operation failed.");
						}
					}
				} else {
					ApplicationRunner.showWarning("Please select a row");
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
	 * This method is used to determine if a hotel room exactly matches search
	 * criteria.
	 * 
	 * @param hotelRoom
	 *            The Hotel room to check.
	 * @param searchCriteria
	 *            The search criteria used to check the Contractor.
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

	private HotelRoomModel getAllHotelRooms() throws GuiControllerException {
		lastSearchCriteria = getSearchCriteria();
		final boolean exactMatch = Boolean.valueOf(SavedConfiguration
				.getSavedConfiguration().getParameter(
						SavedConfiguration.EXACT_MATCH));
		return this.findRoom(lastSearchCriteria, exactMatch);

	}

	private String[] getSearchCriteria() {
		return hotelRoomView.getSearchCriteria();
	}
}
