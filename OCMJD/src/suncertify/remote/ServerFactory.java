package suncertify.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerFactory extends Remote {

	public RemoteDBAccess getClient() throws RemoteException;
}
