package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import suncertify.shared.App;

/**
 * The main application window of the URLyBird client application.
 *
 * @author Gokhan Daglioglu
 */
public class HotelRoomView extends JFrame {

	private static final long serialVersionUID = 2886178206092565805L;

	final JLabel nameLabel = new JLabel("Hotel name:");
	final JLabel locationLabel = new JLabel("Hotel location:");
	final JLabel sizeLabel = new JLabel("Room size:");
	final JLabel smokingLabel = new JLabel("Smoking state:");
	final JLabel rateLabel = new JLabel("Room rate:");
	final JLabel dateLabel = new JLabel("Date:");
	final JLabel ownerLabel = new JLabel("Owner:");

	private JTextField nameSearchField = new JTextField(15);
	private JTextField locationSearchField = new JTextField(15);
	private JTextField sizeSearchField = new JTextField(15);
	private JTextField smokingSearchField = new JTextField(15);
	private JTextField rateSearchField = new JTextField(15);
	private JTextField dateSearchField = new JTextField(15);
	private JTextField ownerSearchField = new JTextField(15);

	private JTable mainTable;

	private JButton bookButton = new JButton("Book Room");

	private JButton searchButton = new JButton("Search");

	private JCheckBox exactMatch = new JCheckBox("Exact match");

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.ui");

	/**
	 * Builds and displays the main application window. The constructor begins
	 * by building the connection selection dialog box. After the user selects a
	 * connection type, the method creates a <code>HotelRoomMainWindow</code>
	 * instance.
	 *
	 * @param args
	 *            an argument specifying whether we are starting a networked
	 *            client (argument missing) or a standalone client (argument =
	 *            "alone").
	 */
	public HotelRoomView(String title) {
		super(title);
		this.setJMenuBar(new HotelRoomsMenuBar());
		this.add(new HotelRoomsMainPanel());
		this.pack();
		this.setSize(1000, 500);
		this.setLocation(App.getCenterOnScreen(this));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		logger.log(Level.FINE, "Initialized Hotel Room View");
	}

	/**
	 * Uses the <code>tableData</code> member to refresh the contents of the
	 * <code>mainTable</code>. The method will attempt to preserve all previous
	 * selections and contents displayed.
	 */
	public void updateTable(final AbstractTableModel tableData) {
		// Preserve the previous selection
		JTable mainTable = this.getMainTable();
		int index = mainTable.getSelectedRow();
		String prevSelected = (index >= 0) ? (String) mainTable.getValueAt(index, 0) : "";

		// Reset the table data
		mainTable.setModel(tableData);

		// Reselect the previous item if it still exists
		for (int i = 0; i < mainTable.getRowCount(); i++) {
			String selected = (String) mainTable.getValueAt(i, 0);
			if (selected.equals(prevSelected)) {
				mainTable.setRowSelectionInterval(i, i);
				break;
			}
		}
	}

	public String[] getSearchCriteria() {
		String[] criteria = new String[7];
		criteria[0] = nameSearchField.getText().trim();
		criteria[1] = locationSearchField.getText().trim();
		criteria[2] = sizeSearchField.getText().trim();
		criteria[3] = smokingSearchField.getText().trim();
		criteria[4] = rateSearchField.getText().trim();
		criteria[5] = dateSearchField.getText().trim();
		criteria[6] = ownerSearchField.getText().trim();
		return criteria;
	}

	public JButton getSearchButton() {
		return this.searchButton;
	}

	public JTable getMainTable() {
		return this.mainTable;
	}

	public JButton getBookButton() {
		return this.bookButton;
	}

	public JCheckBox getExactMatch() {
		return this.exactMatch;
	}

	public String isExactMatchSelected() {
		return String.valueOf(this.exactMatch.isSelected());
	}

	private class HotelRoomsMainPanel extends JPanel {

		private static final long serialVersionUID = -8314548902783510446L;

		/**
		 * Constructs the main panel for the client application.
		 * 
		 */
		public HotelRoomsMainPanel() {

			mainTable = new JTable();
			mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			mainTable.setToolTipText("Select a Hotel room record to book or cancel.");

			bookButton.setRequestFocusEnabled(false);
			bookButton.setMnemonic(KeyEvent.VK_R);
			bookButton.setToolTipText("Book the hotel room selected in the above table.");

			JScrollPane tableScroll = new JScrollPane(mainTable);
			tableScroll.setSize(500, 250);

			JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			bookingPanel.add(bookButton);
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.add(bookingPanel, BorderLayout.SOUTH);

			this.setLayout(new BorderLayout());
			this.add(new SearchPanel(), BorderLayout.NORTH);
			this.add(tableScroll, BorderLayout.CENTER);
			this.add(bottomPanel, BorderLayout.SOUTH);
			logger.log(Level.FINE, "Initialized Hotel Room Main Panel");
		}
	}

	private class SearchPanel extends JPanel {

		private static final long serialVersionUID = -1126709180970637756L;

		public SearchPanel() {

			// load saved configuration
			exactMatch.setSelected(Boolean.parseBoolean(PropertyManager
					.getParameter(PropertyManager.EXACT_MATCH)));
			exactMatch.setMnemonic(KeyEvent.VK_E);

			searchButton.setMnemonic(KeyEvent.VK_S);

			this.setBorder(BorderFactory.createTitledBorder("Search"));
			this.setLayout(new GridLayout(2, 8, 10, 5));

			this.add(nameLabel);
			this.add(locationLabel);
			this.add(sizeLabel);
			this.add(smokingLabel);
			this.add(rateLabel);
			this.add(dateLabel);
			this.add(ownerLabel);

			this.add(searchButton);

			this.add(nameSearchField);
			this.add(locationSearchField);
			this.add(sizeSearchField);
			this.add(smokingSearchField);
			this.add(rateSearchField);
			this.add(dateSearchField);
			this.add(ownerSearchField);

			this.add(exactMatch);

			nameSearchField.setToolTipText("Enter hotel name to search");
			locationSearchField.setToolTipText("Enter hotel location to search");
			sizeSearchField.setToolTipText("Enter room size to search");
			smokingSearchField.setToolTipText("Enter smoking state to search");
			rateSearchField.setToolTipText("Enter room rate to search");
			dateSearchField.setToolTipText("Enter date to search");
			ownerSearchField.setToolTipText("Enter owner name to search");
			searchButton.setToolTipText("Submit the Hotel room search");
			exactMatch.setToolTipText("Tick the box to search with exact match option");
			logger.log(Level.FINE, "Initialized Hotel Room Search Panel");
		}
	}
}