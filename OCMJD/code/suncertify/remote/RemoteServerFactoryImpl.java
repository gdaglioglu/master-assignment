/*
 * ServerFactoryImpl
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * This class implements the <code>RemoteServerFactory</code> interface and is
 * used to provide the client with a <code>RemoteData</code> instance that can
 * be used to remotely interact with a datafile
 * 
 * @author Eoin Mooney
 */
public class RemoteServerFactoryImpl extends UnicastRemoteObject implements
		RemoteServerFactory {

	/**
	 * A version number for this class to support serialization and
	 * de-serialization.
	 */
	private static final long serialVersionUID = 3574077733831896020L;

	/** The location of the datafile that is to be accessed remotely */
	private static String dbLocation = null;

	/**
	 * Creates an instance of the <code>RemoteServerFactory</code> and sets the
	 * location of the datafile
	 * 
	 * @param dbLocation
	 *            The location of the datafile
	 * @throws RemoteException
	 *             the remote exception
	 */
	public RemoteServerFactoryImpl(final String dbLocation)
			throws RemoteException {
		RemoteServerFactoryImpl.dbLocation = dbLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see suncertify.remote.ServerFactory#getClient()
	 */
	@Override
	public RemoteDBAccess getClient() throws RemoteException {
		return new RemoteData(RemoteServerFactoryImpl.dbLocation);
	}
}
