package net.motionintelligence.client.api.request.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

            //attention - at least one multiGraph value must be set to create the multigraph hierarchy
            if ( Stream.of(travelOptions.getMultiGraphEdgeClasses(), travelOptions.getMultiGraphSerializationType(),
                    travelOptions.getMultiGraphSerializationSrid(), travelOptions.getMultiGraphSerializationDecimalPrecision(),
                    travelOptions.getMultiGraphSerializationIncludeEdges(), travelOptions.getMultiGraphAggregationType(),
                    travelOptions.getMultiGraphAggregationIgnoreOutlier(), travelOptions.getMultiGraphAggregationOutlierPenalty(),
                    travelOptions.getMultiGraphAggregationMinSourcesCount(), travelOptions.getMultiGraphAggregationMinSourcesRatio(),
                    travelOptions.getMultiGraphAggregationMaxResultValue(), travelOptions.getMultiGraphAggregationMaxResultValueRatio(),
                    travelOptions.getMultiGraphLayerType())
                    .anyMatch(Objects::nonNull))
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

            JSONBuilder.append(config, "onlyPrintReachablePoints", travelOptions.getOnlyPrintReachablePoints());
            JSONBuilder.appendAndEnd(config, Constants.MAX_EDGE_WEIGTH, travelOptions.getMaxEdgeWeight());
        }
        catch (Exception e) {
            throw new Route360ClientException("Could not generate r360 config object", e);
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
        polygon.put(Constants.POLYGON_VALUES,              new JSONArray(travelOptions.getTravelTimes()));
        polygon.put(Constants.POLYGON_INTERSECTION_MODE, travelOptions.getIntersectionMode().getKey());
        polygon.put(Constants.POINT_REDUCTION,              travelOptions.isPointReduction());
        polygon.put(Constants.MIN_POLYGON_HOLE_SIZE,      travelOptions.getMinPolygonHoleSize());

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

        return polygon;
    }

    private static JSONObject getMultiGraphObject(final TravelOptions travelOptions) throws JSONException {

        JSONObject multigraph = new JSONObject();

        if ( travelOptions.getMultiGraphEdgeClasses() != null )
            multigraph.put(Constants.MULTIGRAPH_EDGE_CLASSES, travelOptions.getMultiGraphEdgeClasses());

        if ( travelOptions.getMultiGraphLayerType() != null )
            multigraph.put(Constants.MULTIGRAPH_LAYER_TYPE, travelOptions.getMultiGraphLayerType().getKey());


        if( Stream.of(travelOptions.getMultiGraphSerializationType(), travelOptions.getMultiGraphSerializationSrid(),
                travelOptions.getMultiGraphSerializationDecimalPrecision(),travelOptions.getMultiGraphSerializationIncludeEdges())
                .anyMatch(Objects::nonNull) ) {
            JSONObject multigraphSerialization = new JSONObject();

            if ( travelOptions.getMultiGraphSerializationType() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_TYPE, travelOptions.getMultiGraphSerializationType().getKey());

            if ( travelOptions.getMultiGraphSerializationIncludeEdges() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_EDGES_INCLUDED, travelOptions.getMultiGraphSerializationIncludeEdges());

            if ( travelOptions.getMultiGraphSerializationSrid() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_SRID, travelOptions.getMultiGraphSerializationSrid());

            if ( travelOptions.getMultiGraphSerializationDecimalPrecision() != null )
                multigraphSerialization.put(Constants.MULTIGRAPH_SERIALIZATION_DECIMAL_PRECISION, travelOptions.getMultiGraphSerializationDecimalPrecision());

            multigraph.put( Constants.MULTIGRAPH_SERIALIZATION, multigraphSerialization);
        }

        if( Stream.of(travelOptions.getMultiGraphAggregationType(), travelOptions.getMultiGraphAggregationIgnoreOutlier(),
                travelOptions.getMultiGraphAggregationOutlierPenalty(), travelOptions.getMultiGraphAggregationMinSourcesCount(),
                travelOptions.getMultiGraphAggregationMinSourcesRatio(), travelOptions.getMultiGraphAggregationMaxResultValue(),
                travelOptions.getMultiGraphAggregationMaxResultValueRatio()).anyMatch(Objects::nonNull) ) {

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

            multigraph.put( Constants.MULTIGRAPH_AGGREGATION, multigraphAggregation);
        }

        return multigraph;
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
                //FIXME walkspeeds should be able to be defined here too
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

        //FIXME probably into TRANSIT
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
