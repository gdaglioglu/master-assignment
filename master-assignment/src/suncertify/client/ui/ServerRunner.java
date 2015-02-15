package suncertify.client.ui;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * Dialog box to get configuration options for client application. This class
 * provides a standard dialog box which allows the user to select the location
 * of the database (which may be a physical file in local mode, or the address
 * (and, optionally, the port) of the server. The user can, of course, cancel,
 * in which case the application should not start (this is at the applications
 * discretion though - the business logic could be changed later in the calling
 * class to decide to start the application anyway if there configuration info
 * can be loaded from file).<br/>
 */
public class ServerRunner {

	private Logger logger = Logger.getLogger("suncertify.client.gui");

	/**
	 * Creates a dialog where the user can specify the location of the
	 * database,including the type of network connection (if this is a networked
	 * client)and IP address and port number; or search and select the database
	 * on a local drive if this is a standalone client.
	 *
	 * @param parent
	 *            Defines the Component that is to be the parent of this dialog
	 *            box. For information on how this is used, see
	 *            <code>JOptionPane</code>
	 * @param applicationMode
	 *            Specifies the type of connection (standalone or networked)
	 * @see JOptionPane
	 */
	public ServerRunner(ApplicationMode applicationMode) {

		switch (applicationMode) {
		case STANDALONE_CLIENT:
			new StandAloneServer();
			break;

		case SERVER:
			new RmiServer();
			break;

		case NETWORK_CLIENT:
			new Client();
			break;
		default:
			break;
		}

	}

}