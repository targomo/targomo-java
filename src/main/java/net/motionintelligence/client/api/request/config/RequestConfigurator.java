package net.motionintelligence.client.api.request.config;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.request.config.builder.JSONBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parse TravelOptions into JSON strings that can be used when calling client methods.
 *
 * Targets are generated using StringBuilders for faster generation.
 * Polygon, sources array and other properties are created as JSONObjects, then appended as Strings.
 *
 */
public final class RequestConfigurator {

	private static final Logger LOG = Logger.getLogger(RequestConfigurator.class);

    private RequestConfigurator() {
    }

	/**
	 * Replaces getCfg methods of Request classes.
	 * Output should be encoded as URL if request method will be GET
	 * @param travelOptions Travel options to be parsed into JSON
	 * @return JSON output
	 * @throws Route360ClientException Thrown when JSON cannot be generated
	 */
	public static String getConfig(final TravelOptions travelOptions) throws Route360ClientException {
	    LOG.trace("Creating configuration...");
	    String config = getCommonConfig(travelOptions);
	    LOG.trace("Configuration created.");
	    return config;
    }

	private static String getCommonConfig(final TravelOptions travelOptions) throws Route360ClientException {

		StringBuilder config = JSONBuilder.beginJson(new StringBuilder());

		try {
			if (travelOptions.getTravelTimes() != null && !travelOptions.getTravelTimes().isEmpty())
				JSONBuilder.append(config, Constants.POLYGON, getPolygonObject(travelOptions));

			if (travelOptions.getIntersectionMode() != null)
				JSONBuilder.appendString(config, Constants.POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode());

			if (travelOptions.getSources() != null && !travelOptions.getSources().isEmpty())
				JSONBuilder.append(config, Constants.SOURCES, getSources(travelOptions));

			if (travelOptions.getTargets() != null && !travelOptions.getTargets().isEmpty())
				JSONBuilder.append(config, Constants.TARGETS, getTargets(travelOptions));

			if (travelOptions.getPathSerializer() != null)
				JSONBuilder.appendString(config, Constants.PATH_SERIALIZER, travelOptions.getPathSerializer().getPathSerializerName());

			if (travelOptions.isElevationEnabled() != null)
				JSONBuilder.append(config, Constants.ENABLE_ELEVATION, travelOptions.isElevationEnabled());

			if (travelOptions.getReverse() != null)
				JSONBuilder.append(config, Constants.REVERSE, travelOptions.getReverse());

			JSONBuilder.appendAndEnd(config, Constants.MAX_ROUTING_TIME, travelOptions.getMaxRoutingTime());

		}
		catch (Exception e) {
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
		if (travelOptions.getBufferInMeter() != null)
			polygon.put(Constants.BUFFER_IN_METER, travelOptions.getBufferInMeter());

		if (travelOptions.getPolygonSerializerType() != null)
			polygon.put(Constants.SERIALIZER, travelOptions.getPolygonSerializerType().getPolygonSerializerName());

		return polygon;
	}

	private static JSONArray getSources(final TravelOptions travelOptions) throws JSONException {
		JSONArray sources = new JSONArray();
		for (Coordinate src : travelOptions.getSources().values()) {
			JSONObject source = getSourceObject(travelOptions, src);
			sources.put(source);
		}
		return sources;
	}

	private static StringBuilder getTargets(final TravelOptions travelOptions) {
		StringBuilder targetsBuilder = new StringBuilder().append("[");
		for (Coordinate trg : travelOptions.getTargets().values()) {
			buildTarget(targetsBuilder, trg).append(",");
		}
		targetsBuilder.deleteCharAt(targetsBuilder.length() - 1);
		targetsBuilder.append("]");
		return targetsBuilder;
	}

	private static StringBuilder buildTarget(final StringBuilder targetsBuilder, final Coordinate trg) {
		JSONBuilder.beginJson(targetsBuilder);
		JSONBuilder.appendString(targetsBuilder, Constants.ID, trg.getId());
		JSONBuilder.append(targetsBuilder, Constants.LATITUDE, trg.getY());
		JSONBuilder.appendAndEnd(targetsBuilder, Constants.LONGITUDE, trg.getX());
		return targetsBuilder;
	}

	private static JSONObject getTravelMode(final TravelOptions travelOptions,
	                                       final TravelType travelType) throws JSONException {
		JSONObject travelMode = new JSONObject();
		switch (travelType) {
			case TRANSIT:
				travelMode.put("frame", new JSONObject()
						.put("time", travelOptions.getTime())
						.put("date", travelOptions.getDate())
						.put("duration", travelOptions.getFrame()));
				break;
			case WALK:
				travelMode.put("speed", travelOptions.getWalkSpeed());
				travelMode.put("uphill", travelOptions.getWalkDownhill());
				travelMode.put("downhill", travelOptions.getWalkUphill());
				break;
			case BIKE:
				travelMode.put("speed", travelOptions.getBikeSpeed());
				travelMode.put("uphill", travelOptions.getBikeDownhill());
				travelMode.put("downhill", travelOptions.getBikeUphill());
				break;
			default:
				break;
		}

		travelMode.put(Constants.TRANSPORT_MODE_TRANSIT_RECOMMENDATIONS, travelOptions.getRecommendations());

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

	private static JSONObject getSourceObject(final TravelOptions travelOptions,
	                                          final Coordinate src) throws JSONException {
		TravelType travelType = getTravelType(travelOptions, src);
		JSONObject travelMode = getTravelMode(travelOptions, travelType);

		JSONObject source = new JSONObject()
				.put(Constants.ID, src.getId())
				.put(Constants.LATITUDE, src.getY())
				.put(Constants.LONGITUDE, src.getX())
				.put(Constants.TRANSPORT_MODE, new JSONObject().put(travelType.toString(), travelMode));

		if (travelOptions.getReverse() != null) {
			source.put(Constants.REVERSE, travelOptions.getReverse());
		}
		return source;
	}

}
