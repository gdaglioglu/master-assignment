package suncertify.ui;

import static suncertify.app.util.App.handleException;
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

import javax.swing.JOptionPane;
import javax.swing.JTable;

import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;
import suncertify.domain.HotelRoom;
import suncertify.server.DataService;

/**
 * Controller for the {@link HotelRoomView} GUI class. Handles all interactions
 * between the GUI layer and the data layer.
 *
 * @author Gokhan Daglioglu
 * @see DBMain
 * @see HotelRoomView
 * @see HotelRoomTableModel
 */
public class HotelRoomController {

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.ui</code>.
	 */
	private Logger logger = Logger.getLogger(HotelRoomController.class.getPackage().getName());

	/**
	 * The model for the client MVC.
	 */
	private HotelRoomTableModel hotelRoomModel;

	/**
	 * The view for the client MVC.
	 */
	private HotelRoomView hotelRoomView;

	/**
	 * The reference to a {@link DataService} interface.
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
	 * @param dataservice
	 *            The Data Service that enables clients to interact with the
	 *            server.
	 */
	public HotelRoomController(DataService dataservice) {
		dataService = dataservice;
		this.hotelRoomView = new HotelRoomView("URLyBird Discounted Hotel Rooms - Client View");
		this.hotelRoomModel = this.getAllHotelRooms();
		this.hotelRoomView.updateTable(this.hotelRoomModel);
		this.control();
		logger.log(Level.FINE, "Initialized Hotel Room Controller");
	}

	private HotelRoomTableModel getAllHotelRooms() {
		logger.entering("HotelRoomController", "getAllHotelRooms");
		lastSearchCriteria = this.hotelRoomView.getSearchCriteria();
		final boolean exactMatch = getBooleanParameter(EXACT_MATCH);
		logger.exiting("HotelRoomController", "getAllHotelRooms");
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
	 */
	private HotelRoomTableModel getHotelRoomsByCriteria(String[] searchCriteria, boolean exactMatch) {
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
	 * This method creates <code>ActionListener</code> instances for searching
	 * and booking and adds them to the relevant <code>JButtons</code> and
	 * <code>JCheckBox</code> in {@link HotelRoomView}
	 */
	private void control() {

		this.searchListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HotelRoomController.this.lastSearchCriteria = HotelRoomController.this.hotelRoomView
						.getSearchCriteria();
				final boolean exactMatch = getBooleanParameter(EXACT_MATCH);
				hotelRoomView.updateTable(HotelRoomController.this.getHotelRoomsByCriteria(
						HotelRoomController.this.lastSearchCriteria, exactMatch));
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
						boolean booked = HotelRoomController.this.bookRoom(customerId,
								hotelRoomRecordNo);
						if (booked == false) {
							handleException(ALREADY_BOOKED_MSG);
						}
						final boolean exactMatch = getBooleanParameter(EXACT_MATCH);
						hotelRoomView.updateTable(HotelRoomController.this.getHotelRoomsByCriteria(
								HotelRoomController.this.lastSearchCriteria, exactMatch));
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
	private boolean bookRoom(String customerId, int hotelRoomRecordNo) {

		logger.entering("HotelRoomController", "bookRoom", "Record number to be booked: "
				+ hotelRoomRecordNo);
		try {
			HotelRoom hotelRoom = this.dataService.read(hotelRoomRecordNo);
			hotelRoom.setOwner(customerId);
			this.dataService.update(hotelRoomRecordNo, hotelRoom);
		} catch (final RecordNotFoundException recordNotFoundException) {
			handleException(recordNotFoundException.getMessage());
		} catch (final RemoteException exp) {
			showErrorAndExit("The remote server is no longer available.");
		}
		logger.exiting("HotelRoomController", "bookRoom");
		return true;
	}
}