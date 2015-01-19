/*
 * ClientWindow
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * This class provides the GUI for the application when running as a client. It
 * consists of a <code>JTable</code>, a <code>JTextField</code> and two
 * <code>JButton</code>s. The <code>JTable</code> is populated with data from
 * the <code>ClientModel</code> and the other three components are used by the
 * <code>ClientController</code> to produce the GUI functionality. This class
 * has no reference to the <code>ClientController</code> and only requires an
 * instance of the <code>AbstractTableModel</code>
 * 
 * @author Eoin Mooney
 */
public class ClientWindow extends JFrame {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = -3556141753736333290L;

	/** The <code>JTable</code> which displays the records. */
	private JTable table;

	/** The <code>JTextField</code> which is used as a search field. */
	private JTextField searchField;

	/**
	 * The <code>JButton</code>s which the <code>ClientController</code> adds
	 * <code>ActionListener</code>s to.
	 */
	private JButton searchButton, bookingButton;

	/**
	 * Instantiates a new client window.
	 * 
	 * @param title
	 *            <code>String</code> that is used for the window title
	 */
	public ClientWindow(final String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	/**
	 * Start client view and populate the <code>JTable</code> with data from the
	 * provided <code>AbstractTableModel</code> instance
	 * 
	 * @param tableData
	 *            An <code>AbstractTableModel</code> instance
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
	 * @return a reference to the search button
	 */
	public JButton getSearchButton() {
		return this.searchButton;
	}

	/**
	 * Gets the contents search field.
	 * 
	 * @return the text in the search field
	 */
	public String getSearchField() {
		return this.searchField.getText();
	}

	/**
	 * Gets the booking button.
	 * 
	 * @return a reference to the button button
	 */
	public JButton getBookingButton() {
		return this.bookingButton;
	}

	/**
	 * Gets the selected row number.
	 * 
	 * @return the currently selected row number
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
	 * Prompts the user for an 8 digit customer ID. Validates the user's input
	 * 
	 * @return the customer ID
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
	 * Reloads the <code>JTable</code> with the provided
	 * <code>AbstractTableModel</code> instance
	 * 
	 * @param tableData
	 *            An <code>AbstractTableModel</code> instance
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

	/**
	 * Shows a dialog when the <code>ClientController</code> catches an
	 * exception
	 * 
	 * @param message
	 *            The message to be displayed
	 * @param title
	 *            The title of the dialog
	 * @param importance
	 *            The severity of the error
	 */
	public void showError(final String message, final String title,
			final Level importance) {
		if (importance == Level.SEVERE) {
			JOptionPane.showMessageDialog(this, message, title,
					JOptionPane.ERROR_MESSAGE);
		} else if (importance == Level.WARNING) {
			JOptionPane.showMessageDialog(this, message, title,
					JOptionPane.WARNING_MESSAGE);
		}
	}

}