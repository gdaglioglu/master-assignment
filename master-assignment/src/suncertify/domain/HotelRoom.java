package suncertify.domain;

import java.io.Serializable;

/**
 * This class is the Domain object class for the application. It represents a
 * Hotel record in the database.
 * 
 * @author gdaglioglu
 */
public class HotelRoom implements Serializable {

	private static final long serialVersionUID = 1746986159728433641L;

	private String name;
	private String location;
	private String size;
	private String smoking;
	private String rate;
	private String date;
	private String owner;

	/**
	 * Construct a new Hotel object instantiating all it's fields.
	 * 
	 * @param name
	 *            The Hotel name.
	 * @param location
	 *            The Hotel location.
	 * @param size
	 *            The maximum number of people permitted in this room, not
	 *            including infants
	 * @param smoking
	 *            Flag indicating if smoking is permitted, "Y" indicating a
	 *            smoking room, and "N" indicating a non-smoking room.
	 * @param rate
	 *            The cost per hour of hiring this HotelRoom.
	 * @param date
	 *            The single night to which this record relates, format is
	 *            yyyy/mm/dd.
	 * @param owner
	 *            The customer id (an 8 digit number) who books the room.
	 */
	public HotelRoom(final String name, final String location,
			final String size, final String smoking, final String rate,
			final String date, final String owner) {
		this.name = name;
		this.location = location;
		this.size = size;
		this.smoking = smoking;
		this.rate = rate;
		this.date = date;
		this.owner = owner;
	}

	/**
	 * A construct provide for convenience to create a new HotelRoom object
	 * instantiating all it's fields, using an array. The array must contain
	 * only 7 elements and the elements must be found in the order same order as
	 * {@link HotelRoom#HotelRoom(int, String, String, String, String, String, String, String)}
	 * .
	 * 
	 * @param recordId
	 *            The ID of this record.
	 * @param record
	 *            A String array containing the 7 data fields.
	 */
	public HotelRoom(final String[] record) {
		this(record[0], record[1], record[2], record[3], record[4], record[5],
				record[6]);
	}

	/**
	 * This method is used to convert a HotelRoom object back to a String array.
	 * It is used to get the data of this HotelRoom in a format ready for
	 * writing to the database.
	 * 
	 * @return A String array containing this HotelRoom's data.
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
	 * Get the Hotel name.
	 * 
	 * @return This HotelRoom's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the Hotel location.
	 * 
	 * @return This HotelRoom's location.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Get the HotelRoom's size.
	 * 
	 * @return This HotelRoom's size.
	 */
	public String getSize() {
		return this.size;
	}

	/**
	 * Get the HotelRoom's smoking state.
	 * 
	 * @return The smoking state of this HotelRoom.
	 */
	public String getSmoking() {
		return this.smoking;
	}

	/**
	 * Get the HotelRoom's rate per night.
	 * 
	 * @return This HotelRoom's rate.
	 */
	public String getRate() {
		return this.rate;
	}

	/**
	 * Get the HotelRoom's availability date.
	 * 
	 * @return This HotelRoom's availability date.
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * Get the HotelRoom's owner.
	 * 
	 * @return This customer ID holding this HotelRoom.
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * Set the Hotel name.
	 * 
	 * @param name
	 *            The new name of this Hotel.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Set the Hotel location.
	 * 
	 * @param location
	 *            The new location of this Hotel.
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * Set the HotelRoom's size.
	 * 
	 * @param size
	 *            The new size of this HotelRoom.
	 */
	public void setSize(final String size) {
		this.size = size;
	}

	/**
	 * Set the HotelRoom's smoking state.
	 * 
	 * @param smoking
	 *            The new smoking state of this HotelRoom.
	 */
	public void setSmoking(final String smoking) {
		this.smoking = smoking;
	}

	/**
	 * Set the HotelRoom's hourly rate.
	 * 
	 * @param rate
	 *            The new hourly rate of this HotelRoom.
	 */
	public void setRate(final String rate) {
		this.rate = rate;
	}

	/**
	 * Set the HotelRoom's availability date.
	 * 
	 * @param date
	 *            The new availability date of this HotelRoom.
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Set the HotelRoom's owner.
	 * 
	 * @param owner
	 *            The new customer ID holding this HotelRoom.
	 */
	public void setOwner(final String owner) {
		this.owner = owner;
	}
}