/*
 * ApplicationMode
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.util;

/**
 * The Enum ApplicationMode is used throughout the application to define what mode the application is running in.
 * 
 * @author Eoin Mooney
 */
public enum ApplicationMode {

	/** The server. */
	SERVER,

	/** The standalone client. */
	STANDALONE_CLIENT,

	/** The networked client. */
	NETWORKED_CLIENT
}