package suncertify.rmi;

import suncertify.utilities.UrlyBirdApplicationConstants;
import suncertify.utilities.UrlyBirdApplicationObjectsFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * This class manages the lifecycle of the RMI Server for the URLyBird
 * Application.
 *
 * @author Luke GJ Potter
 * @since 07/05/2014
 */
public class RmiServerManager {

    private static RmiServerStatus rmiServerStatus = RmiServerStatus.STOPPED;

    /**
     * Starts the RMI Server for the URLyBird Application. It creates the
     * Registry and binds a DatabaseAccessRemoteImpl object to the URL.
     */
    public static void startRmiServer() {

        // Check to see if the server is already running.
        if (isRmiServerRunning()) return;

        try {
            DatabaseAccessRemoteImpl databaseAccessRemote = new DatabaseAccessRemoteImpl();
            LocateRegistry.createRegistry(Integer.parseInt(UrlyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(UrlyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER)));
            Naming.rebind(RmiUtils.formRmiUrl(), databaseAccessRemote);
            rmiServerStatus = RmiServerStatus.RUNNING;

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the status of the RMI Server for the URLyBird Application.
     *
     * @return True, if the server is running.
     * False, if the server is not running.
     */
    public static boolean isRmiServerRunning() {
        return (rmiServerStatus == RmiServerStatus.RUNNING);
    }
}
