package suncertify.rmi;

import suncertify.utilities.URLyBirdApplicationConstants;
import suncertify.utilities.URLyBirdApplicationObjectsFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author Luke GJ Potter
 *         Date: 07/05/2014
 */
public class RmiServerManager {

    private static RmiServerStatus rmiServerStatus = RmiServerStatus.STOPPED;

    public static void startRmiServer() {

        // Check to see if the server is already running.
        if (isRmiServerRunning()) return;

        try {
            DatabaseAccessRemoteImpl databaseAccessRemote = new DatabaseAccessRemoteImpl();
            LocateRegistry.createRegistry(Integer.parseInt(URLyBirdApplicationObjectsFactory.getURLyBirdApplicationProperties().getProperty(URLyBirdApplicationConstants.PROPERTY_FILE_KEY_RMI_PORT_NUMBER)));
            Naming.rebind(RmiUtils.formRmiUrl(), databaseAccessRemote);
            rmiServerStatus = RmiServerStatus.RUNNING;

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRmiServerRunning() {
        return (rmiServerStatus == RmiServerStatus.RUNNING);
    }
}
