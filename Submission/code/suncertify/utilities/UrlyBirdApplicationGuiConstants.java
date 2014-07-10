package suncertify.utilities;

import java.awt.*;

/**
 * Holds the constants for the GUI classes.
 *
 * @author Luke GJ Potter
 * @since 26/04/2014
 */
public class UrlyBirdApplicationGuiConstants {

    // ---------- String Constants ----------
    // ----- Client GUI -----
    /**
     * The title of the Client GUI.
     */
    public static final String CLIENT_GUI_APPLICATION_TITLE =
            "URLyBird Application";
    /**
     * The text of the Book JButton.
     */
    public static final String BOOK_BUTTON = "Book";
    /**
     * The text of the Search JButton.
     */
    public static final String SEARCH_BUTTON = "Search";
    /**
     * The column names for the JTable in the Client GUI.
     */
    public static final String[] COLUMN_NAMES = {"Hotel Name", "Location",
            "Room Size", "Smoking", "Rate", "Date Available", "Customer ID"};
    /**
     * The string representation when a room allows smoking.
     */
    public static final String SMOKING_ALLOWED = "Allowed";
    /**
     * The string representation when a room disallows smoking.
     */
    public static final String SMOKING_NOT_ALLOWED = "Not Allowed";
    /**
     * The text that instructs how to book a room using the Client GUI.
     */
    public static final String BOOKING_HINT =
            "Select a record in the table and press the \"Book\" button to"
                    + " book that room.";
    /**
     * The label for the name search criteria.
     */
    public static final String SEARCH_PANEL_NAME_LABEL = "Name";
    /**
     * The label for the location search criteria.
     */
    public static final String SEARCH_PANEL_LOCATION_LABEL = "Location";
    // ----- Server GUI -----
    /**
     * The title of the Server GUI.
     */
    public static final String SERVER_GUI_APPLICATION_TITLE =
            "URLyBird Application Server";
    /**
     * The label for the database path JTextField on the Server GUI.
     */
    public static final String PATH_TO_DATABASE_FILE = "Path to Database File";
    /**
     * The label for the rmi hostname JTextField on the Server GUI.
     */
    public static final String RMI_HOSTNAME = "RMI Hostname";
    /**
     * The label for the rmi port JTextField on the Server GUI.
     */
    public static final String RMI_PORT_NUMBER = "RMI Port";
    /**
     * The text of the Start Server JButton on the Server GUI.
     */
    public static final String START_SERVER_BUTTON = "Start Server";
    /**
     * The label to be displayed, on the Server GUI, when the server is not
     * running.
     */
    public static final String SERVER_STOPPED = "Server Stopped";
    /**
     * The label to be displayed, on the Server GUI, when the server is
     * running.
     */
    public static final String SERVER_STARTED = "Server Running";

    // ---------- Dimension Constants ----------
    // ----- Client GUI -----
    /**
     * The length of the JTextFields in the Client GUI.
     */
    public static final int CLIENT_SEARCH_TEXT_FIELD_LENGTH = 20;
    /**
     * The dimensions of the Client GUI.
     */
    public static final Dimension CLIENT_GUI_DIMENSION =
            new Dimension(900, 600);
    /**
     * The dimension of the JTable in the Client GUI.
     */
    public static final Dimension CLIENT_GUI_JTABLE_DIMENSION =
            new Dimension(850, 500);
    // ----- Server GUI -----
    /**
     * The length of the JTextFields in the Server GUI.
     */
    public static final int SERVER_CONFIG_TEXT_FIELD_LENGTH = 20;
    /**
     * The dimension of the Server GUI.
     */
    public static final Dimension SERVER_GUI_DIMENSION =
            new Dimension(500, 200);
}
