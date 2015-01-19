/*
 * ServerFactory
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A factory for creating Remote Server objects.
 * 
 * @author Eoin Mooney
 */
public interface RemoteServerFactory extends Remote {

	/**
	 * Creates an instance of <code>RemoteDBAccess</code> to allow for remote
	 * interaction with a datafile
	 * 
	 * @return An instance of the <code>RemoteDBAccess</code> interface
	 * @throws RemoteException
	 *             the remote exception
	 */
	public RemoteDBAccess getClient() throws RemoteException;
}
