package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class HotelRoomsMenuBar extends JMenuBar {

	private static final long serialVersionUID = -81759777197357075L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.ui</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.ui");

	public HotelRoomsMenuBar() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitApplication());
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		fileMenu.add(quitMenuItem);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		this.add(fileMenu);
		logger.log(Level.FINE, "Initialized Hotel Room Menu Bar");
	}

	/**
	 * Handles all application quit events.
	 *
	 * @author Gokhan Daglioglu
	 */
	private class QuitApplication implements ActionListener {

		/**
		 * Quits the application when invoked.
		 *
		 * @param ae
		 *            The event triggering the quit operation.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			logger.log(Level.INFO, "Exiting Client Application");
			System.exit(0);
		}
	}
}