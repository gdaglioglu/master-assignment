package suncertify.util;

import java.io.*;
import java.util.Properties;

public final class PropertyManager {

	private static final String PROPERTY_FILE_NAME = "suncertify.properties";
	private static final String PROPERTY_FILE_DIR = System
			.getProperty("user.dir");
	private static File propertiesFile = new File(PROPERTY_FILE_DIR,
			PROPERTY_FILE_NAME);

	private Properties properties = null;
	private static final PropertyManager instance = new PropertyManager();

	public static PropertyManager getInstance() {
		return instance;
	}

	private PropertyManager() {
		properties = loadProperties();

		if ((properties == null) || properties.isEmpty()) {
			properties = new Properties();

			properties.setProperty(ApplicationConstants.KEY_PROPERTY_DB_PATH,
					"");
			properties
					.setProperty(
							ApplicationConstants.KEY_PROPERTY_NETWORK_HOST,
							"localhost");
			properties.setProperty(
					ApplicationConstants.KEY_PROPERTY_NETWORK_PORT,
					String.valueOf(java.rmi.registry.Registry.REGISTRY_PORT));
		}
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

	public void setProperty(String propertyName, String propertyValue) {
		properties.setProperty(propertyName, propertyValue);
		saveProperties();
	}

	private void saveProperties() {
		try {
			synchronized (propertiesFile) {
				if (propertiesFile.exists()) {
					propertiesFile.delete();
				}
				propertiesFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(propertiesFile);
				properties.store(fos, "URLyBird Properties");
				fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Properties loadProperties() {
		Properties loadedProperties = null;

		if (propertiesFile.exists() && propertiesFile.canRead()) {
			synchronized (propertiesFile) {
				try {
					loadedProperties = new Properties();
					FileInputStream fis = new FileInputStream(propertiesFile);
					loadedProperties.load(fis);
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return loadedProperties;
	}
}