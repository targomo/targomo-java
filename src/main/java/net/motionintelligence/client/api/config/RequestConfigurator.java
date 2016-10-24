package net.motionintelligence.client.api.config;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RequestConfigurator {

	private static final Logger LOG = Logger.getLogger(RequestConfigurator.class);

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

    public static String getTimeConfig(final TravelOptions travelOptions) throws Route360ClientException {
	    LOG.info("Creating time config...");
	    StringBuilder configBuilder = new StringBuilder();
	    try {
		    StringBuilder sourcesBuffer = new StringBuilder().append("[");
		    for (Coordinate src : travelOptions.getSources().values()) {
			    TravelType travelType = getTravelType(travelOptions, src);
			    JSONObject travelMode = getTravelMode(travelOptions, travelType);
			    sourcesBuffer.append("{\"")
					    .append(Constants.ID).append("\":\"").append(src.getId()).append("\",\"")
					    .append(Constants.TRANSPORT_MODE).append("\":").append("{\"").append(travelType.toString()).append("\":"+travelMode.toString()+"},\"")
					    .append(Constants.LATITUDE).append("\":\"").append(src.getY()).append("\",\"")
					    .append(Constants.LONGITUDE).append("\":\"").append(src.getX()).append("\"},");
		    }
		    sourcesBuffer.deleteCharAt(sourcesBuffer.length() - 1);
		    sourcesBuffer.append("]");
		    StringBuilder targetsBuffer = new StringBuilder().append("[");
		    for (Coordinate trg : travelOptions.getTargets().values()) {
			    targetsBuffer.append("{ \"")
					    .append(Constants.ID).append("\":\"").append(trg.getId()).append("\",\"")
					    .append(Constants.LATITUDE).append("\":\"").append(trg.getY()).append("\",\"")
					    .append(Constants.LONGITUDE).append("\":\"").append(trg.getX()).append("\"},");
		    }
		    targetsBuffer.deleteCharAt(targetsBuffer.length() - 1);
		    targetsBuffer.append("]");

		    configBuilder.append("{\"" + Constants.MAX_ROUTING_TIME + "\":" + travelOptions.getMaxRoutingTime() + ",")
				    .append("\"" + Constants.POLYGON_INTERSECTION_MODE + "\":\"" + travelOptions.getIntersectionMode() + "\",");

		    if (travelOptions.isElevationEnabled() != null)
			    configBuilder.append("\"" + Constants.ENABLE_ELEVATION + "\":" + travelOptions.isElevationEnabled() + ",");

		    configBuilder.append("\"" + Constants.SOURCES + "\": " + sourcesBuffer.toString() + ",");
		    configBuilder.append("\"" + Constants.TARGETS + "\": " + targetsBuffer.toString() + "}");

	    } catch (JSONException e) {
		    throw new Route360ClientException("Could not generate r360 config object", e);
	    }
	    String config = configBuilder.toString();
	    LOG.info("Time config created.");
	    return config;
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
