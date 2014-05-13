package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;

/**
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
class TablePanel extends JPanel {

    private final JPanel tablePanel;
    // The JTable that will hold the records.
    public static JTable hotelRoomTable;
    // Concrete subclass of AbstractTableModel.
    public static HotelRoomTableModel hotelRoomTableModel;

    public TablePanel(DatabaseAccessDao databaseAccessDao) {

        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setName("Records");
        hotelRoomTableModel = new HotelRoomTableModel(databaseAccessDao.retrieveAllHotelRooms());
        hotelRoomTable = new JTable(hotelRoomTableModel);
        hotelRoomTable.setPreferredScrollableViewportSize(UrlyBirdApplicationGuiConstants.CLIENT_GUI_JTABLE_DIMENSION);
        tablePanel.add(new JScrollPane(hotelRoomTable));
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public static void refreshHotelRoomTableModel() {
        hotelRoomTable.setModel(hotelRoomTableModel);
    }
}