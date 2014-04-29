package suncertify.gui;

import suncertify.db.DatabaseFileUtils;
import suncertify.model.HotelRoom;
import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The implemetation of the {@code AbstractTableModel} to allow the displaying
 * of {@code HotelRoom} pojos on the User Interface of the URLyBird Application.
 *
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
class HotelRoomTableModel extends AbstractTableModel {

    private int selectedRow;

    // The names of the columns in the database which will map to the GUI.
    private String[] tableColumns = URLyBirdApplicationGuiConstants.COLUMN_NAMES;
    private List<HotelRoom> hotelRoomList;

    public HotelRoomTableModel(ArrayList<HotelRoom> hotelRooms) {

        hotelRoomList = hotelRooms;
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * Gets the number of rows in the table.
     * @return the number of rows in the table.
     */
    @Override public int getRowCount() {
        return hotelRoomList.size();
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * Gets the number of columns in the table.
     *
     * @return the number of columns in the table.
     */
    @Override public int getColumnCount() {
        return tableColumns.length;
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * This method gets the value at the specified row and column.
     *
     * @param row - The row to get information from, zero indexed.
     * @param column - The column to get information from, zero indexed.
     * @return Object in specified row and column
     */
    @Override public Object getValueAt(int row, int column) {

        HotelRoom hotelRoom = hotelRoomList.get(row);

        switch (column) {

            case 0 : return hotelRoom.getName();
            case 1 : return hotelRoom.getLocation();
            case 2 : return hotelRoom.getRoomSize();
            case 3 : return hotelRoom.isSmoking() ? URLyBirdApplicationGuiConstants.SMOKING_ALLOWED : URLyBirdApplicationGuiConstants.SMOKING_NOT_ALLOWED;
            case 4 : return URLyBirdApplicationConstants.CURRENCY_PREFIX + hotelRoom.getRate();
            case 5 : return hotelRoom.getDate();
            case 6 : return hotelRoom.getOwnerName();
            default: return null;
        }
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * Gets the name of the column for the table.
     *
     * @param i
     * @return the name of the column for the table.
     */
    @Override public String getColumnName(int i) {
        return tableColumns[i];
    }
}
