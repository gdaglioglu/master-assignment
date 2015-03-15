package suncertify.domain;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the Domain object class for the application. It represents a
 * hotel room record in the database.
 * 
 * @author Gokhan Daglioglu
 */
public class HotelRoom implements Serializable {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 1746986159728433641L;

	/**
	 * The <code>Logger</code> instance. All log messages from this class are
	 * routed through this member. The <code>Logger</code> namespace is
	 * <code>suncertify.domain</code>.
	 */
	private Logger logger = Logger.getLogger(HotelRoom.class.getPackage().getName());

	/**
	 * Variable used as a representation of the hotel name in the database file.
	 */
	private String name;

	/**
	 * Variable used as a representation of the hotel location in the database
	 * file.
	 */
	private String location;

	/**
	 * Variable used as a representation of the room size in the database file.
	 */
	private String size;

	/**
	 * Variable used as a representation of the room's smoking status in the
	 * database file.
	 */
	private String smoking;

	/**
	 * Variable used as a representation of the room rate in the database file.
	 */
	private String rate;

	/**
	 * Variable used as a representation of the room availability date in the
	 * database file.
	 */
	private String date;

	/**
	 * Variable used as a representation of customer Id which reserves the room
	 * in the database file.
	 */
	private String owner;

	/**
	 * Construct a new Hotel object instantiating all it's fields.
	 * 
	 * @param name
	 *            The hotel name.
	 * @param location
	 *            The hotel location.
	 * @param size
	 *            The maximum number of people permitted in this room, not
	 *            including infants.
	 * @param smoking
	 *            Flag indicating if smoking is permitted, "Y" indicating a
	 *            smoking room, and "N" indicating a non-smoking room.
	 * @param rate
	 *            The cost per night of this hotel room.
	 * @param date
	 *            The single night to which this record relates, format is
	 *            yyyy/mm/dd.
	 * @param owner
	 *            The customer id (an 8 digit number) who books the room.
	 */
	public HotelRoom(final String name, final String location, final String size,
			final String smoking, final String rate, final String date, final String owner) {
		this.name = name;
		this.location = location;
		this.size = size;
		this.smoking = smoking;
		this.rate = rate;
		this.date = date;
		this.owner = owner;
		logger.log(Level.FINE, "Initialized Hotel Room Object with following parameters: name = "
				+ name + ", location = " + location + ", size = " + size + ", smoking = " + smoking
				+ ", rate = " + rate + ", date = " + date + ", owner = " + owner);
	}

	/**
	 * A construct provide for convenience to create a new HotelRoom object
	 * instantiating all it's fields, using an array. The array must contain
	 * only 7 elements and the elements must be found in the order same order as
	 * {@link HotelRoom#HotelRoom(String, String, String, String, String, String, String)}
	 * .
	 * 
	 * @param hotelRoom
	 *            A String array containing the 7 data fields.
	 */
	public HotelRoom(final String[] hotelRoom) {
		this(hotelRoom[0], hotelRoom[1], hotelRoom[2], hotelRoom[3], hotelRoom[4], hotelRoom[5],
				hotelRoom[6]);
		logger.log(Level.FINE, "Initialized Hotel Room Object with following parameters: name = "
				+ hotelRoom[0] + ", location = " + hotelRoom[1] + ", size = " + hotelRoom[2]
				+ ", smoking = " + hotelRoom[3] + ", rate = " + hotelRoom[4] + ", date = "
				+ hotelRoom[5] + ", owner = " + hotelRoom[6]);
	}

	/**
	 * This method is used to convert a {@link HotelRoom} object back to a
	 * String array. It is used to get the data of this {@link HotelRoom} in a
	 * format ready for writing to the database.
	 * 
	 * @return A String array containing this {@link HotelRoom}'s data.
	 */
	public String[] toArray() {
		final String[] data = new String[7];
		data[0] = this.name;
		data[1] = this.location;
		data[2] = this.size;
		data[3] = this.smoking;
		data[4] = this.rate;
		data[5] = this.date;
		data[6] = this.owner;
		return data;
	}

	/**
	 * Get the hotel name.
	 * 
	 * @return This {@link HotelRoom}'s name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the hotel location.
	 * 
	 * @return This {@link HotelRoom}'s location.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Get the hotel room size.
	 * 
	 * @return This {@link HotelRoom}'s size.
	 */
	public String getSize() {
		return this.size;
	}

	/**
	 * Get the hotel room's smoking state.
	 * 
	 * @return This {@link HotelRoom}'s smoking state.
	 */
	public String getSmoking() {
		return this.smoking;
	}

	/**
	 * Get the hotel room rate per night.
	 * 
	 * @return This {@link HotelRoom}'s rate.
	 */
	public String getRate() {
		return this.rate;
	}

	/**
	 * Get the hotel room's availability date.
	 * 
	 * @return This {@link HotelRoom}'s availability date.
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * Get the hotel oom's owner.
	 * 
	 * @return This customer ID which reserved this {@link HotelRoom}.
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * Set the hotel name.
	 * 
	 * @param name
	 *            The new name of this {@link HotelRoom}.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Set the hotel location.
	 * 
	 * @param location
	 *            The new location of this {@link HotelRoom}.
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * Set the hotel room size.
	 * 
	 * @param size
	 *            The new size of this {@link HotelRoom}.
	 */
	public void setSize(final String size) {
		this.size = size;
	}

	/**
	 * Set the hotel room smoking state.
	 * 
	 * @param smoking
	 *            The new smoking state of this {@link HotelRoom}.
	 */
	public void setSmoking(final String smoking) {
		this.smoking = smoking;
	}

	/**
	 * Set the hotel room nightly rate.
	 * 
	 * @param rate
	 *            The new hourly rate of this {@link HotelRoom}.
	 */
	public void setRate(final String rate) {
		this.rate = rate;
	}

	/**
	 * Set the hotel room availability date.
	 * 
	 * @param date
	 *            The new availability date of this {@link HotelRoom}.
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Set the hotel room owner.
	 * 
	 * @param owner
	 *            The new customer ID which reserved this {@link HotelRoom}.
	 */
	public void setOwner(final String owner) {
		this.owner = owner;
	}

	/**
	 * Returns a String representation of the {@link HotelRoom}
	 * 
	 * @return a <code>String</code> containing the hotel room data
	 */
	@Override
	public String toString() {
		String roomString = this.name + " " + this.location + " " + this.size + " " + this.smoking
				+ " " + this.rate + " " + this.date + " " + this.owner;
		return roomString;
	}
}