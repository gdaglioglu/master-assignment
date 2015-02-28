package suncertify.ui;

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

	private static final long serialVersionUID = 797317548930520661L;

	private Logger logger = Logger.getLogger("suncertify.ui");

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

		// Set the rent and return buttons to refuse focus
		bookButton.setRequestFocusEnabled(false);
		// Add the keystroke mnemonics
		bookButton.setMnemonic(KeyEvent.VK_R);
		// Create a panel to add the rental a remove buttons
		JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bookingPanel.add(bookButton);

		// bottom panel
		JPanel bottomPanel = new JPanel(new BorderLayout());
		// bottomPanel.add(searchPanel, BorderLayout.NORTH);
		bottomPanel.add(bookingPanel, BorderLayout.SOUTH);

		// Add the bottom panel to the main window
		this.add(bottomPanel, BorderLayout.SOUTH);

		// Set table properties
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		mainTable.setToolTipText("Select a Hotel room record to book or cancel.");

		bookButton.setToolTipText("Book the hotel room selected in the above table.");

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