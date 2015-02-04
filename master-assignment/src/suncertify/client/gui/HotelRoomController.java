package suncertify.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import javax.swing.JTable;

import suncertify.app.ApplicationRunner;
import suncertify.db.DBMain;

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
	 * Holds a reference to the client interface of the
	 * <code>HotelRoomMediator</code>.
	 */
	private DBMain connection;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	private Logger log = Logger.getLogger("suncertify.client.gui");

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

		// find out where our database is
		DatabaseLocationDialog dbLocationDialog = new DatabaseLocationDialog(
				applicationMode);

		if (dbLocationDialog.userCanceled()) {
			System.exit(0);
		}

		try {
			switch (dbLocationDialog.getNetworkType()) {
			case DIRECT:
				connection = suncertify.direct.HotelConnector
						.getLocal(dbLocationDialog.getLocation());
				break;
			case RMI:
				connection = suncertify.remote.HotelConnector.getRemote(
						dbLocationDialog.getLocation(),
						dbLocationDialog.getPort());
				break;
			default:
				throw new IllegalArgumentException(
						"Invalid connection type specified");
			}
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			// throw new GuiControllerException(e);
		} catch (java.rmi.RemoteException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			// throw new GuiControllerException(e);
		} catch (java.io.IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
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
			this.log.severe("Client started with incorrect Application Mode. Exiting application");
			ApplicationRunner
					.handleException("Client started with incorrect Application Mode. Exiting application");

		}

		// Perform an empty search to retrieve all records
		try {
			this.hotelRoomModel = this.find(new String[] {});
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
	 * @param hotelName
	 *            A String representing the hotel name.
	 * @return A HotelRoomModel containing all found hotel room records.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	public HotelRoomModel find(String[] hotelDetails)
			throws GuiControllerException {
		HotelRoomModel out = new HotelRoomModel();
		try {
			int[] rawResults = new int[0];
			rawResults = this.connection.find(hotelDetails);

			for (final int recNo : rawResults) {
				String[] hotelRoom = this.connection.read(recNo);
				out.addHotelRecord(hotelRoom[0], hotelRoom[1], hotelRoom[2],
						hotelRoom[3], hotelRoom[4], hotelRoom[5], hotelRoom[6]);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw new GuiControllerException(e);
		}
		return out;
	}

	// /**
	// * Retrieves all Hotel room records from the database.
	// *
	// * @return A HotelRoomModel containing all Hotel room Records.
	// * @throws GuiControllerException
	// * Indicates a database or network level exception.
	// */
	// public HotelRoomModel getHotelRooms() throws GuiControllerException {
	// HotelRoomModel out = new HotelRoomModel();
	// try {
	// int[] rawResults = new int[0];
	// rawResults = this.connection.find(new String[1]);
	//
	// for (final int recNo : rawResults) {
	// String[] hotelRoom = this.connection.read(recNo);
	// out.addHotelRecord(hotelRoom[0], hotelRoom[1], hotelRoom[2],
	// hotelRoom[3], hotelRoom[4], hotelRoom[5], hotelRoom[6]);
	// }
	// } catch (Exception e) {
	// log.log(Level.SEVERE, e.getMessage(), e);
	// throw new GuiControllerException(e);
	// }
	// return out;
	// }

	/**
	 * Decrements the number of Dvd's in stock identified by their UPC number.
	 *
	 * @param upc
	 *            The UPC of the Dvd to rent.
	 * @return A boolean indicating if the rent operation was successful.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	public boolean rent(String upc) throws GuiControllerException {
		boolean returnValue = false;
		return returnValue;
	}

	/**
	 * Increments the number of Dvd's in stock identified by their UPC number.
	 *
	 * @param upc
	 *            The UPC of the Dvd to return.
	 * @return A boolean indicating if the return operation was successful.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	public boolean returnRental(String upc) throws GuiControllerException {
		boolean returnValue = false;

		return returnValue;
	}

	private String[] getSearchCriteria() {
		return hotelRoomView.getSearchCriteria();
	}

	public void init() {

		this.searchListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HotelRoomController.this.lastSearchCriteria = HotelRoomController.this
							.getSearchCriteria();
					hotelRoomView.updateTable(HotelRoomController.this
							.find(HotelRoomController.this.lastSearchCriteria));
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

		this.bookingListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String ALREADY_BOOKED_MSG = "Unable to book - room is already booked.";
				String hotelRoomRecordNo = "";
				JTable mainTable = hotelRoomView.getMainTable();
				int index = mainTable.getSelectedRow();
				if ((index >= 0) && (index <= mainTable.getColumnCount())) {
					hotelRoomRecordNo = (String) mainTable.getValueAt(index, 0);
					try {
						boolean booked = HotelRoomController.this
								.rent(hotelRoomRecordNo);
						if (booked == false) {
							ApplicationRunner
									.handleException(ALREADY_BOOKED_MSG);
						}
						// tableData = controller.find(previousCriteria);
						hotelRoomView
								.updateTable(HotelRoomController.this
										.find(HotelRoomController.this.lastSearchCriteria));
					} catch (GuiControllerException gce) {
						ApplicationRunner
								.handleException("Rent operation failed.");
					}
				}
			}

		};

		this.hotelRoomView.getSearchButton().addActionListener(
				this.searchListener);
		this.hotelRoomView.getBookButton().addActionListener(
				this.bookingListener);
	}

}
