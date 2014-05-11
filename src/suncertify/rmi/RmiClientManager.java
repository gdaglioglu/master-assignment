package suncertify.rmi;


import java.net.MalformedURLException;
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

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }
}
