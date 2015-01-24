package suncertify.direct;

import suncertify.db.*;
import java.io.*;

/**
 * A  HotelConnector is used in cases where the GUI client wants to make a
 * connection to the data file. In this case, that connection is an direct
 * connection.
 *
 * @author Gokhan Daglioglu
 * @version 1.0
 * @see suncertify.db.Data
 */
public class HotelConnector {
    /**
     * Since this is a utility class (it only exists for other classes to call
     * it's static methods), lets stop users creating unneeded instances of
     * this class by creating a private constructor.
     */
    private HotelConnector() {
    }

    /**
     * Static method that gets a database handle.
     * The DBClient is a local object.
     *
     * @param dbLocation the path to the database on disk.
     * @return A <code>DBMain</code> instance.
     * @throws IOException Thrown if an <code>IOException</code> is
     * encountered in the <code>Data</code> class.
     * <br>
     * For more information, see {@link Data}.
     * @throws ClassNotFoundException Thrown if an
     * <code>ClassNotFoundException</code> is
     * encountered in the <code>Data</code> class.
     * <br>
     * For more information, see {@link Data}.
     */
    public static DBMain getLocal(String dbLocation)
            throws IOException, ClassNotFoundException {
        return new Data(dbLocation);
    }
}
