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
class BookingPanel extends JPanel {

    private final JPanel bookingPanel;

    public BookingPanel(DatabaseAccessDao databaseAccessDao, String csrNumber) {

        bookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bookingPanel.setName("Booking");

        JLabel instructionLabel = new JLabel(URLyBirdApplicationGuiConstants.BOOKING_HINT);
        bookingPanel.add(instructionLabel);

        JButton bookButton = new JButton(URLyBirdApplicationGuiConstants.BOOK_BUTTON);
        bookButton.addActionListener(new BookHotelRoom(databaseAccessDao, csrNumber));
        bookingPanel.add(bookButton);

        JButton cancelBookingButton = new JButton(URLyBirdApplicationGuiConstants.CANCEL_BOOKING_BUTTON);
        cancelBookingButton.addActionListener(new CancelHotelRoomBooking(databaseAccessDao, csrNumber));
        bookingPanel.add(cancelBookingButton);
    }

    public JPanel getBookingPanel() {
        return bookingPanel;
    }

    private class BookHotelRoom implements ActionListener {

        private final DatabaseAccessDao databaseAccessDao;
        private final String csrNumber;

        public BookHotelRoom(DatabaseAccessDao databaseAccessDao, String csrNumber) {
            this.databaseAccessDao = databaseAccessDao;
            this.csrNumber = csrNumber;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int recordRow = TablePanel.hotelRoomTable.getSelectedRow();

            if (recordRow < 0) {
                JOptionPane.showMessageDialog(bookingPanel, "Please select a row");

            } else {
                try {
                    databaseAccessDao.bookHotelRoom(recordRow, csrNumber, Thread.currentThread().getId());
                    TablePanel.hotelRoomTableModel = new HotelRoomTableModel(databaseAccessDao.retrieveAllHotelRooms());
                    TablePanel.refreshHotelRoomTableModel();
                } catch(Exception e) {
                    System.out.println("Reserve room problem found: " + e.getMessage());
                }
            }
        }
    }

    private class CancelHotelRoomBooking implements ActionListener {

        private final DatabaseAccessDao databaseAccessDao;
        // Todo: Use CSR Number to only allow that CSR to cancel a booking.
        private final String csrNumber;

        public CancelHotelRoomBooking(DatabaseAccessDao databaseAccessDao, String csrNumber) {
            this.databaseAccessDao = databaseAccessDao;
            this.csrNumber = csrNumber;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

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
