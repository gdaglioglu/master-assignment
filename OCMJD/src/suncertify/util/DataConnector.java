package suncertify.util;

import java.io.IOException;
import java.rmi.*;
import java.util.logging.Logger;

import suncertify.db.DBAccess;
import suncertify.db.Data;
import suncertify.remote.RemoteDBAccess;
import suncertify.remote.ServerFactory;

public class DataConnector {

	private static Logger log = Logger.getLogger("suncertify.ui");

	private DataConnector() {
		log.entering("suncertify.util.DataConnector", "DataConnector()");

		log.exiting("suncertify.util.DataConnector", "DataConnector()");
	}

	public static DBAccess getLocal(String dbLocation) throws IOException,
			ClassNotFoundException {
		log.entering("suncertify.util.DataConnector", "getLocal()", dbLocation);
		log.exiting("suncertify.util.DataConnector", "getLocal()");

		return new Data(dbLocation);
	}

	public static RemoteDBAccess getRemote(String host, String port)
			throws RemoteException {
		log.entering("suncertify.util.DataConnector", "getRemote()");

		String url = "rmi://" + host + ":" + port + "/"
				+ ApplicationConstants.RMI_SERVER_IDENTIFIER;

		try {
			ServerFactory factory = (ServerFactory) Naming.lookup(url);

			log.exiting("suncertify.util.DataConnector", "getRemote()");

			return factory.getClient();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			throw new RemoteException(
					ApplicationConstants.RMI_SERVER_IDENTIFIER
							+ " not registered: ", e);
		} catch (java.net.MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RemoteException("cannot connect to " + host, e);
		}

	}
}
