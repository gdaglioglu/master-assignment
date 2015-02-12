package suncertify.client.ui;

import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.JTextField;

class SearchHotelRoom {
	/**
	 * The internal reference to the GUI controller.
	 */
	public HotelRoomController controller;
	/**
	 * The <code>JTable</code> that displays the Hotel room held by the system.
	 */
	public JTable mainTable;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField nameSearchField;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField locationSearchField;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField sizeSearchField;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField smokingSearchField;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField rateSearchField;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField dateSearchField;
	/**
	 * The text field that contains the user defined search String.
	 */
	public JTextField ownerSearchField;
	/**
	 * The internal reference to the the currently displayed table data.
	 */
	public HotelRoomTableModel tableData;
	/**
	 * Holds a copy of the last user defined search String.
	 */
	public String previousSearchString;
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	public Logger log;

	public SearchHotelRoom(JTable mainTable, JTextField nameSearchField,
			JTextField locationSearchField, JTextField sizeSearchField,
			JTextField smokingSearchField, JTextField rateSearchField,
			JTextField dateSearchField, JTextField ownerSearchField,
			String previousSearchString, Logger log) {
		this.mainTable = mainTable;
		this.nameSearchField = nameSearchField;
		this.locationSearchField = locationSearchField;
		this.sizeSearchField = sizeSearchField;
		this.smokingSearchField = smokingSearchField;
		this.rateSearchField = rateSearchField;
		this.dateSearchField = dateSearchField;
		this.ownerSearchField = ownerSearchField;
		this.previousSearchString = previousSearchString;
		this.log = log;
	}
}