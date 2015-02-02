package suncertify.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import suncertify.app.ApplicationRunner;

/**
 * All the items that belong within the main panel of the client
 * application. Having this here helps keep the code clean within the
 * body of the MainWindow constructor.
 */
public class HotelRoomsPanel extends JPanel {

	/**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
	private static final long serialVersionUID = 797317548930520661L;
    
    

	private GuiController controller;

    /**
     * The <code>JTable</code> that displays the Hotel room held by the system.
     */
    private JTable mainTable = new JTable();
    

   
    
    /**
     * The internal reference to the the currently displayed table data.
     */
    private HotelRoomModel tableData;
    
    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>suncertify.client.gui</code>.
     */
    private Logger logger = Logger.getLogger("suncertify.client.gui");



    /**
     * Constructs the main panel for the client application.
     * @param controller 
     */
    public HotelRoomsPanel(GuiController controller) {
    	this.controller = controller;
        this.setLayout(new BorderLayout());
        JScrollPane tableScroll = new JScrollPane(mainTable);
        tableScroll.setSize(500, 250);

        this.add(tableScroll, BorderLayout.CENTER);        
        
        this.add(new SearchPanel(mainTable, controller), BorderLayout.NORTH);
        
        // Setup rent and return buttons
        JButton rentButton = new JButton("Book Room");
        JButton returnButton = new JButton("Cancel Booking");

        // Add the action listeners to rent and return buttons
        rentButton.addActionListener(new BookHotelRoom());
        returnButton.addActionListener(new UnbookHotelRoom());
        // Set the rent and return buttons to refuse focus
        rentButton.setRequestFocusEnabled(false);
        returnButton.setRequestFocusEnabled(false);
        // Add the keystroke mnemonics
        rentButton.setMnemonic(KeyEvent.VK_R);
        returnButton.setMnemonic(KeyEvent.VK_U);
        // Create a panel to add the rental a remove buttons
        JPanel hiringPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        hiringPanel.add(rentButton);
        hiringPanel.add(returnButton);

        // bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
       // bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(hiringPanel, BorderLayout.SOUTH);

        // Add the bottom panel to the main window
        this.add(bottomPanel, BorderLayout.SOUTH);

        // Set table properties
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        mainTable.setToolTipText("Select a Hotel room record to book or cancel.");

        // Add Tool Tips
        returnButton.setToolTipText(
                    "Cancel the booking of the hotel room selected in the above table.");
        rentButton.setToolTipText(
                    "Book the hotel room selected in the above table.");
       
        // A full data set is returned from an empty search
        try {
            tableData = controller.getHotelRooms();
            setupTable();
        } catch (GuiControllerException gce) {
            ApplicationRunner.handleException(
                    "Failed to acquire an initial hotel room list."
                    + "\nPlease check the DB connection.");
        }

   }
    
    

    /**
     * Uses the <code>tableData</code> member to refresh the contents of the
     * <code>mainTable</code>. The method will attempt to preserve all previous
     * selections and contents displayed.
     */
    private void setupTable() {
        // Preserve the previous selection
        int index = mainTable.getSelectedRow();
        String prevSelected = (index >= 0)
                            ? (String) mainTable.getValueAt(index, 0)
                            : "";

        // Reset the table data
        this.mainTable.setModel(this.tableData);

        // Reselect the previous item if it still exists
        for (int i = 0; i < this.mainTable.getRowCount(); i++) {
            String selected = (String) mainTable.getValueAt(i, 0);
            if (selected.equals(prevSelected)) {
                this.mainTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    
    /**
     * Handles all Hotel room booking events.
     *
     * @author Gokhan Daglioglu
     */
    private class BookHotelRoom implements ActionListener {
        private static final String ALREADY_BOOKED_MSG
                = "Unable to book - room is already booked.";
		

        /**
         * Handles the actionPerformed event for the book button.
         *
         * @param actionEvent The event initiated by the book button.
         */
        public void actionPerformed(ActionEvent actionEvent) {
            String hotelRoomRecordNo = "";
            int index = mainTable.getSelectedRow();
            if ((index >= 0) && (index <= mainTable.getColumnCount())) {
                hotelRoomRecordNo = (String) mainTable.getValueAt(index, 0);
                try {
                    boolean booked = controller.rent(hotelRoomRecordNo);
                    if (booked == false) {
                       ApplicationRunner.handleException(ALREADY_BOOKED_MSG);
                    }
                   // tableData = controller.find(previousCriteria);
                    setupTable();
                } catch (GuiControllerException gce) {
                    ApplicationRunner.handleException("Rent operation failed.");
                }
            }
        }
    }

    /**
     * Handles all Hotel room cancellation events.
     *
     * @author Gokhan Daglioglu
     */
    private class UnbookHotelRoom implements ActionListener {
        private static final String CANCELLATION_FAILURE_MSG
                = "Unable to cancel - unknown cancellation failure.";

        /**
         * Handles the actionPerformed event for the cancel button.
         *
         * @param actionEvent The event initiated by the return button.
         */
        public void actionPerformed(ActionEvent actionEvent) {
            String hotelRoomRecordNo = "";
            int index = mainTable.getSelectedRow();
            if ((index >= 0) && (index <= mainTable.getColumnCount())) {
                hotelRoomRecordNo = (String) mainTable.getValueAt(index, 0);
                try {
                    controller.returnRental(hotelRoomRecordNo);
                    setupTable();
                } catch (GuiControllerException gce) {
                    ApplicationRunner.handleException(CANCELLATION_FAILURE_MSG);
                }
            }
        }
    }

   

}