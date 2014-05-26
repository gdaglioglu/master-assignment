package suncertify.model;

import suncertify.utilities.UrlyBirdApplicationConstants;

/**
 * The pojo to represent a Hotel Room.
 *
 * @author Luke GJ Potter
 * @since  06/12/2013
 */
public class HotelRoom {

    private String name;
    private String location;
    private int roomSize;
    private boolean isSmoking;
    private double rate;
    private String date;
    private String ownerName;

    // ---------- Constructors ----------
    /**
     * Constructor for the HotelRoom pojo.
     *
     * @param name The name of the hotel.
     * @param location The city that the hotel is located in.
     * @param roomSize The size of the room.
     * @param isSmoking Weather the room allows occupants to smoke.
     * @param rate The cost of the room.
     * @param date The date that the room will be available from.
     */
    public HotelRoom(String name, String location, int roomSize, boolean isSmoking, double rate, String date) {

        setName(name);
        setLocation(location);
        setRoomSize(roomSize);
        setSmoking(isSmoking);
        setRate(rate);
        setDate(date);
        setOwnerName("");
    }

    /**
     * Constructor for the HotelRoom pojo.
     *
     * Because the output of the {@code Data.readRecord(String)} method looks like
     * <pre>{@code
     * 0. |Palace                                                          |
     *1. |Smallville                                                      |
     *2. |2   |
     *3. |Y|
     *4. |$150.00 |
     *5. |2005/07/27|
     *6. |        |
     * }</pre>
     * for an unbooked room, it must be heavily parsed.
     *
     * @param strings An array of strings ordered to follow the output of he
     * {@code Data.readRecord(String)} method.
     */
    public HotelRoom(String... strings) {

        setName(strings[0].trim());
        setLocation(strings[1].trim());
        setRoomSize(Integer.parseInt(strings[2].trim()));
        setSmoking(strings[3].equalsIgnoreCase(UrlyBirdApplicationConstants.SMOKING_ALLOWED));
        setRate(extractDoubleFromString(strings[4].trim()));
        setDate(strings[5].trim());
        setOwnerName(strings[6].trim());
    }

    // ---------- Getters and Setters ----------
    /**
     * Gets the name.
     *
     * @return The name of the hotel.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name The name of the hotel.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the location.
     *
     * @return The city that the hotel is located in.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location The city that the hotel is located in.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the date.
     *
     * @return The date that the room will be available from.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date The date that the room will be available from.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the owner of a room.
     *
     * @return The name of the owner of ths HotelRoom.
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Sets the owner of a room.
     *
     * @param ownerName The name of the owner of ths HotelRoom.
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Gets the room size.
     *
     * @return The size of the room.
     */
    public int getRoomSize() {
        return roomSize;
    }

    /**
     * Sets the room size.
     *
     * @param roomSize The size of the room.
     */
    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    /**
     * Gets isSmoking.
     *
     * @return Weather the room allows occupants to smoke.
     */
    public boolean isSmoking() {
        return isSmoking;
    }

    /**
     * Sets isSmoking.
     *
     * @param isSmoking Weather the room allows occupants to smoke.
     */
    public void setSmoking(boolean isSmoking) {
        this.isSmoking = isSmoking;
    }

    /**
     * Gets the rate.
     *
     * @return The cost of the room.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Sets the rate.
     *
     * @param rate The cost of the room.
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    // ---------- Public Methods ----------
    /**
     * Converts the HotelRoom as a String.
     *
     * @return A string representation of the HotelRoom object.
     */
    @Override public String toString() {

        return name + ", " + location;
    }

    /**
     * Converts the HotelRoom pojo into a String Array.
     *
     * @return The fields of the HotelRoom object as a String Array.
     */
    public String[] toStringArray() {

        String isSmokingString = UrlyBirdApplicationConstants.SMOKING_NOT_ALLOWED;
        if (isSmoking()) isSmokingString = UrlyBirdApplicationConstants.SMOKING_ALLOWED;

        return new String[] {
                getName(),
                getLocation(),
                getRoomSize() + "",
                isSmokingString,
                UrlyBirdApplicationConstants.CURRENCY_PREFIX + getRate(),
                getDate(),
                getOwnerName()
        };
    }

    /**
     * This method is used for comparing against other objects.
     *
     * @param object Object to compare against.
     * @return True, if the objects are the same. False, if they are not the same.
     */
    @Override public boolean equals(Object object) {

        if (! (object instanceof HotelRoom)) {
            return false;
        }

        HotelRoom otherHotelRoom = (HotelRoom) object;

        return this.name.equalsIgnoreCase(otherHotelRoom.getName())
                && this.location.equalsIgnoreCase(otherHotelRoom.getLocation())
                && this.roomSize == otherHotelRoom.getRoomSize()
                && this.isSmoking == otherHotelRoom.isSmoking()
                && this.rate == otherHotelRoom.getRate();

    }

    /**
     * Used for making a comparison against other objects.
     *
     * @return A hash value of the HotelRoom object.
     */
    @Override public int hashCode() {

        return (name.hashCode() + location.hashCode() * (1 + (int)rate + roomSize));
    }

    // ---------- Private Methods ----------
    /**
     * Extracts a double value from a string.
     *
     * @param string The string containing the double.
     * @return The double value contained in the string.
     */
    private static double extractDoubleFromString(String string) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {

            if (Character.isDigit(string.charAt(i)) || string.charAt(i) == '.') {
                stringBuilder.append(string.charAt(i));
            }
        }
        return Double.parseDouble(stringBuilder.toString().substring(0, stringBuilder.length() - 1));
    }
}
