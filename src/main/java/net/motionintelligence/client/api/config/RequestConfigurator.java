package net.motionintelligence.client.api.config;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RequestConfigurator {

    private RequestConfigurator() {
    }

    public static String getPolygonConfig(final TravelOptions travelOptions) throws Route360ClientException {
        JSONObject config = getCommonConfig(travelOptions);
        try {
            JSONObject polygon = getPolygonObject(travelOptions);
            polygon.put(Constants.BUFFER_IN_METER, travelOptions.getBufferInMeter());
            polygon.put(Constants.SERIALIZER, travelOptions.getPolygonSerializerType().getPolygonSerializerName());

            config.put(Constants.POLYGON, polygon);

        } catch (JSONException e) {
            throw new Route360ClientException("Could not generate r360 config object", e);
        }
        return config.toString();
    }

    private static JSONObject getPolygonObject(final TravelOptions travelOptions) throws JSONException {
        JSONObject polygon = new JSONObject();
        polygon.put(Constants.POLYGON_VALUES, new JSONArray(travelOptions.getTravelTimes()));
        polygon.put(Constants.POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode());
        polygon.put(Constants.POINT_REDUCTION, travelOptions.isPointReduction());
        polygon.put(Constants.MIN_POLYGON_HOLE_SIZE, travelOptions.getMinPolygonHoleSize());
        return polygon;
    }

    private static JSONObject getCommonConfig(final TravelOptions travelOptions) throws Route360ClientException {

        JSONObject config = new JSONObject();

        try {
            JSONArray sources = getSources(travelOptions);
            config.put(Constants.SOURCES, sources);

            config.put(Constants.ENABLE_ELEVATION, travelOptions.isElevationEnabled());
            config.put(Constants.REVERSE, travelOptions.getReverse());

        }
        catch (Exception e) {
            throw new Route360ClientException("Could not generate r360 config object", e);
        }

        return config;
    }

    private static JSONArray getSources(final TravelOptions travelOptions) throws JSONException {
        JSONArray sources = new JSONArray();
        for (Coordinate src : travelOptions.getSources().values()) {
            JSONObject source = getSourceObject(travelOptions, src);

            sources.put(source);
        }
        return sources;
    }

    private static JSONObject getSourceObject(final TravelOptions travelOptions, Coordinate src) throws JSONException {
        TravelType travelType = getTravelType(travelOptions, src);
        JSONObject travelMode = getTravelMode(travelOptions, travelType);

        JSONObject source = new JSONObject()
                .put(Constants.ID, src.getId())
                .put(Constants.LATITUDE, src.getY())
                .put(Constants.LONGITUDE, src.getX())
                .put(Constants.TRANSPORT_MODE, new JSONObject().put(travelType.toString(), travelMode));

        if (travelOptions.getReverse()) {
            source.put(Constants.REVERSE, true);
        }
        return source;
    }

    private static JSONObject getTravelMode(final TravelOptions travelOptions, final TravelType travelType) throws JSONException {
        JSONObject travelMode = new JSONObject();
        if (TravelType.TRANSIT.equals(travelType)) {
            travelMode.put("frame", new JSONObject()
                    .put("time", travelOptions.getTime())
                    .put("date", travelOptions.getDate())
                    .put("duration", travelOptions.getFrame()));
        }

        if (TravelType.WALK.equals(travelType)) {
            travelMode.put("speed", travelOptions.getWalkSpeed());
            travelMode.put("uphill", travelOptions.getWalkDownhill());
            travelMode.put("downhill", travelOptions.getWalkUphill());
        }

        if (TravelType.BIKE.equals(travelType)) {
            travelMode.put("speed", travelOptions.getBikeSpeed());
            travelMode.put("uphill", travelOptions.getBikeDownhill());
            travelMode.put("downhill", travelOptions.getBikeUphill());
        }
        return travelMode;
    }

    private static TravelType getTravelType(final TravelOptions travelOptions, Coordinate src) {
        TravelType travelType = travelOptions.getTravelType();
        if (src.getTravelType() != null
                && src.getTravelType() != travelType
                && src.getTravelType() != TravelType.UNSPECIFIED) {
            travelType = src.getTravelType();
        }
        return travelType;
    }

}
