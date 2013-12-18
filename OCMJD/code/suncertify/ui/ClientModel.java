/*
 * ClientModel
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * This class extends <code>AbstractTableModel</code> and provides the table
 * data to be shown by the <code>ClientWindow</code>. It has no reference to
 * either the <code>ClientWindow</code> or the <code>ClientController</code>
 * 
 * @author Eoin Mooney
 */

public class ClientModel extends AbstractTableModel {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = -2812214454393924967L;

	/** The table header names. */
	private final String[] tableHeaderNames = { "Name", "Location", "Size",
			"Smoking", "Rate", "Date", "Booked By" };

	/** The records content. */
	private final ArrayList<String[]> tableRecords = new ArrayList<String[]>(5);

	/** The record numbers */
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
	 * Adds a record to the <code>ClientModel</code> by adding the record's data
	 * to tableRecords and adding the record number to recNos.
	 * 
	 * @param data
	 *            A <code>String[]</code> where each <code>String</code> is a
	 *            field of the record
	 * @param recNo
	 *            The record number of the record
	 */
	public void addRecord(final String[] data, final long recNo) {
		this.tableRecords.add(data);
		this.recNos.add(recNo);
	}

	/**
	 * Gets the record number for a row
	 * 
	 * @param rowIndex
	 *            A row of the table
	 * @return The record number of the record at rowIndex in the table
	 */
	public long getRecNo(final int rowIndex) {
		return this.recNos.get(rowIndex);
	}
}