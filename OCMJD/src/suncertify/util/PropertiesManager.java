/*
 * 
 */
package suncertify.util;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertyFileManager.
 */
public class PropertyFileManager {

	/** The log. */
	private final Logger log = Logger.getLogger("suncertify.util");

	/** The Constant PROPERTY_FILE_NAME. */
	private static final String PROPERTY_FILE_NAME = "suncertify.properties";

	/** The Constant PROPERTY_FILE_DIR. */
	private static final String PROPERTY_FILE_DIR = System
			.getProperty("user.dir");

	/** The properties file. */
	private static File propertiesFile = new File(
			PropertyFileManager.PROPERTY_FILE_DIR,
			PropertyFileManager.PROPERTY_FILE_NAME);

	/** The properties. */
	private Properties properties = null;

	/** The instance. */
	private static PropertyFileManager instance = new PropertyFileManager();

	/**
	 * Gets the single instance of PropertyFileManager.
	 *
	 * @return single instance of PropertyFileManager
	 */
	public static PropertyFileManager getInstance() {
		return PropertyFileManager.instance;
	}

	/**
	 * Instantiates a new property file manager.
	 */
	private PropertyFileManager() {
		this.log.entering("suncertify.util.PropertyFileManager",
				"PropertyFileManager()");
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

		this.log.exiting("suncertify.util.PropertyFileManager",
				"PropertyFileManager()");
	}

	/**
	 * Gets the property.
	 *
	 * @param propertyName the property name
	 * @return the property
	 */
	public String getProperty(final String propertyName) {
		return this.properties.getProperty(propertyName);
	}

	/**
	 * Sets the property.
	 *
	 * @param propertyName the property name
	 * @param propertyValue the property value
	 */
	public void setProperty(final String propertyName,
			final String propertyValue) {
		this.properties.setProperty(propertyName, propertyValue);
		this.saveProperties();
	}

	/**
	 * Save properties.
	 */
	private void saveProperties() {
		try {
			synchronized (PropertyFileManager.propertiesFile) {
				if (PropertyFileManager.propertiesFile.exists()) {
					PropertyFileManager.propertiesFile.delete();
				}
				PropertyFileManager.propertiesFile.createNewFile();
				final FileOutputStream fos = new FileOutputStream(
						PropertyFileManager.propertiesFile);
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
	 * Load properties.
	 *
	 * @return the properties
	 */
	private Properties loadProperties() {
		Properties loadedProperties = null;

		if (PropertyFileManager.propertiesFile.exists()
				&& PropertyFileManager.propertiesFile.canRead()) {
			synchronized (PropertyFileManager.propertiesFile) {
				try {
					loadedProperties = new Properties();
					final FileInputStream fis = new FileInputStream(
							PropertyFileManager.propertiesFile);
					loadedProperties.load(fis);
					fis.close();
				} catch (final FileNotFoundException fnfe) {
					this.log.log(Level.SEVERE, fnfe.getMessage(), fnfe);
					System.err.println("Unable to open properties file at "
							+ PropertyFileManager.PROPERTY_FILE_DIR
							+ PropertyFileManager.PROPERTY_FILE_NAME + ": "
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