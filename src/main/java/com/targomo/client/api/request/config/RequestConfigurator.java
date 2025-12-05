package com.targomo.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.AbstractGeometry;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceAddress;
import com.targomo.client.api.pojo.AggregationConfiguration;
import com.targomo.client.api.pojo.AggregationInputParameters;
import com.targomo.client.api.pojo.Geometry;
import com.targomo.client.api.quality.Location;
import com.targomo.client.api.quality.criterion.CriterionDefinition;
import com.targomo.client.api.request.config.builder.JSONBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
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

    public static String getConfig(final Map<String, CriterionDefinition> criteria, final List<Location> locations, final List<Location> competitors) throws TargomoClientException {
        LOG.trace("Creating configuration...");
        String config = getCommonConfig(criteria, locations, competitors);
        LOG.trace("Configuration created.");
        return config;
    }

    public static String getConfig(final List<Location> locations, final List<Location> competitors) throws TargomoClientException {
        LOG.trace("Creating configuration...");
        String config = getCommonConfig(locations, competitors);
        LOG.trace("Configuration created.");
        return config;
    }

    private static String getCommonConfig(final Map<String, CriterionDefinition> criteria, final List<Location> locations, final List<Location> competitors) throws TargomoClientException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        StringBuilder config = JSONBuilder.beginJson(new StringBuilder());
        try {
            JSONBuilder.append(config, LOCATIONS, ow.writeValueAsString(locations));
            if (competitors != null) {
                JSONBuilder.append(config, COMPETITORS, ow.writeValueAsString(competitors));
            }
            JSONBuilder.appendAndEnd(config, CRITERIA, ow.writeValueAsString(criteria));
        } catch (Exception e) {
            throw new TargomoClientException("Could not generate targomo config object", e);
        }
        return config.toString();
    }

    private static String getCommonConfig(final List<Location> locations, final List<Location> competitors) throws TargomoClientException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        StringBuilder config = JSONBuilder.beginJson(new StringBuilder());
        try {
            if (competitors != null) {
                JSONBuilder.append(config, COMPETITORS, ow.writeValueAsString(competitors));
            }
            JSONBuilder.appendAndEnd(config, LOCATIONS, ow.writeValueAsString(locations));
        } catch (Exception e) {
            throw new TargomoClientException("Could not generate targomo config object", e);
        }
        return config.toString();
    }

	private static String getCommonConfig(final TravelOptions travelOptions) throws TargomoClientException {

	    StringBuilder config = JSONBuilder.beginJson(new StringBuilder());

        try {
            if (travelOptions.getTravelTimes() != null && !travelOptions.getTravelTimes().isEmpty())
                JSONBuilder.append(config, POLYGON, getPolygonObject(travelOptions));

            //attention - at least one multiGraph value must be set to create the multigraph hierarchy
            if ( Stream.of(
                    travelOptions.getMultiGraphEdgeClasses(),
                    travelOptions.getMultiGraphSerializationFormat(),
                    travelOptions.getMultiGraphSerializationDecimalPrecision(),
                    travelOptions.getMultiGraphSerializationMaxGeometryCount(),
                    travelOptions.getMultiGraphSerializationH3MaxBuffer(),
                    travelOptions.getMultiGraphSerializationH3BufferSpeed(),
                    travelOptions.getMultiGraphSerializationH3BufferFixedValue(),
                    travelOptions.getMultiGraphSerializationH3IdFormat(),
                    travelOptions.getMultiGraphDomainType(),
                    travelOptions.getMultiGraphDomainEdgeAggregationType(),
                    travelOptions.getMultiGraphAggregationType(),
                    travelOptions.getMultiGraphAggregationIgnoreOutliers(),
                    travelOptions.getMultiGraphAggregationOutlierPenalty(),
                    travelOptions.getMultiGraphAggregationMinSourcesRatio(),
                    travelOptions.getMultiGraphAggregationMinSourcesCount(),
                    travelOptions.getMultiGraphAggregationSourceValuesLowerBound(),
                    travelOptions.getMultiGraphAggregationSourceValuesUpperBound(),
                    travelOptions.getMultiGraphAggregationSourceValuesModifier(),
                    travelOptions.getMultiGraphAggregationMinResultValue(),
                    travelOptions.getMultiGraphAggregationMinResultValueRatio(),
                    travelOptions.getMultiGraphAggregationMaxResultValue(),
                    travelOptions.getMultiGraphAggregationMaxResultValueRatio(),
                    travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins(),
                    travelOptions.getMultiGraphAggregationGravitationExponent(),
                    travelOptions.getMultiGraphAggregationProbabilityDecay(),
                    travelOptions.getMultiGraphAggregationLogitBetaAttractionStrength(),
                    travelOptions.getMultiGraphAggregationLogitBetaTravelTime(),
                    travelOptions.getMultiGraphAggregationUseProbabilityBasedWeightedAverage(),
                    travelOptions.getMultiGraphAggregationInputParameters(),
                    travelOptions.getMultiGraphAggregationPostAggregationFactor(),
                    travelOptions.getMultiGraphAggregationMathExpression(),
                    travelOptions.getMultiGraphAggregationLearntMaxEdgeWeight(),
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
                JSONBuilder.append(config, MULTIGRAPH, getMultiGraphObject(travelOptions));

            if (travelOptions.getIntersectionMode() != null)
                JSONBuilder.appendString(config, POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode());

            if (travelOptions.getSources() != null && !travelOptions.getSources().isEmpty())
                JSONBuilder.append(config, SOURCES, getSources(travelOptions));

            if (travelOptions.getSourceGeometries() != null && !travelOptions.getSourceGeometries().isEmpty())
                JSONBuilder.append(config, SOURCE_GEOMETRIES, getSourceGeometries(travelOptions));

            if (travelOptions.getSourceAddresses() != null && !travelOptions.getSourceAddresses().isEmpty())
                JSONBuilder.append(config, SOURCE_ADDRESSES, getSourceAddresses(travelOptions));

            if (travelOptions.getTargets() != null && !travelOptions.getTargets().isEmpty())
                JSONBuilder.append(config, TARGETS, getTargets(travelOptions));

            if (travelOptions.getTargetGeohashes() != null && !travelOptions.getTargetGeohashes().isEmpty())
                JSONBuilder.appendStringList(config, TARGET_GEOHASHES, travelOptions.getTargetGeohashes());

            if (travelOptions.getTargetAddresses() != null && !travelOptions.getTargetAddresses().isEmpty())
                JSONBuilder.appendStringList(config, TARGET_ADDRESSES, travelOptions.getTargetAddresses());

            if (travelOptions.getPathSerializer() != null)
                JSONBuilder.appendString(config, PATH_SERIALIZER, travelOptions.getPathSerializer().getPathSerializerName());

            if (travelOptions.getElevationEnabled() != null)
                JSONBuilder.append(config, ENABLE_ELEVATION, travelOptions.getElevationEnabled());

            if (travelOptions.getReverse() != null)
                JSONBuilder.append(config, REVERSE, travelOptions.getReverse());

            if (travelOptions.getEdgeWeightType() != null)
                JSONBuilder.appendString(config, EDGE_WEIGHT, travelOptions.getEdgeWeightType().getKey());

            if (travelOptions.getStatisticGroupId() != null)
                JSONBuilder.appendString(config, STATISTIC_GROUP_ID, travelOptions.getStatisticGroupId());

            if (travelOptions.getServiceUrl() != null)
                JSONBuilder.append(config, "serviceUrl", "\"" + travelOptions.getServiceUrl() + "\"");

            if (travelOptions.getServiceKey() != null)
                JSONBuilder.append(config, "serviceKey", "\"" + travelOptions.getServiceKey() + "\"");

            if (travelOptions.getFormat() != null)
                JSONBuilder.append(config, FORMAT, "\"" + travelOptions.getFormat().toString().toLowerCase() + "\"");

            if (travelOptions.getBoundingBox() != null)
                JSONBuilder.append(config, "boundingBox", "\"" + travelOptions.getBoundingBox() + "\"");

            if (travelOptions.getOsmTypes() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JSONBuilder.append(config, "osmTypes", mapper.writeValueAsString(travelOptions.getOsmTypes()));
            }

            if(travelOptions.getFilterGeometryForPOIs() != null){
                JSONObject filterGeometry = new JSONObject()
                        .put(CRS, travelOptions.getFilterGeometryForPOIs().getCrs())
                        .put(DATA, travelOptions.getFilterGeometryForPOIs().getData());
                JSONBuilder.append(config, "filterGeometry", filterGeometry);
            }

            if(travelOptions.getPoiGravitationExponent() != null){
                JSONBuilder.append(config, "gravitationExponent", travelOptions.getPoiGravitationExponent());
            }

            if(travelOptions.getPoiGravitationProbabilityDecay() != null){
                JSONBuilder.append(config, "probabilityDecay", travelOptions.getPoiGravitationProbabilityDecay());
            }

            if (travelOptions.getTravelTimeFactors() != null && !travelOptions.getTravelTimeFactors().isEmpty()) {
			    JSONObject travelFactors = new JSONObject();
			    for(Map.Entry<String,Double> factor : travelOptions.getTravelTimeFactors().entrySet())
			        travelFactors.put(factor.getKey(),factor.getValue());
                JSONBuilder.append(config, TRAVEL_TIME_FACTORS, travelFactors);
            }

            if ( travelOptions.getClipGeometry() != null )
                JSONBuilder.append(config, CLIP_GEOMETRY, createGeometryJson(travelOptions.getClipGeometry()));

            if ( travelOptions.getExclusionGeometry() != null )
                JSONBuilder.append(config, EXCLUSION_GEOMETRY, createGeometryJson(travelOptions.getExclusionGeometry()));

            if (travelOptions.getMaxSnapDistance() != null)
                JSONBuilder.append(config, MAX_SNAP_DISTANCE, travelOptions.getMaxSnapDistance());

            if (travelOptions.getNextStopsStartTime() != null)
                JSONBuilder.append(config, NEXT_STOPS_START_TIME, travelOptions.getNextStopsStartTime());

            if (travelOptions.getNextStopsEndTime() != null)
                JSONBuilder.append(config, NEXT_STOPS_END_TIME, travelOptions.getNextStopsEndTime());

            if (travelOptions.getExcludeEdgeClasses() != null)
                JSONBuilder.append(config, EXCLUDE_EDGE_CLASSES, new JSONArray(travelOptions.getExcludeEdgeClasses()));

            JSONBuilder.append(config, "onlyPrintReachablePoints", travelOptions.isOnlyPrintReachablePoints());
            
            JSONBuilder.append(config, FORCE_RECALCULATE, travelOptions.isForceRecalculate());
            JSONBuilder.append(config, CACHE_RESULT, travelOptions.isCacheResult());

            JSONBuilder.appendAndEnd(config, MAX_EDGE_WEIGHT, travelOptions.getMaxEdgeWeight());
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
		polygon.put(POLYGON_VALUES, 			 new JSONArray(travelOptions.getTravelTimes()));
		polygon.put(POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode());
		polygon.put(POINT_REDUCTION, 			 travelOptions.getPointReduction());
		polygon.put(MIN_POLYGON_HOLE_SIZE, 	 travelOptions.getMinPolygonHoleSize());

		if ( travelOptions.getSrid() != null )
			polygon.put(SRID, travelOptions.getSrid());

        if ( travelOptions.getPolygonOrientationRule() != null )
            polygon.put(POLYGON_ORIENTATION_RULE, travelOptions.getPolygonOrientationRule());

        if ( travelOptions.getDecimalPrecision() != null )
            polygon.put(DECIMAL_PRECISION, travelOptions.getDecimalPrecision());
		
		if ( travelOptions.getBuffer() != null )
			polygon.put(BUFFER, travelOptions.getBuffer());

        if ( travelOptions.getSimplify() != null )
            polygon.put(SIMPLIFY, travelOptions.getSimplify());

        if ( travelOptions.getQuadrantSegments() != null )
            polygon.put(QUADRANT_SEGMENTS, travelOptions.getQuadrantSegments());

        if ( travelOptions.getPolygonSerializerType() != null )
            polygon.put(SERIALIZER, travelOptions.getPolygonSerializerType().getPolygonSerializerName());

        if ( travelOptions.getIntersectionGeometry() != null )
            polygon.put("intersectionGeometry", createGeometryJson(travelOptions.getIntersectionGeometry()));

		return polygon;
	}

    private static JSONObject getMultiGraphObject(final TravelOptions travelOptions) throws JSONException {

        JSONObject multiGraph = new JSONObject();

        if( travelOptions.getMultiGraphEdgeClasses() != null )
            multiGraph.put(MULTIGRAPH_EDGE_CLASSES, travelOptions.getMultiGraphEdgeClasses());

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
                multiGraphDomain.put(MULTIGRAPH_DOMAIN_TYPE, travelOptions.getMultiGraphDomainType().getKey());

            if( travelOptions.getMultiGraphDomainEdgeAggregationType() != null )
                multiGraphDomain.put(MULTIGRAPH_DOMAIN_EDGE_AGGREGATION_TYPE, travelOptions.getMultiGraphDomainEdgeAggregationType().getKey());

            multiGraph.put( MULTIGRAPH_DOMAIN, multiGraphDomain);
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
                multiGraphLayer.put(MULTIGRAPH_LAYER_TYPE, travelOptions.getMultiGraphLayerType().getKey());

            if ( travelOptions.getMultiGraphLayerGeometryDetailPerTile() != null )
                multiGraphLayer.put(MULTIGRAPH_LAYER_GEOMETRY_DETAIL_PER_TILE, travelOptions.getMultiGraphLayerGeometryDetailPerTile());

            if ( travelOptions.getMultiGraphLayerMinGeometryDetailLevel() != null )
                multiGraphLayer.put(MULTIGRAPH_LAYER_MIN_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerMinGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerMaxGeometryDetailLevel() != null )
                multiGraphLayer.put(MULTIGRAPH_LAYER_MAX_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerMaxGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerGeometryDetailLevel() != null )
                multiGraphLayer.put(MULTIGRAPH_LAYER_GEOMETRY_DETAIL_LEVEL, travelOptions.getMultiGraphLayerGeometryDetailLevel());

            if ( travelOptions.getMultiGraphLayerCustomGeometryMergeAggregation() != null )
                multiGraphLayer.put(MULTIGRAPH_LAYER_CUSTOM_GEOMETRY_MERGE_AGGREGATION, travelOptions.getMultiGraphLayerCustomGeometryMergeAggregation().getKey());

            multiGraph.put( MULTIGRAPH_LAYER, multiGraphLayer);
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
            multiGraphTile.put(MULTIGRAPH_TILE_ZOOM, travelOptions.getMultiGraphTileZoom());
            multiGraphTile.put(MULTIGRAPH_TILE_X, travelOptions.getMultiGraphTileX());
            multiGraphTile.put(MULTIGRAPH_TILE_Y, travelOptions.getMultiGraphTileY());
            multiGraph.put( MULTIGRAPH_TILE, multiGraphTile);
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
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_FORMAT, travelOptions.getMultiGraphSerializationFormat().getKey());

            if ( travelOptions.getMultiGraphSerializationDecimalPrecision() != null )
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_DECIMAL_PRECISION, travelOptions.getMultiGraphSerializationDecimalPrecision());

            if ( travelOptions.getMultiGraphSerializationMaxGeometryCount() != null )
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_MAX_GEOMETRY_COUNT, travelOptions.getMultiGraphSerializationMaxGeometryCount());

            if ( travelOptions.getMultiGraphSerializationH3MaxBuffer() != null )
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_H3_MAX_BUFFER, travelOptions.getMultiGraphSerializationH3MaxBuffer());

            if ( travelOptions.getMultiGraphSerializationH3BufferSpeed() != null )
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_H3_BUFFER_SPEED, travelOptions.getMultiGraphSerializationH3BufferSpeed());

            if ( travelOptions.getMultiGraphSerializationH3BufferFixedValue() != null )
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_H3_BUFFER_FIXED_VALUE, travelOptions.getMultiGraphSerializationH3BufferFixedValue());

            if ( travelOptions.getMultiGraphSerializationH3IdFormat() != null )
                multiGraphSerialization.put(MULTIGRAPH_SERIALIZATION_H3_ID_FORMAT, travelOptions.getMultiGraphSerializationH3IdFormat().getKey());

            multiGraph.put( MULTIGRAPH_SERIALIZATION, multiGraphSerialization);
        }
    }

    private static void addMultiGraphAggregation(TravelOptions travelOptions, JSONObject multiGraph) throws JSONException {
        if (Stream.of(travelOptions.getMultiGraphAggregationType(),
                travelOptions.getMultiGraphAggregationIgnoreOutliers(),
                travelOptions.getMultiGraphAggregationOutlierPenalty(),
                travelOptions.getMultiGraphAggregationMinSourcesRatio(),
                travelOptions.getMultiGraphAggregationMinSourcesCount(),
                travelOptions.getMultiGraphAggregationSourceValuesLowerBound(),
                travelOptions.getMultiGraphAggregationSourceValuesUpperBound(),
                travelOptions.getMultiGraphAggregationSourceValuesModifier(),
                travelOptions.getMultiGraphAggregationMinResultValueRatio(),
                travelOptions.getMultiGraphAggregationMinResultValue(),
                travelOptions.getMultiGraphAggregationMaxResultValueRatio(),
                travelOptions.getMultiGraphAggregationMaxResultValue(),
                travelOptions.getMultiGraphAggregationPostAggregationFactor(),
                travelOptions.getMultiGraphAggregationFilterValuesForSourceOrigins(),
                travelOptions.getMultiGraphAggregationGravitationExponent(),
                travelOptions.getMultiGraphAggregationProbabilityDecay(),
                travelOptions.getMultiGraphAggregationLogitBetaAttractionStrength(),
                travelOptions.getMultiGraphAggregationLogitBetaTravelTime(),
                travelOptions.getMultiGraphAggregationUseProbabilityBasedWeightedAverage(),
                travelOptions.getMultiGraphAggregationInputParameters(),
                travelOptions.getMultiGraphAggregationMathExpression(),
                travelOptions.getMultiGraphAggregationLearntMaxEdgeWeight())
                .anyMatch(Objects::nonNull)) {
            JSONObject multiGraphAggregation = new JSONObject();
            AggregationConfiguration aggregationConfiguration = new AggregationConfiguration.AggregationConfigurationBuilder(travelOptions, false).build();
            fillJsonAggregationConfig(aggregationConfiguration, multiGraphAggregation);
            multiGraph.put(MULTIGRAPH_AGGREGATION, multiGraphAggregation);
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

            multiGraph.put(MULTIGRAPH_PRE_AGGREGATION_PIPELINE, preAggregationPipelineMap);
        }
    }

    private static void fillJsonAggregationConfig(AggregationConfiguration aggregationConfiguration, JSONObject multiGraphAggregation) throws JSONException {

        if (aggregationConfiguration.getType() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_TYPE, aggregationConfiguration.getType().getKey());

        if (aggregationConfiguration.getIgnoreOutliers() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_IGNORE_OUTLIERS, aggregationConfiguration.getIgnoreOutliers());

        if (aggregationConfiguration.getOutlierPenalty() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_OUTLIER_PENALTY, aggregationConfiguration.getOutlierPenalty());

        if (aggregationConfiguration.getMinSourcesRatio() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MIN_SOURCES_RATIO, aggregationConfiguration.getMinSourcesRatio());

        if (aggregationConfiguration.getMinSourcesCount() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MIN_SOURCES_COUNT, aggregationConfiguration.getMinSourcesCount());

        if (aggregationConfiguration.getSourceValuesLowerBound() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_SOURCE_VALUES_LOWER_BOUND, aggregationConfiguration.getSourceValuesLowerBound());

        if (aggregationConfiguration.getSourceValuesUpperBound() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_SOURCE_VALUES_UPPER_BOUND, aggregationConfiguration.getSourceValuesUpperBound());

        if (aggregationConfiguration.getSourceValuesModifier() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_SOURCE_VALUES_MODIFIER, aggregationConfiguration.getSourceValuesModifier());

        if (aggregationConfiguration.getMinResultValueRatio() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MIN_RESULT_VALUE_RATIO, aggregationConfiguration.getMinResultValueRatio());

        if (aggregationConfiguration.getMinResultValue() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MIN_RESULT_VALUE, aggregationConfiguration.getMinResultValue());

        if (aggregationConfiguration.getMaxResultValueRatio() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE_RATIO, aggregationConfiguration.getMaxResultValueRatio());

        if (aggregationConfiguration.getMaxResultValue() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MAX_RESULT_VALUE, aggregationConfiguration.getMaxResultValue());

        if (aggregationConfiguration.getPostAggregationFactor() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_POST_AGGREGATION_FACTOR, aggregationConfiguration.getPostAggregationFactor());

        if (aggregationConfiguration.getGravitationExponent() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_GRAVITATION_EXPONENT, aggregationConfiguration.getGravitationExponent());

        if (aggregationConfiguration.getProbabilityDecay() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_PROBABILITY_DECAY, aggregationConfiguration.getProbabilityDecay());

        if (aggregationConfiguration.getLogitBetaAttractionStrength() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_LOGIT_BETA_ATTRACTION_STRENGTH, aggregationConfiguration.getLogitBetaAttractionStrength());

        if (aggregationConfiguration.getLogitBetaTravelTime() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_LOGIT_BETA_TRAVEL_TIME, aggregationConfiguration.getLogitBetaTravelTime());

        if (aggregationConfiguration.getUseProbabilityBasedWeightedAverage() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_USE_PROBABILITY_BASED_WEIGHTED_AVERAGE, aggregationConfiguration.getUseProbabilityBasedWeightedAverage());

        if (aggregationConfiguration.getFilterValuesForSourceOrigins() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_FILTER_VALUES_FOR_SOURCE_ORIGINS, aggregationConfiguration.getFilterValuesForSourceOrigins());

        if (aggregationConfiguration.getAggregationInputParameters() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS, buildAggregationInputParameters(aggregationConfiguration.getAggregationInputParameters()));

        if (aggregationConfiguration.getMathExpression() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_MATH_EXPRESSION, aggregationConfiguration.getMathExpression());

        if (aggregationConfiguration.getLearntMaxEdgeWeight() != null)
            multiGraphAggregation.put(MULTIGRAPH_AGGREGATION_LEARNT_MAX_EDGE_WEIGHT, aggregationConfiguration.getLearntMaxEdgeWeight());
    }

    private static JSONObject buildAggregationInputParameters(Map<String, AggregationInputParameters> aggregationInputParameters) throws JSONException {
        JSONObject aggregationInputParams = new JSONObject();
        if (aggregationInputParameters != null) {
            for (Map.Entry<String, AggregationInputParameters> entry : aggregationInputParameters.entrySet()) {
                String name = entry.getKey();
                AggregationInputParameters param = entry.getValue();
                JSONObject sourceParam = new JSONObject();

                if (param.getInputFactor() != null)
                    sourceParam.put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_FACTOR, param.getInputFactor());

                if (param.getGravitationAttractionStrength() != null)
                    sourceParam.put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_ATTRACTION_STRENGTH, param.getGravitationAttractionStrength());

                if (param.getGravitationPositiveInfluence() != null)
                    sourceParam.put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_POSITIVE_INFLUENCE, param.getGravitationPositiveInfluence());

                if (param.getGravitationCompetingPositiveInfluence() != null)
                    sourceParam.put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_COMPETING_POSITIVE_INFLUENCE, param.getGravitationCompetingPositiveInfluence());

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

    private static JSONArray getSourceAddresses(final TravelOptions travelOptions) throws JSONException {
        JSONArray sourceAddresses = new JSONArray();
        for (DefaultSourceAddress src : travelOptions.getSourceAddresses().values()) {

            List<TravelType> travelTypes = getTravelTypes(travelOptions, src);
            JSONObject travelMode = getTravelMode(travelOptions, travelTypes);

            JSONObject source = new JSONObject()
                    .put(H3_ADDRESS, src.getH3Address());

            addTransportationMode(travelOptions, src, source, travelTypes, travelMode);

            sourceAddresses.put(source);
        }
        return sourceAddresses;
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
        JSONBuilder.appendString(targetsBuilder, ID, trg.getId());
        JSONBuilder.append(targetsBuilder,       LATITUDE, trg.getY());
        JSONBuilder.appendAndEnd(targetsBuilder, LONGITUDE, trg.getX());
        return targetsBuilder;
    }

    private static JSONObject getTravelMode(final TravelOptions travelOptions,
                                            final List<TravelType> travelTypes) throws JSONException {
        JSONObject travelMode = new JSONObject();

        if (travelOptions.isAllowPrivateAndServiceRoads())
            travelMode.put(ALLOW_PRIVATE_AND_SERVICE_ROADS, travelOptions.isAllowPrivateAndServiceRoads());

        if (travelOptions.getTrafficJunctionPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_JUNCTION_PENALTY, travelOptions.getTrafficJunctionPenalty());
        if (travelOptions.getTrafficSignalPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_SIGNAL_PENALTY, travelOptions.getTrafficSignalPenalty());
        if (travelOptions.getTrafficLeftTurnPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_LEFT_TURN_PENALTY, travelOptions.getTrafficLeftTurnPenalty());
        if (travelOptions.getTrafficRightTurnPenalty() != null)
            travelMode.put(TRANSPORT_MODE_TRAFFIC_RIGHT_TURN_PENALTY, travelOptions.getTrafficRightTurnPenalty());

        Set<TravelType> includedTravelTypes = new HashSet<>(travelTypes);

        // transit parameters
        // TRANSIT==WALK_TRANSIT (BIKE_TRANSIT not really supported hence it is left out)
        if (includedTravelTypes.contains(TravelType.WALKTRANSIT) || includedTravelTypes.contains(TravelType.TRANSIT)) {
            travelMode.put(TRANSPORT_MODE_TRANSIT_FRAME, new JSONObject()
                    .put(TRANSPORT_MODE_TRANSIT_FRAME_TIME, travelOptions.getTime())
                    .put(TRANSPORT_MODE_TRANSIT_FRAME_DATE, travelOptions.getDate())
                    .put(TRANSPORT_MODE_TRANSIT_FRAME_WEEKDAY, travelOptions.getWeekday())
                    .put(TRANSPORT_MODE_TRANSIT_FRAME_DURATION, travelOptions.getFrame())
                    .put(TRANSPORT_MODE_TRANSIT_FRAME_ARRIVAL_OR_DEPARTURE_DURATION, travelOptions.getArrivalOrDepartureDuration())
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
            travelMode.put(TRANSPORT_MODE_TRANSIT_RECOMMENDATIONS, travelOptions.getRecommendations());
            travelMode.put(TRAVEL_MODE_UPHILL, travelOptions.getWalkUphill());
            travelMode.put(TRAVEL_MODE_DOWNHILL, travelOptions.getWalkDownhill());
        }

        // walk parameters
        if (includedTravelTypes.contains(TravelType.WALK)) {
            travelMode.put(TRAVEL_MODE_UPHILL, travelOptions.getWalkUphill());
            travelMode.put(TRAVEL_MODE_DOWNHILL, travelOptions.getWalkDownhill());
        }
        if (travelTypes.size() == 1 && (includedTravelTypes.contains(TravelType.WALKTRANSIT) || includedTravelTypes.contains(TravelType.TRANSIT) || includedTravelTypes.contains(TravelType.WALK))) {
            // if the only travel mode is walk or transit we need to use "speed" for walk speed
            travelMode.put(TRAVEL_MODE_SPEED, travelOptions.getWalkSpeed());
        }
        else {
            // we need walk speed even for car and bike mode (if snap distance is included in travel time)
            travelMode.put(TRAVEL_MODE_WALK_SPEED, travelOptions.getWalkSpeed());
        }

        // bike parameters
        if (includedTravelTypes.contains(TravelType.BIKE)) {
            if (travelTypes.size() == 1) {
                // if the only travel mode is bike we need to use "speed" for bike speed
                travelMode.put(TRAVEL_MODE_SPEED, travelOptions.getBikeSpeed());
            }
            else {
                travelMode.put(TRAVEL_MODE_BIKE_SPEED, travelOptions.getBikeSpeed());
            }
            travelMode.put(TRAVEL_MODE_UPHILL, travelOptions.getBikeUphill());
            travelMode.put(TRAVEL_MODE_DOWNHILL, travelOptions.getBikeDownhill());
        }

        // car parameters
        if (includedTravelTypes.contains(TravelType.CAR)) {
            travelMode.put(TRANSPORT_MODE_CAR_RUSH_HOUR, travelOptions.getRushHour());
            if (travelOptions.getDate() != null)
                travelMode.put(TRANSPORT_MODE_CAR_DATE, travelOptions.getDate());//date is on the travelMode level unlike for transit where it is on the "transit frame" level
            if (travelOptions.getWeekday() != null)
                travelMode.put(TRANSPORT_MODE_CAR_WEEKDAY, travelOptions.getWeekday());//weekday is on the travelMode level unlike for transit where it is on the "transit frame" level
            if (travelOptions.getTime() != null)
                travelMode.put(TRANSPORT_MODE_CAR_TIME, travelOptions.getTime()); //time is on the travelMode level unlike for transit where it is on the "transit frame" level
        }

        // snapping parameters
        if (travelOptions.getIncludeSnapDistance() != null)
            travelMode.put(INCLUDE_SNAP_DISTANCE, travelOptions.getIncludeSnapDistance());
        if (travelOptions.getIncludeSnapDistanceForTargets() != null)
            travelMode.put(INCLUDE_SNAP_DISTANCE_FOR_TARGETS, travelOptions.getIncludeSnapDistanceForTargets());
        if (travelOptions.getUseAreaSnapping() != null)
            travelMode.put(USE_AREA_SNAPPING, travelOptions.getUseAreaSnapping());
        if (travelOptions.getSnapRadius() != null) // todo: check if that's right ?
            travelMode.put(SNAP_RADIUS, travelOptions.getSnapRadius());
        if (travelOptions.getExcludeEdgeClassesFromSnapping() != null)
            travelMode.put(EXCLUDE_EDGE_CLASSES_FROM_SNAPPING, new JSONArray(travelOptions.getExcludeEdgeClassesFromSnapping()));
        if (travelOptions.getMaxSnapDistance() != null)
            travelMode.put(MAX_SNAP_DISTANCE, travelOptions.getMaxSnapDistance());
        if(travelOptions.getSnappingSpeed() != null)
            travelMode.put(TRAVEL_MODE_SNAPPING_SPEED, travelOptions.getSnappingSpeed());

        return travelMode;
    }

    /**
     * Get the travel type for a location (Either a coordinate or a polygon)
     * @param travelOptions
     * @param src
     * @return
     */
    private static List<TravelType> getTravelTypes(final TravelOptions travelOptions, com.targomo.client.api.geo.Location src) {
        List<TravelType> travelTypes = travelOptions.getTravelTypes();
        if (CollectionUtils.isNotEmpty(src.getTravelTypes())
                && !src.getTravelTypes().equals(travelTypes)) {
            travelTypes = src.getTravelTypes();
        }
        return travelTypes;
    }

    private static JSONObject getSourceObject(final TravelOptions travelOptions,
                                              final com.targomo.client.api.geo.Location src) throws JSONException {
        List<TravelType> travelTypes = getTravelTypes(travelOptions, src);
        JSONObject travelMode = getTravelMode(travelOptions, travelTypes);

        JSONObject source = new JSONObject()
                .put(ID, src.getId());
        if (src instanceof Coordinate) {
            Coordinate coordinate = (Coordinate) src;
            source.put(LATITUDE, coordinate.getY())
                    .put(LONGITUDE, coordinate.getX());
        } else if (src instanceof AbstractGeometry) {
            AbstractGeometry geometry = (AbstractGeometry) src;
            source.put(CRS, geometry.getCrs())
                    .put(DATA, geometry.getData())
                    .put(ROUTE_FROM_CENTROID, geometry.isRouteFromCentroid());
        }
        addTransportationMode(travelOptions, src, source, travelTypes, travelMode);
        return source;
    }

    private static void addTransportationMode(TravelOptions travelOptions, com.targomo.client.api.geo.Location src, JSONObject source, List<TravelType> travelTypes, JSONObject travelMode) throws JSONException {
        String transportationModeKey;
        if (CollectionUtils.isNotEmpty(travelTypes)) {
            if (travelTypes.size() == 1) {
                transportationModeKey = travelTypes.get(0).toString();
            }
            else {
                transportationModeKey = TRAVEL_TYPE_MULTIMODAL;
                JSONArray travelTypesArray = new JSONArray();
                for (TravelType travelType : travelTypes) {
                    travelTypesArray.put(travelType.toString());
                }
                travelMode.put(TRAVEL_MODE_MULTIMODAL_TRAVELTYPES, travelTypesArray);
            }
            source.put(TRANSPORT_MODE, new JSONObject().put(transportationModeKey, travelMode));
        }
        if(src.getProperties() != null){
            source.put(PROPERTIES, new JSONObject()
                    .put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_FACTOR, src.getProperties().getInputFactor())
                    .put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_ATTRACTION_STRENGTH, src.getProperties().getGravitationAttractionStrength())
                    .put(MULTIGRAPH_AGGREGATION_INPUT_PARAMETERS_GRAVITATION_POSITIVE_INFLUENCE, src.getProperties().getGravitationPositiveInfluence())
            );
        }

        if (travelOptions.getReverse() != null) {
            source.put(REVERSE, travelOptions.getReverse());
        }
    }

    /**
     * Parse a geometry into a json object
     * @param geometry The Geometry to be parsed
     * @throws JSONException If failing to add any of the geometry qualities
     */
    private static JSONObject createGeometryJson(Geometry geometry) throws JSONException {
        JSONObject geometryPolygon = new JSONObject();
        geometryPolygon.put(CRS,  geometry.getCrs());
        geometryPolygon.put(DATA, geometry.getData());
        // has to be geojson ATM
        geometryPolygon.put("type", geometry.getType());
        return geometryPolygon;
    }
}
