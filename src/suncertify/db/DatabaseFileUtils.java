package suncertify.db;

/**
 * Created by lukepotter on 31/03/2014.
 */
public class DatabaseFileUtils {

    public static DatabaseFileUtils databaseFileUtils;

    private DatabaseFileUtils() {

        deriveHeaderValues();
        deriveHeaderOffset();
    }

    public static DatabaseFileUtils getInstance() {
        if (databaseFileUtils != null) {
            return databaseFileUtils;
        } else {
            return new DatabaseFileUtils();
        }
    }

    private void deriveHeaderValues() {
    }

    private void deriveHeaderOffset() {
    }
}
