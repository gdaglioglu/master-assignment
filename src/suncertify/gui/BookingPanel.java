package suncertify.gui;

import suncertify.controller.DatabaseAccessDaoImpl;
import suncertify.utilities.URLyBirdApplicationGuiConstants;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Luke GJ Potter
 * Date: 22/04/2014
 */
class BookingPanel extends JPanel {

    JPanel bookingPanel;

    public BookingPanel() {

        bookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bookingPanel.setName("Booking");

        JLabel instructionLabel = new JLabel("Select a record in the table and press 'Book Room' button to book the Hotel Room");
        bookingPanel.add(instructionLabel);

        JButton bookButton = new JButton(URLyBirdApplicationGuiConstants.BOOK_BUTTON);
        bookButton.addActionListener(new BookHotelRoom());
        bookingPanel.add(bookButton);

        JButton cancelBookingButton = new JButton(URLyBirdApplicationGuiConstants.CANCEL_BOOKING_BUTTON);
        cancelBookingButton.addActionListener(new CancelHotelRoomBooking());
        bookingPanel.add(cancelBookingButton);
    }

    public JPanel getBookingPanel() {
        return bookingPanel;
    }

    private class BookHotelRoom implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int recordRow = TablePanel.hotelRoomTable.getSelectedRow();
            String csrNumber = "";
            boolean csrFlag = false;

            if (recordRow < 0) {

                JOptionPane.showMessageDialog(bookingPanel, "Please select a row");
                csrFlag = false;

            } else {

                csrNumber = JOptionPane.showInputDialog(bookingPanel, "Enter CSR number of 8 digits");

                if (csrNumber == null) {

                    csrFlag = false;

                } else if (csrNumber.length() != 8) {

                    JOptionPane.showMessageDialog(bookingPanel, "CSR length must be 8 digits, you have entered " + csrNumber.length() + " characters.");
                    csrFlag = false;

                } else if (!isAllDigits(csrNumber)) {

                    JOptionPane.showMessageDialog(bookingPanel, "CSR length must be all digits");
                    csrFlag = false;

                } else {

                    csrFlag = true;

                }
            }

            if (csrFlag == true) {
                try {
                    new DatabaseAccessDaoImpl().bookHotelRoom(recordRow, csrNumber, Thread.currentThread().getId());
                    TablePanel.hotelRoomTableModel = new HotelRoomTableModel(new DatabaseAccessDaoImpl().retrieveAllHotelRooms());
                    TablePanel.refreshHotelRoomTableModel();
                } catch(Exception e) {
                    System.out.println("Reserve room problem found: " + e.getMessage());
                }
            }
        }
    }

    private class CancelHotelRoomBooking implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                int recordRow = TablePanel.hotelRoomTable.getSelectedRow();
                new DatabaseAccessDaoImpl().cancelHotelRoomBooking(recordRow, Thread.currentThread().getId());
                TablePanel.hotelRoomTableModel = new HotelRoomTableModel(new DatabaseAccessDaoImpl().retrieveAllHotelRooms());
                TablePanel.refreshHotelRoomTableModel();
            } catch(Exception e) {
                System.out.println("Reserve room problem found: " + e.getMessage());
            }
        }
    }

    private boolean isAllDigits(String csrNumber) {

        try {
            Integer.parseInt(csrNumber);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
