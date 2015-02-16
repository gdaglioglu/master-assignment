package suncertify.init;

import static suncertify.shared.App.printUsage;
import static suncertify.shared.App.setLookAndFeel;
import suncertify.app.NetworkApplication;
import suncertify.app.NetworkClientApplication;
import suncertify.app.StandAloneApplication;

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

		setLookAndFeel();

		if (args.length == 0) {
			new NetworkClientApplication().launch();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			new StandAloneApplication().launch();

		} else if (args.length == 1 && "server".equals(args[0])) {
			new NetworkApplication().launch();
		} else {
			printUsage();
		}

	}
}