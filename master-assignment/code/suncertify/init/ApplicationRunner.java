package suncertify.init;

import static suncertify.app.util.App.printUsage;
import static suncertify.app.util.App.setLookAndFeel;

import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.app.NetworkedApplication;
import suncertify.app.NetworkedClientApplication;
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
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.init.ApplicationRunner</code>.
	 */
	private Logger logger = Logger.getLogger(ApplicationRunner.class.getName());

	/**
	 * The method that launches the URLyBird application.
	 *
	 * @param args
	 *            Holds the command line inputs.
	 */
	public static void main(String[] args) {
		new ApplicationRunner(new String[] { "alone" });
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
			logger.log(Level.INFO, "Starting URLybird in Networked Client Mode");
			new NetworkedClientApplication().init();
		} else if (args.length == 1 && "alone".equals(args[0])) {
			logger.log(Level.INFO, "Starting URLybird in Stand Alone Mode");
			new StandAloneApplication().init();
		} else if (args.length == 1 && "server".equals(args[0])) {
			logger.log(Level.INFO, "Starting URLybird in Networked Server Mode");
			new NetworkedApplication().init();
		} else {
			printUsage();
		}

	}
}