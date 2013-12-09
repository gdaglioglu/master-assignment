package suncertify.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerFactoryImpl extends UnicastRemoteObject implements ServerFactory {
	
	// TODO serialVersionUID
	private static final long serialVersionUID = 1006816376037422397L;
	
	private static String dbLocation = null;
	
	public ServerFactoryImpl(String dbLocation) throws RemoteException {
		ServerFactoryImpl.dbLocation = dbLocation;
	}
	
	public RemoteDBAccess getClient() throws RemoteException {
		return new RemoteData(dbLocation);
	}
}
