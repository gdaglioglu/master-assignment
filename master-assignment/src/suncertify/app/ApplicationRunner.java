package suncertify.app;

import static suncertify.client.ui.ApplicationMode.SERVER;
import static suncertify.client.ui.ApplicationMode.STANDALONE_CLIENT;
import suncertify.client.ui.ClientRunner;
import suncertify.client.ui.ServerRunner;

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
		// args = new String[] { "alone" };
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
			new ClientRunner();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			new ServerRunner(STANDALONE_CLIENT);
		} else if (args.length == 1 && "server".equals(args[0])) {
			new ServerRunner(SERVER);
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