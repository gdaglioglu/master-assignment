package suncertify.model;


/**
 * Created by lukepotter on 06/12/2013.
 */
public class HotelRoom {

    private String name;
    private String location;
    private int roomSize;
    private boolean isSmoking;
    private double rate;
    private String date;
    private String ownerName;

    // Constructors.
    public HotelRoom() {}

    public HotelRoom(String name, String location, String date, int roomSize, boolean isSmoking, double rate) {

        setName(name); setLocation(location); setDate(date);
        setRoomSize(roomSize); setSmoking(isSmoking); setRate(rate);
    }

    public HotelRoom(String name, String location, String date, String ownerName, int roomSize, boolean isSmoking, double rate) {

        setName(name); setLocation(location); setDate(date); setOwnerName(ownerName);
        setRoomSize(roomSize); setSmoking(isSmoking); setRate(rate);
    }

    // Getters and Setters.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public boolean isSmoking() {
        return isSmoking;
    }

    public void setSmoking(boolean isSmoking) {
        this.isSmoking = isSmoking;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer(name);

        stringBuffer.append(", ");
        stringBuffer.append(location);


        return stringBuffer.toString();
    }

    @Override
    public boolean equals(Object object) {

        if (! (object instanceof HotelRoom)) {
            return false;
        }

        HotelRoom otherHotelRoom = (HotelRoom) object;

        if (this.name.equalsIgnoreCase(otherHotelRoom.getName())
                && this.location.equalsIgnoreCase(otherHotelRoom.getLocation())
                && this.roomSize == otherHotelRoom.getRoomSize()
                && this.isSmoking == otherHotelRoom.isSmoking()
                && this.rate == otherHotelRoom.getRate()) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return (name.hashCode() + location.hashCode());
    }
}
