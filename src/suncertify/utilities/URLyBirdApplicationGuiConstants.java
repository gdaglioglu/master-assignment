package suncertify.utilities;

import java.awt.*;

/**
 * @author Luke GJ Potter
 *         Date: 26/04/2014
 */
public class URLyBirdApplicationGuiConstants {

    // ---------- String Constants ----------
    // The title of the application.
    public static final String APPLICATION_TITLE = "URLyBird Application";
    public static final String BOOK_BUTTON = "Book";
    public static final String CANCEL_BOOKING_BUTTON = "Cancel Booking";
    public static final String SEARCH_BUTTON = "Search";
    public static final String CREATE_BUTTON = "Create";
    public static final String UPDATE_BUTTON = "Update";
    public static final String DELETE_BUTTON = "Delete";
    public static final String XX_BUTTON = "XX";
    public static final String[] COLUMN_NAMES = {
            "Hotel Name",
            "Location",
            "Room Size",
            "Smoking",
            "Rate",
            "Date Available",
            "Customer ID"};
    public static final String SMOKING_ALLOWED = "Allowed";
    public static final String SMOKING_NOT_ALLOWED = "Not Allowed";


    // ---------- Dimension Constants ----------
    // The length of the JTextFields in the application.
    public static final int TEXT_FIELD_LENGTH = 20;
    public static final Dimension CLIENT_GUI_DIMENSION = new Dimension(900, 600);
    public static final Dimension CLIENT_GUI_JTABLE_DIMENSION = new Dimension(850, 500);
}
