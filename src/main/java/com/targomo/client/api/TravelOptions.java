package com.targomo.client.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.enums.*;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultTargetCoordinate;
import com.targomo.client.api.json.DefaultSourceCoordinateMapDeserializer;
import com.targomo.client.api.json.DefaultSourceCoordinateMapSerializer;
import com.targomo.client.api.json.DefaultTargetCoordinateMapDeserializer;
import com.targomo.client.api.json.DefaultTargetCoordinateMapSerializer;
import com.targomo.client.api.pojo.Geometry;
import com.targomo.client.api.pojo.AggregationInputParameters;
import com.targomo.client.api.request.PolygonRequest;
import com.targomo.client.api.request.ReachabilityRequest;
import com.targomo.client.api.request.RouteRequest;
import com.targomo.client.api.request.TimeRequest;
import com.targomo.client.api.pojo.AggregationConfiguration;
import com.targomo.client.api.statistic.PoiType;

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

@Entity
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

    @JsonDeserialize(contentAs=DefaultTargetCoordinate.class, using=DefaultTargetCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultTargetCoordinateMapSerializer.class)
    @Transient
    private Map<String,Coordinate> targets  = new HashMap<>();

    @Column(name = "bike_speed")
    private double bikeSpeed         = 15.0;

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

    @Transient private Integer trafficJunctionPenalty        = null;
    @Transient private Integer trafficSignalPenalty        = null;

    @Transient private List<Integer> travelTimes                    = Arrays.asList(600, 1200, 1800);

    @Column(name = "travel_type")
    private TravelType travelType                                   = TravelType.UNSPECIFIED;

    @Transient
	private Map<String,Double> travelTimeFactors 	            	= new HashMap<>();

    @Column(name = "elevation_enabled")
    private Boolean elevationEnabled                                = false;

    @Transient private Boolean appendTravelTimes                    = false;
    @Transient private Boolean pointReduction                       = true;
    @Transient private Boolean reverse                              = false;
    @Transient private Long minPolygonHoleSize                      = 100000000L;

    @Column(name = "time") private Integer time                     = 9 * 3600;
    @Column(name = "date")  private Integer date                    = 20170214;
    @Column(name = "frame") private Integer frame                   = 18000;
    @Transient private Integer maxWalkingTimeFromSource             = null;
    @Transient private Integer maxWalkingTimeToTarget               = null;
    @Transient private Integer recommendations                      = 0;
    @Transient private Integer srid                                 = null;
    @Transient private Integer decimalPrecision                     = null;

    // maximum number of transfers when using public transportation
    @Column(name = "max_transfers") private Integer maxTransfers    = null;

    // Transit route types that should not be used for routing
    @Transient private List<Integer> avoidTransitRouteTypes                    = Collections.emptyList();

    @Transient private Double buffer                                = null;
    @Transient private Double simplify                              = null;
    @Transient private PolygonIntersectionMode intersectionMode     = PolygonIntersectionMode.UNION;
    @Transient private PathSerializerType pathSerializer            = PathSerializerType.COMPACT_PATH_SERIALIZER;
    @Transient private PolygonSerializerType polygonSerializerType  = PolygonSerializerType.JSON_POLYGON_SERIALIZER;

    @Transient private Set<Integer> multiGraphEdgeClasses                                    = null;
    @Transient private MultiGraphSerializationFormat multiGraphSerializationFormat           = null;
    @Transient private Integer multiGraphSerializationDecimalPrecision                       = null;
    @Transient private Integer multiGraphSerializationMaxGeometryCount                       = null;
    @Transient private MultiGraphAggregationType multiGraphAggregationType                   = null;
    @Transient private Boolean multiGraphAggregationIgnoreOutliers                           = null;
    @Transient private Float multiGraphAggregationOutlierPenalty                             = null;
    @Transient private Double multiGraphAggregationMinSourcesRatio                           = null;
    @Transient private Integer multiGraphAggregationMinSourcesCount                          = null;
    @Transient private Double multiGraphAggregationMaxResultValueRatio                       = null;
    @Transient private Float multiGraphAggregationMaxResultValue                             = null;
    @Transient private String multiGraphAggregationMathExpression                            = null;
    @Transient private GeometryMergeAggType multiGraphLayerCustomGeometryMergeAggregation = null;
    @Transient private Set<String> multiGraphAggregationFilterValuesForSourceOrigins         = null;
    @Transient private Double multiGraphAggregationGravitationExponent                       = null;
    @Transient private Float multiGraphAggregationPostAggregationFactor                      = null;
    @Transient private Map<String, AggregationInputParameters> multiGraphAggregationInputParameters = null;
    @Transient private LinkedHashMap<String, AggregationConfiguration> multiGraphPreAggregationPipeline = null;
    @Transient private Map<String,Short> multiGraphReferencedStatisticIds                    = null;
    @Transient private MultiGraphLayerType multiGraphLayerType                               = null;
    @Transient private MultiGraphLayerEdgeAggregationType multiGraphLayerEdgeAggregationType = null;
    @Transient private Integer multiGraphLayerGeometryDetailPerTile                          = null;
    @Transient private Integer multiGraphLayerMinGeometryDetailLevel                         = null;
    @Transient private Integer multiGraphLayerMaxGeometryDetailLevel                         = null;
    @Transient private Integer multiGraphLayerGeometryDetailLevel                            = null;
    @Transient private Integer multiGraphLayerStatisticGroupId                               = null;
    @Transient private List<Short> multiGraphLayerStatisticsIds                              = null;
    @Transient private Integer multiGraphTileZoom                                            = null;
    @Transient private Integer multiGraphTileX                                               = null;
    @Transient private Integer multiGraphTileY                                               = null;
    
    @Column(name = "max_edge_weight") private Integer maxEdgeWeight            = 1800;
    @Column(name = "service_url") private String serviceUrl                    = "";
    @Column(name = "fallback_service_url") private String fallbackServiceUrl   = "";
    @Column(name = "service_key") private String serviceKey                    = "";
    @Transient private boolean onlyPrintReachablePoints                        = true;

    @JsonProperty("edgeWeight")
    @Column(name = "edge_weight_type") private EdgeWeightType edgeWeightType   = EdgeWeightType.TIME;

	@Transient private List<Short> statisticIds;
	@Column(name = "statistic_group_id") private Integer statisticGroupId;
    @Column(name = "statistic_service_url") private String statisticServiceUrl = "https://api.targomo.com/statistics/";
	@Column(name = "poi_service_url") private String pointOfInterestServiceUrl = "https://api.targomo.com/pointsofinterest/";

	@Column(name = "overpass_query") private String overpassQuery;
	@Column(name = "overpass_service_url") private String overpassServiceUrl = "https://api.targomo.com/overpass/";

    @Column(name = "inter_service_key") private String interServiceKey = "";

	@Transient
	private Format format;

	@Transient
	private Geometry intersectionGeometry;

	@Transient
	private String boundingBox;
	
    @Transient
    private Set<TravelType> travelTypes = new HashSet<>();

    @Transient
    private Set<PoiType> osmTypes = new HashSet<>();

    @Transient
    private Set<PoiType> customPois = new HashSet<>();

    @Transient
    private boolean disableCache;
    
    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Set<PoiType> getCustomPois() {
        return customPois;
    }

    public void setCustomPois(Set<PoiType> customPois) {
        this.customPois = customPois;
    }

    public Set<TravelType> getTravelTypes() {
        return travelTypes;
    }

    public void setTravelTypes(Set<TravelType> travelTypes) {
        this.travelTypes = travelTypes;
    }

    public Set<PoiType> getOsmTypes() {
        return osmTypes;
    }

    public void setOsmTypes(Set<PoiType> osmTypes) {
        this.osmTypes = osmTypes;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

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
     * @return the sources as Map from ID to location
     */
    public Map<String, Coordinate> getSources() {
        return sources;
    }

    /**
     * <p>
     * Set sources as Map from IDs to location.
     * </p>
     * <p>
     * When using the Time Service the IDs have to be unique and non-empty,
     * possibly generated. The same ID has to be used on the Coordinate itself
     * and as key in the map.
     * </p>
     * <p>
     * The Time Service simply returns the same IDs and doesn't care whether
     * they are unique or even null. But the answer just contains the IDs and
     * the time response. To be able to reconstruct a Map containing coordinates
     * for {@code TimeResponse.getTravelTimes()}, a lookup by ID has to be
     * performed on this map stored in the travel options.
     * </p>
     *
     * @param sources Map from ID to location
     */
    public void setSources(Map<String, Coordinate> sources) {
        this.sources = sources;
    }

    /**
     * @return the targets as Map from ID to location
     */
    public Map<String, Coordinate> getTargets() {
        return targets;
    }

    /**
     * <p>
     * Set sources as Map from IDs to location.
     * </p>
     * <p>
     * When using the Time Service the IDs have to be unique and non-empty,
     * possibly generated. The same ID has to be used on the Coordinate itself
     * and as key in the map.
     * </p>
     * <p>
     * The Time Service simply returns the same IDs and doesn't care whether
     * they are unique or even null. But the answer just contains the IDs and
     * the time response. To be able to reconstruct a Map containing coordinates
     * for {@code TimeResponse.getTravelTimes()}, a lookup by ID has to be
     * performed on this map stored in the travel options.
     * </p>
     *
     * @param targets Map from ID to location
     */
    public void setTargets(Map<String,Coordinate> targets) {
        this.targets = targets;
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
        this.targets = targets.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
    }

    /**
     * @return the bikeSpeed
     */
    public double getBikeSpeed() {
        return bikeSpeed;
    }
    /**
     * @param bikeSpeed the bikeSpeed to set
     */
    public void setBikeSpeed(double bikeSpeed) {
        this.bikeSpeed = bikeSpeed;
    }
    /**
     * @return the bikeUphill
     */
    public double getBikeUphill() {
        return bikeUphill;
    }
    /**
     * @param bikeUphill the bikeUphill to set
     */
    public void setBikeUphill(double bikeUphill) {
        this.bikeUphill = bikeUphill;
    }
    /**
     * @return the bikeDownhill
     */
    public double getBikeDownhill() {
        return bikeDownhill;
    }
    /**
     * @param bikeDownhill the bikeDownhill to set
     */
    public void setBikeDownhill(double bikeDownhill) {
        this.bikeDownhill = bikeDownhill;
    }
    /**
     * @return the trafficJunctionPenalty
     */
    public Integer getTrafficJunctionPenalty() {
        return trafficJunctionPenalty;
    }
    /**
     * @param trafficJunctionPenalty the trafficJunctionPenalty to set
     */
    public void setTrafficJunctionPenalty(Integer trafficJunctionPenalty) {
        this.trafficJunctionPenalty = trafficJunctionPenalty;
    }
    /**
     * @return the trafficSignalPenalty
     */
    public Integer getTrafficSignalPenalty() {
        return trafficSignalPenalty;
    }
    /**
     * @param trafficSignalPenalty the trafficSignalPenalty to set
     */
    public void setTrafficSignalPenalty(Integer trafficSignalPenalty) {
        this.trafficSignalPenalty = trafficSignalPenalty;
    }
    /**
     * @return the walkSpeed
     */
    public double getWalkSpeed() {
        return walkSpeed;
    }
    /**
     * @param walkSpeed the walkSpeed to set
     */
    public void setWalkSpeed(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
    /**
     * @return the walkUphill
     */
    public double getWalkUphill() {
        return walkUphill;
    }
    /**
     * @param walkUphill the walkUphill to set
     */
    public void setWalkUphill(double walkUphill) {
        this.walkUphill = walkUphill;
    }
    /**
     * @return the walkDownhill
     */
    public double getWalkDownhill() {
        return walkDownhill;
    }

    /**
     * @param walkDownhill the walkDownhill to set
     */
    public void setWalkDownhill(double walkDownhill) {
        this.walkDownhill = walkDownhill;
    }

    public void setRushHour(boolean rushHourEnabled) {
        this.rushHour = rushHourEnabled;
    }

    public boolean getRushHour() {
        return rushHour;
    }

    /**
     * @return the travelTimes
     */
    public List<Integer> getTravelTimes() {
        return travelTimes;
    }
    /**
     * @param travelTimes the travelTimes to set
     */
    public void setTravelTimes(List<Integer> travelTimes) {
        this.travelTimes = travelTimes;
    }
    /**
     * @return the travelType
     */
    public TravelType getTravelType() {
        return travelType;
    }
    /**
     * @param travelType the travelType to set
     */
    public void setTravelType(TravelType travelType) {
        this.travelType = travelType;
    }
    /**
     * @return the elevationEnabled
     */
    public Boolean isElevationEnabled() {
        return elevationEnabled;
    }
    /**
     * @param elevationEnabled the elevationEnabled to set
     */
    public void setElevationEnabled(Boolean elevationEnabled) {
        this.elevationEnabled = elevationEnabled;
    }
    /**
     * @return the minPolygonHoleSize
     */
    public long getMinPolygonHoleSize() {
        return minPolygonHoleSize;
    }
    /**
     * @param minPolygonHoleSize the minPolygonHoleSize to set
     */
    public void setMinPolygonHoleSize(long minPolygonHoleSize) {
        this.minPolygonHoleSize = minPolygonHoleSize;
    }
    /**
     * @return the time as seconds of the day
     */
    public Integer getTime() {
        return time;
    }
    /**
     * @param time seconds of the day
     */
    public void setTime(Integer time) {
        this.time = time;
    }
    /**
     * @return the date as integer in the format {@code yyyy * 10_000 + MM * 100 + dd}
     */
    public Integer getDate() {
        return date;
    }
    /**
     * @param date the date to set as integer in the format {@code yyyy * 10_000 + MM * 100 + dd}
     */
    public void setDate(Integer date) {
        this.date = date;
    }
    /**
     * @return the frame, which is the length of the time interval to search for transit connections, in seconds
     */
    public Integer getFrame() {
        return frame;
    }
    /**
     * @param frame the frame, which is the length of the time interval to search for transit connections, in seconds
     */
    public void setFrame(Integer frame) {
        this.frame = frame;
    }
    /**
     * @return the maxWalkingTimeFromSource, which is the maximum time that can be used from the sources to a transit stop
     * (in seconds)
     */
    public Integer getMaxWalkingTimeFromSource() {
        return maxWalkingTimeFromSource;
    }
    /**
     * @param maxWalkingTimeFromSource is the maximum time that can be used from the sources to a transit stop
     *                                (in seconds)
     */
    public void setMaxWalkingTimeFromSource(Integer maxWalkingTimeFromSource) {
        this.maxWalkingTimeFromSource = maxWalkingTimeFromSource;
    }
    /**
     * @return the maxWalkingTimeFromTarget, which is the maximum time that can be used to the targets from a transit stop (in
     * seconds)
     */
    public Integer getMaxWalkingTimeToTarget() {
        return maxWalkingTimeToTarget;
    }
    /**
     * @param maxWalkingTimeToTarget is the maximum time that can be used to the targets from a transit stop (in
     *                               seconds)
     */
    public void setMaxWalkingTimeToTarget(Integer maxWalkingTimeToTarget) {
        this.maxWalkingTimeToTarget = maxWalkingTimeToTarget;
    }
    /**
     * @return the recommendations
     */
    public int getRecommendations() {
        return recommendations;
    }
    /**
     * @param recommendations the recommendations to set
     */
    public void setRecommendations(int recommendations) {
        this.recommendations = recommendations;
    }
    /**
     * @return the intersectionMode
     */
    public PolygonIntersectionMode getIntersectionMode() {
        return intersectionMode;
    }
    /**
     * @param intersectionMode the intersectionMode to set. Default: UNION
     */
    public void setIntersectionMode(PolygonIntersectionMode intersectionMode) {
        this.intersectionMode = intersectionMode;
    }
    /**
     * @return the pathSerializer
     */
    public PathSerializerType getPathSerializer() {
        return pathSerializer;
    }
    /**
     * @param pathSerializer the pathSerializer to set. Default: COMPACT
     */
    public void setPathSerializer(PathSerializerType pathSerializer) {
        this.pathSerializer = pathSerializer;
    }
    /**
     * @return the polygonSerializerType
     */
    public PolygonSerializerType getPolygonSerializerType() {
        return polygonSerializerType;
    }
    /**
     * @param polygonSerializerType the polygonSerializerType to set. Default: JSON
     */
    public void setPolygonSerializerType(PolygonSerializerType polygonSerializerType) {
        this.polygonSerializerType = polygonSerializerType;
    }
    /**
     * @return the pointReduction
     */
    public boolean isPointReduction() {
        return pointReduction;
    }
    /**
     * @param pointReduction the pointReduction to set. Default: true
     */
    public void setPointReduction(boolean pointReduction) {
        this.pointReduction = pointReduction;
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
     * @return the serviceUrl
     */
    public String getServiceUrl() {
        return serviceUrl;
    }
    /**
     * @param serviceUrl the serviceUrl to set
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    /**
     * @return the serviceKey
     */
    public String getServiceKey() {
        return serviceKey;
    }
    /**
     * @param serviceKey the serviceKey to set
     */
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * @param source Source coordinate
     */
    public void addSource(Coordinate source) {
        this.sources.put(source.getId(), source);
    }

    /**
     * @param target Target coordinate
     */
    public void addTarget(Coordinate target) {
        this.targets.put(target.getId(), target);
    }

    public Map<String, Short> getMultiGraphReferencedStatisticIds() {
        return multiGraphReferencedStatisticIds;
    }

    public void setMultiGraphReferencedStatisticIds(Map<String, Short> multiGraphReferencedStatisticIds) {
        this.multiGraphReferencedStatisticIds = multiGraphReferencedStatisticIds;
    }

    public Map<String, AggregationInputParameters> getMultiGraphAggregationInputParameters() {
        return multiGraphAggregationInputParameters;
    }

    public void setMultiGraphAggregationInputParameters(Map<String, AggregationInputParameters> multiGraphAggregationInputParameters) {
        this.multiGraphAggregationInputParameters = multiGraphAggregationInputParameters;
    }

    public LinkedHashMap<String, AggregationConfiguration> getMultiGraphPreAggregationPipeline() {
        return multiGraphPreAggregationPipeline;
    }

    public void setMultiGraphPreAggregationPipeline(LinkedHashMap<String, AggregationConfiguration> multiGraphPreAggregationPipeline) {
        this.multiGraphPreAggregationPipeline = multiGraphPreAggregationPipeline;
    }

    public String getMultiGraphAggregationMathExpression() {
        return multiGraphAggregationMathExpression;
    }

    public void setMultiGraphAggregationMathExpression(String multiGraphAggregationMathExpression) {
        this.multiGraphAggregationMathExpression = multiGraphAggregationMathExpression;
    }

    public GeometryMergeAggType getMultiGraphLayerCustomGeometryMergeAggregation() {
        return multiGraphLayerCustomGeometryMergeAggregation;
    }

    public void setMultiGraphLayerCustomGeometryMergeAggregation(GeometryMergeAggType multiGraphLayerCustomGeometryMergeAggregation) {
        this.multiGraphLayerCustomGeometryMergeAggregation = multiGraphLayerCustomGeometryMergeAggregation;
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
                Objects.equals(that.trafficJunctionPenalty, trafficJunctionPenalty) &&
                Objects.equals(that.trafficSignalPenalty, trafficSignalPenalty) &&
                onlyPrintReachablePoints == that.onlyPrintReachablePoints &&
                Objects.equals(sources, that.sources) &&
                Objects.equals(targets, that.targets) &&
                Objects.equals(rushHour, that.rushHour) &&
                Objects.equals(travelTimes, that.travelTimes) &&
                travelType == that.travelType &&
                Objects.equals(elevationEnabled, that.elevationEnabled) &&
                Objects.equals(pointReduction, that.pointReduction) &&
                Objects.equals(reverse, that.reverse) &&
                Objects.equals(minPolygonHoleSize, that.minPolygonHoleSize) &&
                Objects.equals(time, that.time) &&
                Objects.equals(date, that.date) &&
                Objects.equals(frame, that.frame) &&
				Objects.equals(intersectionGeometry, that.intersectionGeometry) &&
                Objects.equals(recommendations, that.recommendations) &&
                Objects.equals(srid, that.srid) &&
                Objects.equals(decimalPrecision, that.decimalPrecision) &&
                Objects.equals(buffer, that.buffer) &&
                Objects.equals(simplify, that.simplify) &&
                intersectionMode == that.intersectionMode &&
                pathSerializer == that.pathSerializer &&
                polygonSerializerType == that.polygonSerializerType &&
                Objects.equals(multiGraphEdgeClasses, that.multiGraphEdgeClasses) &&
                multiGraphSerializationFormat == that.multiGraphSerializationFormat &&
                Objects.equals(multiGraphSerializationDecimalPrecision, that.multiGraphSerializationDecimalPrecision) &&
                Objects.equals(multiGraphSerializationMaxGeometryCount, that.multiGraphSerializationMaxGeometryCount) &&
                multiGraphAggregationType == that.multiGraphAggregationType &&
                Objects.equals(multiGraphAggregationIgnoreOutliers, that.multiGraphAggregationIgnoreOutliers) &&
                Objects.equals(multiGraphAggregationOutlierPenalty, that.multiGraphAggregationOutlierPenalty) &&
                Objects.equals(multiGraphAggregationMinSourcesRatio, that.multiGraphAggregationMinSourcesRatio) &&
                Objects.equals(multiGraphAggregationMinSourcesCount, that.multiGraphAggregationMinSourcesCount) &&
                Objects.equals(multiGraphAggregationMaxResultValueRatio, that.multiGraphAggregationMaxResultValueRatio) &&
                Objects.equals(multiGraphAggregationMaxResultValue, that.multiGraphAggregationMaxResultValue) &&
                Objects.equals(multiGraphAggregationFilterValuesForSourceOrigins, that.multiGraphAggregationFilterValuesForSourceOrigins) &&
                Objects.equals(multiGraphAggregationGravitationExponent, that.multiGraphAggregationGravitationExponent) &&
                Objects.equals(multiGraphAggregationInputParameters, that.multiGraphAggregationInputParameters) &&
                Objects.equals(multiGraphAggregationMathExpression, that.multiGraphAggregationMathExpression) &&
                Objects.equals(multiGraphLayerCustomGeometryMergeAggregation, that.multiGraphLayerCustomGeometryMergeAggregation) &&
                Objects.equals(multiGraphAggregationPostAggregationFactor, that.multiGraphAggregationPostAggregationFactor) &&
                Objects.equals(multiGraphReferencedStatisticIds, that.multiGraphReferencedStatisticIds) &&
                multiGraphLayerType == that.multiGraphLayerType &&
                multiGraphLayerEdgeAggregationType == that.multiGraphLayerEdgeAggregationType &&
                Objects.equals(multiGraphLayerGeometryDetailPerTile, that.multiGraphLayerGeometryDetailPerTile) &&
                Objects.equals(multiGraphLayerMinGeometryDetailLevel, that.multiGraphLayerMinGeometryDetailLevel) &&
                Objects.equals(multiGraphLayerMaxGeometryDetailLevel, that.multiGraphLayerMaxGeometryDetailLevel) &&
                Objects.equals(multiGraphLayerGeometryDetailLevel, that.multiGraphLayerGeometryDetailLevel) &&
                Objects.equals(multiGraphLayerStatisticGroupId, that.multiGraphLayerStatisticGroupId) &&
                Objects.equals(multiGraphLayerStatisticsIds, that.multiGraphLayerStatisticsIds) &&
                Objects.equals(multiGraphTileZoom, that.multiGraphTileZoom) &&
                Objects.equals(multiGraphTileX, that.multiGraphTileX) &&
                Objects.equals(multiGraphTileY, that.multiGraphTileY) &&
                Objects.equals(maxEdgeWeight, that.maxEdgeWeight) &&
                Objects.equals(serviceUrl, that.serviceUrl) &&
                Objects.equals(fallbackServiceUrl, that.fallbackServiceUrl) &&
                Objects.equals(serviceKey, that.serviceKey) &&
                edgeWeightType == that.edgeWeightType &&
                Objects.equals(statisticIds, that.statisticIds) &&
                Objects.equals(statisticGroupId, that.statisticGroupId) &&
                Objects.equals(statisticServiceUrl, that.statisticServiceUrl) &&
                Objects.equals(pointOfInterestServiceUrl, that.pointOfInterestServiceUrl) &&
                Objects.equals(overpassQuery, that.overpassQuery) &&
                Objects.equals(overpassServiceUrl, that.overpassServiceUrl) &&
                Objects.equals(interServiceKey, that.interServiceKey) &&
                format == that.format &&
                Objects.equals(boundingBox, that.boundingBox) &&
                Objects.equals(travelTypes, that.travelTypes) &&
                Objects.equals(osmTypes, that.osmTypes) &&
                Objects.equals(customPois, that.customPois) &&
                Objects.equals(travelTimeFactors, that.travelTimeFactors) &&
                Objects.equals(maxTransfers, that.maxTransfers) &&
                Objects.equals(avoidTransitRouteTypes, that.avoidTransitRouteTypes) &&
                Objects.equals(multiGraphPreAggregationPipeline, that.multiGraphPreAggregationPipeline) &&
                Objects.equals(maxWalkingTimeFromSource, that.maxWalkingTimeFromSource) &&
                Objects.equals(maxWalkingTimeToTarget, that.maxWalkingTimeToTarget);
    }
                

    //excluding id
    @Override
    public int hashCode() {

        return Objects.hash(sources, targets, bikeSpeed, bikeUphill, bikeDownhill, walkSpeed, walkUphill, walkDownhill,
                rushHour, travelTimes, travelType, elevationEnabled, appendTravelTimes, pointReduction, reverse,
                minPolygonHoleSize, time, date, frame, recommendations, srid, decimalPrecision, buffer, simplify,
                intersectionMode, pathSerializer, polygonSerializerType, intersectionGeometry,
                multiGraphEdgeClasses, multiGraphSerializationFormat,
                multiGraphSerializationDecimalPrecision, multiGraphSerializationMaxGeometryCount,
                multiGraphAggregationType, multiGraphAggregationIgnoreOutliers, multiGraphAggregationOutlierPenalty,
                multiGraphAggregationMinSourcesRatio, multiGraphAggregationMinSourcesCount,
                multiGraphAggregationMaxResultValueRatio, multiGraphAggregationMaxResultValue,
                multiGraphAggregationGravitationExponent, multiGraphLayerCustomGeometryMergeAggregation,
                multiGraphAggregationInputParameters, multiGraphAggregationFilterValuesForSourceOrigins,
                multiGraphPreAggregationPipeline, multiGraphAggregationMathExpression, multiGraphReferencedStatisticIds,
                multiGraphLayerType, multiGraphLayerEdgeAggregationType, multiGraphLayerGeometryDetailPerTile,
                multiGraphLayerMinGeometryDetailLevel, multiGraphLayerMaxGeometryDetailLevel,
                multiGraphLayerGeometryDetailLevel, multiGraphLayerStatisticGroupId, multiGraphLayerStatisticsIds,
                multiGraphTileZoom, multiGraphTileX, multiGraphTileY, multiGraphAggregationPostAggregationFactor,
                maxEdgeWeight, serviceUrl, fallbackServiceUrl, serviceKey, onlyPrintReachablePoints, edgeWeightType,
                statisticIds, statisticGroupId, statisticServiceUrl, pointOfInterestServiceUrl, overpassQuery,
                overpassServiceUrl, interServiceKey, format, boundingBox, travelTypes, osmTypes, customPois,
                travelTimeFactors, maxTransfers, avoidTransitRouteTypes, trafficJunctionPenalty, trafficSignalPenalty,
                maxWalkingTimeFromSource, maxWalkingTimeToTarget);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final int maxLen = 5;
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append(" {\n\tsources: ");
        builder.append(sources != null ? toString(sources.entrySet(), maxLen) : null);
        builder.append("\n\ttargets: ");
        builder.append(targets != null ? toString(targets.entrySet(), maxLen) : null);
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
        builder.append("\n\ttrafficJunctionPenalty: ");
        builder.append(trafficJunctionPenalty);
        builder.append("\n\ttrafficSignalPenalty: ");
        builder.append(trafficSignalPenalty);
        builder.append("\n\trushHour: ");
        builder.append(rushHour);
        builder.append("\n\ttravelTimes: ");
        builder.append(travelTimes != null ? toString(travelTimes, maxLen) : null);
        builder.append("\n\ttravelType: ");
        builder.append(travelType);
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
        builder.append("\n\tframe: ");
        builder.append(frame);
        builder.append("\n\trecommendations: ");
        builder.append(recommendations);
        builder.append("\n\tsrid: ");
        builder.append(srid);
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
        builder.append("\n\tmultiGraphEdgeClasses: ");
        builder.append(multiGraphEdgeClasses);
        builder.append("\n\tmultiGraphSerializationFormat: ");
        builder.append(multiGraphSerializationFormat);
        builder.append("\n\tmultiGraphSerializationDecimalPrecision: ");
        builder.append(multiGraphSerializationDecimalPrecision);
        builder.append("\n\tmultiGraphSerializationMaxGeometryCount: ");
        builder.append(multiGraphSerializationMaxGeometryCount);
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
        builder.append("\n\tmultiGraphAggregationInputParameters: ");
        builder.append(multiGraphAggregationInputParameters);
        builder.append("\n\tmultiGraphAggregationFilterValuesForSourceOrigins: ");
        builder.append(multiGraphAggregationFilterValuesForSourceOrigins);
        builder.append("\n\tmultiGraphPreAggregationPipeline: ");
        builder.append(multiGraphPreAggregationPipeline);
        builder.append("\n\tmultiGraphReferencedStatisticIds: ");
        builder.append(multiGraphReferencedStatisticIds);
        builder.append("\n\tmultiGraphLayerType: ");
        builder.append(multiGraphLayerType);
        builder.append("\n\tmultiGraphLayerEdgeAggregationType: ");
        builder.append(multiGraphLayerEdgeAggregationType);
        builder.append("\n\tmultiGraphLayerGeometryDetailPerTile: ");
        builder.append(multiGraphLayerGeometryDetailPerTile);
        builder.append("\n\tmultiGraphLayerMinGeometryDetailLevel: ");
        builder.append(multiGraphLayerMinGeometryDetailLevel);
        builder.append("\n\tmultiGraphLayerMaxGeometryDetailLevel: ");
        builder.append(multiGraphLayerMaxGeometryDetailLevel);
        builder.append("\n\tmultiGraphLayerGeometryDetailLevel: ");
        builder.append(multiGraphLayerGeometryDetailLevel);
        builder.append("\n\tmultiGraphLayerStatisticGroupId: ");
        builder.append(multiGraphLayerStatisticGroupId);
        builder.append("\n\tmultiGraphLayerStatisticsIds: ");
        builder.append(multiGraphLayerStatisticsIds);
        builder.append("\n\tmultiGraphTileZoom: ");
        builder.append(multiGraphTileZoom);
        builder.append("\n\tmultiGraphTileX: ");
        builder.append(multiGraphTileX);
        builder.append("\n\tmultiGraphTileY: ");
        builder.append(multiGraphTileY);
        builder.append("\n\tmaxEdgeWeight: ");
        builder.append(maxEdgeWeight);
        builder.append("\n\tserviceUrl: ");
        builder.append(serviceUrl);
        builder.append("\n\tserviceKey: ");
        builder.append(fallbackServiceUrl);
        builder.append("\n\tfallbackServiceUrl: ");
        builder.append(serviceKey);
        builder.append("\n\tonlyPrintReachablePoints: ");
        builder.append(onlyPrintReachablePoints);
        builder.append("\n\tedgeWeightType: ");
        builder.append(edgeWeightType);
        builder.append("\n\tstatisticIds: ");
        builder.append(statisticIds != null ? toString(statisticIds, maxLen) : null);
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
        builder.append("\n}\n");
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
     * @param id ID of source Coordinate
     * @return Target coordinate
     */
    public Coordinate getTarget(String id) {
        return this.targets.get(id);
    }

    public List<Short> getStatisticIds() {
        return this.statisticIds;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * Determines whether to return only reachable points or all
     * @param onlyPrintReachablePoints default: true
     */
    public void setOnlyPrintReachablePoints(boolean onlyPrintReachablePoints) {
        this.onlyPrintReachablePoints = onlyPrintReachablePoints;
    }

    public boolean getOnlyPrintReachablePoints() {
        return onlyPrintReachablePoints;
    }

    public void setStatisticIds(List<Short> statisticIds) {
        this.statisticIds = statisticIds ;
    }

    /**
     * Get the buffer value of polygons. Unit can be meters or degrees depending on the output CRS
     * @return Buffer value in meters or in degrees
     */
    public Double getBuffer() {
        return buffer;
    }

    /**
     * Set how much the polygons will be buffered. Unit can be meters or degrees depending on the output CRS
     * @param buffer Buffer value in meters or in degrees
     */
    public void setBuffer(Double buffer) {
        this.buffer = buffer;
    }

    /**
     * Get the simplify value of polygons (in meters).
     * @return Simplify value in meters or in degrees
     */
    public Double getSimplify() {
        return simplify;
    }

    /**
     * Set how much the polygons will be simplified (in meters). This can reduce the points in the polygon significantly.
     * @param simplify Simplify value in meters
     */
    public void setSimplify(Double simplify) {
        this.simplify = simplify;
    }

    public Boolean getReverse() {
        return reverse;
    }

    /**
     *
     * @param reverse default: false
     */
    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public Integer getDecimalPrecision() {
        return decimalPrecision;
    }

    public void setDecimalPrecision(Integer decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    /**
     *
     * @return Append travel times setting
     */
    public Boolean getAppendTravelTimes() {
        return this.appendTravelTimes;
    }

    public Set<Integer> getMultiGraphEdgeClasses() {
        return multiGraphEdgeClasses;
    }

    public void setMultiGraphEdgeClasses(Set<Integer> multiGraphEdgeClasses) {
        this.multiGraphEdgeClasses = multiGraphEdgeClasses;
    }

    public MultiGraphSerializationFormat getMultiGraphSerializationFormat() {
        return multiGraphSerializationFormat;
    }

    public void setMultiGraphSerializationFormat(MultiGraphSerializationFormat multiGraphSerializationFormat) {
        this.multiGraphSerializationFormat = multiGraphSerializationFormat;
    }

    public Integer getMultiGraphSerializationDecimalPrecision() {
        return multiGraphSerializationDecimalPrecision;
    }

    public void setMultiGraphSerializationDecimalPrecision(Integer multiGraphSerializationDecimalPrecision) {
        this.multiGraphSerializationDecimalPrecision = multiGraphSerializationDecimalPrecision;
    }

    public Integer getMultiGraphSerializationMaxGeometryCount() {
        return multiGraphSerializationMaxGeometryCount;
    }

    public void setMultiGraphSerializationMaxGeometryCount(Integer multiGraphSerializationMaxGeometryCount) {
        this.multiGraphSerializationMaxGeometryCount = multiGraphSerializationMaxGeometryCount;
    }

    public MultiGraphAggregationType getMultiGraphAggregationType() {
        return multiGraphAggregationType;
    }

    public void setMultiGraphAggregationType(MultiGraphAggregationType multiGraphAggregationType) {
        this.multiGraphAggregationType = multiGraphAggregationType;
    }

    public Boolean getMultiGraphAggregationIgnoreOutliers() {
        return multiGraphAggregationIgnoreOutliers;
    }

    public void setMultiGraphAggregationIgnoreOutliers(Boolean multiGraphAggregationIgnoreOutliers) {
        this.multiGraphAggregationIgnoreOutliers = multiGraphAggregationIgnoreOutliers;
    }

    public Float getMultiGraphAggregationOutlierPenalty() {
        return multiGraphAggregationOutlierPenalty;
    }

    public void setMultiGraphAggregationOutlierPenalty(Float multiGraphAggregationOutlierPenalty) {
        this.multiGraphAggregationOutlierPenalty = multiGraphAggregationOutlierPenalty;
    }

    public Double getMultiGraphAggregationMinSourcesRatio() {
        return multiGraphAggregationMinSourcesRatio;
    }

    public void setMultiGraphAggregationMinSourcesRatio(Double multiGraphAggregationMinSourcesRatio) {
        this.multiGraphAggregationMinSourcesRatio = multiGraphAggregationMinSourcesRatio;
    }

    public Integer getMultiGraphAggregationMinSourcesCount() {
        return multiGraphAggregationMinSourcesCount;
    }

    public void setMultiGraphAggregationMinSourcesCount(Integer multiGraphAggregationMinSourcesCount) {
        this.multiGraphAggregationMinSourcesCount = multiGraphAggregationMinSourcesCount;
    }

    public Double getMultiGraphAggregationMaxResultValueRatio() {
        return multiGraphAggregationMaxResultValueRatio;
    }

    public void setMultiGraphAggregationMaxResultValueRatio(Double multiGraphAggregationMaxResultValueRatio) {
        this.multiGraphAggregationMaxResultValueRatio = multiGraphAggregationMaxResultValueRatio;
    }

    public Float getMultiGraphAggregationMaxResultValue() {
        return multiGraphAggregationMaxResultValue;
    }

    public void setMultiGraphAggregationMaxResultValue(Float multiGraphAggregationMaxResultValue) {
        this.multiGraphAggregationMaxResultValue = multiGraphAggregationMaxResultValue;
    }

    public Double getMultiGraphAggregationGravitationExponent() {
        return multiGraphAggregationGravitationExponent;
    }

    public void setMultiGraphAggregationGravitationExponent(Double multiGraphAggregationGravitationExponent) {
        this.multiGraphAggregationGravitationExponent = multiGraphAggregationGravitationExponent;
    }

    public Set<String> getMultiGraphAggregationFilterValuesForSourceOrigins() {
        return multiGraphAggregationFilterValuesForSourceOrigins;
    }

    public void setMultiGraphAggregationFilterValuesForSourceOrigins(Set<String> multiGraphAggregationFilterValuesForSourceOrigins) {
        this.multiGraphAggregationFilterValuesForSourceOrigins = multiGraphAggregationFilterValuesForSourceOrigins;
    }

    public MultiGraphLayerType getMultiGraphLayerType() {
        return multiGraphLayerType;
    }

    public void setMultiGraphLayerType(MultiGraphLayerType multiGraphLayerType) {
        this.multiGraphLayerType = multiGraphLayerType;
    }

    public MultiGraphLayerEdgeAggregationType getMultiGraphLayerEdgeAggregationType() {
        return multiGraphLayerEdgeAggregationType;
    }

    public void setMultiGraphLayerEdgeAggregationType(MultiGraphLayerEdgeAggregationType multiGraphLayerEdgeAggregationType) {
        this.multiGraphLayerEdgeAggregationType = multiGraphLayerEdgeAggregationType;
    }

    public Integer getMultiGraphLayerGeometryDetailPerTile() {
        return multiGraphLayerGeometryDetailPerTile;
    }

    public void setMultiGraphLayerGeometryDetailPerTile(Integer multiGraphLayerGeometryDetailPerTile) {
        this.multiGraphLayerGeometryDetailPerTile = multiGraphLayerGeometryDetailPerTile;
    }

    public Integer getMultiGraphLayerMinGeometryDetailLevel() {
        return multiGraphLayerMinGeometryDetailLevel;
    }

    public void setMultiGraphLayerMinGeometryDetailLevel(Integer multiGraphLayerMinGeometryDetailLevel) {
        this.multiGraphLayerMinGeometryDetailLevel = multiGraphLayerMinGeometryDetailLevel;
    }

    public Integer getMultiGraphLayerMaxGeometryDetailLevel() {
        return multiGraphLayerMaxGeometryDetailLevel;
    }

    public void setMultiGraphLayerMaxGeometryDetailLevel(Integer multiGraphLayerMaxGeometryDetailLevel) {
        this.multiGraphLayerMaxGeometryDetailLevel = multiGraphLayerMaxGeometryDetailLevel;
    }

    public Integer getMultiGraphLayerGeometryDetailLevel() {
        return multiGraphLayerGeometryDetailLevel;
    }

    public void setMultiGraphLayerGeometryDetailLevel(Integer multiGraphLayerGeometryDetailLevel) {
        this.multiGraphLayerGeometryDetailLevel = multiGraphLayerGeometryDetailLevel;
    }

    public Integer getMultiGraphLayerStatisticGroupId() {
        return multiGraphLayerStatisticGroupId;
    }

    public void setMultiGraphLayerStatisticGroupId(Integer multiGraphLayerStatisticGroupId) {
        this.multiGraphLayerStatisticGroupId = multiGraphLayerStatisticGroupId;
    }

    public List<Short> getMultiGraphLayerStatisticsIds() {
        return multiGraphLayerStatisticsIds;
    }

    public void setMultiGraphLayerStatisticsIds(List<Short> multiGraphLayerStatisticsIds) {
        this.multiGraphLayerStatisticsIds = multiGraphLayerStatisticsIds;
    }

    public Integer getMultiGraphTileZoom() {
        return multiGraphTileZoom;
    }

    public void setMultiGraphTileZoom(Integer multiGraphTileZoom) {
        this.multiGraphTileZoom = multiGraphTileZoom;
    }

    public Integer getMultiGraphTileX() {
        return multiGraphTileX;
    }

    public void setMultiGraphTileX(Integer multiGraphTileX) {
        this.multiGraphTileX = multiGraphTileX;
    }

    public Integer getMultiGraphTileY() {
        return multiGraphTileY;
    }

    public void setMultiGraphTileY(Integer multiGraphTileY) {
        this.multiGraphTileY = multiGraphTileY;
    }
    
    public Float getMultiGraphAggregationPostAggregationFactor() {
        return multiGraphAggregationPostAggregationFactor;
    }

    public void setMultiGraphAggregationPostAggregationFactor(Float multiGraphAggregationPostAggregationFactor) {
        this.multiGraphAggregationPostAggregationFactor = multiGraphAggregationPostAggregationFactor;
    }

    public EdgeWeightType getEdgeWeightType() {
        return edgeWeightType;
    }

    public void setEdgeWeightType(final EdgeWeightType edgeWeightType) {
        this.edgeWeightType = edgeWeightType;
    }

    public void setAppendTravelTimes(Boolean appendTravelTimes) {
        this.appendTravelTimes = appendTravelTimes;
    }

    public String getStatisticServiceUrl() {
        return statisticServiceUrl;
    }

    public void setStatisticServiceUrl(String statisticServiceUrl) {
        this.statisticServiceUrl = statisticServiceUrl;
    }

    public Integer getStatisticGroupId() {
        return statisticGroupId;
    }

    public void setStatisticGroupId(Integer statisticGroupId) {
        this.statisticGroupId = statisticGroupId;
    }

    public Integer getMaxEdgeWeight() {
        return maxEdgeWeight;
    }

    public void setMaxEdgeWeight(Integer maxEdgeWeight) {
        this.maxEdgeWeight = maxEdgeWeight;
    }

    public void addAllSources(Map<String, Coordinate> sources) {
        this.sources.putAll(sources);
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

    public String getFallbackServiceUrl() {
        return fallbackServiceUrl;
    }

    public void setFallbackServiceUrl(String fallbackServiceUrl) {
        this.fallbackServiceUrl = fallbackServiceUrl;
    }

	public String getPointOfInterestServiceUrl() {
		return pointOfInterestServiceUrl;
	}

    public String getOverpassServiceUrl() {
        return overpassServiceUrl;
    }

    public void setOverpassServiceUrl(String overpassServiceUrl) {
        this.overpassServiceUrl = overpassServiceUrl;
    }

    public String getOverpassQuery() {
        return overpassQuery;
    }

    public void setOverpassQuery(String overpassQuery) {
        this.overpassQuery = overpassQuery;
    }

    public String getInterServiceKey() {
        return interServiceKey;
    }

    public void setInterServiceKey(String interServiceKey) {
        this.interServiceKey = interServiceKey;
    }

    public Integer getMaxTransfers() {
        return maxTransfers;
    }

    public void setMaxTransfers(Integer maxTransfers) {
        this.maxTransfers = maxTransfers;
    }

	public Map<String, Double> getTravelTimeFactors() {
		return travelTimeFactors;
	}

	public void setTravelTimeFactors(Map<String, Double> travelTimeFactors) {
		this.travelTimeFactors = travelTimeFactors;
	}

	public Geometry getIntersectionGeometry() {
		return intersectionGeometry;
	}

	public void setIntersectionGeometry(Geometry intersectionGeometry) {
		this.intersectionGeometry = intersectionGeometry;
	}
	
    public boolean isDisableCache() {
        return disableCache;
    }

    public void setDisableCache(boolean disableCache) {
        this.disableCache = disableCache;
    }

    public List<Integer> getAvoidTransitRouteTypes() {
        return avoidTransitRouteTypes;
    }

    public void setAvoidTransitRouteTypes(List<Integer> avoidTransitRouteTypes) {
        this.avoidTransitRouteTypes = avoidTransitRouteTypes;
    }
}
