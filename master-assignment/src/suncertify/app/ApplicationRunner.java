package suncertify.app;

import static suncertify.client.gui.ApplicationMode.NETWORK_CLIENT;
import static suncertify.client.gui.ApplicationMode.STANDALONE_CLIENT;
import suncertify.client.gui.HotelRoomController;
import suncertify.client.gui.ServerWindow;

/**
 * The URLyBird application loader - a facade to the two modes the application
 * can run in. This class will check the command line arguments and then call
 * the classes to start the application in the correct mode.
 *
 * @author Gokhan Daglioglu
 */
public class ApplicationRunner {

	/**
	 * The method that launches the URLyBird application.
	 *
	 * @param args
	 *            Holds the command line inputs.
	 */
	public static void main(String[] args) {
		// args = new String[] { "server" };
		args = new String[] {};
		new ApplicationRunner(args);
	}

	/**
	 * Sets the default Swing look and feel, then instantiates the main
	 * application window.
	 *
	 * @param args
	 *            The command line arguments, which may be one of "alone",
	 *            "server" or no argument.
	 */
	public ApplicationRunner(String[] args) {

		App.setLookAndFeel();

		if (args.length == 0) {
			final HotelRoomController hotelRoomController = new HotelRoomController(
					NETWORK_CLIENT);
			hotelRoomController.init();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			final HotelRoomController hotelRoomController = new HotelRoomController(
					STANDALONE_CLIENT);
			hotelRoomController.init();
		} else if (args.length == 1 && "server".equals(args[0])) {
			new ServerWindow();
		} else {
			System.err
					.println("Command line options are case sensitive and may be only one of:");
			System.err
					.println("\"\"       - (no command line option): starts networked client");
			System.err.println("\"alone\"  - starts standalone client");
			System.err.println("\"server\" - starts server");
		}

	}
}