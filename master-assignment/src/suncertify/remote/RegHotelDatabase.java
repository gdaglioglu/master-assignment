package suncertify.remote;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * RegDvdDatabase starts the rmi registry on the client machine. Registers the
 * DvdDatabase object for the RMI naming service.
 *
 * @author Gokhan Daglioglu
 */
public class RegHotelDatabase {
	/**
	 * Since this is a utility class (it only exists for other classes to call
	 * it's static methods), lets stop users creating unneeded instances of this
	 * class by creating a private constructor.
	 */
	private RegHotelDatabase() {
	}

	/**
	 * Creates the HotelDatabase class and binds it to the name "HotelDatabase".
	 *
	 * @throws RemoteException
	 *             on network error.
	 * @throws MalformedURLException
	 */
	public static void register() throws RemoteException {
		register(
				"C:\\Users\\gdaglioglu\\Git\\master-assignment\\src\\suncertify\\db\\db-1x3.db",
				java.rmi.registry.Registry.REGISTRY_PORT);
	}

	/**
	 * Creates the HotelDatabase class and binds it to the name "HotelDatabase".
	 *
	 * @param dbLocation
	 *            the location of the data file on disk.
	 * @param rmiPort
	 *            the port the RMI Registry will listen on.
	 * @throws RemoteException
	 *             on network error.
	 */
	public static void register(String dbLocation, int rmiPort)
			throws RemoteException {
		Registry r = java.rmi.registry.LocateRegistry.createRegistry(rmiPort);

		// make a hotel database instance on a random port and register
		// our service name and our port number on the RMI registry.
		r.rebind("HotelMediator", new HotelDatabaseFactoryImpl(dbLocation));
	}

	/**
	 * Simple entry point so that the RMI server can be started manually for
	 * testing purposes.
	 *
	 * @param args
	 *            the command line arguments which will be ignored.
	 * @throws RemoteException
	 *             on network error.
	 */
	public static void main(String[] args) throws RemoteException {
		register();
	}
}
