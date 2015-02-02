package suncertify.client.gui;

import java.util.logging.*;

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
	
	private static final long serialVersionUID = 2886178206092565805L;

    /**
     * The internal reference to the GUI controller.
     */
    private GuiController controller;
    
    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>suncertify.client.gui</code>.
     */
    private Logger logger = Logger.getLogger("suncertify.client.gui");

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

        this.setJMenuBar(new HotelRoomsMenu());
        
        this.add(new HotelRoomsPanel(controller));
        
        this.pack();
        this.setSize(1000, 500);        
        this.setLocation(ApplicationRunner.getCenterOnScreen(this));
        this.setVisible(true);
    }


}
