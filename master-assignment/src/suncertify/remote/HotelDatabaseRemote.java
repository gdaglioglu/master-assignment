package suncertify.remote;

import java.rmi.Remote;

import suncertify.db.RemoteDBMain;

/**
 * The remote interface for the GUI-Client. Exactly matches the DBMain interface
 * in the db package.
 *
 * @author gdaglioglu
 */
public interface HotelDatabaseRemote extends Remote, RemoteDBMain {
}
