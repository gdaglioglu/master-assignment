package suncertify.db;

import static suncertify.app.ui.DatabaseLocator.getLocation;
import static suncertify.app.util.PropertyManager.DATABASE_LOCATION;
import static suncertify.app.util.PropertyManager.getParameter;
import suncertify.app.util.App;

/**
 * This is a factory class responsible for creating and retrieving an instance
 * of {@link DBMain}.
 * 
 * @author Gokhan Daglioglu
 */
public class DAOFactory {

	/**
	 * The reference to {@link DBMain} instance.
	 * 
	 */
	private static DBMain dataService;

	/**
	 * Factory method for getting an instance of {@link DBMain}.
	 * 
	 * @return An instance of DBMain.
	 */
	public static DBMain getDataService() {

		if (dataService == null) {
			final String location = getDBLocation();
			dataService = new Data(location);
		}

		return dataService;
	}

	/**
	 * This method is responsible for getting the location of the database file
	 * which is used to construct the instance of DBMain.
	 * 
	 * @return The location of the database file.
	 */
	private static String getDBLocation() {
		String location = getParameter(DATABASE_LOCATION);

		if (location == null) {
			location = getLocation();
			if (location == null) {
				App.showErrorAndExit("You did not select a database location.");
			}
		}

		return location;
	}
}