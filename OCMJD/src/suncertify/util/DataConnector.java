package suncertify.util;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import suncertify.db.DBAccess;
import suncertify.db.Data;
import suncertify.remote.RemoteDBAccess;
import suncertify.remote.ServerFactory;

public class DataConnector {
	
	private DataConnector() {
    }
	
    public static DBAccess getLocal(String dbLocation)
            throws IOException, ClassNotFoundException {
        return new Data(dbLocation);
    }
    
	public static RemoteDBAccess getRemote (String host, String port) throws RemoteException {
		String url = "rmi://" + host + ":" + port + "/" + ApplicationConstants.RMI_SERVER_IDENTIFIER;
		
		try {
			ServerFactory factory = (ServerFactory) Naming.lookup(url);
			return (RemoteDBAccess) factory.getClient();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			throw new RemoteException(ApplicationConstants.RMI_SERVER_IDENTIFIER + " not registered: ", e);
		} catch (java.net.MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RemoteException("cannot connect to " + host, e);
		}
				
	}
}
