package suncertify.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import suncertify.db.Record;

public class Model extends AbstractTableModel {

	private static final long serialVersionUID = 2302760553289448235L;

	private String[] tableHeaderNames = { "id", "name", "location", "size",
			"smoking", "rate", "date", "owner" };

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

	public void addRecord(Record record) {
		addRecord(record.getAllFields());
	}

}