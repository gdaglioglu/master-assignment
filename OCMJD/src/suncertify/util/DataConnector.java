/*
 * 
 */
package suncertify.util;

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.logging.Logger;

import suncertify.db.DBAccess;
import suncertify.db.Data;
import suncertify.remote.RemoteDBAccess;
import suncertify.remote.ServerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class DataConnector.
 */
public class DataConnector {

	/** The log. */
	private static Logger log = Logger.getLogger("suncertify.ui");

	/**
	 * Instantiates a new data connector.
	 */
	private DataConnector() {
		DataConnector.log.entering("suncertify.util.DataConnector",
				"DataConnector()");

		DataConnector.log.exiting("suncertify.util.DataConnector",
				"DataConnector()");
	}

	/**
	 * Gets the local.
	 *
	 * @param dbLocation the db location
	 * @return the local
	 */
	public static DBAccess getLocal(final String dbLocation) {
		return new Data(dbLocation);
	}

	/**
	 * Gets the remote.
	 *
	 * @param host the host
	 * @param port the port
	 * @return the remote
	 * @throws RemoteException the remote exception
	 */
	public static RemoteDBAccess getRemote(final String host, final String port)
			throws RemoteException {
		DataConnector.log.entering("suncertify.util.DataConnector",
				"getRemote()");

		final String url = "rmi://" + host + ":" + port + "/"
				+ ApplicationConstants.RMI_SERVER_IDENTIFIER;

		try {
			final ServerFactory factory = (ServerFactory) Naming.lookup(url);

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
