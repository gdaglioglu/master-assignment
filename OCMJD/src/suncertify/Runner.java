/*
 * 
 */
package suncertify;

import java.util.logging.*;

import suncertify.ui.ClientController;
import suncertify.ui.Server;
import suncertify.util.ApplicationMode;

// TODO: Auto-generated Javadoc
/**
 * The Class Runner.
 */
public class Runner {

	/** The log. */
	private static Logger log = Logger.getLogger("suncertify");

	/** The handler. */
	private static ConsoleHandler handler = new ConsoleHandler();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		Runner.log.setLevel(Level.ALL);
		Runner.handler.setLevel(Level.ALL);
		Runner.log.addHandler(Runner.handler);

		new Runner(args);
	}

	/**
	 * Instantiates a new runner.
	 *
	 * @param args the args
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
