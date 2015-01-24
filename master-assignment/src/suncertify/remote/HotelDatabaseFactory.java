package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Specifies the methods that may be remotely called on our DvdDatabaseFactory.
 */
interface HotelDatabaseFactory extends Remote {
    /**
     * Returns a reference to a remote instance of a class unique to the
     * connecting client containing all the methods that may be remotely called
     * on the database.
     *
     * @return a unique database connectivity class.
     * @throws RemoteException on network errors.
     */
    public HotelDatabaseRemote getClient() throws RemoteException;
}
