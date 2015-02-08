package suncertify.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import suncertify.app.ApplicationRunner;

/**
 * The graphical user interface the user sees when they start the server
 * application. This class provides the structure necessary for displaying
 * configurable objects, for starting the server, and for exiting the server in
 * a safe manner.
 */
public class ServerWindow extends JFrame implements Observer {
	// All strings are defined in static final declarations at the start of the
	// class. This will make localisation easier later (if we want to add it).
	private static final String START_BUTTON_TEXT = "Start server";
	private static final String SHUTDOWN_BUTTON_TEXT = "Shutdown";
	private static final String SHUTDOWN_BUTTON_TOOL_TIP = "Stops the server as soon as it is safe to do so";
	private static final String INITIAL_STATUS = "Enter configuration parameters and click \""
			+ START_BUTTON_TEXT + "\"";

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 *
	 * Not that we ever serialize this class of course, but JFrame implements
	 * Serializable, so therefore by default we do as well.
	 */
	private static final long serialVersionUID = 5165L;

	// All user modifiable fields are defined here, along with all buttons.
	// This makes it easy to disable the fields and buttons once the
	// configuration is complete and the server starts.
	private ConfigOptions configOptionsPanel = new ConfigOptions(
			ApplicationMode.SERVER);
	private JButton startServerButton = new JButton(START_BUTTON_TEXT);
	private JButton shutDownButton = new JButton(SHUTDOWN_BUTTON_TEXT);
	private JLabel status = new JLabel();

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.client.gui</code>.
	 */
	private Logger log = Logger.getLogger("suncertify.client.gui");

	/**
	 * Constructs the standard Server GUI Window and displays it on screen.
	 */
	public ServerWindow() {
		super("URLyBird Discounted Hotel Rooms - Server Application");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		// Add the menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(quitMenuItem);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);

		configOptionsPanel.getObservable().addObserver(this);
		this.add(configOptionsPanel, BorderLayout.NORTH);
		this.add(commandOptionsPanel(), BorderLayout.CENTER);

		status.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.add(status, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);

		// load saved configuration
		SavedConfiguration config = SavedConfiguration.getSavedConfiguration();

		// there may not be a default database location, so we had better
		// validate before using the returned value.
		String databaseLocation = config
				.getParameter(SavedConfiguration.DATABASE_LOCATION);
		configOptionsPanel.setLocationFieldText((databaseLocation == null) ? ""
				: databaseLocation);

		// there is always at least a default port number, so we don't have to
		// validate this.
		configOptionsPanel.setPortNumberText(config
				.getParameter(SavedConfiguration.SERVER_PORT));

		status.setText(INITIAL_STATUS);

		this.pack();
		this.setLocation(ApplicationRunner.getCenterOnScreen(this));
		this.setVisible(true);
	}

	/**
	 * This private panel provides the buttons the user may click upon in order
	 * to do a major action (start the server or exit).
	 *
	 * @return a panel containing the mode change buttons.
	 */
	private JPanel commandOptionsPanel() {
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// this is one way to add an action listener - have a class (even a
		// private class) implement ActionListener, then create a new instance
		// of it - this can make the code very clean if you have many operations
		// to perform in your action listener.
		startServerButton.addActionListener(new StartServer());
		// startServerButton.setEnabled(false);
		thePanel.add(startServerButton);

		// this is another option for adding an action listener - create an
		// anonymous ActionListener. This has the advantage of having the code
		// that is run right with the button - it is obvious what is going to
		// happen if someone clicks the exitButton.
		shutDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		shutDownButton.setToolTipText(SHUTDOWN_BUTTON_TOOL_TIP);
		thePanel.add(shutDownButton);

		return thePanel;
	}

	/**
	 * A private class that will get called if the "Start" button is called. It
	 * will disable any buttons that should no longer be clicked upon, extract
	 * the configuration information from the server application, saving it
	 * where necessary, and start the server.
	 * <p>
	 *
	 * Note 1: If this was an MVC, this reaction to an event would be in the
	 * controller.
	 * <p>
	 *
	 * Note 2: There are a lot of hard coded widgets disabled here - this could
	 * potentially cause problems if a widget is added somewhere else in the
	 * class, and the programmer forgets to disable it here. Options to work
	 * around this include: creating a collection that widgets could be added to
	 * (then at this point we just iterate through the collection, disabling
	 * widgets), or using reflection to programmatically find all widgets
	 * (possibly excluding specific widgets) and disable them all. However this
	 * is getting a bit beyond scope for this application.
	 */
	private class StartServer implements ActionListener {
		/** {@inheritDoc} */
		@Override
		public void actionPerformed(ActionEvent ae) {
			configOptionsPanel.setLocationFieldEnabled(false);
			configOptionsPanel.setPortNumberEnabled(false);
			configOptionsPanel.setBrowseButtonEnabled(false);
			configOptionsPanel.setRmiOptionEnabled(false);

			startServerButton.setEnabled(false);

			String port = configOptionsPanel.getPortNumberText();
			String databaseLocation = configOptionsPanel.getLocationFieldText();

			new NetworkStarterRmi(databaseLocation, port, status);
		}

	}

	/**
	 * Called whenever the user changes something in our common connectivity
	 * panel. Using this allows us to build the common panel without it needing
	 * to know how it will be used in this frame - it only needs to update any
	 * interested observers who can then decide for themselves what to do with
	 * the information.
	 *
	 * @param o
	 *            the <code>Observable</code> object that is calling this
	 *            method.
	 * @param arg
	 *            the information that we need to be updated with - in this case
	 *            information on what network choice was made.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// we are going to ignore the Observable object, since we are only
		// observing one object. All we are interested in is the argument.

		if (!(arg instanceof OptionUpdate)) {
			return;
		}

		OptionUpdate optionUpdate = (OptionUpdate) arg;

		switch (optionUpdate.getUpdateType()) {
		default:
			log.warning("Observed unknown update type "
					+ optionUpdate.getUpdateType());
			break;
		}
	}
}
