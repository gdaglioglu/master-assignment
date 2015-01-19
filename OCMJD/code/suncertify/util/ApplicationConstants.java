/*
 * ApplicationConstants
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.util;

/**
 * This class holds various constants used throughout the application
 * 
 * @author Eoin Mooney
 */
public class ApplicationConstants {

	/** The Constant MAGIC_COOKIE. Used to identify datafiles */
	public static final int MAGIC_COOKIE = 0x103;

	/**
	 * The Constant RMI_SERVER_IDENTIFIER. Used to binding and looking up the
	 * RMI Server
	 */
	public static final String RMI_SERVER_IDENTIFIER = "OCMJD";

	/**
	 * The Constant KEY_PROPERTY_DB_PATH. Used to identify and access the
	 * datafile location using the PropertyManager
	 */
	public static final String KEY_PROPERTY_DB_PATH = "dbPath";

	/**
	 * The Constant KEY_PROPERTY_NETWORK_HOST. Used to identify and access the
	 * host using the PropertyManager
	 */
	public static final String KEY_PROPERTY_NETWORK_HOST = "networkHost";

	/**
	 * The Constant KEY_PROPERTY_NETWORK_PORT. Used to identify and access the
	 * port using the PropertyManager
	 */
	public static final String KEY_PROPERTY_NETWORK_PORT = "networkPort";

	/**
	 * The Constant PROPERTY_FILE_NAME. Used as filename for the properties file
	 */
	public static final String PROPERTY_FILE_NAME = "suncertify.properties";

	/**
	 * The Constant PROPERTY_FILE_NAME. Used as directory for the properties
	 * file
	 */
	public static final String PROPERTY_FILE_DIR = System
			.getProperty("user.dir");
}
