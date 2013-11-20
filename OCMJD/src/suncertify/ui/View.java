package suncertify.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class View extends JFrame {

	private static final long serialVersionUID = -826146775442786697L;

	private JTable table;
	private JTextField searchField;
	private JButton searchButton;
	private JButton reserveButton;

	public View(AbstractTableModel tableData) {
		super("URLyBird 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		searchField = new JTextField(20);
		searchButton = new JButton("Search");

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(searchPanel, BorderLayout.NORTH);

		table = new JTable();
		updateTable(tableData);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane tableScrollPanel = new JScrollPane(table);
		tableScrollPanel.setSize(500, 250);

		reserveButton = new JButton("Reserve Room");

		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		actionPanel.add(reserveButton);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(actionPanel, BorderLayout.SOUTH);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(tableScrollPanel, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		this.add(panel);

		pack();
		this.setSize(650, 300);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - getWidth()) / 2);
		int y = (int) ((d.getHeight() - getHeight()) / 2);
		this.setLocation(x, y);
		setVisible(true);

	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public void updateTable(AbstractTableModel tableData) {
		int index = table.getSelectedRow();
		String prevSelected = (index >= 0) ? (String) table
				.getValueAt(index, 0) : "";

		table.setModel(tableData);

		for (int i = 0; i < table.getRowCount(); i++) {
			String id = (String) table.getValueAt(i, 0);
			if (id.equals(prevSelected)) {
				table.setRowSelectionInterval(i, i);
				break;
			}
		}
	}
}
