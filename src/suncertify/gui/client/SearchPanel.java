package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
class SearchPanel extends JPanel {

    private final JPanel searchPanel;
    private final JTextField nameField;
    private final JTextField locationField;

    public SearchPanel(DatabaseAccessDao databaseAccessDao) {

        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setName("Search");

        JLabel nameLabel = new JLabel("Name");
        searchPanel.add(nameLabel);
        nameField = new JTextField(URLyBirdApplicationGuiConstants.CLIENT_SEARCH_TEXT_FIELD_LENGTH);
        searchPanel.add(nameField);

        JLabel locationLabel = new JLabel("Location");
        searchPanel.add(locationLabel);
        locationField = new JTextField(URLyBirdApplicationGuiConstants.CLIENT_SEARCH_TEXT_FIELD_LENGTH);
        searchPanel.add(locationField);

        JButton searchButton = new JButton(" Search ");
        searchButton.addActionListener(new SearchForRoomActionListener(databaseAccessDao));
        searchPanel.add(searchButton);
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    private class SearchForRoomActionListener implements ActionListener {

        private final DatabaseAccessDao databaseAccessDao;

        public SearchForRoomActionListener(DatabaseAccessDao databaseAccessDao) {
            this.databaseAccessDao = databaseAccessDao;
        }

        /**
         * The handler for the ActionListener for Searching For a Hotel Room.
         *
         * @param actionEvent - the event performed.
         */
        @Override public void actionPerformed(ActionEvent actionEvent) {

            TablePanel.hotelRoomTableModel = new HotelRoomTableModel(
                    databaseAccessDao.findHotelRooms(
                            nameField.getText().trim(),
                            locationField.getText().trim()));

            TablePanel.refreshHotelRoomTableModel();
        }
    }
}
