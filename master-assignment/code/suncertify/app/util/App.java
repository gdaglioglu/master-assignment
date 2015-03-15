package suncertify.app.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class is a utility class for the overall application. Providing
 * convenience methods for log, error/exception management as well as printing
 * usage details.
 *
 * @author Gokhan Daglioglu
 */
public class App {

	/**
	 * Loads the <code>LookAndFeel</code> depending on the system which the
	 * application is running on.
	 * 
	 */
	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException uex) {
			logWarning("Unsupported look and feel specified");
		} catch (ClassNotFoundException cex) {
			logWarning("Look and feel could not be located");
		} catch (InstantiationException iex) {
			logWarning("Look and feel could not be instantiated");
		} catch (IllegalAccessException iaex) {
			logWarning("Look and feel cannot be used on this platform");
		}
	}

	/**
	 * Logs a warning message to the application log. Uses the
	 * {@link Level#WARNING} as it's logging level.
	 * 
	 * @param warningMessage
	 *            The message to log.
	 */
	private static void logWarning(final String warningMessage) {
		log(warningMessage, Level.WARNING);
	}

	/**
	 * Logs a message with the specified log severity to the application log.
	 * 
	 * @param message
	 *            The message to log.
	 * @param level
	 *            The logging level to use.
	 */
	private static void log(final String message, final Level level) {
		Logger.getLogger(App.class.getPackage().getName()).log(level, message);
	}

	/**
	 * This method will display a usage message on the command line and exit
	 * with an error.
	 */
	public static void printUsage() {
		System.out.println("Usage: java -jar <path_and_filename> [mode]");
		System.out.println("Mode can be one of,");
		System.out.println("\\tserver:\\tTo start networked server.");
		System.out.println("\\alone:\\tTo start unnetworked standalone application.");
		System.out.println("\\No mode:\\tTo start networked client.");
		System.exit(1);
	}

	/**
	 * Prompts the user with an error message in an alert window whilst logging
	 * a warning message.
	 *
	 * @param warningMessage
	 *            The message displayed and logged.
	 */
	public static void showWarning(String warningMessage) {
		logWarning(warningMessage);
		showWarningDialog(warningMessage);
	}

	/**
	 * Prompts the user with an error message in an alert window.
	 *
	 * @param warningMessage
	 *            The message displayed in the error window.
	 */
	private static void showWarningDialog(String warningMessage) {
		JOptionPane alert = new JOptionPane(warningMessage, JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION);
		JDialog dialog = alert.createDialog(null, "Alert");
		dialog.setLocation(getCenterOnScreen(dialog));
		dialog.setVisible(true);
	}

	/**
	 * Returns the center <code>Point</code> of the screen depending on the size
	 * of the given component.
	 *
	 * @param component
	 *            The graphical object representation that can be displayed on
	 *            the screen.
	 * @return the center <code>Point</code> of the screen.
	 */
	public static Point getCenterOnScreen(Component component) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - component.getWidth()) / 2);
		int y = (int) ((d.getHeight() - component.getHeight()) / 2);
		return new Point(x, y);
	}

	/**
	 * Shows an error dialog to the user whilst logging a warning message.
	 * 
	 * @param errorMessage
	 *            The message displayed and logged.
	 */
	public static void showError(final String errorMessage) {
		logWarning(errorMessage);
		JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Similar to {@link #showError(String)}, shows an error dialog to the user.
	 * Once the error dialog is dismissed, this method will then exit the
	 * application with an error code of 2.
	 * 
	 * @param errorAndExitMessage
	 *            The message you wish to display before exiting.
	 */
	public static void showErrorAndExit(final String errorAndExitMessage) {
		logError(errorAndExitMessage);
		JOptionPane.showMessageDialog(null, errorAndExitMessage + "\nApplication will now exit!",
				"Fatal error", JOptionPane.ERROR_MESSAGE);
		System.exit(2);
	}

	/**
	 * Logs an application error to the application log. Uses the
	 * {@link Level#SEVERE} as it's logging level.
	 * 
	 * @param errorMessage
	 *            The message to log.
	 */
	private static void logError(final String errorMessage) {
		log(errorMessage, Level.SEVERE);
	}

	/**
	 * Prompts the user with an error message in an alert window whilst logging
	 * an error message.
	 *
	 * @param exceptionMessage
	 *            The message displayed and logged.
	 */
	public static void handleException(String exceptionMessage) {
		logError(exceptionMessage);
		showWarningDialog(exceptionMessage);
	}
}