/*
 * Runner
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify;

import suncertify.ui.ClientController;
import suncertify.ui.Server;
import suncertify.util.ApplicationMode;

/**
 * This class is the entry point for the application.
 * 
 * @author Eoin Mooney
 */
public class Runner {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		new Runner(args);
	}

	/**
	 * Instantiates a new runner and starts the application based on the
	 * arguments it receives
	 * 
	 * @param args
	 *            This application will only accept "", "alone" or "server" as
	 *            valid arguments
	 */
	public Runner(final String[] args) {
		if (args.length == 0) {
			final ClientController controller = new ClientController(
					ApplicationMode.NETWORKED_CLIENT);
			controller.control();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			final ClientController controller = new ClientController(
					ApplicationMode.STANDALONE_CLIENT);
			controller.control();
		} else if (args.length == 1 && "server".equals(args[0])) {
			final Server server = new Server();
			server.startServer();
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
