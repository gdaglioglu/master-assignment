package suncertify.controller;

import suncertify.db.DatabaseFileSchema;
import suncertify.model.HotelRoom;
import suncertify.utilities.URLyBirdApplicationConstants;

/**
 * Created by lukepotter on 31/03/2014.
 */
public class DatabaseAccessDAOUtils {

    // ---------- Public Methods ----------
    /**
     * Because the output of the {@code Data.readRecord(String)} method looks like
     * <code>
     *     0. |Palace                                                          |
     *     1. |Smallville                                                      |
     *     2. |2   |
     *     3. |Y|
     *     4. |$150.00 |
     *     5. |2005/07/27|
     *     6. |        |
     * </code>
     * for an unbooked room, it must be heavily parsed.
     *
     * @param strings
     * @return A {@code HotelRoom} pojo.
     */
    public static HotelRoom parseStringArrayIntoHotelRoomPojo(String[] strings) {

        if (strings != null) {

            String hotelName = strings[0].trim();
            String hotelLocation = strings[1].trim();
            int roomSize = Integer.parseInt(strings[2].trim());
            boolean isSmoking = strings[3].equalsIgnoreCase(DatabaseFileSchema.SMOKING_ALLOWED);
            double hotelRoomRate = extractDoubleFromString(strings[4].trim());
            String date = strings[5].trim();
            String bookedByPerson = strings[6].trim();

            return new HotelRoom(hotelName, hotelLocation, date, bookedByPerson, roomSize, isSmoking, hotelRoomRate);
        }

        return null;
    }

    public static String[] parseHotelRoomPojoIntoStringArray(HotelRoom hotelRoom) {
        return new String[0];
    }

    // ---------- Private Methods ----------
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