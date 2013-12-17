/*
 * 
 */
package suncertify.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerFactoryImpl.
 */
public class ServerFactoryImpl extends UnicastRemoteObject implements
		ServerFactory {

	/** The db location. */
	private static String dbLocation = null;

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.remote");

	/**
	 * Instantiates a new server factory impl.
	 *
	 * @param dbLocation the db location
	 * @throws RemoteException the remote exception
	 */
	public ServerFactoryImpl(final String dbLocation) throws RemoteException {
		this.log.entering("suncertify.remote.ServerFactoryImpl",
				"ServerFactoryImpl()", dbLocation);

		ServerFactoryImpl.dbLocation = dbLocation;

		this.log.exiting("suncertify.remote.ServerFactoryImpl",
				"ServerFactoryImpl()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.ServerFactory#getClient()
	 */
	@Override
	public RemoteDBAccess getClient() throws RemoteException {
		this.log.entering("suncertify.remote.ServerFactoryImpl", "getClient()");
		this.log.exiting("suncertify.remote.ServerFactoryImpl", "getClient()");

		return new RemoteData(ServerFactoryImpl.dbLocation);
	}
}
