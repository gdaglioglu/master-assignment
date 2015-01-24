package suncertify.remote;

import java.rmi.Remote;

import suncertify.db.DBLocal;

/**
 * The remote interface for the GUI-Client.
 * Exactly matches the DBMain interface in the db package.
 *
 * @author gdaglioglu
 * @version 1.0
 */
public interface HotelDatabaseRemote extends Remote, DBLocal {
}
