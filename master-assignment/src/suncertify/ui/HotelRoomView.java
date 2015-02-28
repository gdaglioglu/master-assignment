package suncertify.ui;

import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import suncertify.shared.App;

/**
 * The main application window of the URLyBird client application.
 *
 * @author Gokhan Daglioglu
 */
public class HotelRoomView extends JFrame {

	private static final long serialVersionUID = 2886178206092565805L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.ui");

	private HotelRoomsMenu hotelRoomsMenu;

	private HotelRoomsPanel hotelRoomsPanel;

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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		hotelRoomsMenu = new HotelRoomsMenu();

		hotelRoomsPanel = new HotelRoomsPanel();

		this.setJMenuBar(hotelRoomsMenu);

		this.add(hotelRoomsPanel);

		this.pack();
		this.setSize(1000, 500);
		this.setLocation(App.getCenterOnScreen(this));
		this.setVisible(true);
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
		return this.hotelRoomsPanel.getSearchCriteria();
	}

	public JButton getSearchButton() {
		return this.hotelRoomsPanel.getSearchButton();
	}

	public JTable getMainTable() {
		return this.hotelRoomsPanel.getMainTable();
	}

	public JButton getBookButton() {
		return this.hotelRoomsPanel.getBookButton();
	}

	public JCheckBox getExactMatch() {
		return this.hotelRoomsPanel.getExactMatch();
	}

	public String isExactMatchSelected() {
		return this.hotelRoomsPanel.isExactMatchSelected();
	}

}