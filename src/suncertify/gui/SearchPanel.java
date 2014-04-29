package suncertify.gui;

import suncertify.controller.DatabaseAccessDaoImpl;
import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
class SearchPanel extends JPanel {

    private final JPanel searchPanel;
    private final JLabel nameLabel;
    private final JTextField nameField;
    private final JLabel locationLabel;
    private final JTextField locationField;

    public SearchPanel() {

        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setName("Search");

        nameLabel = new JLabel("Name");
        searchPanel.add(nameLabel);
        nameField = new JTextField(URLyBirdApplicationGuiConstants.TEXT_FIELD_LENGTH);
        searchPanel.add(nameField);

        locationLabel = new JLabel("Location");
        searchPanel.add(locationLabel);
        locationField = new JTextField(URLyBirdApplicationGuiConstants.TEXT_FIELD_LENGTH);
        searchPanel.add(locationField);

        JButton searchButton = new JButton(" Search ");
        searchButton.addActionListener(new SearchForRoomActionListener());
        searchPanel.add(searchButton);
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    private class SearchForRoomActionListener implements ActionListener {

        /**
         * The handler for the ActionListener for Searching For a Hotel Room.
         *
         * @param actionEvent - the event performed.
         */
        @Override public void actionPerformed(ActionEvent actionEvent) {

            TablePanel.hotelRoomTableModel = new HotelRoomTableModel(
                    new DatabaseAccessDaoImpl().findHotelRooms(
                            nameField.getText().trim(),
                            locationField.getText().trim()));

            TablePanel.refreshHotelRoomTableModel();
        }
    }
}
