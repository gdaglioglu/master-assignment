/*
 * 
 */
package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Server objects.
 */
public interface ServerFactory extends Remote {

	/**
	 * Gets the client.
	 *
	 * @return the client
	 * @throws RemoteException the remote exception
	 */
	public RemoteDBAccess getClient() throws RemoteException;
}
