package suncertify;

import java.util.logging.*;

import suncertify.ui.Controller;
import suncertify.ui.Server;
import suncertify.util.ApplicationMode;

public class Runner {

	private static Logger log = Logger.getLogger("suncertify");
	private static ConsoleHandler handler = new ConsoleHandler();

	public static void main(String[] args) {
		log.setLevel(Level.ALL);
		handler.setLevel(Level.ALL);
		log.addHandler(handler);

		@SuppressWarnings("unused")
		Runner application = new Runner(args);
	}

	public Runner(String[] args) {
		log.entering("suncertify.ui.Runner", "Runner()", args);

		if (args.length == 0) {
			Controller controller = new Controller(
					ApplicationMode.NETWORKED_CLIENT);
			controller.control();
		} else if ((args.length == 1) && "alone".equals(args[0])) {
			Controller controller = new Controller(
					ApplicationMode.STANDALONE_CLIENT);
			controller.control();
		} else if ((args.length == 1) && "server".equals(args[0])) {
			Server server = new Server();
			server.startServer();
		} else {
			System.err
					.println("Command line options are case sensitive and may be only one of:");
			System.err
					.println("\"\"       - (no command line option): starts networked client");
			System.err.println("\"alone\"  - starts standalone client");
			System.err.println("\"server\" - starts server");
		}
		log.exiting("suncertify.ui.Runner", "Runner()");
	}
}
