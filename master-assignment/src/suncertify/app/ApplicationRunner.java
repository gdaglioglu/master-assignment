package suncertify.app;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import suncertify.client.gui.HotelRoomController;

/**
 * The URLyBird application loader - a facade to the two modes the application
 * can run in. This class will check the command line arguments and then call
 * the classes to start the application in the correct mode.
 *
 * @author Gokhan Daglioglu
 * @version 1.0
 */
public class ApplicationRunner {
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.app</code>.
	 */
	private Logger log = Logger.getLogger("suncertify.app");

	/**
	 * The method that launches the URLyBird application.
	 *
	 * @param args
	 *            Holds the command line inputs
	 */
	public static void main(String[] args) {
		args = new String[] { "alone" };
		ApplicationRunner app = new ApplicationRunner(args);
	}

	/**
	 * Sets the default Swing look and feel, then instantiates the main
	 * application window.
	 *
	 * @param args
	 *            the command line arguments, which may be one of "alone",
	 *            "server" or no argument.
	 */
	public ApplicationRunner(String[] args) {

		setLookAndFeel();

		if (args.length == 0) {
			final HotelRoomController hotelRoomController = new HotelRoomController(
					suncertify.client.gui.ApplicationMode.NETWORK_CLIENT);
			hotelRoomController.init();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			final HotelRoomController hotelRoomController = new HotelRoomController(
					suncertify.client.gui.ApplicationMode.STANDALONE_CLIENT);
			hotelRoomController.init();
		} else if (args.length == 1 && "server".equals(args[0])) {
			final ServerWindow server = new ServerWindow();
			// server.startServer();
		} else {
			System.err
					.println("Command line options are case sensitive and may be only one of:");
			System.err
					.println("\"\"       - (no command line option): starts networked client");
			System.err.println("\"alone\"  - starts standalone client");
			System.err.println("\"server\" - starts server");
		}

	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException uex) {
			log.warning("Unsupported look and feel specified");
		} catch (ClassNotFoundException cex) {
			log.warning("Look and feel could not be located");
		} catch (InstantiationException iex) {
			log.warning("Look and feel could not be instantiated");
		} catch (IllegalAccessException iaex) {
			log.warning("Look and feel cannot be used on this platform");
		}
	}

	/**
	 * Prompts the user with an error message in an alert window.
	 *
	 * @param msg
	 *            The message displayed in the error window.
	 */
	public static void handleException(String msg) {
		JOptionPane alert = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION);
		JDialog dialog = alert.createDialog(null, "Alert");

		dialog.setLocation(getCenterOnScreen(dialog));

		dialog.setVisible(true);
	}

	public static Point getCenterOnScreen(Component component) {
		// Center on screen
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - component.getWidth()) / 2);
		int y = (int) ((d.getHeight() - component.getHeight()) / 2);
		return new Point(x, y);
	}

}
