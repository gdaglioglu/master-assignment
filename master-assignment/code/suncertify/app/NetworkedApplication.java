package suncertify.app;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.app.ui.ServerUI;
import suncertify.app.util.App;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;

/**
 * This class is responsible for establishing a networked server, using RMI in
 * this case.
 * 
 * @author Gokhan Daglioglu
 */
public class NetworkedApplication implements Application {

	/**
	 * The name to associate with the remote reference.
	 */
	public static final String RMI_SERVER = "remote.database.server";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		new ServerUI(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch() {
		final DataService dataService = new DataServiceImpl();
		this.register(dataService);
	}

	/**
	 * This method is responsible for publishing the {@link DataService}
	 * interface via RMI for networked clients to connect to.
	 * 
	 * @param dataService
	 *            The remote server interface to be published.
	 */
	private void register(final DataService dataService) {
		try {
			final DataService rmiStub = (DataService) UnicastRemoteObject.exportObject(dataService,
					Registry.REGISTRY_PORT);
			final Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			registry.rebind(RMI_SERVER, rmiStub);
		} catch (final RemoteException e) {
			App.showErrorAndExit("Cannot publish the RMI server, check no other applications are using the default RMI port 1099.");
		}
	}
}