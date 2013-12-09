package suncertify.remote;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyFileManager;

public class ServerRegistry {

	private static Logger log = Logger.getLogger("suncertify.remote");

	private ServerRegistry() {
		log.entering("suncertify.remote.ServerRegistry", "ServerRegistry()");
		log.exiting("suncertify.remote.ServerRegistry", "ServerRegistry()");
	}

	public static void register() throws RemoteException,
			java.net.MalformedURLException {
		log.entering("suncertify.remote.ServerRegistry", "register()");

		PropertyFileManager properties = PropertyFileManager.getInstance();

		String dbLocation = properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH);
		int port = Integer.parseInt(properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));

		register(dbLocation, port);

		log.exiting("suncertify.remote.ServerRegistry", "register()");
	}

	public static void register(String dbLocation, int port)
			throws RemoteException, java.net.MalformedURLException {
		log.entering("suncertify.remote.ServerRegistry", "register()");

		java.rmi.registry.LocateRegistry.createRegistry(port);

		ServerFactoryImpl server = new ServerFactoryImpl(dbLocation);

		Naming.rebind(ApplicationConstants.RMI_SERVER_IDENTIFIER, server);

		log.exiting("suncertify.remote.ServerRegistry", "register()");
	}

}
