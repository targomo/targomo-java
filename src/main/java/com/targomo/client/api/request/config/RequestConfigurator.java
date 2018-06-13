package com.targomo.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.request.config.builder.JSONBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
	 * @throws TargomoClientException Thrown when JSON cannot be generated
	 */
	public static String getConfig(final TravelOptions travelOptions) throws TargomoClientException {
	    LOG.trace("Creating configuration...");
	    String config = getCommonConfig(travelOptions);
	    LOG.trace("Configuration created.");
	    return config;
    }

	private static String getCommonConfig(final TravelOptions travelOptions) throws TargomoClientException {

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

			if (travelOptions.getEdgeWeightType() != null)
				JSONBuilder.appendString(config, Constants.EDGE_WEIGHT, travelOptions.getEdgeWeightType());

			if (travelOptions.getStatisticGroupId() != null)
				JSONBuilder.appendString(config, Constants.STATISTIC_GROUP_ID, travelOptions.getStatisticGroupId());

			if (travelOptions.getStatisticIds() != null)
				JSONBuilder.append(config, Constants.STATISTICS_ID, travelOptions.getStatisticIds());

			if (travelOptions.getServiceUrl() != null)
				JSONBuilder.append(config, "serviceUrl", "\"" + travelOptions.getServiceUrl() + "\"");

			if (travelOptions.getServiceKey() != null)
				JSONBuilder.append(config, "serviceKey", "\"" + travelOptions.getServiceKey() + "\"");

			if (travelOptions.getFormat() != null)
				JSONBuilder.append(config, Constants.FORMAT, "\"" + travelOptions.getFormat().toString().toLowerCase() + "\"");

			if (travelOptions.getBoundingBox() != null)
				JSONBuilder.append(config, "boundingBox", "\"" + travelOptions.getBoundingBox() + "\"");


			if (travelOptions.getOsmTypes() != null) {

                ObjectMapper mapper = new ObjectMapper();
                JSONBuilder.append(config, "osmTypes", mapper.writeValueAsString(travelOptions.getOsmTypes()));
            }

            if (travelOptions.getTravelTimeFactors() != null && !travelOptions.getTravelTimeFactors().isEmpty()) {
			    JSONObject travelFactors = new JSONObject();
			    for(Map.Entry<String,Double> factor : travelOptions.getTravelTimeFactors().entrySet())
			        travelFactors.put(factor.getKey(),factor.getValue());
                JSONBuilder.append(config, Constants.TRAVEL_TIME_FACTORS, travelFactors);
            }

            JSONBuilder.append(config, "onlyPrintReachablePoints", travelOptions.getOnlyPrintReachablePoints());
			JSONBuilder.appendAndEnd(config, Constants.MAX_EDGE_WEIGTH, travelOptions.getMaxEdgeWeight());
		}
		catch (Exception e) {
			throw new TargomoClientException("Could not generate targomo config object", e);
		}

		return config.toString();
	}

	/**
	 *
	 * @param travelOptions
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject getPolygonObject(final TravelOptions travelOptions) throws JSONException {

		JSONObject polygon = new JSONObject();
		polygon.put(Constants.POLYGON_VALUES, 			 new JSONArray(travelOptions.getTravelTimes()));
		polygon.put(Constants.POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode());
		polygon.put(Constants.POINT_REDUCTION, 			 travelOptions.isPointReduction());
		polygon.put(Constants.MIN_POLYGON_HOLE_SIZE, 	 travelOptions.getMinPolygonHoleSize());

		if ( travelOptions.getSrid() != null )
			polygon.put(Constants.SRID, travelOptions.getSrid());

		if ( travelOptions.getBuffer() != null )
			polygon.put(Constants.BUFFER, travelOptions.getBuffer());

        if ( travelOptions.getSimplify() != null )
            polygon.put(Constants.SIMPLIFY, travelOptions.getSimplify());

        if ( travelOptions.getPolygonSerializerType() != null )
			polygon.put(Constants.SERIALIZER, travelOptions.getPolygonSerializerType().getPolygonSerializerName());

        if ( travelOptions.getIntersectionGeometry() != null ) {

			JSONObject intersectionPolygon = new JSONObject();
			intersectionPolygon.put("crs",  travelOptions.getIntersectionGeometry().getCrs());
			intersectionPolygon.put("data", travelOptions.getIntersectionGeometry().getData());
			// has to be geojson ATM
			intersectionPolygon.put("type", travelOptions.getIntersectionGeometry().getType());
            polygon.put("intersectionGeometry", intersectionPolygon);
		}

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
		JSONBuilder.append(targetsBuilder, 		 Constants.LATITUDE, trg.getY());
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
				if (travelOptions.getMaxTransfers() != null && travelOptions.getMaxTransfers() >= 0) {
				    travelMode.put("maxTransfers", travelOptions.getMaxTransfers());
                }
				break;
			case WALK:
				travelMode.put("speed", travelOptions.getWalkSpeed());
				travelMode.put("uphill", travelOptions.getWalkUphill());
				travelMode.put("downhill", travelOptions.getWalkDownhill());
				break;
			case BIKE:
				travelMode.put("speed", travelOptions.getBikeSpeed());
				travelMode.put("uphill", travelOptions.getBikeUphill());
				travelMode.put("downhill", travelOptions.getBikeDownhill());
				break;
            case CAR:
                travelMode.put("rushHour", travelOptions.getRushHour());
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
