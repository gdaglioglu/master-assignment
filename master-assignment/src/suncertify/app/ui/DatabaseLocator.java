package suncertify.app.ui;

import static suncertify.app.util.App.showError;
import static suncertify.app.util.PropertyManager.DATABASE_LOCATION;
import static suncertify.app.util.PropertyManager.setParameter;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class is responsible for prompting the user to enter the location of the
 * database file.
 * 
 * @author Gokhan Daglioglu
 */
public class DatabaseLocator {

	/**
	 * This method will display a file chooser dialog to the user allowing the
	 * user chose a new database file.
	 * 
	 * @return The location of the file the user picked.
	 */
	public static String getLocation() {
		final JFileChooser chooser = createDialog();
		String location = null;
		final int action = chooser.showOpenDialog(null);

		if (action == JFileChooser.APPROVE_OPTION) {
			final File choice = chooser.getSelectedFile();

			if (choice.getName().endsWith(".db")) {
				location = choice.getAbsolutePath();
				setParameter(DATABASE_LOCATION, location);
			} else {
				showError("The selected file is not a valid database file.");
			}

		}

		return location;
	}

	/**
	 * This method creates the <code>JFileChooser</code> with the correct
	 * properties.
	 * 
	 * @return A JFileChooser allowing selection of .db files.
	 */
	private static JFileChooser createDialog() {
		final JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.setFileFilter(new FileNameExtensionFilter(".db files only", "db"));
		chooser.setDialogTitle("Database location");
		return chooser;
	}
}