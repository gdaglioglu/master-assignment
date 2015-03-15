package suncertify.app.util;

import static suncertify.app.util.App.handleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides read/write access to the user's saved configuration parameters on
 * disk, so that next time they connect, we can offer the same configuration
 * parameters as a default.
 *
 * @author Gokhan Daglioglu
 */
public class PropertyManager {

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.config</code>.
	 */
	private Logger logger = Logger.getLogger("suncertify.config");

	/**
	 * The key in application properties indicating that the value will be the
	 * database location.
	 */
	public static final String DATABASE_LOCATION = "database.location";

	/**
	 * The key in application properties indicating that the value will be the
	 * RMI Registry server address.
	 */
	public static final String SERVER_ADDRESS = "server.address";

	/**
	 * The key in application properties indicating that the value will be the
	 * port the RMI Registry.
	 */
	public static final String SERVER_PORT = "server.port";

	/**
	 * The name used to persist the whether the state of the "Exact match"
	 * checkbox on client UI.
	 */
	public static final String EXACT_MATCH = "exact.match.enabled";

	/**
	 * The <code>Properties</code> for this application.
	 */
	private Properties props;

	/**
	 * The location where our configuration file will be saved.
	 */
	private static final String BASE_DIRECTORY = System.getProperty("user.dir");

	/**
	 * The name of our properties file.
	 */
	private static final String OPTIONS_FILENAME = "suncertify.properties";

	/**
	 * The <code>File</code> containing application's saved configuration.
	 */
	private static File savedOptionsFile = new File(BASE_DIRECTORY, OPTIONS_FILENAME);

	/**
	 * Placeholder for our singleton version of <code>PropertyManager</code>.
	 * Since we know that we will want at least one of these, we are creating
	 * our instance as soon as we possibly can.
	 */
	private static PropertyManager propertyManager = new PropertyManager();

	/**
	 * Creates a new instance of SavedConfiguration. There should only ever be
	 * one instance of this class (a Singleton), so we have made it private.
	 */
	private PropertyManager() {
		init();
		logger.log(Level.FINE, "Initialized Singleton Property Manager Object");
	}

	private void init() {
		props = loadParametersFromFile();

		if (props == null) {
			props = new Properties();
			props.setProperty(SERVER_ADDRESS, "localhost");
			props.setProperty(SERVER_PORT, "" + java.rmi.registry.Registry.REGISTRY_PORT);
			props.setProperty(EXACT_MATCH, "false");
		}

	}

	/**
	 * Returns the value of the named parameter.
	 *
	 * @param parameterName
	 *            the name of the parameter for which the user is requesting the
	 *            value.
	 * @return the value of the named parameter in
	 */
	public static String getParameter(String parameterName) {
		return propertyManager.props.getProperty(parameterName);
	}

	/**
	 * Returns the <code>Boolean</code> value of the named parameter.
	 *
	 * @param parameterName
	 *            the name of the parameter for which the user is requesting the
	 *            value.
	 * @return the value of the named parameter.
	 */
	public static boolean getBooleanParameter(String parameterName) {
		return Boolean.parseBoolean(propertyManager.props.getProperty(parameterName));
	}

	/**
	 * Updates the saved parameters with the new values. Always saves the new
	 * values immediately - this means we will be saving the properties file far
	 * more often than we need when we enter our first set of values, however
	 * once the initial set of values have been entered, they should rarely (if
	 * ever) be changed. So this will not be a problem most of the time. Doing
	 * it this way means that the user of this class need not explicitly save
	 * the parameters.
	 *
	 * @param parameterName
	 *            the name of the parameter.
	 * @param parameterValue
	 *            the value to be stored for the parameter
	 */
	public static void setParameter(String parameterName, String parameterValue) {
		propertyManager.props.setProperty(parameterName, parameterValue);
		saveParametersToFile();
	}

	/**
	 * Saves the parameters to a file so that they can be used again next time
	 * the application starts.
	 */
	private static void saveParametersToFile() {
		try {
			synchronized (savedOptionsFile) {
				if (savedOptionsFile.exists()) {
					savedOptionsFile.delete();
				}
				savedOptionsFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(savedOptionsFile);
				propertyManager.props.store(fos, "URLyBird configuration");
				fos.close();
			}
		} catch (IOException e) {
			handleException("Unable to save user parameters to file. "
					+ "They will not be remembered next time you start.");
		}
	}

	/**
	 * Attempts to load the saved parameters from the file so that the user does
	 * not have to re-enter all the information.
	 *
	 * @return The properties loaded from file or null if file does not exist.
	 */
	private Properties loadParametersFromFile() {
		Properties loadedProperties = null;

		if (savedOptionsFile.exists() && savedOptionsFile.canRead()) {
			synchronized (savedOptionsFile) {
				try {
					FileInputStream fis = new FileInputStream(savedOptionsFile);
					loadedProperties = new Properties();
					loadedProperties.load(fis);
					fis.close();
				} catch (FileNotFoundException e) {
					handleException("Unable to load user "
							+ "parameters. Default values will be used.\n" + e);
				} catch (IOException e) {
					handleException("Unable to load user "
							+ "parameters. Default values will be used.\n" + e);
				}
			}
		}
		return loadedProperties;
	}
}