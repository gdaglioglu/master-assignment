package suncertify.rmi;


import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Manages the client connections to the server via RMI for the URLyBird
 * Application.
 *
 * @author Luke GJ Potter
 * @since 08/05/2014
 */
public class RmiClientManager {

    /**
     * Connects the client GUI to the server.
     *
     * @return The DatabaseAccessRemote to make database calls to.
     */
    public static DatabaseAccessRemote connectToRemoteServerViaRmi() {

        try {
            return (DatabaseAccessRemote) Naming.lookup(RmiUtils.formRmiUrl());
        } catch (ConnectException ignored) {
        } catch (NotBoundException ignored) {
        } catch (MalformedURLException ignored) {
        } catch (RemoteException ignored) {
        }

        return null;
    }
}
