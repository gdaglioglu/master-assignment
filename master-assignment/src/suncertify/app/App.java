package suncertify.app;

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

public class App {

	/**
	 * Prompts the user with an error message in an alert window.
	 *
	 * @param msg
	 *            The message displayed in the error window.
	 */
	public static void showWarning(String msg) {
		logWarning(msg);
		JOptionPane alert = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE,
				JOptionPane.DEFAULT_OPTION);
		JDialog dialog = alert.createDialog(null, "Alert");

		dialog.setLocation(getCenterOnScreen(dialog));

		dialog.setVisible(true);
	}

	/**
	
	 * 
	 */
	static void setLookAndFeel() {
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
	 * Prompts the user with an error message in an alert window.
	 *
	 * @param msg
	 *            The message displayed in the error window.
	 */
	public static void handleException(String msg) {
		logError(msg);
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

	/**
	 * This is a convenience method to log a warning message to the application
	 * log. It uses the {@link Level#WARNING} as it's logging level.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public static void logWarning(final String msg) {
		log(msg, Level.WARNING);
	}

	/**
	 * This is a convenience method to log an application error to the
	 * application log. It uses the {@link Level#SEVERE} as it's logging level.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public static void logError(final String msg) {
		log(msg, Level.SEVERE);
	}

	/**
	 * This is a convenience method to log a message with the specified log
	 * severity to the application log.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param lvl
	 *            The logging level to use.
	 */
	public static void log(final String msg, final Level lvl) {
		Logger.getLogger("suncertify.app").log(lvl, msg);
	}

}
