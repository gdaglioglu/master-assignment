/*
 * DataConnector
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.util;

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.logging.Logger;

import suncertify.db.DBAccess;
import suncertify.db.Data;
import suncertify.remote.RemoteDBAccess;
import suncertify.remote.RemoteServerFactory;

/**
 * This class is used when the GUI wants to create a connection to a database.
 * This utility class provides connections for both local and remote databases
 * 
 * @author Eoin Mooney
 */
public class DataConnector {

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.util</code>
	 */
	private static Logger log = Logger.getLogger("suncertify.util");

	/**
	 * This is a utility class and its methods are accessed statically, so there
	 * is no need for anyone to call its constructor
	 */
	private DataConnector() {
	}

	/**
	 * Gets a connection to a local database
	 * 
	 * @param dbLocation
	 *            The location of the datafile
	 * @return A local <code>Data</code> instance that implements
	 *         <code>DBAccess</code>
	 */
	public static DBAccess getLocal(final String dbLocation) {
		return new Data(dbLocation);
	}

	/**
	 * Gets a connection to a remote database in the form of a remote object
	 * implementing the <code>RemoteDBAccess</code> interface
	 * 
	 * @param host
	 *            The host where the RMI Server is running
	 * @param port
	 *            The port on which the RMI registry is running
	 * @return A remote instance of the <code>RemoteDBAccess</code> interface
	 * @throws RemoteException
	 *             Signals that a remote instance of the
	 *             <code>RemoteDBAccess</code> interface cannot be created
	 */
	public static RemoteDBAccess getRemote(final String host, final String port)
			throws RemoteException {
		DataConnector.log.entering("suncertify.util.DataConnector",
				"getRemote()");

		final String url = "rmi://" + host + ":" + port + "/"
				+ ApplicationConstants.RMI_SERVER_IDENTIFIER;

		try {
			final RemoteServerFactory factory = (RemoteServerFactory) Naming.lookup(url);

			DataConnector.log.exiting("suncertify.util.DataConnector",
					"getRemote()");

			return factory.getClient();
		} catch (final NotBoundException nbe) {
			throw new RemoteException(
					ApplicationConstants.RMI_SERVER_IDENTIFIER
							+ " not registered: ", nbe);
		} catch (final MalformedURLException murle) {
			throw new RemoteException("Cannot connect to " + host, murle);
		}

	}
}
