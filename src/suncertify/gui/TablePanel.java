package suncertify.gui;

import suncertify.controller.DatabaseAccessDaoImpl;
import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;

/**
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
class TablePanel extends JPanel {

    JPanel tablePanel;
    // The JTable that will hold the records.
    public static JTable hotelRoomTable;
    // Concrete subclass of AbstractTableModel.
    public static HotelRoomTableModel hotelRoomTableModel;

    public TablePanel() {

        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setName("Records");
        hotelRoomTableModel = new HotelRoomTableModel(new DatabaseAccessDaoImpl().retrieveAllHotelRooms());
        hotelRoomTable = new JTable(hotelRoomTableModel);
        hotelRoomTable.setPreferredScrollableViewportSize(URLyBirdApplicationGuiConstants.CLIENT_GUI_JTABLE_DIMENSION);
        tablePanel.add(new JScrollPane(hotelRoomTable));
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public static void refreshHotelRoomTableModel() {
        hotelRoomTable.setModel(hotelRoomTableModel);
    }
}