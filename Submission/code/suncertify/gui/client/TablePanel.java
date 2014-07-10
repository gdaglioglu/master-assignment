package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;

/**
 * The JPanel that holds the JTable to display the records.
 *
 * @author Luke GJ Potter
 * @since 22/04/2014
 */
class TablePanel extends JPanel {

    /**
     * The JTable that will hold the records.
     */
    public static JTable hotelRoomTable;
    /**
     * Concrete subclass of AbstractTableModel.
     */
    public static HotelRoomTableModel hotelRoomTableModel;
    private final JPanel tablePanel;

    /**
     * The constructor for the TablePanel. It populates its JTable with all the
     * records in the database.
     *
     * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to use
     *                          for retrieving the data for the GUI.
     */
    public TablePanel(DatabaseAccessDao databaseAccessDao) {

        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setName("Records");
        hotelRoomTableModel = new HotelRoomTableModel(
                databaseAccessDao.retrieveAllHotelRooms());
        hotelRoomTable = new JTable(hotelRoomTableModel);
        hotelRoomTable.setPreferredScrollableViewportSize(
                UrlyBirdApplicationGuiConstants.CLIENT_GUI_JTABLE_DIMENSION);
        tablePanel.add(new JScrollPane(hotelRoomTable));
    }

    /**
     * Refreshes the JTable with the current status of the HotelRoomTableModel.
     */
    public static void refreshHotelRoomTableModel() {
        hotelRoomTable.setModel(hotelRoomTableModel);
    }

    /**
     * Returns the JPanel instance.
     *
     * @return the JPanel instance.
     */
    public JPanel getTablePanel() {
        return tablePanel;
    }
}