package suncertify.remote;

import java.rmi.Naming;
import java.rmi.*;
import suncertify.db.*;

/**
 * A  HotelConnector is used in cases where the GUI client wants to make a
 * network connection. In this case, that connection is an RMI connection.
 *
 * @author gdaglioglu
 * @version 1.0
 * @see suncertify.db.Data
 */
public class HotelConnector {
    /**
     * Since this is a utility class (it only exists for other classes to call
     * it's static methods), lets stop users creating unneeded instances of
     * this class by creating a private constructor.
     */
    private HotelConnector() {
    }

    /**
     * Static method that creates an RMI connection.
     * The Data is a remote object.
     *
     * @param hostname The IP or address of the host machine.
     * @param port the port the RMI Registry is listening on.
     * @return A DBMain instance.
     * @throws RemoteException Indicates that a remote instance of the DBMain
     * interface cannot be created.
     */
    public static DBMain getRemote(String hostname, String port)
            throws RemoteException {
        String url = "rmi://" + hostname + ":" + port + "/HotelMediator";

        try {
        	HotelDatabaseFactory factory
                    = (HotelDatabaseFactory) Naming.lookup(url);
            return (DBMain) factory.getClient();
        } catch (NotBoundException e) {
            System.err.println("Hotel Mediator not registered: "
                    + e.getMessage());
            throw new RemoteException("Hotel Mediator not registered: ", e);
        } catch (java.net.MalformedURLException e) {
            System.err.println(hostname + " not valid: " + e.getMessage());
            throw new RemoteException("cannot connect to " + hostname, e);
        }
    }
}
