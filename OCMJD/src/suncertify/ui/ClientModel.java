/*
 * ClientModel
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientModel.
 */
@SuppressWarnings("serial")
public class ClientModel extends AbstractTableModel {

	/** The table header names. */
	private final String[] tableHeaderNames = { "Name", "Location", "Size",
			"Smoking", "Rate", "Date", "Booked By" };

	/** The table records. */
	private final ArrayList<String[]> tableRecords = new ArrayList<String[]>(5);

	/** The rec nos. */
	private final ArrayList<Long> recNos = new ArrayList<Long>(5);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return this.tableHeaderNames.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.tableRecords.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		final String[] rowValues = this.tableRecords.get(rowIndex);
		return rowValues[columnIndex];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(final Object obj, final int rowIndex,
			final int columnIndex) {
		final Object[] rowValues = this.tableRecords.get(rowIndex);
		rowValues[columnIndex] = obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(final int columnIndex) {
		return this.tableHeaderNames[columnIndex];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}

	/**
	 * Adds the record.
	 *
	 * @param data the data
	 * @param recNo the rec no
	 */
	public void addRecord(final String[] data, final long recNo) {
		this.tableRecords.add(data);
		this.recNos.add(recNo);
	}

	/**
	 * Gets the rec no.
	 *
	 * @param rowIndex the row index
	 * @return the rec no
	 */
	public long getRecNo(final int rowIndex) {
		return this.recNos.get(rowIndex);
	}
}