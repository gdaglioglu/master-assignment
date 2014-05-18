package suncertify.rmi;


import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Luke GJ Potter
 *         Date: 08/05/2014
 */
public class RmiClientManager {

    public static DatabaseAccessRemote connectToRemoteServerViaRmi() {

        try {
            return (DatabaseAccessRemote) Naming.lookup(RmiUtils.formRmiUrl());
        } catch (ConnectException ignored) {
        } catch (NotBoundException ignored) {
        } catch (MalformedURLException ignored) {
        } catch (RemoteException ignored) {}

        return null;
    }
}
