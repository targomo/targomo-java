package com.targomo.client.api.util;

import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultTargetCoordinate;

/**
 * Utility class with useful methods such as unit conversion
 */
public final class GeoUtil {

    private GeoUtil() { }

    /**
     * Convert a latitude into x - y coordinates in meters
     * Source: <a href="http://pordlabs.ucsd.edu/matlab/coord.htm">http://pordlabs.ucsd.edu/matlab/coord.htm</a>
     * @param latitude Latitude for conversion
     * @return Converted coordinate in meters
     */
    public Coordinate degreeToMeters(final double latitude) {
        double rLat = latitude * (Math.PI / 180);
        double latLength = 111132.92 - 559.82 * Math.cos(2 * rLat) + 1.175 * Math.cos(4 * rLat);
        double lngLength = 111415.13 * Math.cos(rLat) - 94.55 * Math.cos(3 * rLat);

        return new DefaultTargetCoordinate("", lngLength, latLength);
    }

    /**
     * Convert a length from meters to degrees with respect to a certain latitude
     * @param meters Length in meters to be converted to degrees
     * @param latitude Latitude to be used for conversion
     * @return Converted coordinate in degrees
     */
    public Coordinate metersToDegrees(final double meters, final double latitude) {
        Coordinate degreeLengths = degreeToMeters(latitude);
        double lat = meters / degreeLengths.getY();
        double lng = meters / degreeLengths.getX();

        return new DefaultTargetCoordinate("", lng, lat);
    }
}
