package suncertify.remote;

import java.rmi.Naming;
import java.rmi.RemoteException;

import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyManager;

public class ServerRegistry {

	private ServerRegistry() {
		
	}
	
	public static void register() throws RemoteException, java.net.MalformedURLException {
		PropertyManager properties = PropertyManager.getInstance();
		
		String dbLocation = properties.getProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH);
		int port = Integer.parseInt(properties.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));
		
		register(dbLocation, port);
	}
	
	public static void register(String dbLocation, int port) throws RemoteException, java.net.MalformedURLException {
			
		java.rmi.registry.LocateRegistry.createRegistry(port);
		
		RemoteData server = new RemoteData(dbLocation);
		
		Naming.rebind(ApplicationConstants.RMI_SERVER_IDENTIFIER, server);
	}
	
}
