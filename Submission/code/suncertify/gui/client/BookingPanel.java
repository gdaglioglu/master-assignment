package suncertify.gui.client;

import suncertify.controller.DatabaseAccessDao;
import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The JPanel that displays and handles the resources for booking a Hotel Room.
 *
 * @author Luke GJ Potter
 * @since 22/04/2014
 */
class BookingPanel extends JPanel {

    private final JPanel bookingPanel;

    /**
     * The constructor for the BookingPanel, it initialises the GUI components
     * used for booking a Hotel Room.
     *
     * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to use
     *                          for booking a room using the GUI.
     * @param csrNumber         The CSR Number of the GUI Operator.
     */
    public BookingPanel(DatabaseAccessDao databaseAccessDao, String csrNumber) {

        bookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bookingPanel.setName("Booking");

        JLabel instructionLabel = new JLabel(
                UrlyBirdApplicationGuiConstants.BOOKING_HINT);
        bookingPanel.add(instructionLabel);

        JButton bookButton = new JButton(
                UrlyBirdApplicationGuiConstants.BOOK_BUTTON);
        bookButton.addActionListener(
                new BookHotelRoom(databaseAccessDao, csrNumber));
        bookingPanel.add(bookButton);
    }

    /**
     * Returns the JPanel instance.
     *
     * @return the JPanel instance.
     */
    public JPanel getBookingPanel() {
        return bookingPanel;
    }

    /**
     * The ActionListener for the Book JButton.
     */
    private class BookHotelRoom implements ActionListener {

        private final DatabaseAccessDao databaseAccessDao;
        private final String csrNumber;
        private String startDate, endDate;

        /**
         * The constructor for the BookHotelRoom class.
         *
         * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to
         *                          use for creating a booking using the GUI.
         * @param csrNumber         The CSR Number of the GUI Operator.
         */
        public BookHotelRoom(DatabaseAccessDao databaseAccessDao,
                             String csrNumber) {
            this.databaseAccessDao = databaseAccessDao;
            this.csrNumber = csrNumber;
            startDate = UrlyBirdApplicationConstants.EMPTY_STRING;
            endDate = UrlyBirdApplicationConstants.EMPTY_STRING;
        }

        /**
         * The handler for the ActionListener for booking a Hotel Room. It takes
         * the room to be booked, the CSR number, and the end date of the
         * booking. It updates the database with this information. It then
         * refreshes the HotelRoomTableModel with the new booking.
         *
         * @param actionEvent The event performed.
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            long recordRow = TablePanel.hotelRoomTable.getSelectedRow();

            if (recordRow < 0) {
                JOptionPane.showMessageDialog(bookingPanel,
                        "Please select a row");

            } else {
                /*
                 * Ensure the the recordRow matches the correct position in the
                 * database of the selected record. This is so it is possible to
                 * book a record when viewing a search result.
                 */
                recordRow = databaseAccessDao.getRecordPositionInDatabase(
                        (String) TablePanel.hotelRoomTable.getValueAt(
                                (int) recordRow, 0),
                        (String) TablePanel.hotelRoomTable.getValueAt(
                                (int) recordRow, 1));

                try {
                    if (areStartDateAndEndDateOfBookingSet()) {

                        databaseAccessDao.bookHotelRoom(recordRow, csrNumber,
                                endDate, Thread.currentThread().getId());
                    }
                } catch (Exception e) {

                    System.err.println(
                            "There was an error when booking record number "
                                    + recordRow);
                    e.printStackTrace();

                } finally {
                    TablePanel.hotelRoomTableModel = new HotelRoomTableModel(
                            databaseAccessDao.retrieveAllHotelRooms());
                    TablePanel.refreshHotelRoomTableModel();

                    endDate = startDate =
                            UrlyBirdApplicationConstants.EMPTY_STRING;
                }
            }
        }

        private boolean areStartDateAndEndDateOfBookingSet() {

            String[] choices = {"Today", "Tomorrow"};
            startDate = (String) JOptionPane.showInputDialog(bookingPanel,
                    "Choose booking start date.", "Booking Start Date",
                    JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

            try {
                if (startDate.equals(
                        UrlyBirdApplicationConstants.EMPTY_STRING)) {
                    return false;
                }

                while (!isValidEndDate()) {
                    endDate = JOptionPane.showInputDialog(bookingPanel,
                            "Enter the date to end the booking ("
                                    + UrlyBirdApplicationConstants.DATE_FORMAT
                                    + ")");

                    if (endDate.equals(
                            UrlyBirdApplicationConstants.EMPTY_STRING)) {
                        return false;
                    }
                }

                return true;
            } catch (NullPointerException ignored) {
                return false;
            }
        }

        private boolean isValidEndDate() {

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        UrlyBirdApplicationConstants.DATE_FORMAT);
                simpleDateFormat.setLenient(false);
                simpleDateFormat.parse(endDate);

                return true;

            } catch (NullPointerException | ParseException ignored) {
                return false;
            }
        }
    }
}
