package com.targomo.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.AbstractGeometry;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.Location;
import com.targomo.client.api.pojo.AggregationInputParameters;
import com.targomo.client.api.request.config.builder.JSONBuilder;
import com.targomo.client.api.pojo.AggregationConfiguration;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.targomo.client.Constants.*;

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
            if ( Stream.of(
                    travelOptions.getMultiGraphEdgeClasses(),
                    travelOptions.getMultiGraphSerializationFormat(),
                    travelOptions.getMultiGraphSerializationDecimalPrecision(),
                    travelOptions.getMultiGraphSerializationMaxGeometryCount(),
                    travelOptions.getMultiGraphDomainType(),
                    travelOptions.getMultiGraphDomainEdgeAggregationType(),
                    travelOptions.getMultiGraphAggregationType(),
                    travelOptions.getMultiGraphAggregationIgnoreOutliers(),
                    travelOptions.getMultiGraphAggregationOutlierPenalty(),
                    travelOptions.getMultiGraphAggregationMinSourcesRatio(),
                    travelOptions.getMultiGraphAggregationMinSourcesCount(),
                    travelOptions.getMultiGraphAggregationSourceValuesLowerBound(),
                    travelOptions.getMultiGraphAggregationSourceValuesUpperBound(),
                    travelOptions.getMultiGraphAggregationMinResultValue(),
                    travelOptions.getMultiGraphAggregationMinResultValueRatio(),
                    travelOptions.getMultiGraphAggregationMaxResultValue(),
                    travelOptions.getMultiGraphAggregationMaxResultValueRatio(),
                    travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins(),
                    travelOptions.getMultiGraphAggregationGravitationExponent(),
                    travelOptions.getMultiGraphAggregationInputParameters(),
                    travelOptions.getMultiGraphAggregationPostAggregationFactor(),
                    travelOptions.getMultiGraphAggregationMathExpression(),
                    travelOptions.getMultiGraphPreAggregationPipeline(),
                    travelOptions.getMultiGraphLayerType(),
                    travelOptions.getMultiGraphDomainEdgeAggregationType(),
                    travelOptions.getMultiGraphLayerCustomGeometryMergeAggregation(),
                    travelOptions.getMultiGraphLayerGeometryDetailLevel(),
                    travelOptions.getMultiGraphLayerGeometryDetailPerTile(),
                    travelOptions.getMultiGraphLayerMaxGeometryDetailLevel(),
                    travelOptions.getMultiGraphLayerMinGeometryDetailLevel())
                    .anyMatch(Objects::nonNull) ||
                    Stream.of(travelOptions.getMultiGraphTileZoom(), travelOptions.getMultiGraphTileX(),
                            travelOptions.getMultiGraphTileY()).allMatch(Objects::nonNull))
                JSONBuilder.append(config, Constants.MULTIGRAPH, getMultiGraphObject(travelOptions));

            if (travelOptions.getIntersectionMode() != null)
                JSONBuilder.appendString(config, Constants.POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode());

            if (travelOptions.getSources() != null && !travelOptions.getSources().isEmpty())
                JSONBuilder.append(config, Constants.SOURCES, getSources(travelOptions));

            if (travelOptions.getSourceGeometries() != null && !travelOptions.getSourceGeometries().isEmpty())
                JSONBuilder.append(config, Constants.SOURCE_GEOMETRIES, getSourceGeometries(travelOptions));

            if (travelOptions.getGravitationCompetitors() != null && !travelOptions.getGravitationCompetitors().isEmpty())
                JSONBuilder.append(config, Constants.GRAVITATION_COMPETITORS, getGravitationCompetitors(travelOptions));

            if (travelOptions.getTargets() != null && !travelOptions.getTargets().isEmpty())
                JSONBuilder.append(config, Constants.TARGETS, getTargets(travelOptions));

            if (travelOptions.getTargetGeohashes() != null && !travelOptions.getTargetGeohashes().isEmpty())
                JSONBuilder.append(config, Constants.TARGET_GEOHASHES, travelOptions.getTargetGeohashes());

            if (travelOptions.getPathSerializer() != null)
                JSONBuilder.appendString(config, Constants.PATH_SERIALIZER, travelOptions.getPathSerializer().getPathSerializerName());

            if (travelOptions.isElevationEnabled() != null)
                JSONBuilder.append(config, Constants.ENABLE_ELEVATION, travelOptions.isElevationEnabled());

            if (travelOptions.getReverse() != null)
                JSONBuilder.append(config, Constants.REVERSE, travelOptions.getReverse());

            if (travelOptions.getEdgeWeightType() != null)
                JSONBuilder.appendString(config, Constants.EDGE_WEIGHT, travelOptions.getEdgeWeightType().getKey());

            if (travelOptions.getStatisticGroupId() != null)
                JSONBuilder.appendString(config, Constants.STATISTIC_GROUP_ID, travelOptions.getStatisticGroupId());

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
            
            JSONBuilder.append(config, Constants.DISABLE_CACHE, travelOptions.isDisableCache());

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

        if ( travelOptions.getPolygonOrientationRule() != null )
            polygon.put(Constants.POLYGON_ORIENTATION_RULE, travelOptions.getPolygonOrientationRule());

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

        addMultiGraphDomain(travelOptions, multiGraph);
        addMultiGraphLayer(travelOptions, multiGraph);
        addMultiGraphTile(travelOptions, multiGraph);
        addMultiGraphSerialization(travelOptions, multiGraph);
        addMultiGraphAggregation(travelOptions, multiGraph);
        addMultiGraphPreAggregationPipeline(travelOptions, multiGraph);
        return multiGraph;
    }

    private static void addMultiGraphDomain(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphDomainType(),
                travelOptions.getMultiGraphDomainEdgeAggregationType()).anyMatch(Objects::nonNull) ) {

            JSONObject multiGraphDomain = new JSONObject();

            if ( travelOptions.getMultiGraphDomainType() != null )
                multiGraphDomain.put(Constants.MULTIGRAPH_DOMAIN_TYPE, travelOptions.getMultiGraphDomainType().getKey());

            if( travelOptions.getMultiGraphDomainEdgeAggregationType() != null )
                multiGraphDomain.put(Constants.MULTIGRAPH_DOMAIN_EDGE_AGGREGATION_TYPE, travelOptions.getMultiGraphDomainEdgeAggregationType().getKey());

            multiGraph.put( Constants.MULTIGRAPH_DOMAIN, multiGraphDomain);
        }
    }

    private static void addMultiGraphLayer(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphLayerType(),
                travelOptions.getMultiGraphLayerGeometryDetailPerTile(),
                travelOptions.getMultiGraphLayerMinGeometryDetailLevel(),
                travelOptions.getMultiGraphLayerMaxGeometryDetailLevel(),
                travelOptions.getMultiGraphLayerGeometryDetailLevel(),
                travelOptions.getMultiGraphLayerCustomGeometryMergeAggregation()).anyMatch(Objects::nonNull) ) {

            JSONObject multiGraphLayer = new JSONObject();

            if ( travelOptions.getMultiGraphLayerType() != null )
                multiGraphLayer.put(Constants.MULTIGRAPH_LAYER_TYPE, travelOptions.getMultiGraphLayerType().getKey());

            if ( travelOptions.getMultiGraphLayerGeometryDetailPerTile() != null )
                multiGraphLayer.put(Constants.MULTIGRAPH_LAYER_GEOMETRY_DETAIL_PER_TILE, travelOptions.getMultiGraphLayerGeometryDetailPerTile());

            if ( travelOptions.getMultiGraphLayerMinGeometryDetailLevel() != null )
                multiGraphLayer.put(Constants.MULTIGRAPH_LAYER_MIN_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerMinGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerMaxGeometryDetailLevel() != null )
                multiGraphLayer.put(Constants.MULTIGRAPH_LAYER_MAX_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerMaxGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerGeometryDetailLevel() != null )
                multiGraphLayer.put(Constants.MULTIGRAPH_LAYER_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerCustomGeometryMergeAggregation() != null )
                multiGraphLayer.put(Constants.MULTIGRAPH_LAYER_CUSTOM_GEOMETRY_MERGE_AGGREGATION, travelOptions.getMultiGraphLayerCustomGeometryMergeAggregation().getKey());

            multiGraph.put( Constants.MULTIGRAPH_LAYER, multiGraphLayer);
        }
    }

    /**
     * Parse the MultiGraph tile attributes. This is used for tiled requests, so it's needed to have
     * all attributes defined (zoom, x and y). If it is partially define (either x, y or zoom), an
     * exception is thrown
     * @param travelOptions
     * @param multiGraph
     * @throws JSONException
     */
    private static void addMultiGraphTile(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphTileZoom(), travelOptions.getMultiGraphTileX(),
                travelOptions.getMultiGraphTileY()).allMatch(Objects::nonNull)) {

            JSONObject multiGraphTile = new JSONObject();
            multiGraphTile.put(Constants.MULTIGRAPH_TILE_ZOOM, travelOptions.getMultiGraphTileZoom());
            multiGraphTile.put(Constants.MULTIGRAPH_TILE_X, travelOptions.getMultiGraphTileX());
            multiGraphTile.put(Constants.MULTIGRAPH_TILE_Y, travelOptions.getMultiGraphTileY());
            multiGraph.put( Constants.MULTIGRAPH_TILE, multiGraphTile);
        } else if (Stream.of(travelOptions.getMultiGraphTileZoom(), travelOptions.getMultiGraphTileX(),
                travelOptions.getMultiGraphTileY()).anyMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("None or all elements in the multiGraphTile definition have to be set (zoom, x and y).");
        }
    }

    private static void addMultiGraphSerialization(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if( Stream.of(travelOptions.getMultiGraphSerializationFormat(),
                travelOptions.getMultiGraphSerializationDecimalPrecision()).anyMatch(Objects::nonNull) ) {
            JSONObject multiGraphSerialization = new JSONObject();

            if ( travelOptions.getMultiGraphSerializationFormat() != null )
                multiGraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_FORMAT, travelOptions.getMultiGraphSerializationFormat().getKey());

            if ( travelOptions.getMultiGraphSerializationDecimalPrecision() != null )
                multiGraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_DECIMAL_PRECISION, travelOptions.getMultiGraphSerializationDecimalPrecision());

            if ( travelOptions.getMultiGraphSerializationMaxGeometryCount() != null )
                multiGraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_MAX_GEOMETRY_COUNT, travelOptions.getMultiGraphSerializationMaxGeometryCount());

            multiGraph.put( Constants.MULTIGRAPH_SERIALIZATION, multiGraphSerialization);
        }
    }

    private static void addMultiGraphAggregation(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if (Stream.of(travelOptions.getMultiGraphAggregationType(), travelOptions.getMultiGraphAggregationIgnoreOutliers(),
                travelOptions.getMultiGraphAggregationOutlierPenalty(), travelOptions.getMultiGraphAggregationMinSourcesCount(),
                travelOptions.getMultiGraphAggregationSourceValuesLowerBound(), travelOptions.getMultiGraphAggregationSourceValuesUpperBound(),
                travelOptions.getMultiGraphAggregationMinSourcesRatio(), travelOptions.getMultiGraphAggregationMaxResultValue(),
                travelOptions.getMultiGraphAggregationMaxResultValueRatio() ,travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins(), 
                travelOptions.getMultiGraphAggregationGravitationExponent(), travelOptions.getMultiGraphAggregationPostAggregationFactor())
                .anyMatch(Objects::nonNull)) {
            JSONObject multiGraphAggregation = new JSONObject();
            AggregationConfiguration aggregationConfiguration = new AggregationConfiguration.AggregationConfigurationBuilder(travelOptions, false).build();
            fillJsonAggregationConfig(aggregationConfiguration, multiGraphAggregation);
            multiGraph.put(Constants.MULTIGRAPH_AGGREGATION, multiGraphAggregation);
        }
    }

    private static void addMultiGraphPreAggregationPipeline(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if (travelOptions.getMultiGraphPreAggregationPipeline() != null) {

            Map<String, JSONObject> preAggregationPipelineMap = new LinkedHashMap<>();

            for (Map.Entry<String, AggregationConfiguration> entry : travelOptions.getMultiGraphPreAggregationPipeline().entrySet()) {
                String aggregationName = entry.getKey();
                AggregationConfiguration aggregationConfiguration = entry.getValue();
                JSONObject multiGraphAggregation = new JSONObject();
                fillJsonAggregationConfig(aggregationConfiguration, multiGraphAggregation);
                preAggregationPipelineMap.put(aggregationName, multiGraphAggregation);
            }

            multiGraph.put(Constants.MULTIGRAPH_PRE_AGGREGATION_PIPELINE, preAggregationPipelineMap);
        }
    }

    private static void fillJsonAggregationConfig(AggregationConfiguration aggregationConfiguration, JSONObject multiGraphAggregation) throws JSONException {

        if (aggregationConfiguration.getType() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_TYPE, aggregationConfiguration.getType().getKey());

        if (aggregationConfiguration.getIgnoreOutliers() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_IGNORE_OUTLIERS, aggregationConfiguration.getIgnoreOutliers());

        if (aggregationConfiguration.getOutlierPenalty() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_OUTLIER_PENALTY, aggregationConfiguration.getOutlierPenalty());

        if (aggregationConfiguration.getMinSourcesRatio() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_RATIO, aggregationConfiguration.getMinSourcesRatio());

        if (aggregationConfiguration.getMinSourcesCount() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MIN_SOURCES_COUNT, aggregationConfiguration.getMinSourcesCount());

        if (aggregationConfiguration.getSourceValuesLowerBound() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_SOURCE_VALUES_LOWER_BOUND, aggregationConfiguration.getSourceValuesLowerBound());

        if (aggregationConfiguration.getSourceValuesUpperBound() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_SOURCE_VALUES_UPPER_BOUND, aggregationConfiguration.getSourceValuesUpperBound());

        if (aggregationConfiguration.getMinResultValueRatio() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MIN_RESULT_VALUE_RATIO, aggregationConfiguration.getMinResultValueRatio());

        if (aggregationConfiguration.getMinResultValue() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MIN_RESULT_VALUE, aggregationConfiguration.getMinResultValue());

        if (aggregationConfiguration.getMaxResultValueRatio() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE_RATIO, aggregationConfiguration.getMaxResultValueRatio());

        if (aggregationConfiguration.getMaxResultValue() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE, aggregationConfiguration.getMaxResultValue());

        if (aggregationConfiguration.getPostAggregationFactor() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_POST_AGGREGATION_FACTOR, aggregationConfiguration.getPostAggregationFactor());

        if (aggregationConfiguration.getGravitationExponent() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_GRAVITATION_EXPONENT, aggregationConfiguration.getGravitationExponent());

        if (aggregationConfiguration.getFilterValuesForSourceOrigins() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_FILTER_VALUES_FOR_SOURCE_ORIGINS, aggregationConfiguration.getFilterValuesForSourceOrigins());

        if (aggregationConfiguration.getAggregationInputParameters() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS, buildAggregationInputParameters(aggregationConfiguration.getAggregationInputParameters()));

        if (aggregationConfiguration.getMathExpression() != null)
            multiGraphAggregation.put(Constants.MULTIGRAPH_AGGREGATION_MATH_EXPRESSION, aggregationConfiguration.getMathExpression());
    }

    private static JSONObject buildAggregationInputParameters(Map<String, AggregationInputParameters> aggregationInputParameters) throws JSONException {
        JSONObject aggregationInputParams = new JSONObject();
        if (aggregationInputParameters != null) {
            for (Map.Entry<String, AggregationInputParameters> entry : aggregationInputParameters.entrySet()) {
                String name = entry.getKey();
                AggregationInputParameters param = entry.getValue();
                JSONObject sourceParam = new JSONObject();

                if (param.getInputFactor() != null)
                    sourceParam.put(Constants.MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_FACTOR, param.getInputFactor());

                if (param.getGravitationAttractionStrength() != null)
                    sourceParam.put(Constants.MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_ATTRACTION_STRENGTH, param.getGravitationAttractionStrength());

                if (param.getGravitationPositiveInfluence() != null)
                    sourceParam.put(Constants.MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_POSITIVE_INFLUENCE, param.getGravitationPositiveInfluence());

                aggregationInputParams.put(name, sourceParam);
            }
        }
        return aggregationInputParams;
    }

    private static JSONArray getSources(final TravelOptions travelOptions) throws JSONException {
        JSONArray sources = new JSONArray();
        for (Coordinate src : travelOptions.getSources().values()) {
            JSONObject source = getSourceObject(travelOptions, src);
            sources.put(source);
        }
        return sources;
    }

    private static JSONArray getSourceGeometries(final TravelOptions travelOptions) throws JSONException {
        JSONArray sourceGeometries = new JSONArray();
        for (AbstractGeometry src : travelOptions.getSourceGeometries().values()) {
            JSONObject source = getSourceObject(travelOptions, src);
            sourceGeometries.put(source);
        }
        return sourceGeometries;
    }

    private static JSONArray getGravitationCompetitors(final TravelOptions travelOptions) throws JSONException {
        JSONArray sources = new JSONArray();
        for (Coordinate src : travelOptions.getGravitationCompetitors().values()) {
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

        if (travelOptions.getTrafficJunctionPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_JUNCTION_PENALTY, travelOptions.getTrafficJunctionPenalty());
        if (travelOptions.getTrafficSignalPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_SIGNAL_PENALTY, travelOptions.getTrafficSignalPenalty());
        if (travelOptions.getTrafficLeftTurnPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_LEFT_TURN_PENALTY, travelOptions.getTrafficLeftTurnPenalty());
        if (travelOptions.getTrafficRightTurnPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_RIGHT_TURN_PENALTY, travelOptions.getTrafficRightTurnPenalty());

        switch (travelType) {
            case WALKTRANSIT:
            case TRANSIT: //Equivalent with WALK_TRANSIT (BIKE_TRANSIT not really supported hence it is left out)
                travelMode.put(TRANSPORT_MODE_TRANSIT_FRAME, new JSONObject()
                        .put(TRANSPORT_MODE_TRANSIT_FRAME_TIME, travelOptions.getTime())
                        .put(TRANSPORT_MODE_TRANSIT_FRAME_DATE, travelOptions.getDate())
                        .put(TRANSPORT_MODE_TRANSIT_FRAME_DURATION, travelOptions.getFrame())
                        .put(TRANSPORT_MODE_TRANSIT_EARLIEST_ARRIVAL, travelOptions.getEarliestArrival()));
                if (travelOptions.getMaxTransfers() != null && travelOptions.getMaxTransfers() >= 0) {
                    travelMode.put(TRANSPORT_MODE_TRANSIT_MAX_TRANSFERS, travelOptions.getMaxTransfers());
                }
                if (travelOptions.getMaxWalkingTimeFromSource() != null && travelOptions.getMaxWalkingTimeFromSource() >= 0) {
                    travelMode.put(TRANSPORT_MODE_TRANSIT_MAX_WALKING_TIME_FROM_SOURCE, travelOptions.getMaxWalkingTimeFromSource());
                }
                if (travelOptions.getMaxWalkingTimeToTarget() != null && travelOptions.getMaxWalkingTimeToTarget() >= 0) {
                    travelMode.put(TRANSPORT_MODE_TRANSIT_MAX_WALKING_TIME_TO_TARGET, travelOptions.getMaxWalkingTimeToTarget());
                }
                if (travelOptions.getAvoidTransitRouteTypes() != null && !travelOptions.getAvoidTransitRouteTypes().isEmpty()) {
                    travelMode.put(TRANSPORT_MODE_TRANSIT_AVOID_TRANSIT_ROUTE_TYPES, travelOptions.getAvoidTransitRouteTypes());
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
                travelMode.put(TRANSPORT_MODE_CAR_RUSH_HOUR, travelOptions.getRushHour());
                if (travelOptions.getDate() != null)
                    travelMode.put(TRANSPORT_MODE_CAR_DATE, travelOptions.getDate()); //date is on the travelMode level unlike for transit where it is on the "transit frame" level
                if (travelOptions.getTime() != null)
                    travelMode.put(TRANSPORT_MODE_CAR_TIME, travelOptions.getTime()); //time is on the travelMode level unlike for transit where it is on the "transit frame" level
                break;
            default:
                break;
        }

        return travelMode;
    }

    /**
     * Get the travel type for a location (Either a coordinate or a polygon)
     * @param travelOptions
     * @param src
     * @return
     */
    private static TravelType getTravelType(final TravelOptions travelOptions, Location src) {
        TravelType travelType = travelOptions.getTravelType();
        if (src.getTravelType() != null
                && src.getTravelType() != travelType
                && src.getTravelType() != TravelType.UNSPECIFIED) {
            travelType = src.getTravelType();
        }
        return travelType;
    }



    private static JSONObject getSourceObject(final TravelOptions travelOptions,
                                              final Location src) throws JSONException {
        TravelType travelType = getTravelType(travelOptions, src);
        JSONObject travelMode = getTravelMode(travelOptions, travelType);

        JSONObject source = new JSONObject()
                .put(Constants.ID, src.getId());
        if (src instanceof Coordinate) {
            Coordinate coordinate = (Coordinate) src;
            source.put(Constants.LATITUDE, coordinate.getY())
                    .put(Constants.LONGITUDE, coordinate.getX());
        } else if (src instanceof AbstractGeometry) {
            AbstractGeometry geometry = (AbstractGeometry) src;
            source.put(Constants.CRS, geometry.getCrs())
                    .put(Constants.DATA, geometry.getData());
        }
        source.put(Constants.TRANSPORT_MODE, new JSONObject().put(travelType.toString(), travelMode));

        if (travelOptions.getReverse() != null) {
            source.put(Constants.REVERSE, travelOptions.getReverse());
        }
        return source;
    }
}
