/*
 * PropertyManager
 * 
 * Software developed for Oracle Certified Master, Java SE 6 Developer
 */
package suncertify.util;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This singleton class is used to read and write to the suncertify.properties
 * file
 * 
 * @author Eoin Mooney
 */
public class PropertyManager {

	/**
	 * The logger instance. All log message from this class are routed through
	 * this member. The logger namespace is <code>suncertify.util</code>
	 */
	private final Logger log = Logger.getLogger("suncertify.util");

	/** The Constant PROPERTY_FILE_NAME. */
	private static final String PROPERTY_FILE_NAME = ApplicationConstants.PROPERTY_FILE_NAME;

	/** The Constant PROPERTY_FILE_DIR. */
	private static final String PROPERTY_FILE_DIR = ApplicationConstants.PROPERTY_FILE_DIR;

	/**
	 * The properties file which will hold the configuration options between
	 * sessions
	 */
	private static File propertiesFile = new File(
			PropertyManager.PROPERTY_FILE_DIR,
			PropertyManager.PROPERTY_FILE_NAME);

	/**
	 * An instance of <code>Properties</code> that will be used to hold
	 * properties and their values
	 */
	private Properties properties = null;

	/** Create the singleton instance of this class */
	private static PropertyManager instance = new PropertyManager();

	/**
	 * Gets the single instance of <code>PropertiesManager</code>.
	 * 
	 * @return single instance of <code>PropertiesManager</code>
	 */
	public static PropertyManager getInstance() {
		return PropertyManager.instance;
	}

	/**
	 * Singleton constructor for <code>PropertyManager</code>
	 * 
	 */
	private PropertyManager() {
		this.properties = this.loadProperties();

		if (this.properties == null || this.properties.isEmpty()) {
			this.properties = new Properties();

			this.properties.setProperty(
					ApplicationConstants.KEY_PROPERTY_DB_PATH, "");
			this.properties
					.setProperty(
							ApplicationConstants.KEY_PROPERTY_NETWORK_HOST,
							"localhost");
			this.properties.setProperty(
					ApplicationConstants.KEY_PROPERTY_NETWORK_PORT,
					String.valueOf(java.rmi.registry.Registry.REGISTRY_PORT));
		}
	}

	/**
	 * Gets a property.
	 * 
	 * @param propertyName
	 *            The name of the property to be retrieved
	 * @return The value of propertyName
	 */
	public String getProperty(final String propertyName) {
		return this.properties.getProperty(propertyName);
	}

	/**
	 * Sets a property.
	 * 
	 * @param propertyName
	 *            The name of the property to be stored
	 * @param propertyValue
	 *            The value to be stored as propertyName
	 */
	public void setProperty(final String propertyName,
			final String propertyValue) {
		this.properties.setProperty(propertyName, propertyValue);
		this.saveProperties();
	}

	/**
	 * Save properties and write to file
	 */
	private void saveProperties() {
		try {
			synchronized (PropertyManager.propertiesFile) {
				if (PropertyManager.propertiesFile.exists()) {
					PropertyManager.propertiesFile.delete();
				}
				PropertyManager.propertiesFile.createNewFile();
				final FileOutputStream fos = new FileOutputStream(
						PropertyManager.propertiesFile);
				this.properties.store(fos, "URLyBird Properties");
				fos.close();
			}
		} catch (final IOException ioe) {
			this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
			System.err
					.println("I/O problem encountered when saving properties file: "
							+ ioe.getMessage());
			ioe.printStackTrace();
		}
	}

	/**
	 * Load properties from properties file
	 * 
	 * @return A <code>Properties</code> object holding the properties read from
	 *         the file
	 */
	private Properties loadProperties() {
		Properties loadedProperties = null;

		if (PropertyManager.propertiesFile.exists()
				&& PropertyManager.propertiesFile.canRead()) {
			synchronized (PropertyManager.propertiesFile) {
				try {
					loadedProperties = new Properties();
					final FileInputStream fis = new FileInputStream(
							PropertyManager.propertiesFile);
					loadedProperties.load(fis);
					fis.close();
				} catch (final FileNotFoundException fnfe) {
					this.log.log(Level.SEVERE, fnfe.getMessage(), fnfe);
					System.err.println("Unable to open properties file at "
							+ PropertyManager.PROPERTY_FILE_DIR
							+ PropertyManager.PROPERTY_FILE_NAME + ": "
							+ fnfe.getMessage());
					fnfe.printStackTrace();
				} catch (final IOException ioe) {
					this.log.log(Level.SEVERE, ioe.getMessage(), ioe);
					System.err
							.println("I/O problem encountered when loading properties: "
									+ ioe.getMessage());
					ioe.printStackTrace();
				}
			}
		}
		return loadedProperties;
	}
}