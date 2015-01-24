package suncertify.client.gui;

import java.util.*;
import java.util.logging.*;

import javax.swing.table.*;

import suncertify.domain.HotelRoom;

/**
 * The custom table model used by the <code>MainWindow</code> instance.
 *
 * @author gdaglioglu
 * @version 1.0
 * @see suncertify.gui.MainWindow
 */
public class HotelRoomModel extends AbstractTableModel {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     *
     * Not that we ever serialize this class of course, but AbstractTableModel
     * implements Serializable, so therefore by default we do as well.
     */
    private static final long serialVersionUID = 5165L;

   /**
    * The Logger instance. All log messages from this class are routed through
    * this member. The Logger namespace is <code>suncertify.client.gui</code>.
    */
    private Logger log = Logger.getLogger("suncertify.client.gui");

    /**
     * An array of <code>String</code> objects representing the table headers.
     */
    private String [] headerNames = {"Name", "Location", "Size",
                                    "Smoking", "Rate",
                                    "Date", "Owner"};

    /**
     * Holds all Hotel instances displayed in the main table.
     */
    private ArrayList<String[]> hotelRecords = new ArrayList<String[]>(7);

    /**
     * Returns the column count of the table.
     *
     * @return An integer indicating the number or columns in the table.
     */
    public int getColumnCount() {
        return this.headerNames.length;
    }

    /**
     * Returns the number of rows in the table.
     *
     * @return An integer indicating the number of rows in the table.
     */
    public int getRowCount() {
        return this.hotelRecords.size();
    }

    /**
     * Gets a value from a specified index in the table.
     *
     * @param row An integer representing the row index.
     * @param column An integer representing the column index.
     * @return The object located at the specified row and column.
     */
    public Object getValueAt(int row, int column) {
        String[] rowValues = this.hotelRecords.get(row);
        return rowValues[column];
    }

    /**
     * Sets the cell value at a specified index.
     *
     * @param obj The object that is placed in the table cell.
     * @param row The row index.
     * @param column The column index.
     */
    public void setValueAt(Object obj, int row, int column) {
        Object[] rowValues = this.hotelRecords.get(row);
        rowValues[column] = obj;
    }

    /**
     * Returns the name of a column at a given column index.
     *
     * @param column The specified column index.
     * @return A String containing the column name.
     */
    public String getColumnName(int column) {
        return headerNames[column];
    }

    /**
     * Given a row and column index, indicates if a table cell can be edited.
     *
     * @param row Specified row index.
     * @param column Specified column index.
     * @return A boolean indicating if a cell is editable.
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Adds a row of Hotel data to the table.
     *
     * @param name The name of the hotel.
     * @param location The location of the hotel.
     * @param size The size of the hotel.
     * @param smoking The smoking preference
     * @param rate The rate of the hotel.
     * @param date The date of the hotel..
     * @param owner The owner of the hotel.
     */
    public void addHotelRecord(String name, String location, String size,
                             String smoking, String rate,
                             String date, String owner) {

        String [] hotel = {name, location, size, smoking,
                            rate, date, owner};
        this.hotelRecords.add(hotel);
    }

    /**
     * Adds a Hotel object to the table.
     *
     * @param hotelRoom The HotelRoom object to add to the table.
     */
    public void addHotelRecord(HotelRoom hotelRoom) {
        addHotelRecord(hotelRoom.getName(), hotelRoom.getLocation(),
                     hotelRoom.getSize(), hotelRoom.getSmoking(),
                     hotelRoom.getRate(), hotelRoom.getDate(), hotelRoom.getOwner());
    }
}
