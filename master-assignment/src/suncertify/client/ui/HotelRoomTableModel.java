package suncertify.client.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import suncertify.domain.HotelRoom;

/**
 * The custom table model used to display the information gathered from the
 * database in the main GUI.
 * 
 * @author Gokhan Daglioglu
 */
public class HotelRoomTableModel extends AbstractTableModel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 *
	 * Not that we ever serialize this class of course, but AbstractTableModel
	 * implements Serializable, so therefore by default we do as well.
	 */
	private static final long serialVersionUID = -1536374087517864732L;

	/**
	 * An array of <code>String</code> objects representing the table headers.
	 */
	private String[] headerNames = { "Name", "Location", "Size", "Smoking",
			"Rate", "Date", "Owner" };

	/**
	 * Holds all Hotel instances displayed in the main table.
	 */
	private ArrayList<HotelRoom> hotelRoomRecords = new ArrayList<HotelRoom>();

	/**
	 * Adds a row of hotel room data to the table.
	 *
	 * @param hotelRoomRecord
	 *            a <code>String</code> array representing a hotel room
	 */
	public void addHotelRoomRecord(String[] hotelRoomRecord) {
		this.hotelRoomRecords.add(new HotelRoom(hotelRoomRecord[0],
				hotelRoomRecord[1], hotelRoomRecord[2], hotelRoomRecord[3],
				hotelRoomRecord[4], hotelRoomRecord[5], hotelRoomRecord[6]));
	}

	/**
	 * Adds a {@link HotelRoom} object to the table.
	 *
	 * @param hotelRoom
	 *            The {@link HotelRoom} object to add to the table.
	 */
	public void addHotelRecord(HotelRoom hotelRoom) {
		this.hotelRoomRecords.add(hotelRoom);
	}

	/**
	 * Clear all records from the table model.
	 */
	public void clearData() {
		this.hotelRoomRecords.clear();
		// this.fireTableDataChanged();
	}

	/**
	 * Add all the Contractors from a list to the table model
	 * 
	 * @param records
	 *            The list of Contractors to add.
	 */
	public void addAll(final List<HotelRoom> records) {
		for (final HotelRoom rec : records) {
			addHotelRecord(rec);
		}
		// this.fireTableDataChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount() {
		return this.headerNames.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowCount() {
		return this.hotelRoomRecords.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int row, int column) {
		return this.hotelRoomRecords.get(row).toArray()[column];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAt(Object obj, int row, int column) {
		Object[] rowValues = this.hotelRoomRecords.get(row).toArray();
		rowValues[column] = obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName(int column) {
		return this.headerNames[column];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}