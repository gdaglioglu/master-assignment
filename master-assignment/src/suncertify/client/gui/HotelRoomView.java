package suncertify.client.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import java.util.regex.*;

import javax.swing.*;

import suncertify.app.ApplicationRunner;

/**
 * The main application window of the URLyBird client application.
 *
 * @author Gokhan Daglioglu
 */
public class HotelRoomView extends JFrame {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.<p>
     *
     * Not that we ever serialize this class of course, but JFrame implements
     * Serializable, so therefore by default we do as well.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * The internal reference to the GUI controller.
     */
    private GuiController controller;

    /**
     * The <code>JTable</code> that displays the Hotel room held by the system.
     */
    private JTable mainTable = new JTable();

    /**
     * The text field that contains the user defined name String.
     */
    private JTextField nameSearchField = new JTextField(15);

    /**
     * The text field that contains the user defined location String.
     */
    private JTextField locationSearchField = new JTextField(15);
    /**
     * The text field that contains the user defined size String.
     */
    private JTextField sizeSearchField = new JTextField(15);
    /**
     * The text field that contains the user defined smoking String.
     */
    private JTextField smokingSearchField = new JTextField(15);
    /**
     * The text field that contains the user defined rate String.
     */
    private JTextField rateSearchField = new JTextField(15);
    /**
     * The text field that contains the user defined date String.
     */
    private JTextField dateSearchField = new JTextField(15);
    /**
     * The text field that contains the user defined owner String.
     */
    private JTextField ownerSearchField = new JTextField(15);

    
    /**
     * The internal reference to the the currently displayed table data.
     */
    private HotelRoomModel tableData;

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>suncertify.client.gui</code>.
     */
    private Logger log = Logger.getLogger("suncertify.client.gui");

    /**
     * Builds and displays the main application window. The constructor begins
     * by building the connection selection dialog box. After the user selects
     * a connection type, the method creates a <code>HotelRoomMainWindow</code>
     * instance.
     *
     * @param args an argument specifying whether we are starting a networked
     * client (argument missing) or a standalone client (argument = "alone").
     */
    public HotelRoomView(String[] args) {
        super("URLyBird Discounted Hotel Rooms");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ApplicationMode connectionType = (args.length == 0)
                                       ? ApplicationMode.NETWORK_CLIENT
                                       : ApplicationMode.STANDALONE_CLIENT;

        // find out where our database is
        DatabaseLocationDialog dbLocation =
                new DatabaseLocationDialog(this, connectionType);

        if (dbLocation.userCanceled()) {
        	System.exit(0);
        }

        try {
            controller = new GuiController(dbLocation.getNetworkType(), dbLocation.getLocation(),
                                           dbLocation.getPort());
        } catch (GuiControllerException gce) {
            ApplicationRunner.handleException(
                    "Failed to connect to the database");
        }

        // Add the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new QuitApplication());
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);
        fileMenu.add(quitMenuItem);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);

        // A full data set is returned from an empty search
        try {
            tableData = controller.getHotelRooms();
            setupTable();
        } catch (GuiControllerException gce) {
            ApplicationRunner.handleException(
                    "Failed to acquire an initial hotel room list."
                    + "\nPlease check the DB connection.");
        }

        this.add(new HotelRoomsPanel());

        this.pack();
        this.setSize(1000, 500);

        // Center on screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        this.setVisible(true);
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
     * All the items that belong within the main panel of the client
     * application. Having this here helps keep the code clean within the
     * body of the MainWindow constructor.
     */
    private class HotelRoomsPanel extends JPanel {
        /**
         * A version number for this class so that serialization can occur
         * without worrying about the underlying class changing between
         * serialization and deserialization.
         */
        private static final long serialVersionUID = 5165L;

        /**
         * Constructs the main panel for the client application.
         */
        public HotelRoomsPanel() {
            this.setLayout(new BorderLayout());
            JScrollPane tableScroll = new JScrollPane(mainTable);
            tableScroll.setSize(500, 250);

            this.add(tableScroll, BorderLayout.CENTER);

            // Set up the search pane
            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(new SearchHotelRoom());
            searchButton.setMnemonic(KeyEvent.VK_S);
            // Search panel
            JPanel searchPanel = new JPanel(new GridLayout(2, 8, 10, 5));
            searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
            final JLabel nameLabel = new JLabel("Hotel name:");
            final JLabel locationLabel = new JLabel("Hotel location:");
            final JLabel sizeLabel = new JLabel("Room size:");
            final JLabel smokingLabel = new JLabel("Smoking state:");
            final JLabel rateLabel = new JLabel("Room rate:");
            final JLabel dateLabel = new JLabel("Date:");
            final JLabel ownerLabel = new JLabel("Owner:");
            
            searchPanel.add(nameLabel);
            searchPanel.add(locationLabel);
            searchPanel.add(sizeLabel);
            searchPanel.add(smokingLabel);
            searchPanel.add(rateLabel);
            searchPanel.add(dateLabel);
            searchPanel.add(ownerLabel);            
            
            searchPanel.add(searchButton);
            
            searchPanel.add(nameSearchField);
            searchPanel.add(locationSearchField);
            searchPanel.add(sizeSearchField);
            searchPanel.add(smokingSearchField);
            searchPanel.add(rateSearchField);
            searchPanel.add(dateSearchField);
            searchPanel.add(ownerSearchField);
            
            
            this.add(searchPanel, BorderLayout.NORTH);
            
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
            nameSearchField.setToolTipText(
                        "Enter hotel name to search.");
            locationSearchField.setToolTipText(
                    "Enter hotel location to search.");
            sizeSearchField.setToolTipText(
                    "Enter room size to search.");
            smokingSearchField.setToolTipText(
                    "Enter smoking state to search.");
            rateSearchField.setToolTipText(
                    "Enter room rate to search.");
            dateSearchField.setToolTipText(
                    "Enter date to search.");
            ownerSearchField.setToolTipText(
                    "Enter owner name to search.");
            searchButton.setToolTipText("Submit the Hotel room search.");

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

    /**
     * Handles all DVD search events.
     *
     * @author Gokhan Daglioglu
     */
    private class SearchHotelRoom implements ActionListener {

        /**
         * Handles the actionPerformed event for the search button. Uses the
         * String values of the <code>searchField</code> member.
         *
         * @param ae The event initiated by the search button.
         */
        public void actionPerformed(ActionEvent ae) {
        	String[] criteria = new String[7];
            criteria[0] = nameSearchField.getText().trim();
            criteria[1] = locationSearchField.getText().trim();
            criteria[2] = sizeSearchField.getText().trim();
            criteria[3] = smokingSearchField.getText().trim();
            criteria[4] = rateSearchField.getText().trim();
            criteria[5] = dateSearchField.getText().trim();
            criteria[6] = ownerSearchField.getText().trim();
            try {
                tableData = controller.find(criteria);
                setupTable();
            } catch (GuiControllerException gce) {
                // Inspect the exception chain
                Throwable rootException = gce.getCause();
                String msg = "Search operation failed.";
                // If a syntax error occurred, get the message
                if (rootException instanceof PatternSyntaxException) {
                    msg += ("\n" + rootException.getMessage());
                }
                ApplicationRunner.handleException(msg);
            }
        }
    }

    /**
     * Handles all application quit events.
     *
     * @author Gokhan Daglioglu
     */
    private class QuitApplication implements ActionListener {

        /**
         * Quits the application when invoked.
         *
         * @param ae The event triggering the quit operation.
         */
        public void actionPerformed(ActionEvent ae) {
            System.exit(0);
        }
    }
}
