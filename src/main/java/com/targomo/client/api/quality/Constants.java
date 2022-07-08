package com.targomo.client.api.quality;

public class Constants {
    public static final String SOURCE_TYPE_POI = "poi";
    public static final String SOURCE_TYPE_POI_IN_ZONE = "poiInZone";
    public static final String SOURCE_TYPE_STATISTICS = "statistics";
    public static final String SOURCE_TYPE_STATISTICS_IN_ZONE = "statisticsInZone";
    public static final String SOURCE_TYPE_STATISTICS_GRAVITATION = "gravitation";
    public static final String SOURCE_TYPE_POI_GRAVITATION = "poiGravitation";
    public static final String SOURCE_TYPE_EDGE_STATISTICS = "edgeStatistics";
    public static final String SOURCE_TYPE_MATH_AGGREGATION = "mathAggregation";
    public static final String SOURCE_TYPE_MOBILITY = "mobility";
    public static final String SOURCE_TYPE_TRANSIT_STOPS = "transitStops";
    public static final String CRITERION_TYPE_POI_COVERAGE_COUNT = "poiCoverageCount";
    public static final String CRITERION_TYPE_POI_COVERAGE_DISTANCE = "poiCoverageDistance";
    public static final String CRITERION_TYPE_CLOSEST_POI_DISTANCE = "closestPoiDistance";
    public static final String CRITERION_TYPE_POI_COUNT_IN_ZONE = "poiCountInZone";
    public static final String CRITERION_TYPE_STATISTICS_SUM = "statisticsSum";
    public static final String CRITERION_TYPE_STATISTICS_SUM_IN_ZONE = "statisticsSumInZone";
    public static final String CRITERION_TYPE_STATISTICS_DISTANCE = "statisticsDistance";
    public static final String CRITERION_TYPE_GRAVITATION_SUM = "gravitationSum";
    public static final String CRITERION_TYPE_POI_GRAVITATION_SUM = "poiGravitationSum";
    public static final String CRITERION_TYPE_EDGE_STATISTICS = "edgeStatistics";
    public static final String CRITERION_TYPE_MATH_AGGREGATION = "mathAggregation";
    public static final String CRITERION_TYPE_STAYPOINT_COUNT = "staypointCount";
    public static final String CRITERION_TYPE_TRANSIT_STOPS_SUM = "transitStopsSum";
    public static final String CRITERION_TYPE_TRANSIT_STOPS_DISTANCE = "transitStopsDistance";
    public static final String CRITERION_TYPE_REFERENCE = "criterionReference";
    public static final String QUALITY_SERVICE_NAME = "quality";
    public static final String LOCATIONS_SCORE_SERVICE_NAME = "scores";
    public static final String LOCATIONS_RATING_SERVICE_NAME = "rating";
    public static final String MVT_RATING_SERVICE_NAME = "mvt_rating";
    public static final String MVT_CRITERION_SERVICE_NAME = "mvt_criterion";
    public static final int NB_BUNDESLANDER = 16;
    public static final int DECIMAL_PRECISION_CRS_4326_FOR_GEOJSON_SERIALIZATION = 6;
    public static final String SERIALIZATION_FORMAT_GEOJSON = "geojson";
    public static final String SERIALIZATION_FORMAT_MAPBOX_VECTOR_TILES = "mvt";
    public static final String QUALITY_TILES_NAME = "quality";
    public static final String REFERENCE_AREAS_TILES_NAME = "reference-areas";

    private Constants() {
    }
}
