package suncertify;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.ui.Controller;
import suncertify.ui.Server;
import suncertify.util.ApplicationMode;

public class Runner {

	private static Logger log = Logger.getLogger("suncertify");
	private static ConsoleHandler handler = new ConsoleHandler();

	public static void main(final String[] args) {
		Runner.log.setLevel(Level.ALL);
		Runner.handler.setLevel(Level.ALL);
		Runner.log.addHandler(Runner.handler);

		@SuppressWarnings("unused")
		final Runner application = new Runner(args);
	}

	public Runner(final String[] args) {
		if (args.length == 0) {
			final Controller controller = new Controller(
					ApplicationMode.NETWORKED_CLIENT);
			controller.control();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			final Controller controller = new Controller(
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
