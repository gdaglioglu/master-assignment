package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The JPanel that displays and handles the resources for searching the database
 * in the URLyBird Application.
 *
 * @author Luke GJ Potter
 * @since 22/04/2014
 */
class SearchPanel extends JPanel {

    private final JPanel searchPanel;
    private final JTextField nameField;
    private final JTextField locationField;

    /**
     * The constructor for the SearchPanel. It initialises the JPanel for
     * managing the searching on the Name and Location fields in the database.
     *
     * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to use
     *                          for searching the data for the GUI.
     */
    public SearchPanel(DatabaseAccessDao databaseAccessDao) {

        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setName("Search");

        JLabel nameLabel = new JLabel(UrlyBirdApplicationGuiConstants.SEARCH_PANEL_NAME_LABEL);
        searchPanel.add(nameLabel);
        nameField = new JTextField(UrlyBirdApplicationGuiConstants.CLIENT_SEARCH_TEXT_FIELD_LENGTH);
        searchPanel.add(nameField);

        JLabel locationLabel = new JLabel(UrlyBirdApplicationGuiConstants.SEARCH_PANEL_LOCATION_LABEL);
        searchPanel.add(locationLabel);
        locationField = new JTextField(UrlyBirdApplicationGuiConstants.CLIENT_SEARCH_TEXT_FIELD_LENGTH);
        searchPanel.add(locationField);

        JButton searchButton = new JButton(UrlyBirdApplicationGuiConstants.SEARCH_BUTTON);
        searchButton.addActionListener(new SearchForRoomActionListener(databaseAccessDao));
        searchPanel.add(searchButton);
    }

    /**
     * Returns the JPanel instance.
     *
     * @return the JPanel instance.
     */
    public JPanel getSearchPanel() {
        return searchPanel;
    }

    /**
     * The ActionListener for the Search JButton.
     */
    private class SearchForRoomActionListener implements ActionListener {

        private final DatabaseAccessDao databaseAccessDao;

        /**
         * The constructor for the SearchForRoomActionListener class.
         *
         * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to
         *                          use for searching the data for the GUI.
         */
        public SearchForRoomActionListener(DatabaseAccessDao databaseAccessDao) {
            this.databaseAccessDao = databaseAccessDao;
        }

        /**
         * The handler for the ActionListener for searching for a Hotel Room. It
         * uses the {@code DatabaseAccessDao} to search the database with the
         * text in the Name and Location JTextFields. It then refreshes the
         * {@code HotelRoomTableModel}.
         *
         * @param actionEvent The event performed.
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            TablePanel.hotelRoomTableModel = new HotelRoomTableModel(
                    databaseAccessDao.findHotelRooms(
                            nameField.getText().trim(),
                            locationField.getText().trim()));

            TablePanel.refreshHotelRoomTableModel();
        }
    }
}
