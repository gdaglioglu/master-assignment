package suncertify.ui;

import suncertify.util.ApplicationMode;

public class Runner {

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Runner application = new Runner(args);
	}

	public Runner(String[] args) {
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
	}
}
