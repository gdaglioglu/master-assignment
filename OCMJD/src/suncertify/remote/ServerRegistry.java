/*
 * 
 */
package suncertify.remote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyFileManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerRegistry.
 */
public class ServerRegistry {

	/** The log. */
	private static Logger log = Logger.getLogger("suncertify.remote");

	/**
	 * Instantiates a new server registry.
	 */
	private ServerRegistry() {
	}

	/**
	 * Register.
	 *
	 * @throws RemoteException the remote exception
	 * @throws MalformedURLException the malformed url exception
	 */
	public static void register() throws RemoteException,
			java.net.MalformedURLException {
		ServerRegistry.log.entering("suncertify.remote.ServerRegistry",
				"register()");

		final PropertyFileManager properties = PropertyFileManager
				.getInstance();

		final String dbLocation = properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH);
		final int port = Integer.parseInt(properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));

		ServerRegistry.register(dbLocation, port);

		ServerRegistry.log.exiting("suncertify.remote.ServerRegistry",
				"register()");
	}

	/**
	 * Register.
	 *
	 * @param dbLocation the db location
	 * @param port the port
	 * @throws RemoteException the remote exception
	 * @throws MalformedURLException the malformed url exception
	 */
	public static void register(final String dbLocation, final int port)
			throws RemoteException, java.net.MalformedURLException {
		ServerRegistry.log.entering("suncertify.remote.ServerRegistry",
				"register()");

		java.rmi.registry.LocateRegistry.createRegistry(port);

		final ServerFactoryImpl server = new ServerFactoryImpl(dbLocation);

		Naming.rebind(ApplicationConstants.RMI_SERVER_IDENTIFIER, server);

		ServerRegistry.log.exiting("suncertify.remote.ServerRegistry",
				"register()");
	}

}
