package suncertify.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import suncertify.db.RoomRecord;

@SuppressWarnings("serial")
public class Model extends AbstractTableModel {

	private String[] tableHeaderNames = { "Rec No", "Name", "Location", "Size",
			"Smoking", "Rate", "Date", "Booked By" };

	private ArrayList<String[]> tableRecords = new ArrayList<String[]>(5);

	@Override
	public int getColumnCount() {
		return tableHeaderNames.length;
	}

	@Override
	public int getRowCount() {
		return tableRecords.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String[] rowValues = tableRecords.get(rowIndex);
		return rowValues[columnIndex];
	}

	@Override
	public void setValueAt(Object obj, int rowIndex, int columnIndex) {
		Object[] rowValues = tableRecords.get(rowIndex);
		rowValues[columnIndex] = obj;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return tableHeaderNames[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void addRecord(String[] data) {
		tableRecords.add(data);
	}

	public void addRecord(RoomRecord record) {
		addRecord(record.getAllFieldsData());
	}

}