package suncertify.client.gui;

import java.io.InterruptedIOException;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;
import suncertify.db.*;


/**
 * Handles all interactions between the GUI layer and the data layer.
 *
 * @author Gokhan Daglioglu
 * @version 2.0
 * @see DBMain
 * @see GuiControllerException
 */
public class GuiController {
    /**
     * Holds a reference to the client interface of the <code>HotelRoomMediator</code>.
     */
    private DBMain connection;

    /**
    * The Logger instance. All log messages from this class are routed through
    * this member. The Logger namespace is <code>suncertify.client.gui</code>.
    */
    private Logger log = Logger.getLogger("suncertify.client.gui");

    /**
     * Creates a <code>GuiController</code> instance with a specified connection
     * type.
     *
     * @param connectionType the method of connecting the client and database.
     * @param dbLocation the path to the data file, or the network address of
     * the server hosting the data file.
     * @param port the port the network server is listening on.
     * @throws GuiControllerException on communication error with database.
     */
    public GuiController(ConnectionType connectionType,
                         String dbLocation, String port)
            throws GuiControllerException {
        try{
            switch (connectionType) {
                case DIRECT:
                    connection = suncertify.direct.HotelConnector.getLocal(dbLocation);
                    break;
                case RMI:
                    connection = suncertify.remote.HotelConnector.getRemote(dbLocation, port);
                    break;
                default:
                    throw new IllegalArgumentException
                            ("Invalid connection type specified");
            }
        } catch(ClassNotFoundException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } catch(java.rmi.RemoteException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } catch(java.io.IOException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
    }

    /**
     * Locates a Hotel Room record by hotel name.
     *
     * @param hotelName A String representing the hotel name.
     * @return A HotelRoomModel containing all found hotel room records.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public HotelRoomModel find(String[] hotelDetails) throws GuiControllerException{
        HotelRoomModel out = new HotelRoomModel();
        int[] hotelRooms = new int[10];
        try{
        	hotelRooms = this.connection.find(hotelDetails);
            out.addDvdRecord((DVD) dvdArray.get(0));
        }

        catch(PatternSyntaxException pse){
            log.log(Level.WARNING, pse.getMessage(), pse);
            throw new GuiControllerException(pse);
        }
        catch(Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
        return out;
    }

    /**
     * Locates a Dvd based on a user defined search query.
     *
     * @param searchString The user defined search String
     * @return A DvdTableModel containing all Dvd records that met the search
     * criteria.
     * @throws GuiControllerException Indicates a database or network
     * level exception.
     */
    public DvdTableModel find(String searchString) throws
                                                    GuiControllerException{
        DvdTableModel out = new DvdTableModel();
        ArrayList dvdArray = new ArrayList();

        try {
            dvdArray = (ArrayList)this.connection.findDVD(searchString);
            Iterator it = dvdArray.iterator();
            while(it.hasNext()){
                out.addDvdRecord((DVD)it.next());
            }
        }
        catch(PatternSyntaxException pse){
            log.log(Level.WARNING, pse.getMessage(), pse);
            throw new GuiControllerException(pse);
        }
        catch(Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
        return out;
    }

    /**
     * Retrieves all Hotel room records from the database.
     *
     * @return A HotelRoomModel containing all Hotel room Records.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public HotelRoomModel getHotelRooms() throws GuiControllerException{
    	HotelRoomModel out = new HotelRoomModel();
        ArrayList dvdArray = new ArrayList();
        try {
            dvdArray = (ArrayList)this.connection.
            Iterator it = dvdArray.iterator();
            while(it.hasNext()){
                out.addDvdRecord((DVD)it.next());
            }
        }
        catch(Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
        return out;
    }

    /**
     * Decrements the number of Dvd's in stock identified by their UPC number.
     *
     * @param upc The UPC of the Dvd to rent.
     * @return A boolean indicating if the rent operation was successful.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public boolean rent(String upc) throws GuiControllerException{
        boolean returnValue = false;
        try {
            if (this.connection.reserveDVD(upc)) {
                DVD dvd = this.connection.getDVD(upc);
                if (dvd.getCopy() > 0) {
                    dvd.setCopy(dvd.getCopy() - 1);
                    this.connection.modifyDVD(dvd);
                    returnValue = true;
                }
            }
        } catch(InterruptedIOException ie) {
            log.log(Level.SEVERE, ie.getMessage(), ie);
            throw new GuiControllerException(ie);
        } catch(Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } finally {
            try {
                this.connection.releaseDVD(upc);
            } catch(IOException ie) {
                log.log(Level.SEVERE, ie.getMessage(), ie);
                throw new GuiControllerException(ie);
            }
        }
        return returnValue;
    }

    /**
     * Increments the number of Dvd's in stock identified by their UPC number.
     *
     * @param upc The UPC of the Dvd to return.
     * @return A boolean indicating if the return operation was successful.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public boolean returnRental(String upc) throws GuiControllerException{
        boolean returnValue = false;
        try {
            if (this.connection.reserveDVD(upc)) {
                DVD dvd = this.connection.getDVD(upc);
                dvd.setCopy(dvd.getCopy() + 1);
                this.connection.modifyDVD(dvd);
                returnValue = true;
            }
        } catch(InterruptedException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } catch(java.io.IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } finally {
            try {
                this.connection.releaseDVD(upc);
            } catch(IOException ie) {
                log.log(Level.SEVERE, ie.getMessage(), ie);
                throw new GuiControllerException(ie);
            }
        }
        return returnValue;
    }
}
