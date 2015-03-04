package suncertify.ui;

import static suncertify.app.util.App.getCenterOnScreen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import suncertify.app.util.PropertyManager;

/**
 * The main application window of the URLyBird client application.
 *
 * @author Gokhan Daglioglu
 */
public class HotelRoomView extends JFrame {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2886178206092565805L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.ui</code>.
	 */
	private Logger logger = Logger.getLogger(HotelRoomView.class.getPackage().getName());

	/**
	 * The <code>JTextField</code> which is used as a name search field for the
	 * hotel.
	 */
	private JTextField nameSearchField = new JTextField(15);

	/**
	 * The <code>JTextField</code> which is used as a location search field for
	 * the hotel.
	 */
	private JTextField locationSearchField = new JTextField(15);

	/**
	 * The <code>JTextField</code> which is used as a size search field for the
	 * hotel.
	 */
	private JTextField sizeSearchField = new JTextField(15);

	/**
	 * The <code>JTextField</code> which is used as a smoking search field for
	 * the hotel.
	 */
	private JTextField smokingSearchField = new JTextField(15);

	/**
	 * The <code>JTextField</code> which is used as a rate search field for the
	 * hotel.
	 */
	private JTextField rateSearchField = new JTextField(15);

	/**
	 * The <code>JTextField</code> which is used as a date search field for the
	 * hotel.
	 */
	private JTextField dateSearchField = new JTextField(15);

	/**
	 * The <code>JTextField</code> which is used as a owner search field for the
	 * hotel.
	 */
	private JTextField ownerSearchField = new JTextField(15);

	/**
	 * The <code>JTable</code> which displays the records.
	 */
	private JTable mainTable;

	/**
	 * The <code>JButton</code>s which allows user to perform book operation.
	 */
	private JButton bookButton = new JButton("Book Room");

	/**
	 * The <code>JButton</code>s which allows user to perform search operation.
	 */
	private JButton searchButton = new JButton("Search");

	/**
	 * The <code>JCheckBox</code> which allows user to perform a search
	 * operation using exact match functionality.
	 */
	private JCheckBox exactMatch = new JCheckBox("Exact match");

	/**
	 * Builds and displays the main application window.
	 *
	 * @param title
	 *            The title to be displayed on the main application window.
	 */
	public HotelRoomView(String title) {
		super(title);
		this.setJMenuBar(new HotelRoomsMenuBar());
		this.add(new HotelRoomsMainPanel());
		this.pack();
		this.setSize(1000, 500);
		this.setLocation(getCenterOnScreen(this));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		logger.log(Level.FINE, "Initialized Hotel Room View");
	}

	/**
	 * Uses the <code>tableData</code> member to refresh the contents of the
	 * <code>mainTable</code>. The method will attempt to preserve all previous
	 * selections and contents displayed.
	 * 
	 * @param tableData
	 *            The new data set to be used to update the records shown to the
	 *            user.
	 */
	public void updateTable(final AbstractTableModel tableData) {
		int index = mainTable.getSelectedRow();
		// Preserve the previous selection
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

	/**
	 * Gets the contents of search fields.
	 * 
	 * @return the array that contains the contents of each search field.
	 */
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

	/**
	 * Gets the search button.
	 * 
	 * @return a reference to the search button.
	 */
	public JButton getSearchButton() {
		return this.searchButton;
	}

	/**
	 * Gets the main table.
	 * 
	 * @return a reference to the main table.
	 */
	public JTable getMainTable() {
		return this.mainTable;
	}

	/**
	 * Gets the booking button.
	 * 
	 * @return a reference to the button button.
	 */
	public JButton getBookButton() {
		return this.bookButton;
	}

	/**
	 * Gets the exact match check box.
	 * 
	 * @return a reference to the exact match check box.
	 */
	public JCheckBox getExactMatch() {
		return this.exactMatch;
	}

	/**
	 * Gets the exact match check box's state.
	 * 
	 * @return the state of the exact match check box.
	 */
	public String isExactMatchSelected() {
		return String.valueOf(this.exactMatch.isSelected());
	}

	/**
	 * The menu bar of main application window of the URLyBird client
	 * application.
	 *
	 * @author Gokhan Daglioglu
	 */
	private class HotelRoomsMenuBar extends JMenuBar {

		/**
		 * A version number for this class so that serialization can occur
		 * without worrying about the underlying class changing between
		 * serialization and deserialization.
		 */
		private static final long serialVersionUID = -81759777197357075L;

		public HotelRoomsMenuBar() {
			JMenu fileMenu = new JMenu("File");
			JMenuItem quitMenuItem = new JMenuItem("Quit");
			quitMenuItem.addActionListener(new QuitApplication());
			quitMenuItem.setMnemonic(KeyEvent.VK_Q);
			fileMenu.add(quitMenuItem);
			fileMenu.setMnemonic(KeyEvent.VK_F);
			this.add(fileMenu);
			logger.log(Level.FINE, "Initialized Hotel Room Menu Bar");
		}

		/**
		 * Handles all application quit events.
		 *
		 * @author Gokhan Daglioglu
		 */
		private class QuitApplication implements ActionListener {

			/**
			 * Quits the application when invoked.
			 *
			 * @param actionEvent
			 *            The event triggering the quit operation.
			 */
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				logger.log(Level.INFO, "Exiting Client Application");
				System.exit(0);
			}
		}
	}

	/**
	 * The main panel of application window of the URLyBird client application.
	 *
	 * @author Gokhan Daglioglu
	 */
	private class HotelRoomsMainPanel extends JPanel {

		/**
		 * A version number for this class so that serialization can occur
		 * without worrying about the underlying class changing between
		 * serialization and deserialization.
		 */
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

	/**
	 * The search panel of application's main panel.
	 *
	 * @author Gokhan Daglioglu
	 */
	private class SearchPanel extends JPanel {

		/**
		 * A version number for this class so that serialization can occur
		 * without worrying about the underlying class changing between
		 * serialization and deserialization.
		 */
		private static final long serialVersionUID = -1126709180970637756L;

		/**
		 * Constructs the main panel for the client application.
		 * 
		 */
		public SearchPanel() {
			final JLabel nameLabel = new JLabel("Hotel name:");
			final JLabel locationLabel = new JLabel("Hotel location:");
			final JLabel sizeLabel = new JLabel("Room size:");
			final JLabel smokingLabel = new JLabel("Smoking state:");
			final JLabel rateLabel = new JLabel("Room rate:");
			final JLabel dateLabel = new JLabel("Date:");
			final JLabel ownerLabel = new JLabel("Owner:");

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