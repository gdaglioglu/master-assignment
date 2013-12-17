/*
 * ServerRegistry
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.remote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyManager;

/**
 * This utility class starts the RMI Registry and binds the implementation of the
 * <code>RemoteServerFactory</code> to the naming service. This allows for a
 * client to lookup a <code>RemoteServerFactory</code> instance and use it to
 * get a <code>RemoteDBAccess</code> implementation that can be used to interact
 * with a datafile remotely
 * 
 * @author Eoin Mooney
 */
public class RemoteServerRegistry {

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.remote</code>
	 */
	private static Logger log = Logger.getLogger("suncertify.remote");

	/**
 	 * This is a utility class and its methods are accessed statically, so there
	 * is no need for anyone to call its constructor
	 */
	private RemoteServerRegistry() {
	}

	/**
	 * Calls <code>register()</code> with values from the properties file
	 * 
	 * @throws RemoteException
	 *             the remote exception
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
	public static void register() throws RemoteException,
			java.net.MalformedURLException {

		final PropertyManager properties = PropertyManager.getInstance();

		final String dbLocation = properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH);
		final int port = Integer.parseInt(properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));

		RemoteServerRegistry.register(dbLocation, port);
	}

	/**
	 * Starts the RMI registry, creates a <code>RemoteServerFactory</code> instance referencing a local datafile and binds it to the naming service
	 * 
	 * @param dbLocation
	 *            The location of the datafile
	 * @param port
	 *            The port the RMI registry will be created on
	 * @throws RemoteException
	 *             the remote exception
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
	public static void register(final String dbLocation, final int port)
			throws RemoteException, java.net.MalformedURLException {
		RemoteServerRegistry.log.entering("suncertify.remote.ServerRegistry",
				"register()");

		java.rmi.registry.LocateRegistry.createRegistry(port);

		final RemoteServerFactoryImpl server = new RemoteServerFactoryImpl(
				dbLocation);

		Naming.rebind(ApplicationConstants.RMI_SERVER_IDENTIFIER, server);

		RemoteServerRegistry.log.exiting("suncertify.remote.ServerRegistry",
				"register()");
	}

}
