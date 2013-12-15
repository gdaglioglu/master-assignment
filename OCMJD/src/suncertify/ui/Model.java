package suncertify.ui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class Model extends AbstractTableModel {

	private String[] tableHeaderNames = { "Name", "Location", "Size",
			"Smoking", "Rate", "Date", "Booked By" };

	private ArrayList<String[]> tableRecords = new ArrayList<String[]>(5);
	private ArrayList<Long> recNos = new ArrayList<Long>(5);
	
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

	public void addRecord(String[] data, long recNo) {
		tableRecords.add(data);
		recNos.add(recNo);
	}
	
	public long getRecNo(int rowIndex){
		return recNos.get(rowIndex);
	}
}