package suncertify.gui.client;

import suncertify.model.HotelRoom;
import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;

import javax.swing.table.AbstractTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the {@code AbstractTableModel} to allow the displaying
 * of {@code HotelRoom} pojos on the User Interface of the URLyBird Application.
 *
 * @author Luke GJ Potter
 * @since  22/04/2014
 */
class HotelRoomTableModel extends AbstractTableModel {

    // The names of the columns in the database which will map to the GUI.
    private final String[] tableColumns = UrlyBirdApplicationGuiConstants.COLUMN_NAMES;
    private final List<HotelRoom> hotelRoomList;

    /**
     * The constructor for the HotelRoomTableModel class.
     *
     * @param hotelRooms
     */
    public HotelRoomTableModel(ArrayList<HotelRoom> hotelRooms) {
        hotelRoomList = hotelRooms;
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * Gets the number of rows in the table.
     *
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
            case 3 : return hotelRoom.isSmoking() ? UrlyBirdApplicationGuiConstants.SMOKING_ALLOWED : UrlyBirdApplicationGuiConstants.SMOKING_NOT_ALLOWED;
            case 4 : return UrlyBirdApplicationConstants.CURRENCY_PREFIX + hotelRoom.getRate();
            case 5 : return isAvailableOrBooked(hotelRoom.getDate());
            case 6 : return hotelRoom.getOwnerName();
            default: return null;
        }
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * Gets the name of the column for the table.
     *
     * @param i the index of the column
     * @return the name of the column for the table.
     */
    @Override public String getColumnName(int i) {
        return tableColumns[i];
    }

    // ----- Private Methods -----
    private String isAvailableOrBooked(String date) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(UrlyBirdApplicationConstants.DATE_FORMAT);
            simpleDateFormat.setLenient(false);
            Date databaseDate = simpleDateFormat.parse(date);

            if (new Date().after(databaseDate)) return "Available";

        } catch (ParseException e) { e.printStackTrace(); }

        return date;
    }
}
