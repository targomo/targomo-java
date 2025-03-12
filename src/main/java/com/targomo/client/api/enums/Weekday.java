package com.targomo.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Enum denoting Day of the week with int values for both monday->sunday and sunday->saturday.
 */
public enum Weekday {

    SUNDAY    (0, 6),
    MONDAY    (1, 0),
    TUESDAY   (2, 1),
    WEDNESDAY (3, 2),
    THURSDAY  (4, 3),
    FRIDAY    (5, 4),
    SATURDAY  (6, 5);

    // Day of the week as a number, starting with sunday as 0
    public final int weekdaySunMon;
    // Day of the week as a number, starting with monday as 0
    public final int weekdayMonSun;

    Weekday(int weekdaySunMon, int weekdayMonSun) {
        this.weekdaySunMon = weekdaySunMon;
        this.weekdayMonSun = weekdayMonSun;
    }

    /**
     * Get the GeometryType based on a key string if one of the three allowed types (Polygon, Multipolygon, Linestring)
     * @param key geometry type as a string. will be converted to all lowercase
     * @return GeometryType if one of the three allowed types
     * @throws IllegalArgumentException if the key is not one of the three allowed types
     */
    @JsonCreator
    public static Weekday fromString(String key) {
        if(key == null) return null;

        return Stream.of(Weekday.values())
                .filter( enu -> enu.name().equalsIgnoreCase(key)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Weekday '%s' is not a valid day. Must be one of '%s'.", key, Arrays.asList(Weekday.values()))));
    }

    /**
     * Takes an integer value from 0-6 (inclusive) and returns a day of the week. Starts with Monday at 0 and ends with Sunday at 6
     * @param day Int value 0-6 (inclusive)
     * @return Day of the week corresponding to the provided integer
     * @throws IllegalArgumentException if the number is outside the range 0-6 (inclusive)
     */
    public static Weekday fromIntMonSun(int day) {
        return Stream.of(Weekday.values())
                .filter( enu -> enu.weekdayMonSun == day).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Weekday '%s' is not a valid day. Must be in range 0-6.", day)));
    }

    /**
     * Takes an integer value from 0-6 (inclusive) and returns a day of the week. Starts with Sunday at 0 and ends with Saturday at 6
     * @param day Int value 0-6 (inclusive)
     * @return Day of the week corresponding to the provided integer
     * @throws IllegalArgumentException if the number is outside the range 0-6 (inclusive)
     */
    public static Weekday fromIntSunMon(int day) {
        return Stream.of(Weekday.values())
                .filter( enu -> enu.weekdaySunMon == day).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Weekday '%s' is not a valid day. Must be in range 0-6.", day)));
    }
}
