package suncertify.app;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.*;

import javax.swing.*;

import suncertify.client.gui.MainWindow;

/**
 * The URLyBird application loader - a facade to the two modes the
 * application can run in. This class will check the command line arguments
 * and then call the classes to start the application in the correct mode.
 *
 * @author Gokhan Daglioglu
 * @version 1.0
 */
public class ApplicationRunner {
    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>suncertify.app</code>.
     */
    private Logger log = Logger.getLogger("suncertify.app");

    /**
     * The method that launches the URLyBird application.
     *
     * @param args Holds the command line inputs
     */
    public static void main(String[] args) {
    	args = new String[]{"alone"};
        ApplicationRunner app = new ApplicationRunner(args);
    }

    /**
     * Sets the default Swing look and feel, then instantiates the main
     * application window.
     *
     * @param args the command line arguments, which may be one of "alone",
     * "server" or no argument.
     */
    public ApplicationRunner(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException uex) {
            log.warning("Unsupported look and feel specified");
        } catch (ClassNotFoundException cex) {
            log.warning("Look and feel could not be located");
        } catch (InstantiationException iex) {
            log.warning("Look and feel could not be instantiated");
        } catch (IllegalAccessException iaex) {
            log.warning("Look and feel cannot be used on this platform");
        }

        if (args.length == 0 || "alone".equalsIgnoreCase(args[0])) {
            // Create an instance of the main application window
            new MainWindow(args);
        } else if ("server".equalsIgnoreCase(args[0])) {
            new ServerWindow();
        } else {
            log.info("Invalid parameter passed in startup: " + args[0]);
            // Logging may be turned off, or may be going to a file, so
            // send usage information to the error output (usually the screen).
            System.err.println("Command line options may be one of:");
            System.err.println("\"server\" - starts server application");
            System.err.println("\"alone\"  - starts non-networked client");
            System.err.println("\"\"       - (no command line option): "
                               + "networked client will start");
        }
    }

    /**
     * Prompts the user with an error message in an alert window.
     *
     * @param msg The message displayed in the error window.
     */
    public static void handleException(String msg) {
        JOptionPane alert = new JOptionPane(msg,
                                            JOptionPane.ERROR_MESSAGE,
                                            JOptionPane.DEFAULT_OPTION);
        JDialog dialog = alert.createDialog(null, "Alert");

        // Center on screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - dialog.getWidth()) / 2);
        int y = (int) ((d.getHeight() - dialog.getHeight()) / 2);
        dialog.setLocation(x, y);

        dialog.setVisible(true);
    }
}
