package suncertify.ui;

import static suncertify.app.util.App.handleException;
import static suncertify.app.util.App.showError;
import static suncertify.app.util.App.showErrorAndExit;
import static suncertify.app.util.App.showWarning;
import static suncertify.app.util.PropertyManager.EXACT_MATCH;
import static suncertify.app.util.PropertyManager.getBooleanParameter;
import static suncertify.app.util.PropertyManager.setParameter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.HotelRoom;
import suncertify.init.ApplicationRunner;
import suncertify.server.DataService;

/**
 * Controller for the {@link HotelRoomView} GUI class. Handles all interactions
 * between the GUI layer and the data layer.
 *
 * @author Gokhan Daglioglu
 * @see DBMain
 * @see HotelRoomView
 * @see GuiControllerException
 */

public class HotelRoomController {

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.ui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.ui");

	/**
	 * The model for the client MVC.
	 */
	private HotelRoomTableModel hotelRoomModel;

	/**
	 * The view for the client MVC.
	 */
	private HotelRoomView hotelRoomView;

	/**
	 * The reference to a local datafile.
	 */
	private DataService dataService;

	/**
	 * The search listener, attached to the search button in the
	 * {@link HotelRoomView} instance.
	 */
	private ActionListener searchListener;

	/**
	 * The booking listener, attached to the book button in the
	 * {@link HotelRoomView} instance.
	 */
	private ActionListener bookingListener;

	/**
	 * This exact match listener, attached to the exact match check box in the
	 * {@link HotelRoomView} instance.
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
	public HotelRoomController(DataService dataservice) {
		dataService = dataservice;
		this.hotelRoomView = new HotelRoomView("URLyBird Discounted Hotel Rooms - Client View");

		try {
			this.hotelRoomModel = this.getAllHotelRooms();
		} catch (GuiControllerException e) {
			handleException("Failed to acquire an initial hotel room list."
					+ "\nPlease check the DB connection.");
		}

		this.hotelRoomView.updateTable(this.hotelRoomModel);
		this.control();

		logger.log(Level.FINE, "Initialized Hotel Room Controller");
	}

	private HotelRoomTableModel getAllHotelRooms() throws GuiControllerException {
		lastSearchCriteria = this.hotelRoomView.getSearchCriteria();
		final boolean exactMatch = getBooleanParameter(EXACT_MATCH);
		return this.getHotelRoomsByCriteria(lastSearchCriteria, exactMatch);
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
	private HotelRoomTableModel getHotelRoomsByCriteria(String[] searchCriteria, boolean exactMatch)
			throws GuiControllerException {
		logger.entering("HotelRoomController", "getHotelRoomsByCriteria",
				"Parameters: searchCriteria = " + Arrays.toString(searchCriteria)
						+ "\nexactMatch = " + exactMatch);
		HotelRoomTableModel hotelRoomModel = new HotelRoomTableModel();
		try {
			final List<HotelRoom> records = this.dataService.find(searchCriteria, exactMatch);
			hotelRoomModel.clearData();
			hotelRoomModel.addAll(records);
		} catch (final RemoteException exp) {
			showErrorAndExit("The remote server is no longer available.");
		}
		logger.exiting("HotelRoomController", "getHotelRoomsByCriteria");
		return hotelRoomModel;
	}

	/**
	 * This method is called by the {@link ApplicationRunner} after
	 * instantiating a <code>HotelRoomController</code>. It creates
	 * <code>ActionListener</code> instances for searching and booking and adds
	 * them to the relevant <code>JButtons</code> in {@link HotelRoomView}
	 */
	private void control() {

		this.searchListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HotelRoomController.this.lastSearchCriteria = HotelRoomController.this.hotelRoomView
							.getSearchCriteria();
					final boolean exactMatch = getBooleanParameter(EXACT_MATCH);
					hotelRoomView.updateTable(HotelRoomController.this.getHotelRoomsByCriteria(
							HotelRoomController.this.lastSearchCriteria, exactMatch));
				} catch (GuiControllerException gce) {
					// Inspect the exception chain
					Throwable rootException = gce.getCause();
					String msg = "Search operation failed.";
					// If a syntax error occurred, get the message
					if (rootException instanceof PatternSyntaxException) {
						msg += ("\n" + rootException.getMessage());
					}
					handleException(msg);
				}

			}

		};

		this.exactMatchListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setParameter(EXACT_MATCH,
						HotelRoomController.this.hotelRoomView.isExactMatchSelected());
			}
		};

		this.bookingListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String ALREADY_BOOKED_MSG = "Unable to book - room is already booked.";

				JTable mainTable = hotelRoomView.getMainTable();
				int hotelRoomRecordNo = mainTable.getSelectedRow();
				if ((hotelRoomRecordNo >= 0)) {
					final String customerId = JOptionPane.showInputDialog(null,
							"Enter Customer ID", "Book Room", JOptionPane.QUESTION_MESSAGE);
					if (isValidCustomerId(customerId)) {
						try {
							boolean booked = HotelRoomController.this.bookRoom(customerId,
									hotelRoomRecordNo);
							if (booked == false) {
								handleException(ALREADY_BOOKED_MSG);
							}
							final boolean exactMatch = getBooleanParameter(EXACT_MATCH);
							hotelRoomView
									.updateTable(HotelRoomController.this
											.getHotelRoomsByCriteria(
													HotelRoomController.this.lastSearchCriteria,
													exactMatch));
						} catch (GuiControllerException gce) {
							handleException("Booking operation failed.");
						}
					}
				} else {
					showWarning("Please select a row");
				}
			}

			private boolean isValidCustomerId(String id) {
				if (id == null) {
					return false;
				} else if (!id.matches("^\\d{8}$")) {
					showWarning("Please enter a valid Customer Id");
					return false;
				} else
					return true;
			}

		};

		this.hotelRoomView.getSearchButton().addActionListener(this.searchListener);
		this.hotelRoomView.getBookButton().addActionListener(this.bookingListener);
		this.hotelRoomView.getExactMatch().addActionListener(this.exactMatchListener);
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

		logger.entering("HotelRoomController", "bookRoom", "Record number to be booked: "
				+ hotelRoomRecordNo);
		try {
			HotelRoom hotelRoom = this.dataService.read(hotelRoomRecordNo);
			hotelRoom.setOwner(customerId);
			this.dataService.update(hotelRoomRecordNo, hotelRoom);
		} catch (final RecordNotFoundException recordNotFoundException) {
			logger.log(Level.SEVERE, recordNotFoundException.getMessage(), recordNotFoundException);
			showError(recordNotFoundException.getMessage());
		} catch (final RemoteException exp) {
			showErrorAndExit("The remote server is no longer available.");
		}
		logger.exiting("HotelRoomController", "bookRoom");
		return true;
	}
}