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
 * @since  22/04/2014
 */
class BookingPanel extends JPanel {

    private final JPanel bookingPanel;

    /**
     * The constructor for the BookingPanel, it initialises the GUI components
     * used for booking a Hotel Room.
     *
     * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to use
     *                          for booking a room using the GUI.
     * @param csrNumber The CSR Number of the GUI Operator.
     */
    public BookingPanel(DatabaseAccessDao databaseAccessDao, String csrNumber) {

        bookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bookingPanel.setName("Booking");

        JLabel instructionLabel = new JLabel(UrlyBirdApplicationGuiConstants.BOOKING_HINT);
        bookingPanel.add(instructionLabel);

        JButton bookButton = new JButton(UrlyBirdApplicationGuiConstants.BOOK_BUTTON);
        bookButton.addActionListener(new BookHotelRoom(databaseAccessDao, csrNumber));
        bookingPanel.add(bookButton);

        JButton cancelBookingButton = new JButton(UrlyBirdApplicationGuiConstants.CANCEL_BOOKING_BUTTON);
        cancelBookingButton.addActionListener(new CancelHotelRoomBooking(databaseAccessDao, csrNumber));
        bookingPanel.add(cancelBookingButton);
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
         * @param csrNumber The CSR Number of the GUI Operator.
         */
        public BookHotelRoom(DatabaseAccessDao databaseAccessDao, String csrNumber) {
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
        @Override public void actionPerformed(ActionEvent actionEvent) {

            int recordRow = TablePanel.hotelRoomTable.getSelectedRow();

            if (recordRow < 0) {
                JOptionPane.showMessageDialog(bookingPanel, "Please select a row");

            } else {
                try {
                    if (areStartDateAndEndDateOfBookingSet()) {

                        databaseAccessDao.bookHotelRoom(recordRow, csrNumber, endDate, Thread.currentThread().getId());

                        TablePanel.hotelRoomTableModel = new HotelRoomTableModel(databaseAccessDao.retrieveAllHotelRooms());
                        TablePanel.refreshHotelRoomTableModel();

                        endDate = startDate = UrlyBirdApplicationConstants.EMPTY_STRING;
                    }
                } catch(Exception e) {
                    System.out.println("Reserve room problem found: " + e.getMessage());
                }
            }
        }

        private boolean areStartDateAndEndDateOfBookingSet() {

            String[] choices = {"Today", "Tomorrow"};
            startDate = (String) JOptionPane.showInputDialog(bookingPanel, "Choose booking start date.", "Booking Start Date", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

            if (startDate.equals(UrlyBirdApplicationConstants.EMPTY_STRING)) return false;

            while (! isValidEndDate()) {
                endDate = JOptionPane.showInputDialog(bookingPanel, "Enter the date to end the booking (" + UrlyBirdApplicationConstants.DATE_FORMAT + ")");
            }

            return true;
        }

        private boolean isValidEndDate() {

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(UrlyBirdApplicationConstants.DATE_FORMAT);
                simpleDateFormat.setLenient(false);
                simpleDateFormat.parse(endDate);

                return true;

            } catch (ParseException ignored) { return false; }
        }
    }

    /**
     * The ActionListener for the Cancel Booking JButton.
     */
    private class CancelHotelRoomBooking implements ActionListener {

        private final DatabaseAccessDao databaseAccessDao;
        // Todo: Use CSR Number to only allow that CSR to cancel a booking.
        private final String csrNumber;

        /**
         * The constructor for the CancelHotelRoomBooking class.
         *
         * @param databaseAccessDao The Client's {@code DatabaseAccessDao} to
         *                          use for canceling a booking using the GUI.
         * @param csrNumber The CSR Number of the GUI Operator.
         */
        public CancelHotelRoomBooking(DatabaseAccessDao databaseAccessDao, String csrNumber) {
            this.databaseAccessDao = databaseAccessDao;
            this.csrNumber = csrNumber;
        }

        /**
         * The handler for the ActionListener for canceling a booking of a Hotel
         * Room. It updates the database to cancel the booking. Then it
         * refreshes the HotelRoomTableModel to show that the room is no longer
         * booked.
         *
         * @param actionEvent The event performed.
         */
        @Override public void actionPerformed(ActionEvent actionEvent) {

            try {
                int recordRow = TablePanel.hotelRoomTable.getSelectedRow();
                databaseAccessDao.cancelHotelRoomBooking(recordRow, Thread.currentThread().getId());
                TablePanel.hotelRoomTableModel = new HotelRoomTableModel(databaseAccessDao.retrieveAllHotelRooms());
                TablePanel.refreshHotelRoomTableModel();
            } catch(Exception e) {
                System.out.println("Reserve room problem found: " + e.getMessage());
            }
        }
    }
}
