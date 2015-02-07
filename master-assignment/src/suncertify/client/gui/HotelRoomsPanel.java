package suncertify.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * All the items that belong within the main panel of the client application.
 * Having this here helps keep the code clean within the body of the MainWindow
 * constructor.
 */
public class HotelRoomsPanel extends JPanel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 797317548930520661L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.client.gui");

	private SearchPanel searchPanel;

	/**
	 * The <code>JTable</code> that displays the Hotel room held by the system.
	 */
	private JTable mainTable;

	JButton bookButton = new JButton("Book Room");

	/**
	 * Constructs the main panel for the client application.
	 * 
	 * @param controller
	 */
	public HotelRoomsPanel() {
		this.setLayout(new BorderLayout());
		mainTable = new JTable();
		JScrollPane tableScroll = new JScrollPane(mainTable);
		tableScroll.setSize(500, 250);

		this.add(tableScroll, BorderLayout.CENTER);
		this.searchPanel = new SearchPanel();
		this.add(searchPanel, BorderLayout.NORTH);

		// Setup rent and return buttons

		JButton unbookButton = new JButton("Cancel Booking");

		// Set the rent and return buttons to refuse focus
		bookButton.setRequestFocusEnabled(false);
		unbookButton.setRequestFocusEnabled(false);
		// Add the keystroke mnemonics
		bookButton.setMnemonic(KeyEvent.VK_R);
		unbookButton.setMnemonic(KeyEvent.VK_U);
		// Create a panel to add the rental a remove buttons
		JPanel hiringPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		hiringPanel.add(bookButton);
		hiringPanel.add(unbookButton);

		// bottom panel
		JPanel bottomPanel = new JPanel(new BorderLayout());
		// bottomPanel.add(searchPanel, BorderLayout.NORTH);
		bottomPanel.add(hiringPanel, BorderLayout.SOUTH);

		// Add the bottom panel to the main window
		this.add(bottomPanel, BorderLayout.SOUTH);

		// Set table properties
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		mainTable
				.setToolTipText("Select a Hotel room record to book or cancel.");

		// Add Tool Tips
		unbookButton
				.setToolTipText("Cancel the booking of the hotel room selected in the above table.");
		bookButton
				.setToolTipText("Book the hotel room selected in the above table.");

	}

	public String[] getSearchCriteria() {
		return this.searchPanel.getSearchCriteria();
	}

	public JButton getSearchButton() {
		return this.searchPanel.getSearchButton();
	}

	public JTable getMainTable() {
		return this.mainTable;
	}

	public JButton getBookButton() {
		return bookButton;
	}

	public JCheckBox getExactMatch() {
		return this.searchPanel.getExactMatch();
	}

	public String isExactMatchSelected() {
		return this.searchPanel.isExactMatchSelected();
	}

}