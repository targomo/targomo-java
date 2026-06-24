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
import lombok.EqualsAndHashCode;

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

@Data @EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TravelOptions implements Serializable {

    @JsonDeserialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapSerializer.class)
    private Map<String,Coordinate> sources = new HashMap<>();

    @JsonDeserialize(contentAs=DefaultSourceAddress.class, using=DefaultSourceAddressMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceAddress.class, using=DefaultSourceAddressMapSerializer.class)
    private Map<String, DefaultSourceAddress> sourceAddresses = new HashMap<>();

    @JsonDeserialize(contentAs= DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapDeserializer.class)
    @JsonSerialize(contentAs= DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapSerializer.class)
    private Map<String, AbstractGeometry> sourceGeometries = new HashMap<>();

    @JsonDeserialize(contentAs=DefaultTargetCoordinate.class, using=DefaultTargetCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultTargetCoordinateMapSerializer.class)
    private Map<String,Coordinate> targets = new HashMap<>();
    
    private List<String> targetGeohashes = new ArrayList<>();
    private List<String> targetAddresses = new ArrayList<>();

    private double bikeSpeed = 18.0;
    private double bikeUphill = 20.0;
    private double bikeDownhill = -10.0;

    private double walkSpeed = 5.0;
    private double walkUphill = 10.0;
    private double walkDownhill = 0.0;

    private Double snappingSpeed;

    private Boolean rushHour = false;
    
    private boolean allowPrivateAndServiceRoads = false;

    //the following four setting are only used for bike, car (and bike-transit)
    private Integer trafficJunctionPenalty = null;
    private Integer trafficSignalPenalty = null;
    private Integer trafficLeftTurnPenalty = null;
    private Integer trafficRightTurnPenalty = null;

    private List<Integer> travelTimes = Arrays.asList(600, 1200, 1800);
    
    // If there is more than one element in the travelTypes list, multi modal routing will be used.
    private List<TravelType> travelTypes = Collections.emptyList();
    
	private Map<String,Double> travelTimeFactors = new HashMap<>();

    private Boolean elevationEnabled = true;

    private Boolean appendTravelTimes = false;
    private Boolean pointReduction = true;
    private Boolean reverse = false;
    private Long minPolygonHoleSize = 100000000L;

    private Integer time = null; //default is used in core
    private Integer date = null; //default is used in core
    private Weekday weekday = null; //default is used in core
    private Integer frame = null; //default is used in core
    private Boolean earliestArrival = false;
    private Integer arrivalOrDepartureDuration = null;
    private Integer maxWalkingTimeFromSource = null;
    private Integer maxWalkingTimeToTarget = null;
    private Integer recommendations = 0;
    private Integer srid = null;
    private PolygonOrientationRule polygonOrientationRule = null;
    private Integer decimalPrecision = null;

    // maximum number of transfers when using public transportation
    private Integer maxTransfers = null;

    // Transit route types that should not be used for routing
    private List<Integer> avoidTransitRouteTypes = Collections.emptyList();

    private Double buffer = null;
    private Double simplify = null;
    private Integer quadrantSegments = null;
    private Integer flyCircleDetailLevel = null;
    private PolygonIntersectionMode intersectionMode = PolygonIntersectionMode.UNION;
    private PathSerializerType pathSerializer = PathSerializerType.COMPACT_PATH_SERIALIZER;
    private PolygonSerializerType polygonSerializerType = PolygonSerializerType.JSON_POLYGON_SERIALIZER;
    private Integer maxSnapDistance = null;

    private Set<Integer> multiGraphEdgeClasses                                    = null;
    private MultiGraphDomainType multiGraphDomainType                             = null;
    private MultiGraphDomainEdgeAggregationType multiGraphDomainEdgeAggregationType = null;
    private MultiGraphSerializationFormat multiGraphSerializationFormat           = null;
    private Integer multiGraphSerializationDecimalPrecision                       = null;
    private Integer multiGraphSerializationMaxGeometryCount                       = null;
    private Integer multiGraphSerializationH3MaxBufferMeters      = null;
    private Integer multiGraphSerializationH3MaxBufferCells       = null;
    private Float multiGraphSerializationH3BufferSpeed            = null;
    private Boolean multiGraphSerializationH3BufferFixedValue     = null;
    private MultiGraphLayerCustomGeometryMergeAggregation multiGraphSerializationH3BufferAggregationType = null;
    private MultiGraphSerializationH3IdFormat multiGraphSerializationH3IdFormat = null;
    private MultiGraphAggregationType multiGraphAggregationType                   = null;
    private Boolean multiGraphAggregationIgnoreOutliers                           = null;
    private Float multiGraphAggregationOutlierPenalty                             = null;
    private Double multiGraphAggregationMinSourcesRatio                           = null;
    private Integer multiGraphAggregationMinSourcesCount                          = null;
    private Float multiGraphAggregationSourceValuesLowerBound                     = null;
    private Float multiGraphAggregationSourceValuesUpperBound                     = null;

    private Float multiGraphAggregationSourceValuesModifier                                  = null;
    private Double multiGraphAggregationMinResultValueRatio                       = null;
    private Float multiGraphAggregationMinResultValue                             = null;
    private Double multiGraphAggregationMaxResultValueRatio                       = null;
    private Float multiGraphAggregationMaxResultValue                             = null;
    private String multiGraphAggregationMathExpression                            = null;
    private Set<String> multiGraphAggregationFilterValuesForSourceOrigins         = null;
    private Double multiGraphAggregationGravitationExponent                       = null;
    private Double multiGraphAggregationProbabilityDecay                          = null;
    private Double multiGraphAggregationLogitBetaAttractionStrength               = null;
    private Double multiGraphAggregationLogitBetaTravelTime                       = null;
    private Boolean multiGraphAggregationUseProbabilityBasedWeightedAverage       = null;
    private Float multiGraphAggregationPostAggregationFactor                      = null;
    private Map<String, AggregationInputParameters> multiGraphAggregationInputParameters = null;
    private LinkedHashMap<String, AggregationConfiguration> multiGraphPreAggregationPipeline = null;
    private MultiGraphLayerType multiGraphLayerType                               = null;
    private Integer multiGraphLayerGeometryDetailPerTile                          = null;
    private Integer multiGraphLayerMinGeometryDetailLevel                         = null;
    private Integer multiGraphLayerMaxGeometryDetailLevel                         = null;
    private Integer multiGraphLayerGeometryDetailLevel                            = null;
    private MultiGraphLayerCustomGeometryMergeAggregation multiGraphLayerCustomGeometryMergeAggregation = null;
    private Integer multiGraphTileZoom                                            = null;
    private Integer multiGraphTileX                                               = null;
    private Integer multiGraphTileY                                               = null;
    private Geometry clipGeometry                                                 = null;
    private Integer multiGraphH3FixedZoomLevel                                    = null;


    private Integer maxEdgeWeight = 1800;
    private String serviceUrl = "";
    private String serviceKey = "";
    private boolean onlyPrintReachablePoints = true;

    @JsonProperty("edgeWeight")
    private EdgeWeightType edgeWeightType = EdgeWeightType.TIME;

	private Integer statisticGroupId;
    private String statisticServiceUrl = "https://api.targomo.com/statistics/";
	private String pointOfInterestServiceUrl = "https://api.targomo.com/pointofinterest/";

	private String overpassQuery;
	private String overpassServiceUrl = "https://api.targomo.com/overpass/";

    private String interServiceKey = "";
    
    private String interServiceRequestType = "";
    
	private Format format;
    
	private Geometry intersectionGeometry;
    
    private Geometry exclusionGeometry;
    
    private List<Integer> excludeEdgeClasses;
    
	private String boundingBox;
    
    private Set<PoiType> osmTypes = new HashSet<>();
    
    private Set<PoiType> customPois = new HashSet<>();
    
    @JsonProperty("filterGeometry")
    private AbstractGeometry filterGeometryForPOIs;
    
    @JsonProperty("gravitationExponent")
    private Double poiGravitationExponent;
    
    @JsonProperty("probabilityDecay")
    private Double poiGravitationProbabilityDecay;
    
    private boolean forceRecalculate = false;
    
    private boolean cacheResult = true;

    //parameters for requesting "transit/stops" endpoint - not for routing
    private Integer nextStopsStartTime;
    private Integer nextStopsEndTime;
    
    private Boolean includeSnapDistance;
    
    private Boolean includeSnapDistanceForTargets;
    
    private Boolean useAreaSnapping;

    // snap radius is in meters
    private Integer snapRadius;
    // maximum distance between the lanes to snap to the opposite direction lane of multi-lane roads
    private Integer areaSnappingOppositeLanesMaxDist;

    private List<Integer> excludeEdgeClassesFromSnapping;
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
    @JsonIgnore @Deprecated
    public TravelType getTravelType() throws TargomoClientException {
        if (travelTypes.size() != 1) {
            throw new TargomoClientException("Number of travel types was expected to be exactly one.");
        }
        else {
            return travelTypes.get(0);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final int maxLen = 5;
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
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
        builder.append("\n\tweekday: ");
        builder.append(weekday);
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
        builder.append("\n\tmultiGraphSerializationH3MaxBufferMeters: ");
        builder.append(multiGraphSerializationH3MaxBufferMeters);
        builder.append("\n\tmultiGraphSerializationH3MaxBufferCells: ");
        builder.append(multiGraphSerializationH3MaxBufferCells);
        builder.append("\n\tmultiGraphSerializationH3BufferSpeed: ");
        builder.append(multiGraphSerializationH3BufferSpeed);
        builder.append("\n\tmultiGraphSerializationH3BufferFixedValue: ");
        builder.append(multiGraphSerializationH3BufferFixedValue);
        builder.append("\n\tmultiGraphSerializationH3BufferAggregationType: ");
        builder.append(multiGraphSerializationH3BufferAggregationType);
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
        builder.append("\n\tsnappingSpeed: ");
        builder.append(snappingSpeed);
        builder.append("\n\texcludeEdgeClassesFromSnapping: ");
        builder.append(excludeEdgeClassesFromSnapping != null ? toString(excludeEdgeClassesFromSnapping, maxLen) :null);
        builder.append("\n\tareaSnappingOppositeLanesMaxDist: ");
        builder.append(areaSnappingOppositeLanesMaxDist);
        builder.append("\n\tmultiGraphAggregationLearntMaxEdgeWeight: ");
        builder.append(multiGraphAggregationLearntMaxEdgeWeight);
        builder.append("\n}\n");
        return builder.toString();
    }
}
