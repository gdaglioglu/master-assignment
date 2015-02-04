package suncertify.client.gui;

import suncertify.client.gui.HotelRoomController;

/**
 * Holds all exceptions that may occur in the <code>HotelRoomController</code>.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see HotelRoomController
 */
public class GuiControllerException extends Exception {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     *
     * Not that we ever serialize this class of course, but Exception
     * implements Serializable, so therefore by default we do as well.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * Creates a default <code>GuiControllerException</code> instance.
     */
    public GuiControllerException() {
    }

    /**
     * Creates a <code>GuiControllerException</code> instance and chains an
     * exception.
     *
     * @param e The exception to wrap and chain.
     */
    public GuiControllerException(Throwable e) {
        super(e);
    }
}
