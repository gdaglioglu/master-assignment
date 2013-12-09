package suncertify.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class ServerFactoryImpl extends UnicastRemoteObject implements
		ServerFactory {

	// TODO serialVersionUID
	private static final long serialVersionUID = 1006816376037422397L;

	private static String dbLocation = null;

	private Logger log = Logger.getLogger("suncertify.remote");

	public ServerFactoryImpl(String dbLocation) throws RemoteException {
		log.entering("suncertify.remote.ServerFactoryImpl",
				"ServerFactoryImpl()", dbLocation);

		ServerFactoryImpl.dbLocation = dbLocation;

		log.exiting("suncertify.remote.ServerFactoryImpl",
				"ServerFactoryImpl()");
	}

	public RemoteDBAccess getClient() throws RemoteException {
		log.entering("suncertify.remote.ServerFactoryImpl", "getClient()");
		log.exiting("suncertify.remote.ServerFactoryImpl", "getClient()");

		return new RemoteData(dbLocation);
	}
}
