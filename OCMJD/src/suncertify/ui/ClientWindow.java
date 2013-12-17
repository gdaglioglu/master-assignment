/*
 * 
 */
package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientWindow.
 */
public class ClientWindow extends JFrame {

	/** The table. */
	private JTable table;

	/** The search field. */
	private JTextField searchField;

	/** The search button. */
	private JButton searchButton;

	/** The booking button. */
	private JButton bookingButton;

	/**
	 * Instantiates a new client window.
	 *
	 * @param title the title
	 */
	public ClientWindow(final String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	/**
	 * Start client view.
	 *
	 * @param tableData the table data
	 */
	public void startClientView(final AbstractTableModel tableData) {
		this.setLayout(new BorderLayout());

		this.searchField = new JTextField(20);
		this.searchButton = new JButton("Search");

		final JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		searchPanel.add(this.searchField);
		searchPanel.add(this.searchButton);

		final JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(searchPanel, BorderLayout.NORTH);

		this.table = new JTable();
		this.updateTable(tableData);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		final JScrollPane tableScrollPanel = new JScrollPane(this.table);
		tableScrollPanel.setSize(500, 250);

		this.bookingButton = new JButton("Book Room");

		final JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		actionPanel.add(this.bookingButton);

		final JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(actionPanel, BorderLayout.SOUTH);

		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(tableScrollPanel, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		this.add(panel);

		this.pack();
		this.setSize(650, 300);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Gets the search button.
	 *
	 * @return the search button
	 */
	public JButton getSearchButton() {
		return this.searchButton;
	}

	/**
	 * Gets the search field.
	 *
	 * @return the search field
	 */
	public String getSearchField() {
		return this.searchField.getText();
	}

	/**
	 * Gets the booking button.
	 *
	 * @return the booking button
	 */
	public JButton getBookingButton() {
		return this.bookingButton;
	}

	/**
	 * Gets the selected row no.
	 *
	 * @return the selected row no
	 */
	public int getSelectedRowNo() {
		if (this.table.getSelectedRowCount() == 0) {
			JOptionPane.showMessageDialog(this,
					"You have not selected a room.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return -1;
		} else {
			final String selectedOwner = (String) this.table.getValueAt(
					this.table.getSelectedRow(), 6);
			if (!selectedOwner.equals("        ")) {
				JOptionPane.showMessageDialog(this,
						"This room has already been booked.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return -1;
			} else {

				return this.table.getSelectedRow();
			}
		}
	}

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public String getCustomerID() {
		String customerID = "";

		do {
			customerID = JOptionPane.showInputDialog(this,
					"Please enter the 8 digit customer ID:",
					"Enter Customer ID", JOptionPane.QUESTION_MESSAGE);
		} while (customerID != null && !customerID.matches("\\d{8}"));

		return customerID;
	}

	/**
	 * Update table.
	 *
	 * @param tableData the table data
	 */
	public void updateTable(final AbstractTableModel tableData) {
		final int index = this.table.getSelectedRow();
		final String prevSelected = index >= 0 ? (String) this.table
				.getValueAt(index, 0) : "";

		this.table.setModel(tableData);

		for (int i = 0; i < this.table.getRowCount(); i++) {
			final String id = (String) this.table.getValueAt(i, 0);
			if (id.equals(prevSelected)) {
				this.table.setRowSelectionInterval(i, i);
				break;
			}
		}
	}

}