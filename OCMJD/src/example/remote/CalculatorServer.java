package example.remote;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.util.ApplicationConstants;
import suncertify.util.PropertyFileManager;

public class CalculatorServer {

	public CalculatorServer() {
		PropertyFileManager properties = PropertyFileManager.getInstance();
		int port = Integer.parseInt(properties
				.getProperty(ApplicationConstants.KEY_PROPERTY_NETWORK_PORT));

		try {
			String name = "Calculator";
			Calculator c = new CalculatorImpl();

			UnicastRemoteObject.unexportObject(c, false);
			Calculator stub = (Calculator) UnicastRemoteObject.exportObject(c,
					port);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind(name, stub);
			System.out.println("CalculatorServer bound");
		} catch (Exception e) {
			System.err.println("CalculatorServer exception:");
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new CalculatorServer();
	}
}