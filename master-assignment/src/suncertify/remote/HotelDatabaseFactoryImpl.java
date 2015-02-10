package suncertify.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The implementation of our remote factory for client connectivity.
 */
class HotelDatabaseFactoryImpl extends UnicastRemoteObject implements
		HotelDatabaseFactory {
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 5165L;

	/**
	 * The physical location of the database.
	 */
	private static String dbLocation = null;

	/**
	 * Creates an instance of this factory, specifying where the database can be
	 * found.
	 *
	 * @param dbLocation
	 *            the location of the database.
	 * @throws RemoteException
	 *             Thrown if a <code>HotelDatabaseImpl</code> instance cannot be
	 *             created.
	 */
	public HotelDatabaseFactoryImpl(String dbLocation) throws RemoteException {
		HotelDatabaseFactoryImpl.dbLocation = dbLocation;
	}

	/** {@inheritDoc} */
	@Override
	public HotelDatabaseRemote getClient() throws RemoteException {
		return new HotelDatabaseRemoteImpl(dbLocation);
	}
}
