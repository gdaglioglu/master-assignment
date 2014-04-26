package suncertify.gui;

import suncertify.db.DatabaseFileUtils;
import suncertify.model.HotelRoom;

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
    private String[] tableColumns = {"Hotel Name", "Location", "Room Size",
            "Smoking", "Rate", "Date Available", "Customer ID"};
    private List<HotelRoom> hotelRoomList;

    public HotelRoomTableModel(ArrayList<HotelRoom> hotelRooms) {

        hotelRoomList = hotelRooms;
    }

    @Override
    public int getRowCount() {
        return hotelRoomList.size();
    }

    @Override
    public int getColumnCount() {
        return tableColumns.length;
    }

    /**
     * Overridden getValueAt method from super class AbstractTableModel.
     * This method gets the value at the specified row and column.
     *
     * @param row
     * @param column
     * @return Object in specified row and column
     */
    @Override
    public Object getValueAt(int row, int column) {

        HotelRoom hotelRoom = hotelRoomList.get(row);

        switch (column) {

            case 0 : return hotelRoom.getName();
            case 1 : return hotelRoom.getLocation();
            case 2 : return hotelRoom.getRoomSize();
            case 3 : return hotelRoom.isSmoking();
            case 4 : return hotelRoom.getRate();
            case 5 : return hotelRoom.getDate();
            case 6 : return hotelRoom.getOwnerName();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int i) {
        return tableColumns[i];
    }

    /**
     * Overriding setValueAt method from AbstractTablemodel
     * Sets the value at specified row and column
     *
     * @param value
     * @param row
     * @param column
     */
    @Override
    public void setValueAt(Object value, int row, int column) {

        HotelRoom hotelRoom = hotelRoomList.get(row);

        switch (column) {

            case 0 : hotelRoom.setName((String) value);      break;
            case 1 : hotelRoom.setLocation((String) value);  break;
            case 2 : hotelRoom.setRoomSize((Integer) value); break;
            case 3 : hotelRoom.setSmoking((Boolean) value);  break;
            case 4 : hotelRoom.setRate((Double) value);      break;
            case 5 : hotelRoom.setDate((String) value);      break;
            case 6 : hotelRoom.setOwnerName((String) value); break;
            default: break;
        }

        hotelRoomList.set(row, hotelRoom);
    }

    public int getSelectedRow() {
        return selectedRow;
    }
}
