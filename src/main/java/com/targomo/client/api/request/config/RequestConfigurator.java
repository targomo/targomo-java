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

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Parse TravelOptions into JSON strings that can be used when calling client methods.
 *
 * Targets are generated using StringBuilders for faster generation.
 * Polygon, sources array and other properties are created as JSONObjects, then appended as Strings.
 *
 */
public final class RequestConfigurator {

    private static final Logger LOG = Logger.getLogger(RequestConfigurator.class);

    private RequestConfigurator() { }

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

            //attention - at least one multiGraph value must be set to create the multigraph hierarchy
            if ( Stream.of(travelOptions.getMultiGraphEdgeClasses(), travelOptions.getMultiGraphSerializationFormat(),
                    travelOptions.getMultiGraphSerializationDecimalPrecision(), travelOptions.getMultiGraphAggregationType(),
                    travelOptions.getMultiGraphAggregationIgnoreOutlier(), travelOptions.getMultiGraphAggregationOutlierPenalty(),
                    travelOptions.getMultiGraphAggregationMinSourcesCount(), travelOptions.getMultiGraphAggregationMinSourcesRatio(),
                    travelOptions.getMultiGraphAggregationMaxResultValue(), travelOptions.getMultiGraphAggregationMaxResultValueRatio(),
                    travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins(), travelOptions.getMultiGraphLayerType(),
                    travelOptions.getMultiGraphLayerEdgeAggregationType())
                    .anyMatch(Objects::nonNull) ||
                    Stream.of(travelOptions.getMultiGraphTileZoom(), travelOptions.getMultiGraphTileX(),
                            travelOptions.getMultiGraphTileY()).allMatch(Objects::nonNull))
                JSONBuilder.append(config, Constants.MULTIGRAPH, getMultiGraphObject(travelOptions));

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

            JSONBuilder.appendAndEnd(config, Constants.MAX_EDGE_WEIGHT, travelOptions.getMaxEdgeWeight());
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

        if ( travelOptions.getDecimalPrecision() != null )
            polygon.put(Constants.DECIMAL_PRECISION, travelOptions.getDecimalPrecision());
		
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

    private static JSONObject getMultiGraphObject(final TravelOptions travelOptions) throws JSONException {

        JSONObject multiGraph = new JSONObject();

        if( travelOptions.getMultiGraphEdgeClasses() != null )
            multiGraph.put(Constants.MULTIGRAPH_EDGE_CLASSES, travelOptions.getMultiGraphEdgeClasses());

        addMultiGraphLayer(travelOptions, multiGraph);
        addMultiGraphTile(travelOptions, multiGraph);
        addMultiGraphSerialization(travelOptions, multiGraph);
        addMultiGraphAggregation(travelOptions, multiGraph);

        return multiGraph;
    }

    private static void addMultiGraphLayer(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphLayerType(), travelOptions.getMultiGraphLayerEdgeAggregationType(),
                travelOptions.getMultiGraphLayerGeometryDetailPerTile(), travelOptions.getMultiGraphLayerMinGeometryDetailLevel(),
                travelOptions.getMultiGraphLayerMaxGeometryDetailLevel(),
                travelOptions.getMultiGraphLayerGeometryDetailLevel()).anyMatch(Objects::nonNull) ) {

            JSONObject multigraphLayer = new JSONObject();

            if ( travelOptions.getMultiGraphSerializationFormat() != null )
                multigraphLayer.put(Constants.MULTIGRAPH_LAYER_TYPE, travelOptions.getMultiGraphLayerType().getKey());

            if( travelOptions.getMultiGraphLayerEdgeAggregationType() != null )
                multigraphLayer.put(Constants.MULTIGRAPH_LAYER_EDGE_AGGREGATION_TYPE, travelOptions.getMultiGraphLayerEdgeAggregationType().getKey());

            if ( travelOptions.getMultiGraphLayerGeometryDetailPerTile() != null )
                multigraphLayer.put(Constants.MULTIGRAPH_LAYER_GEOMETRY_DETAIL_PER_TILE, travelOptions.getMultiGraphLayerGeometryDetailPerTile());

            if ( travelOptions.getMultiGraphLayerMinGeometryDetailLevel() != null )
                multigraphLayer.put(Constants.MULTIGRAPH_LAYER_MIN_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerMinGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerMaxGeometryDetailLevel() != null )
                multigraphLayer.put(Constants.MULTIGRAPH_LAYER_MAX_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerMaxGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerGeometryDetailLevel() != null )
                multigraphLayer.put(Constants.MULTIGRAPH_LAYER_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerGeometryDetailLevel());

            multiGraph.put( Constants.MULTIGRAPH_LAYER, multigraphLayer);
        }
    }

    private static void addMultiGraphTile(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphTileZoom(), travelOptions.getMultiGraphTileX(),
                travelOptions.getMultiGraphTileY()).allMatch(Objects::nonNull)) {

            JSONObject multigraphTile = new JSONObject();
            multigraphTile.put(Constants.MULTIGRAPH_TILE_ZOOM, travelOptions.getMultiGraphTileZoom());
            multigraphTile.put(Constants.MULTIGRAPH_TILE_X, travelOptions.getMultiGraphTileX());
            multigraphTile.put(Constants.MULTIGRAPH_TILE_Y, travelOptions.getMultiGraphTileY());
            multiGraph.put( Constants.MULTIGRAPH_TILE, multigraphTile);
        } else if (Stream.of(travelOptions.getMultiGraphTileZoom(), travelOptions.getMultiGraphTileX(),
                travelOptions.getMultiGraphTileY()).anyMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("None or all elements in the tile definition have to be set.");
        }
    }

    private static void addMultiGraphSerialization(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphSerializationFormat(),
                travelOptions.getMultiGraphSerializationDecimalPrecision()).anyMatch(Objects::nonNull) ) {
            JSONObject multigraphSerialization = new JSONObject();

            if ( travelOptions.getMultiGraphSerializationFormat() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_FORMAT, travelOptions.getMultiGraphSerializationFormat().getKey());

            if ( travelOptions.getMultiGraphSerializationDecimalPrecision() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_DECIMAL_PRECISION, travelOptions.getMultiGraphSerializationDecimalPrecision());

            if ( travelOptions.getMultiGraphSerializationDecimalPrecision() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_MAX_GEOMETRY_COUNT, travelOptions.getMultiGraphSerializationMaxGeometryCount());

            multiGraph.put( Constants.MULTIGRAPH_SERIALIZATION, multigraphSerialization);
        }
    }

    private static void addMultiGraphAggregation(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphAggregationType(), travelOptions.getMultiGraphAggregationIgnoreOutlier(),
                travelOptions.getMultiGraphAggregationOutlierPenalty(), travelOptions.getMultiGraphAggregationMinSourcesCount(),
                travelOptions.getMultiGraphAggregationMinSourcesRatio(), travelOptions.getMultiGraphAggregationMaxResultValue(),
                travelOptions.getMultiGraphAggregationMaxResultValueRatio(),
                travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins()).anyMatch(Objects::nonNull) ) {

            JSONObject multigraphAggregation = new JSONObject();

            if ( travelOptions.getMultiGraphAggregationType() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_TYPE, travelOptions.getMultiGraphAggregationType().getKey());

            if ( travelOptions.getMultiGraphAggregationIgnoreOutlier() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_IGNORE_OUTLIERS, travelOptions.getMultiGraphAggregationIgnoreOutlier());

            if ( travelOptions.getMultiGraphAggregationOutlierPenalty() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_OUTLIER_PENALTY, travelOptions.getMultiGraphAggregationOutlierPenalty());

            if ( travelOptions.getMultiGraphAggregationMinSourcesCount() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_COUNT, travelOptions.getMultiGraphAggregationMinSourcesCount());

            if ( travelOptions.getMultiGraphAggregationMinSourcesRatio() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_RATIO, travelOptions.getMultiGraphAggregationMinSourcesRatio());

            if ( travelOptions.getMultiGraphAggregationMaxResultValueRatio() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE_RATIO, travelOptions.getMultiGraphAggregationMaxResultValueRatio());

            if ( travelOptions.getMultiGraphAggregationMaxResultValue() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE, travelOptions.getMultiGraphAggregationMaxResultValue());

            if ( travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins() != null )
                multigraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_FILTER_VALUES_FOR_SOURCE_ORIGINS, travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins());

            multiGraph.put( Constants.MULTIGRAPH_AGGREGATION, multigraphAggregation);
        }
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
        JSONBuilder.append(targetsBuilder,          Constants.LATITUDE, trg.getY());
        JSONBuilder.appendAndEnd(targetsBuilder, Constants.LONGITUDE, trg.getX());
        return targetsBuilder;
    }

    private static JSONObject getTravelMode(final TravelOptions travelOptions,
                                           final TravelType travelType) throws JSONException {
        JSONObject travelMode = new JSONObject();
        switch (travelType) {
            case TRANSIT: //Equivalent with WALK_TRANSIT (BIKE_TRANSIT not really supported hence it is left out)
                travelMode.put("frame", new JSONObject()
                        .put("time", travelOptions.getTime())
                        .put("date", travelOptions.getDate())
                        .put("duration", travelOptions.getFrame()));
                if (travelOptions.getMaxTransfers() != null && travelOptions.getMaxTransfers() >= 0) {
                    travelMode.put("maxTransfers", travelOptions.getMaxTransfers());
                }
                if (travelOptions.getMaxWalkingTime() != null && travelOptions.getMaxWalkingTime() >= 0) {
                    travelMode.put("maxWalkingTime", travelOptions.getMaxWalkingTime());
                }
                travelMode.put(Constants.TRANSPORT_MODE_TRANSIT_RECOMMENDATIONS, travelOptions.getRecommendations());
                travelMode.put(Constants.TRAVEL_MODE_SPEED, travelOptions.getWalkSpeed());
                travelMode.put(Constants.TRAVEL_MODE_UPHILL, travelOptions.getWalkUphill());
                travelMode.put(Constants.TRAVEL_MODE_DOWNHILL, travelOptions.getWalkDownhill());
                break;
            case WALK:
                travelMode.put(Constants.TRAVEL_MODE_SPEED, travelOptions.getWalkSpeed());
                travelMode.put(Constants.TRAVEL_MODE_UPHILL, travelOptions.getWalkUphill());
                travelMode.put(Constants.TRAVEL_MODE_DOWNHILL, travelOptions.getWalkDownhill());
                break;
            case BIKE:
                travelMode.put(Constants.TRAVEL_MODE_SPEED, travelOptions.getBikeSpeed());
                travelMode.put(Constants.TRAVEL_MODE_UPHILL, travelOptions.getBikeUphill());
                travelMode.put(Constants.TRAVEL_MODE_DOWNHILL, travelOptions.getBikeDownhill());
                break;
            case CAR:
                travelMode.put("rushHour", travelOptions.getRushHour());
                break;
            default:
                break;
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
