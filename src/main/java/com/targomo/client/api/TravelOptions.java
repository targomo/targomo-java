package com.targomo.client.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.enums.*;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.geo.*;
import com.targomo.client.api.json.*;
import com.targomo.client.api.pojo.AggregationConfiguration;
import com.targomo.client.api.pojo.AggregationInputParameters;
import com.targomo.client.api.pojo.Geometry;
import com.targomo.client.api.request.PolygonRequest;
import com.targomo.client.api.request.ReachabilityRequest;
import com.targomo.client.api.request.RouteRequest;
import com.targomo.client.api.request.TimeRequest;
import com.targomo.client.api.statistic.PoiType;
import com.targomo.client.api.util.SerializationUtil;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Common configuration class for executing all requests.
 * Define source points, target points and other configuration values such as travel times, elevation etc. here.
 * See: {@link PolygonRequest},
 * {@link RouteRequest},
 * {@link TimeRequest},
 * {@link ReachabilityRequest}.
 */

@Entity @Data
@Table(name = "travel_option")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TravelOptions implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private Integer id;

    @JsonDeserialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapSerializer.class)
    @Transient
    private Map<String,Coordinate> sources  = new HashMap<>();

    @JsonDeserialize(contentAs=DefaultSourceAddress.class, using=DefaultSourceAddressMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceAddress.class, using=DefaultSourceAddressMapSerializer.class)
    @Transient
    private Map<String, DefaultSourceAddress> sourceAddresses  = new HashMap<>();

    @JsonDeserialize(contentAs= DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapDeserializer.class)
    @JsonSerialize(contentAs= DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapSerializer.class)
    @Transient
    private Map<String, AbstractGeometry> sourceGeometries = new HashMap<>();

    @JsonDeserialize(contentAs=DefaultTargetCoordinate.class, using=DefaultTargetCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultTargetCoordinateMapSerializer.class)
    @Transient
    private Map<String,Coordinate> targets  = new HashMap<>();

    @Transient
    private List<String> targetGeohashes = new ArrayList<>();

    @Transient
    private List<String> targetAddresses = new ArrayList<>();

    @Column(name = "bike_speed")
    private double bikeSpeed         = 18.0;

    @Column(name = "bike_uphill")
    private double bikeUphill        = 20.0;

    @Column(name = "bike_downhill")
    private double bikeDownhill      = -10.0;

    @Column(name = "walk_speed")
    private double walkSpeed         = 5.0;

    @Column(name = "walk_uphill")
    private double walkUphill        = 10.0;

    @Column(name = "walk_downhill")
    private double walkDownhill      = 0.0;

    @Column(name = "rush_hour")
    private Boolean rushHour         = false;

    @Transient
    private boolean allowPrivateAndServiceRoads = false;

    //the following four setting are only used for bike, car (and bike-transit)
    @Transient
    private Integer trafficJunctionPenalty  = null;
    @Transient
    private Integer trafficSignalPenalty    = null;
    @Transient
    private Integer trafficLeftTurnPenalty  = null;
    @Transient
    private Integer trafficRightTurnPenalty = null;

    @Transient private List<Integer> travelTimes                    = Arrays.asList(600, 1200, 1800);

    /**
     * If there is more than one element in the travelTypes list, multi modal routing will be used.
     */
    @Column(name = "travel_types")
    private List<TravelType> travelTypes                            = Collections.emptyList();

    @Transient
	private Map<String,Double> travelTimeFactors 	            	= new HashMap<>();

    @Column(name = "elevation_enabled")
    private Boolean elevationEnabled                                = true;

    @Transient private Boolean appendTravelTimes                    = false;
    @Transient private Boolean pointReduction                       = true;
    @Transient private Boolean reverse                              = false;
    @Transient private Long minPolygonHoleSize                      = 100000000L;

    @Column(name = "time") private Integer time                     = null; //default is used in core
    @Column(name = "date")  private Integer date                    = null; //default is used in core
    @Column(name = "frame") private Integer frame                   = null; //default is used in core
    @Column(name = "earliestArrival") private Boolean earliestArrival = false;
    @Transient private Integer arrivalOrDepartureDuration           = null;
    @Transient private Integer maxWalkingTimeFromSource             = null;
    @Transient private Integer maxWalkingTimeToTarget               = null;
    @Transient private Integer recommendations                      = 0;
    @Transient private Integer srid                                 = null;
    @Transient private PolygonOrientationRule polygonOrientationRule = null;
    @Transient private Integer decimalPrecision                     = null;

    // maximum number of transfers when using public transportation
    @Column(name = "max_transfers") private Integer maxTransfers    = null;

    // Transit route types that should not be used for routing
    @Transient private List<Integer> avoidTransitRouteTypes         = Collections.emptyList();

    @Transient private Double buffer                                = null;
    @Transient private Double simplify                              = null;
    @Transient private Integer quadrantSegments                     = null;
    @Transient private PolygonIntersectionMode intersectionMode     = PolygonIntersectionMode.UNION;
    @Transient private PathSerializerType pathSerializer            = PathSerializerType.COMPACT_PATH_SERIALIZER;
    @Transient private PolygonSerializerType polygonSerializerType  = PolygonSerializerType.JSON_POLYGON_SERIALIZER;
    @Transient private Integer maxSnapDistance                                               = null;

    @Transient private Set<Integer> multiGraphEdgeClasses                                    = null;
    @Transient private MultiGraphDomainType multiGraphDomainType                             = null;
    @Transient private MultiGraphDomainEdgeAggregationType multiGraphDomainEdgeAggregationType = null;
    @Transient private MultiGraphSerializationFormat multiGraphSerializationFormat           = null;
    @Transient private Integer multiGraphSerializationDecimalPrecision                       = null;
    @Transient private Integer multiGraphSerializationMaxGeometryCount                       = null;
    @Transient private Integer multiGraphSerializationH3MaxBuffer            = null;
    @Transient private Float multiGraphSerializationH3BufferSpeed            = null;
    @Transient private Boolean multiGraphSerializationH3BufferFixedValue     = null;
    @Transient private MultiGraphSerializationH3IdFormat multiGraphSerializationH3IdFormat = null;
    @Transient private MultiGraphAggregationType multiGraphAggregationType                   = null;
    @Transient private Boolean multiGraphAggregationIgnoreOutliers                           = null;
    @Transient private Float multiGraphAggregationOutlierPenalty                             = null;
    @Transient private Double multiGraphAggregationMinSourcesRatio                           = null;
    @Transient private Integer multiGraphAggregationMinSourcesCount                          = null;
    @Transient private Float multiGraphAggregationSourceValuesLowerBound                     = null;
    @Transient private Float multiGraphAggregationSourceValuesUpperBound                     = null;
    @Transient
    private Float multiGraphAggregationSourceValuesModifier                                  = null;
    @Transient private Double multiGraphAggregationMinResultValueRatio                       = null;
    @Transient private Float multiGraphAggregationMinResultValue                             = null;
    @Transient private Double multiGraphAggregationMaxResultValueRatio                       = null;
    @Transient private Float multiGraphAggregationMaxResultValue                             = null;
    @Transient private String multiGraphAggregationMathExpression                            = null;
    @Transient private Set<String> multiGraphAggregationFilterValuesForSourceOrigins         = null;
    @Transient private Double multiGraphAggregationGravitationExponent                       = null;
    @Transient private Double multiGraphAggregationProbabilityDecay                          = null;
    @Transient private Double multiGraphAggregationLogitBetaAttractionStrength               = null;
    @Transient private Double multiGraphAggregationLogitBetaTravelTime                       = null;
    @Transient private Boolean multiGraphAggregationUseProbabilityBasedWeightedAverage       = null;
    @Transient private Float multiGraphAggregationPostAggregationFactor                      = null;
    @Transient private Map<String, AggregationInputParameters> multiGraphAggregationInputParameters = null;
    @Transient private LinkedHashMap<String, AggregationConfiguration> multiGraphPreAggregationPipeline = null;
    @Transient private MultiGraphLayerType multiGraphLayerType                               = null;
    @Transient private Integer multiGraphLayerGeometryDetailPerTile                          = null;
    @Transient private Integer multiGraphLayerMinGeometryDetailLevel                         = null;
    @Transient private Integer multiGraphLayerMaxGeometryDetailLevel                         = null;
    @Transient private Integer multiGraphLayerGeometryDetailLevel                            = null;
    @Transient private MultiGraphLayerCustomGeometryMergeAggregation multiGraphLayerCustomGeometryMergeAggregation = null;
    @Transient private Integer multiGraphTileZoom                                            = null;
    @Transient private Integer multiGraphTileX                                               = null;
    @Transient private Integer multiGraphTileY                                               = null;
    @Transient private Geometry clipGeometry                                                 = null;
    @Transient private Integer multiGraphH3FixedZoomLevel                                    = null;


    @Column(name = "max_edge_weight") private Integer maxEdgeWeight            = 1800;
    @Column(name = "service_url") private String serviceUrl                    = "";
    @Column(name = "service_key") private String serviceKey                    = "";
    @Transient private boolean onlyPrintReachablePoints                        = true;

    @JsonProperty("edgeWeight")
    @Column(name = "edge_weight_type") private EdgeWeightType edgeWeightType   = EdgeWeightType.TIME;

	@Column(name = "statistic_group_id") private Integer statisticGroupId;
    @Column(name = "statistic_service_url") private String statisticServiceUrl = "https://api.targomo.com/statistics/";
	@Column(name = "poi_service_url") private String pointOfInterestServiceUrl = "https://api.targomo.com/pointofinterest/";

	@Column(name = "overpass_query") private String overpassQuery;
	@Column(name = "overpass_service_url") private String overpassServiceUrl = "https://api.targomo.com/overpass/";

    @Column(name = "inter_service_key") private String interServiceKey = "";

    @Transient
    private String interServiceRequestType = "";

	@Transient
	private Format format;

	@Transient
	private Geometry intersectionGeometry;

    @Transient
    private Geometry exclusionGeometry;

    @Transient
    private List<Integer> excludeEdgeClasses;

	@Transient
	private String boundingBox;

    @Transient
    private Set<PoiType> osmTypes = new HashSet<>();

    @Transient
    private Set<PoiType> customPois = new HashSet<>();

    @Transient
    @JsonProperty("filterGeometry")
    private AbstractGeometry filterGeometryForPOIs;

    @Transient
    @JsonProperty("gravitationExponent")
    private Double poiGravitationExponent;

    @Transient
    @JsonProperty("probabilityDecay")
    private Double poiGravitationProbabilityDecay;

    @Transient
    private boolean forceRecalculate = false;

    @Transient
    private boolean cacheResult = true;

    //parameters for requesting "transit/stops" endpoint - not for routing
    @Transient
    private Integer nextStopsStartTime;
    @Transient
    private Integer nextStopsEndTime;

    @Transient
    private Boolean includeSnapDistance;

    @Transient
    private Boolean includeSnapDistanceForTargets;

    @Transient
    private Boolean useAreaSnapping;

    // snap radius is in meters
    @Transient
    private Integer snapRadius;

    @Transient
    private List<Integer> excludeEdgeClassesFromSnapping;
    @Transient
    private Integer multiGraphAggregationLearntMaxEdgeWeight;


    /**
     * Set the travel type to use when routing.
     */
    @JsonProperty("travelType")
    public void setTravelType(TravelType type) {
        setTravelTypes(type == null ? Collections.emptyList() : Collections.singletonList(type));
    }

    /**
     *
     * @return source coordinates array
     */
    @JsonIgnore
    public double[][] getSourceCoordinates() {
        return getCoordinates(this.sources);
    }

    /**
     *
     * @return target coordinates array
     */
    @JsonIgnore
    public double[][] getTargetCoordinates(){
        return getCoordinates(this.targets);
    }

    /**
     * Convert a map of Coordinate ID, Coordinate value into double arrays
     * @param points map of coordinates
     * @return coordinates array in the form of [[x0, y0], [x1, y1]]
     */
    private double[][] getCoordinates(Map<String, Coordinate> points) {

        Coordinate[] pointList = points.values().toArray(new Coordinate[0]);

        double[][] coordinates = new double[points.size()][];
        for ( int i = 0 ; i < points.size() ; i ++ )
            coordinates[i] = new double[]{pointList[i].getX(), pointList[i].getY()};

        return coordinates;
    }

    /**
     * @param targets add all specified targets to the target map
     */
    public void addAllTargets(Map<String,Coordinate> targets) {
        this.targets.putAll(targets);
    }

   /**
    * @param targets add all specified targets to the target map using their ID as key
    */
    public void addAllTargets(Collection<Coordinate> targets) {
        this.targets = targets.stream().collect(Collectors.toMap(Location::getId, Function.identity()));
    }

    public void addAllTargetGeohashes(List<String> geohashes){
        this.targetGeohashes.addAll(geohashes);
    }

    public void addAllTargetAddresses(List<String> targetAddresses){
        this.targetAddresses.addAll(targetAddresses);
    }

    /**
     * This function will be removed in a future release.
     * Use maxEdgeWeight and edgeWeightType instead.
     * @return the maxRoutingTime
     */
    @JsonIgnore
    @Deprecated
    public int getMaxRoutingTime() {
        if (edgeWeightType == EdgeWeightType.TIME) {
            return maxEdgeWeight;
        }
        return 0;
    }

    /**
     * This function will be removed in a future release.
     * Use maxEdgeWeight and edgeWeightType instead.
     * @param maxRoutingTime the maxRoutingTime to set
     */
    @JsonIgnore
    @Deprecated
    public void setMaxRoutingTime(int maxRoutingTime) {
        this.maxEdgeWeight = maxRoutingTime;
        this.edgeWeightType = EdgeWeightType.TIME;
    }

    /**
     * This function will be removed in a future release.
     * Use maxEdgeWeight and edgeWeightType instead.
     * @return the maxRoutingLength
     */
    @JsonIgnore
    @Deprecated
    public Integer getMaxRoutingLength() {
        if (edgeWeightType == EdgeWeightType.DISTANCE) {
            return maxEdgeWeight;
        }
        return 0;
    }

    /**
     * This function will be removed in a future release.
     * Use maxEdgeWeight and edgeWeightType instead.
     * @param maxRoutingLength the maxRoutingLength to set
     */
    @JsonIgnore
    @Deprecated
    public void setMaxRoutingLength(Integer maxRoutingLength) {
        this.maxEdgeWeight = maxRoutingLength;
        this.edgeWeightType = EdgeWeightType.DISTANCE;
    }

    /**
     * @param source Source coordinate
     */
    public void addSource(Coordinate source) {
        this.sources.put(source.getId(), source);
    }

    /**
     * @param source Source geometry
     */
    public void addSourceGeometry(AbstractGeometry source) {
        this.sourceGeometries.put(source.getId(), source);
    }

    public void addSourceAddress(DefaultSourceAddress address) {
        this.sourceAddresses.put(address.getH3Address(), address);
    }

    /**
     * @param target Target coordinate
     */
    public void addTarget(Coordinate target) {
        this.targets.put(target.getId(), target);
    }

    /**
     * @param geoHash Geohash string
     */
    public void addTargetGeohash(String geoHash) {
        this.targetGeohashes.add(geoHash);
    }

    private String toString(Collection<?> collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
            if (i > 0)
                builder.append(", ");
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }


    /**
     *
     * @param id ID of source Coordinate
     * @return Source coordinate
     */
    public Coordinate getSource(String id) {
        return this.sources.get(id);
    }

    /**
     *
     * @param id ID of source geometry
     * @return Source geometry
     */
    public AbstractGeometry getSourcegeometry(String id) {
        return this.sourceGeometries.get(id);
    }

    /**
     *
     * @param id ID of source Coordinate
     * @return Target coordinate
     */
    public Coordinate getTarget(String id) {
        return this.targets.get(id);
    }

    public void addAllSources(Map<String, Coordinate> sources) {
        this.sources.putAll(sources);
    }

    public void addAllSourceGeometries(Map<String, AbstractGeometry> sourceGeometries) {
        this.sourceGeometries.putAll(sourceGeometries);
    }

    /**
     * Clear sources and add new one
     * @param id ID for the new source
     * @param source New source coordinate
     */
    public void clearAndAddSource(String id, Coordinate source) {
        this.sources.clear();
        this.sources.put(id, source);
    }

    /**
     * Clear sourceGeometries and add new one
     * @param id ID for the new source
     * @param source New source geometry
     */
    public void clearAndAddSource(String id, AbstractGeometry source) {
        this.sourceGeometries.clear();
        this.sourceGeometries.put(id, source);
    }

    public String getTravelTypesString() {
        return SerializationUtil.travelTypeListToString(travelTypes);
    }

    /**
     * Returns the travel type used in the travel options.
     * Throws an exception if there are more than one travel type, use `getTravelTypes()` in this case instead.
     * @deprecated for backwards compatibility
     * @return the travel type
     * @throws TargomoClientException if there is more than one travel type
     */
    @JsonIgnore
    public TravelType getTravelType() throws TargomoClientException {
        if (travelTypes.size() != 1) {
            throw new TargomoClientException("Number of travel types was expected to be exactly one.");
        }
        else {
            return travelTypes.get(0);
        }
    }

    //excluding id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelOptions)) return false;
        TravelOptions that = (TravelOptions) o;
        return Double.compare(that.bikeSpeed, bikeSpeed) == 0 &&
                Double.compare(that.bikeUphill, bikeUphill) == 0 &&
                Double.compare(that.bikeDownhill, bikeDownhill) == 0 &&
                Double.compare(that.walkSpeed, walkSpeed) == 0 &&
                Double.compare(that.walkUphill, walkUphill) == 0 &&
                Double.compare(that.walkDownhill, walkDownhill) == 0 &&
                Objects.equals(that.allowPrivateAndServiceRoads, allowPrivateAndServiceRoads) &&
                Objects.equals(that.trafficJunctionPenalty, trafficJunctionPenalty) &&
                Objects.equals(that.trafficSignalPenalty, trafficSignalPenalty) &&
                Objects.equals(that.trafficLeftTurnPenalty, trafficLeftTurnPenalty) &&
                Objects.equals(that.trafficRightTurnPenalty, trafficRightTurnPenalty) &&
                onlyPrintReachablePoints == that.onlyPrintReachablePoints &&
                Objects.equals(sources, that.sources) &&
                Objects.equals(sourceGeometries, that.sourceGeometries) &&
                Objects.equals(sourceAddresses, that.sourceAddresses) &&
                Objects.equals(targets, that.targets) &&
                Objects.equals(targetGeohashes, that.targetGeohashes) &&
                Objects.equals(targetAddresses, that.targetAddresses) &&
                Objects.equals(rushHour, that.rushHour) &&
                Objects.equals(travelTimes, that.travelTimes) &&
                Objects.equals(elevationEnabled, that.elevationEnabled) &&
                Objects.equals(pointReduction, that.pointReduction) &&
                Objects.equals(reverse, that.reverse) &&
                Objects.equals(minPolygonHoleSize, that.minPolygonHoleSize) &&
                Objects.equals(time, that.time) &&
                Objects.equals(date, that.date) &&
                Objects.equals(frame, that.frame) &&
                Objects.equals(arrivalOrDepartureDuration, that.arrivalOrDepartureDuration) &&
				Objects.equals(intersectionGeometry, that.intersectionGeometry) &&
				Objects.equals(exclusionGeometry, that.exclusionGeometry) &&
                Objects.equals(excludeEdgeClasses, that.excludeEdgeClasses) &&
                Objects.equals(recommendations, that.recommendations) &&
                Objects.equals(srid, that.srid) &&
                Objects.equals(polygonOrientationRule, that.polygonOrientationRule) &&
                Objects.equals(decimalPrecision, that.decimalPrecision) &&
                Objects.equals(buffer, that.buffer) &&
                Objects.equals(simplify, that.simplify) &&
                intersectionMode == that.intersectionMode &&
                pathSerializer == that.pathSerializer &&
                polygonSerializerType == that.polygonSerializerType &&
                Objects.equals(maxSnapDistance, that.maxSnapDistance) &&
                Objects.equals(multiGraphEdgeClasses, that.multiGraphEdgeClasses) &&
                multiGraphSerializationFormat == that.multiGraphSerializationFormat &&
                Objects.equals(multiGraphSerializationDecimalPrecision, that.multiGraphSerializationDecimalPrecision) &&
                Objects.equals(multiGraphSerializationMaxGeometryCount, that.multiGraphSerializationMaxGeometryCount) &&
                Objects.equals(multiGraphSerializationH3MaxBuffer, that.multiGraphSerializationH3MaxBuffer) &&
                Objects.equals(multiGraphSerializationH3BufferSpeed, that.multiGraphSerializationH3BufferSpeed) &&
                Objects.equals(multiGraphSerializationH3BufferFixedValue, that.multiGraphSerializationH3BufferFixedValue) &&
                Objects.equals(multiGraphSerializationH3IdFormat, that.multiGraphSerializationH3IdFormat) &&
                multiGraphAggregationType == that.multiGraphAggregationType &&
                Objects.equals(multiGraphAggregationIgnoreOutliers, that.multiGraphAggregationIgnoreOutliers) &&
                Objects.equals(multiGraphAggregationOutlierPenalty, that.multiGraphAggregationOutlierPenalty) &&
                Objects.equals(multiGraphAggregationMinSourcesRatio, that.multiGraphAggregationMinSourcesRatio) &&
                Objects.equals(multiGraphAggregationMinSourcesCount, that.multiGraphAggregationMinSourcesCount) &&
                Objects.equals(multiGraphAggregationSourceValuesLowerBound, that.multiGraphAggregationSourceValuesLowerBound) &&
                Objects.equals(multiGraphAggregationSourceValuesUpperBound, that.multiGraphAggregationSourceValuesUpperBound) &&
                Objects.equals(multiGraphAggregationSourceValuesModifier, that.multiGraphAggregationSourceValuesModifier) &&
                Objects.equals(multiGraphAggregationMinResultValueRatio, that.multiGraphAggregationMinResultValueRatio) &&
                Objects.equals(multiGraphAggregationMinResultValue, that.multiGraphAggregationMinResultValue) &&
                Objects.equals(multiGraphAggregationMaxResultValueRatio, that.multiGraphAggregationMaxResultValueRatio) &&
                Objects.equals(multiGraphAggregationMaxResultValue, that.multiGraphAggregationMaxResultValue) &&
                Objects.equals(multiGraphAggregationFilterValuesForSourceOrigins, that.multiGraphAggregationFilterValuesForSourceOrigins) &&
                Objects.equals(multiGraphAggregationGravitationExponent, that.multiGraphAggregationGravitationExponent) &&
                Objects.equals(multiGraphAggregationProbabilityDecay, that.multiGraphAggregationProbabilityDecay) &&
                Objects.equals(multiGraphAggregationLogitBetaAttractionStrength, that.multiGraphAggregationLogitBetaAttractionStrength) &&
                Objects.equals(multiGraphAggregationLogitBetaTravelTime, that.multiGraphAggregationLogitBetaTravelTime) &&
                Objects.equals(multiGraphAggregationUseProbabilityBasedWeightedAverage, that.multiGraphAggregationUseProbabilityBasedWeightedAverage) &&
                Objects.equals(multiGraphAggregationInputParameters, that.multiGraphAggregationInputParameters) &&
                Objects.equals(multiGraphAggregationMathExpression, that.multiGraphAggregationMathExpression) &&
                Objects.equals(multiGraphLayerCustomGeometryMergeAggregation, that.multiGraphLayerCustomGeometryMergeAggregation) &&
                Objects.equals(multiGraphAggregationPostAggregationFactor, that.multiGraphAggregationPostAggregationFactor) &&
                multiGraphLayerType == that.multiGraphLayerType &&
                Objects.equals(multiGraphDomainType, that.multiGraphDomainType) &&
                multiGraphDomainEdgeAggregationType == that.multiGraphDomainEdgeAggregationType &&
                Objects.equals(multiGraphLayerGeometryDetailPerTile, that.multiGraphLayerGeometryDetailPerTile) &&
                Objects.equals(multiGraphLayerMinGeometryDetailLevel, that.multiGraphLayerMinGeometryDetailLevel) &&
                Objects.equals(multiGraphLayerMaxGeometryDetailLevel, that.multiGraphLayerMaxGeometryDetailLevel) &&
                Objects.equals(multiGraphLayerGeometryDetailLevel, that.multiGraphLayerGeometryDetailLevel) &&
                Objects.equals(multiGraphTileZoom, that.multiGraphTileZoom) &&
                Objects.equals(multiGraphTileX, that.multiGraphTileX) &&
                Objects.equals(multiGraphTileY, that.multiGraphTileY) &&
                Objects.equals(clipGeometry, that.clipGeometry) &&
                Objects.equals(multiGraphH3FixedZoomLevel, that.multiGraphH3FixedZoomLevel) &&
                Objects.equals(maxEdgeWeight, that.maxEdgeWeight) &&
                Objects.equals(serviceUrl, that.serviceUrl) &&
                Objects.equals(serviceKey, that.serviceKey) &&
                edgeWeightType == that.edgeWeightType &&
                Objects.equals(statisticGroupId, that.statisticGroupId) &&
                Objects.equals(statisticServiceUrl, that.statisticServiceUrl) &&
                Objects.equals(pointOfInterestServiceUrl, that.pointOfInterestServiceUrl) &&
                Objects.equals(overpassQuery, that.overpassQuery) &&
                Objects.equals(overpassServiceUrl, that.overpassServiceUrl) &&
                Objects.equals(interServiceKey, that.interServiceKey) &&
                Objects.equals(interServiceRequestType, that.interServiceRequestType) &&
                format == that.format &&
                Objects.equals(boundingBox, that.boundingBox) &&
                Objects.equals(travelTypes, that.travelTypes) &&
                Objects.equals(osmTypes, that.osmTypes) &&
                Objects.equals(customPois, that.customPois) &&
                Objects.equals(filterGeometryForPOIs, that.filterGeometryForPOIs) &&
                Objects.equals(poiGravitationExponent, that.poiGravitationExponent) &&
                Objects.equals(poiGravitationProbabilityDecay, that.poiGravitationProbabilityDecay) &&
                Objects.equals(travelTimeFactors, that.travelTimeFactors) &&
                Objects.equals(maxTransfers, that.maxTransfers) &&
                Objects.equals(avoidTransitRouteTypes, that.avoidTransitRouteTypes) &&
                Objects.equals(multiGraphPreAggregationPipeline, that.multiGraphPreAggregationPipeline) &&
                Objects.equals(maxWalkingTimeFromSource, that.maxWalkingTimeFromSource) &&
                Objects.equals(maxWalkingTimeToTarget, that.maxWalkingTimeToTarget) &&
                Objects.equals(nextStopsStartTime, that.nextStopsStartTime) &&
                Objects.equals(nextStopsEndTime, that.nextStopsEndTime) &&
                Objects.equals(includeSnapDistance, that.includeSnapDistance) &&
                Objects.equals(includeSnapDistanceForTargets, that.includeSnapDistanceForTargets) &&
                Objects.equals(useAreaSnapping, that.useAreaSnapping) &&
                Objects.equals(snapRadius, that.snapRadius) &&
                Objects.equals(excludeEdgeClassesFromSnapping, that.excludeEdgeClassesFromSnapping) &&
                Objects.equals(multiGraphAggregationLearntMaxEdgeWeight, that.multiGraphAggregationLearntMaxEdgeWeight);
    }
//todo

    //excluding id
    @Override
    public int hashCode() {

        return Objects.hash(sources, sourceGeometries, sourceAddresses, targets, targetGeohashes, targetAddresses, bikeSpeed,
                bikeUphill, bikeDownhill, walkSpeed, walkUphill, walkDownhill, rushHour, travelTimes, elevationEnabled,
                appendTravelTimes, pointReduction, reverse, minPolygonHoleSize, time, date, frame, arrivalOrDepartureDuration,
                recommendations, srid, polygonOrientationRule, decimalPrecision, buffer, simplify,
                intersectionMode, pathSerializer, polygonSerializerType, maxSnapDistance, intersectionGeometry, exclusionGeometry, excludeEdgeClasses,
                multiGraphEdgeClasses, multiGraphSerializationFormat,
                multiGraphSerializationDecimalPrecision, multiGraphSerializationMaxGeometryCount,
                multiGraphSerializationH3MaxBuffer, multiGraphSerializationH3BufferSpeed, multiGraphSerializationH3BufferFixedValue, multiGraphSerializationH3IdFormat,
                multiGraphAggregationType, multiGraphAggregationIgnoreOutliers, multiGraphAggregationOutlierPenalty,
                multiGraphAggregationMinSourcesRatio, multiGraphAggregationMinSourcesCount,
                multiGraphAggregationSourceValuesLowerBound, multiGraphAggregationSourceValuesUpperBound, multiGraphAggregationSourceValuesModifier,
                multiGraphAggregationMinResultValueRatio, multiGraphAggregationMinResultValue,
                multiGraphAggregationMaxResultValueRatio, multiGraphAggregationMaxResultValue,
                multiGraphAggregationGravitationExponent, multiGraphAggregationProbabilityDecay, multiGraphAggregationLogitBetaAttractionStrength,
                multiGraphAggregationLogitBetaTravelTime, multiGraphAggregationUseProbabilityBasedWeightedAverage, multiGraphLayerCustomGeometryMergeAggregation,
                multiGraphAggregationInputParameters, multiGraphAggregationFilterValuesForSourceOrigins,
                multiGraphPreAggregationPipeline, multiGraphAggregationMathExpression, multiGraphLayerType,
                multiGraphDomainType, multiGraphDomainEdgeAggregationType, multiGraphLayerGeometryDetailPerTile,
                multiGraphLayerMinGeometryDetailLevel, multiGraphLayerMaxGeometryDetailLevel, multiGraphH3FixedZoomLevel,
                multiGraphLayerGeometryDetailLevel, multiGraphTileZoom, multiGraphTileX, multiGraphTileY,
                multiGraphAggregationPostAggregationFactor, clipGeometry, maxEdgeWeight, serviceUrl, serviceKey,
                onlyPrintReachablePoints, edgeWeightType, statisticGroupId, statisticServiceUrl,
                pointOfInterestServiceUrl, overpassQuery, overpassServiceUrl, interServiceKey, interServiceRequestType,
                format, boundingBox, travelTypes, osmTypes, customPois, filterGeometryForPOIs, poiGravitationExponent, poiGravitationProbabilityDecay,
                travelTimeFactors, maxTransfers, avoidTransitRouteTypes, allowPrivateAndServiceRoads,
                trafficJunctionPenalty, trafficSignalPenalty, trafficLeftTurnPenalty, trafficRightTurnPenalty,
                maxWalkingTimeFromSource, maxWalkingTimeToTarget, nextStopsStartTime, nextStopsEndTime,
                includeSnapDistance, includeSnapDistanceForTargets, useAreaSnapping, snapRadius,
                excludeEdgeClassesFromSnapping, multiGraphAggregationLearntMaxEdgeWeight);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final int maxLen = 5;
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append("\n\tid: ");
        builder.append(id);
        builder.append("\n\tforceRecalculate: ");
        builder.append(forceRecalculate);
        builder.append("\n\tcacheResult: ");
        builder.append(cacheResult);
        builder.append("\n\tintersectionGeometry: ");
        builder.append(intersectionGeometry != null ? intersectionGeometry.toString() : null);
        builder.append("\n\texclusionGeometry: ");
        builder.append(exclusionGeometry != null ? exclusionGeometry.toString() : null);
        builder.append("\n\texcludeEdgeClasses: ");
        builder.append(excludeEdgeClasses != null ? toString(excludeEdgeClasses, maxLen) :null);
        builder.append(" {\n\tsources: ");
        builder.append(sources != null ? toString(sources.entrySet(), maxLen) : null);
        builder.append(" {\n\tsourceGeometries: ");
        builder.append(sourceGeometries != null ? toString(sourceGeometries.entrySet(), maxLen) : null);
        builder.append(" {\n\tsourceAddresses: ");
        builder.append(sourceAddresses != null ? toString(sourceAddresses.entrySet(), maxLen) : null);
        builder.append("\n\ttargets: ");
        builder.append(targets != null ? toString(targets.entrySet(), maxLen) : null);
        builder.append("\n\ttargetGeohashes: ");
        builder.append(targetGeohashes != null ? toString(targetGeohashes, maxLen) : null);
        builder.append("\n\ttargetAddresses: ");
        builder.append(targetAddresses != null ? toString(targetAddresses, maxLen) : null);
        builder.append("\n\tbikeSpeed: ");
        builder.append(bikeSpeed);
        builder.append("\n\tbikeUphill: ");
        builder.append(bikeUphill);
        builder.append("\n\tbikeDownhill: ");
        builder.append(bikeDownhill);
        builder.append("\n\twalkSpeed: ");
        builder.append(walkSpeed);
        builder.append("\n\twalkUphill: ");
        builder.append(walkUphill);
        builder.append("\n\twalkDownhill: ");
        builder.append(walkDownhill);
        builder.append("\n\tallowPrivateAndServiceRoads: ");
        builder.append(allowPrivateAndServiceRoads);
        builder.append("\n\ttrafficJunctionPenalty: ");
        builder.append(trafficJunctionPenalty);
        builder.append("\n\ttrafficSignalPenalty: ");
        builder.append(trafficSignalPenalty);
        builder.append("\n\ttrafficLeftTurnPenalty: ");
        builder.append(trafficLeftTurnPenalty);
        builder.append("\n\ttrafficRightTurnPenalty: ");
        builder.append(trafficRightTurnPenalty);
        builder.append("\n\trushHour: ");
        builder.append(rushHour);
        builder.append("\n\ttravelTimes: ");
        builder.append(travelTimes != null ? toString(travelTimes, maxLen) : null);
        builder.append("\n\televationEnabled: ");
        builder.append(elevationEnabled);
        builder.append("\n\tappendTravelTimes: ");
        builder.append(appendTravelTimes);
        builder.append("\n\tpointReduction: ");
        builder.append(pointReduction);
        builder.append("\n\treverse: ");
        builder.append(reverse);
        builder.append("\n\tminPolygonHoleSize: ");
        builder.append(minPolygonHoleSize);
        builder.append("\n\ttime: ");
        builder.append(time);
        builder.append("\n\tdate: ");
        builder.append(date);
        builder.append("\n\tarrivalOrDepartureDuration: ");
        builder.append(arrivalOrDepartureDuration);
        builder.append("\n\tframe: ");
        builder.append(frame);
        builder.append("\n\trecommendations: ");
        builder.append(recommendations);
        builder.append("\n\tsrid: ");
        builder.append(srid);
        builder.append("\n\tpolygonOrientationRule: ");
        builder.append(polygonOrientationRule);
        builder.append("\n\tdecimalPrecision: ");
        builder.append(decimalPrecision);
        builder.append("\n\tbuffer: ");
        builder.append(buffer);
        builder.append("\n\tsimplify: ");
        builder.append(simplify);
        builder.append("\n\tintersectionMode: ");
        builder.append(intersectionMode);
        builder.append("\n\tpathSerializer: ");
        builder.append(pathSerializer);
        builder.append("\n\tpolygonSerializerType: ");
        builder.append(polygonSerializerType);
        builder.append("\n\tmaxSnapDistance: ");
        builder.append(maxSnapDistance);
        builder.append("\n\tmultiGraphEdgeClasses: ");
        builder.append(multiGraphEdgeClasses);
        builder.append("\n\tmultiGraphSerializationFormat: ");
        builder.append(multiGraphSerializationFormat);
        builder.append("\n\tmultiGraphSerializationDecimalPrecision: ");
        builder.append(multiGraphSerializationDecimalPrecision);
        builder.append("\n\tmultiGraphSerializationMaxGeometryCount: ");
        builder.append(multiGraphSerializationMaxGeometryCount);
        builder.append("\n\tmultiGraphSerializationH3MaxBuffer: ");
        builder.append(multiGraphSerializationH3MaxBuffer);
        builder.append("\n\tmultiGraphSerializationH3BufferSpeed: ");
        builder.append(multiGraphSerializationH3BufferSpeed);
        builder.append("\n\tmultiGraphSerializationH3BufferFixedValue: ");
        builder.append(multiGraphSerializationH3BufferFixedValue);
        builder.append("\n\tmultiGraphSerializationH3IdFormat: ");
        builder.append(multiGraphSerializationH3IdFormat);
        builder.append("\n\tmultiGraphDomainType: ");
        builder.append(multiGraphDomainType);
        builder.append("\n\tmultiGraphDomainEdgeAggregationType: ");
        builder.append(multiGraphDomainEdgeAggregationType);
        builder.append("\n\tmultiGraphAggregationType: ");
        builder.append(multiGraphAggregationType);
        builder.append("\n\tmultiGraphAggregationIgnoreOutliers: ");
        builder.append(multiGraphAggregationIgnoreOutliers);
        builder.append("\n\tmultiGraphAggregationOutlierPenalty: ");
        builder.append(multiGraphAggregationOutlierPenalty);
        builder.append("\n\tmultiGraphAggregationMinSourcesRatio: ");
        builder.append(multiGraphAggregationMinSourcesRatio);
        builder.append("\n\tmultiGraphAggregationMinSourcesCount: ");
        builder.append(multiGraphAggregationMinSourcesCount);
        builder.append("\n\tmultiGraphAggregationSourceValuesLowerBound: ");
        builder.append(multiGraphAggregationSourceValuesLowerBound);
        builder.append("\n\tmultiGraphAggregationSourceValuesUpperBound: ");
        builder.append(multiGraphAggregationSourceValuesUpperBound);
        builder.append("\n\tmultiGraphAggregationSourceValuesModifier: ");
        builder.append(multiGraphAggregationSourceValuesModifier);
        builder.append("\n\tmultiGraphAggregationMinResultValueRatio: ");
        builder.append(multiGraphAggregationMinResultValueRatio);
        builder.append("\n\tmultiGraphAggregationMinResultValue: ");
        builder.append(multiGraphAggregationMinResultValue);
        builder.append("\n\tmultiGraphAggregationMaxResultValueRatio: ");
        builder.append(multiGraphAggregationMaxResultValueRatio);
        builder.append("\n\tmultiGraphAggregationMaxResultValue: ");
        builder.append(multiGraphAggregationMaxResultValue);
        builder.append("\n\tmultiGraphAggregationMathExpression: ");
        builder.append(multiGraphAggregationMathExpression);
        builder.append("\n\tmultiGraphLayerCustomGeometryMergeAggregation: ");
        builder.append(multiGraphLayerCustomGeometryMergeAggregation);
        builder.append("\n\tmultiGraphAggregationPostAggregationFactor: ");
        builder.append(multiGraphAggregationPostAggregationFactor);
        builder.append("\n\tmultiGraphAggregationGravitationExponent: ");
        builder.append(multiGraphAggregationGravitationExponent);
        builder.append("\n\tmultiGraphAggregationProbabilityDecay: ");
        builder.append(multiGraphAggregationProbabilityDecay);
        builder.append("\n\tmultiGraphAggregationLogitBetaAttractionStrength: ");
        builder.append(multiGraphAggregationLogitBetaAttractionStrength);
        builder.append("\n\tmultiGraphAggregationLogitBetaTravelTime: ");
        builder.append(multiGraphAggregationLogitBetaTravelTime);
        builder.append("\n\tmultiGraphAggregationUseProbabilityBasedWeightedAverage: ");
        builder.append(multiGraphAggregationUseProbabilityBasedWeightedAverage);
        builder.append("\n\tmultiGraphAggregationInputParameters: ");
        builder.append(multiGraphAggregationInputParameters);
        builder.append("\n\tmultiGraphAggregationFilterValuesForSourceOrigins: ");
        builder.append(multiGraphAggregationFilterValuesForSourceOrigins);
        builder.append("\n\tmultiGraphPreAggregationPipeline: ");
        builder.append(multiGraphPreAggregationPipeline);
        builder.append("\n\tmultiGraphLayerType: ");
        builder.append(multiGraphLayerType);
        builder.append("\n\tmultiGraphLayerGeometryDetailPerTile: ");
        builder.append(multiGraphLayerGeometryDetailPerTile);
        builder.append("\n\tmultiGraphLayerMinGeometryDetailLevel: ");
        builder.append(multiGraphLayerMinGeometryDetailLevel);
        builder.append("\n\tmultiGraphLayerMaxGeometryDetailLevel: ");
        builder.append(multiGraphLayerMaxGeometryDetailLevel);
        builder.append("\n\tmultiGraphLayerGeometryDetailLevel: ");
        builder.append(multiGraphLayerGeometryDetailLevel);
        builder.append("\n\tmultiGraphTileZoom: ");
        builder.append(multiGraphTileZoom);
        builder.append("\n\tmultiGraphTileX: ");
        builder.append(multiGraphTileX);
        builder.append("\n\tmultiGraphTileY: ");
        builder.append(multiGraphTileY);
        builder.append("\n\tclipGeometry: ");
        builder.append(clipGeometry != null  ? clipGeometry.toString() : null);
        builder.append("\n\tmultiGraphH3FixedZoomLevel: ");
        builder.append(multiGraphH3FixedZoomLevel);
        builder.append("\n\tmaxEdgeWeight: ");
        builder.append(maxEdgeWeight);
        builder.append("\n\tserviceUrl: ");
        builder.append(serviceUrl);
        builder.append("\n\tserviceKey: ");
        builder.append(serviceKey);
        builder.append("\n\tonlyPrintReachablePoints: ");
        builder.append(onlyPrintReachablePoints);
        builder.append("\n\tedgeWeightType: ");
        builder.append(edgeWeightType);
        builder.append("\n\tstatisticGroupId: ");
        builder.append(statisticGroupId);
        builder.append("\n\tstatisticServiceUrl: ");
        builder.append(statisticServiceUrl);
        builder.append("\n\tpointOfInterestServiceUrl: ");
        builder.append(pointOfInterestServiceUrl);
        builder.append("\n\toverpassQuery: ");
        builder.append(overpassQuery);
        builder.append("\n\toverpassServiceUrl: ");
        builder.append(overpassServiceUrl);
        builder.append("\n\tinterServiceKey: ");
        builder.append(interServiceKey);
        builder.append("\n\tinterServiceRequestType: ");
        builder.append(interServiceRequestType);
        builder.append("\n\tformat: ");
        builder.append(format);
        builder.append("\n\tboundingBox: ");
        builder.append(boundingBox);
        builder.append("\n\ttravelTypes: ");
        builder.append(travelTypes != null ? toString(travelTypes, maxLen) : null );
        builder.append("\n\tosmTypes: ");
        builder.append(osmTypes != null ? toString(osmTypes, maxLen) : null);
        builder.append("\n\tcustomPois: ");
        builder.append(customPois != null ? toString(customPois, maxLen) : null);
        builder.append("\n\tfilterGeometry: ");
        builder.append(filterGeometryForPOIs);
        builder.append("\n\tpoiGravitationExponent: ");
        builder.append(poiGravitationExponent);
        builder.append("\n\tpoiGravitationProbabilityDecay: ");
        builder.append(poiGravitationProbabilityDecay);
        builder.append("\n\ttravelTimeFactors: ");
        builder.append(travelTimeFactors != null ? toString(travelTimeFactors.entrySet(), maxLen) : null);
        builder.append("\n\tmaxTransfers: ");
        builder.append(maxTransfers);
        builder.append("\n\tavoidTransitRouteTypes: ");
        builder.append(avoidTransitRouteTypes != null ? toString(avoidTransitRouteTypes, maxLen) : null);
        builder.append("\n\tmaxWalkingTimeFromSource: ");
        builder.append(maxWalkingTimeFromSource);
        builder.append("\n\tmaxWalkingTimeToTarget: ");
        builder.append(maxWalkingTimeToTarget);
        builder.append("\n\tnextStopsStartTime: ");
        builder.append(nextStopsStartTime);
        builder.append("\n\tnextStopsEndTime: ");
        builder.append(nextStopsEndTime);
        builder.append("\n\tincludeSnapDistance: ");
        builder.append(includeSnapDistance);
        builder.append("\n\tincludeSnapDistanceForTargets: ");
        builder.append(includeSnapDistanceForTargets);
        builder.append("\n\tuseAreaSnapping: ");
        builder.append(useAreaSnapping);
        builder.append("\n\tsnapRadius: ");
        builder.append(snapRadius);
        builder.append("\n\texcludeEdgeClassesFromSnapping: ");
        builder.append(excludeEdgeClassesFromSnapping != null ? toString(excludeEdgeClassesFromSnapping, maxLen) :null);
        builder.append("\n\tmultiGraphAggregationLearntMaxEdgeWeight: ");
        builder.append(multiGraphAggregationLearntMaxEdgeWeight);
        builder.append("\n}\n");
        return builder.toString();
    }
}
