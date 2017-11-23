package net.motionintelligence.client.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.motionintelligence.client.api.enums.*;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import net.motionintelligence.client.api.json.DefaultSourceCoordinateMapDeserializer;
import net.motionintelligence.client.api.json.DefaultSourceCoordinateMapSerializer;
import net.motionintelligence.client.api.json.DefaultTargetCoordinateMapDeserializer;
import net.motionintelligence.client.api.json.DefaultTargetCoordinateMapSerializer;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.statistic.PoiType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Common configuration class for executing all requests.
 * Define source points, target points and other configuration values such as travel times, elevation etc. here.
 * See: {@link net.motionintelligence.client.api.request.PolygonRequest},
 * {@link net.motionintelligence.client.api.request.RouteRequest},
 * {@link net.motionintelligence.client.api.request.TimeRequest},
 * {@link net.motionintelligence.client.api.request.ReachabilityRequest}.
 */
@Entity
@Table(name = "travel_option")
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
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

    @Transient private List<Integer> travelTimes                    = Arrays.asList(600, 1200, 1800);

    @Column(name = "travel_type")
    private TravelType travelType                                   = TravelType.UNSPECIFIED;

    @Column(name = "elevation_enabled")
    private Boolean elevationEnabled                                = false;

    @Transient private Boolean appendTravelTimes                    = false;
    @Transient private Boolean pointReduction                       = true;
    @Transient private Boolean reverse                              = false;
    @Transient private Long minPolygonHoleSize                      = 100000000L;

    @Column(name = "time") private Integer time                     = 9 * 3600;
    @Column(name = "date")  private Integer date                    = 20170214;
    @Column(name = "frame") private Integer frame                   = 18000;
    @Transient private Integer recommendations                      = 0;
    @Transient private Integer srid                                 = null;
    @Transient private Integer decimalPrecision                     = null;

    @Transient private Double buffer                                = null;
    @Transient private Double simplify                              = null;
    @Transient private PolygonIntersectionMode intersectionMode     = PolygonIntersectionMode.UNION;
    @Transient private PathSerializerType pathSerializer            = PathSerializerType.COMPACT_PATH_SERIALIZER;
    @Transient private PolygonSerializerType polygonSerializerType  = PolygonSerializerType.JSON_POLYGON_SERIALIZER;

    @Transient private MultiGraphSerializerType  multiGraphSerializer  = null;
    @Transient private MultiGraphAggregationType multiGraphAggregation = null;
    @Transient private Boolean multiGraphIncludeEdges = null;

    @Column(name = "max_edge_weight") private Integer maxEdgeWeight            = 1800;
    @Column(name = "service_url") private String serviceUrl                    = "";
    @Column(name = "fallback_service_url") private String fallbackServiceUrl   = "";
    @Column(name = "service_key") private String serviceKey                    = "";
    @Transient private boolean onlyPrintReachablePoints                           = true;

    @JsonProperty("edgeWeight")
    @Column(name = "edge_weight_type") private EdgeWeightType edgeWeightType   = EdgeWeightType.TIME;

    @Transient private List<Short> statisticIds;
    @Column(name = "statistic_group_id") private Integer statisticGroupId;
    @Column(name = "statistic_service_url") private String statisticServiceUrl = "https://service.route360.net/statistics/";
    @Column(name = "poi_service_url") private String pointOfInterestServiceUrl = "https://service.route360.net/pointsofinterest/";

    @Column(name = "overpass_query") private String overpassQuery;
    @Column(name = "overpass_service_url") private String overpassServiceUrl = "https://service.route360.net/overpass/";

    @Column(name = "inter_service_key") private String interServiceKey = "";

//    "rush_hour" -> "rushHour"

    @Transient
    private Format format;

    @Transient
    private String boundingBox;

    @Transient
    private Set<TravelType> travelTypes = new HashSet<>();

    @Transient
    private Set<PoiType> osmTypes = new HashSet<>();

    @Transient
    private Set<PoiType> customPois = new HashSet<>();

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

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
     * @return the sources
     */
    public Map<String, Coordinate> getSources() {
        return sources;
    }
    /**
     * @param sources the sources to set
     */
    public void setSources(Map<String, Coordinate> sources) {
        this.sources = sources;
    }
    /**
     * @return the targets
     */
    public Map<String, Coordinate> getTargets() {
        return targets;
    }
    /**
     * @param targets the targets to set
     */
    public void setTargets(Map<String,Coordinate> targets) {
        this.targets = targets;
    }
    
    /**
     * 
     * @param targets add all specified targets to the target map
     */
    public void addAllTargets(Map<String,Coordinate> targets) {
        this.targets.putAll(targets);
    }

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
     * @return the time
     */
    public int getTime() {
        return time;
    }
    /**
     * @param time the time to set
     */
    public void setTime(Integer time) {
        this.time = time;
    }
    /**
     * @return the date
     */
    public int getDate() {
        return date;
    }
    /**
     * @param date the date to set
     */
    public void setDate(Integer date) {
        this.date = date;
    }
    /**
     * @return the frame
     */
    public int getFrame() {
        return frame;
    }
    /**
     * @param frame the frame to set
     */
    public void setFrame(int frame) {
        this.frame = frame;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TravelOptions that = (TravelOptions) o;

        if (Double.compare(that.bikeSpeed, bikeSpeed) != 0) return false;
        if (Double.compare(that.bikeUphill, bikeUphill) != 0) return false;
        if (Double.compare(that.bikeDownhill, bikeDownhill) != 0) return false;
        if (Double.compare(that.walkSpeed, walkSpeed) != 0) return false;
        if (Double.compare(that.walkUphill, walkUphill) != 0) return false;
        if (Double.compare(that.walkDownhill, walkDownhill) != 0) return false;
        if (rushHour != that.rushHour) return false;
        if (onlyPrintReachablePoints != that.onlyPrintReachablePoints) return false;
        if (sources != null ? !sources.equals(that.sources) : that.sources != null) return false;
        if (targets != null ? !targets.equals(that.targets) : that.targets != null) return false;
        if (travelTimes != null ? !travelTimes.equals(that.travelTimes) : that.travelTimes != null) return false;
        if (travelType != that.travelType) return false;
        if (elevationEnabled != null ? !elevationEnabled.equals(that.elevationEnabled) : that.elevationEnabled != null)
            return false;
        if (appendTravelTimes != null ? !appendTravelTimes.equals(that.appendTravelTimes) : that.appendTravelTimes != null)
            return false;
        if (pointReduction != null ? !pointReduction.equals(that.pointReduction) : that.pointReduction != null)
            return false;
        if (reverse != null ? !reverse.equals(that.reverse) : that.reverse != null) return false;
        if (minPolygonHoleSize != null ? !minPolygonHoleSize.equals(that.minPolygonHoleSize) : that.minPolygonHoleSize != null)
            return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (frame != null ? !frame.equals(that.frame) : that.frame != null) return false;
        if (recommendations != null ? !recommendations.equals(that.recommendations) : that.recommendations != null)
            return false;
        if (srid != null ? !srid.equals(that.srid) : that.srid != null) return false;
        if (decimalPrecision != null ? !decimalPrecision.equals(that.decimalPrecision) : that.decimalPrecision != null) return false;
        if (buffer != null ? !buffer.equals(that.buffer) : that.buffer != null)
            return false;
        if (simplify != null ? !simplify.equals(that.simplify) : that.simplify != null)
            return false;
        if (intersectionMode != that.intersectionMode) return false;
        if (pathSerializer != that.pathSerializer) return false;
        if (polygonSerializerType != that.polygonSerializerType) return false;
        if (multiGraphAggregation != that.multiGraphAggregation) return false;
        if (multiGraphSerializer != that.multiGraphSerializer) return false;
        if (multiGraphIncludeEdges != null ? !multiGraphIncludeEdges.equals(that.multiGraphIncludeEdges) : that.multiGraphIncludeEdges != null) return false;
        if (maxEdgeWeight != null ? !maxEdgeWeight.equals(that.maxEdgeWeight) : that.maxEdgeWeight != null)
            return false;
        if (serviceUrl != null ? !serviceUrl.equals(that.serviceUrl) : that.serviceUrl != null) return false;
        if (fallbackServiceUrl != null ? !fallbackServiceUrl.equals(that.fallbackServiceUrl) : that.fallbackServiceUrl != null)
            return false;
        if (serviceKey != null ? !serviceKey.equals(that.serviceKey) : that.serviceKey != null) return false;
        if (edgeWeightType != that.edgeWeightType) return false;
        if (statisticIds != null ? !statisticIds.equals(that.statisticIds) : that.statisticIds != null) return false;
        if (statisticGroupId != null ? !statisticGroupId.equals(that.statisticGroupId) : that.statisticGroupId != null)
            return false;
        if (interServiceKey != null ? !interServiceKey.equals(that.interServiceKey) : that.interServiceKey != null) return false;
        return statisticServiceUrl != null ? statisticServiceUrl.equals(that.statisticServiceUrl) : that.statisticServiceUrl == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = sources != null ? sources.hashCode() : 0;
        result = 31 * result + (targets != null ? targets.hashCode() : 0);
        temp = Double.doubleToLongBits(bikeSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bikeUphill);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bikeDownhill);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(walkSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(walkUphill);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(walkDownhill);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (rushHour != null ? rushHour.hashCode() : 0);
        result = 31 * result + (travelTimes != null ? travelTimes.hashCode() : 0);
        result = 31 * result + (travelType != null ? travelType.hashCode() : 0);
        result = 31 * result + (elevationEnabled != null ? elevationEnabled.hashCode() : 0);
        result = 31 * result + (appendTravelTimes != null ? appendTravelTimes.hashCode() : 0);
        result = 31 * result + (pointReduction != null ? pointReduction.hashCode() : 0);
        result = 31 * result + (reverse != null ? reverse.hashCode() : 0);
        result = 31 * result + (minPolygonHoleSize != null ? minPolygonHoleSize.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (frame != null ? frame.hashCode() : 0);
        result = 31 * result + (recommendations != null ? recommendations.hashCode() : 0);
        result = 31 * result + (srid != null ? srid.hashCode() : 0);
        result = 31 * result + (decimalPrecision != null ? decimalPrecision.hashCode() : 0);
        result = 31 * result + (buffer != null ? buffer.hashCode() : 0);
        result = 31 * result + (simplify != null ? simplify.hashCode() : 0);
        result = 31 * result + (intersectionMode != null ? intersectionMode.hashCode() : 0);
        result = 31 * result + (pathSerializer != null ? pathSerializer.hashCode() : 0);
        result = 31 * result + (polygonSerializerType != null ? polygonSerializerType.hashCode() : 0);
        result = 31 * result + (multiGraphSerializer != null ? multiGraphSerializer.hashCode() : 0);
        result = 31 * result + (multiGraphAggregation != null ? multiGraphAggregation.hashCode() : 0);
        result = 31 * result + (multiGraphIncludeEdges != null ? multiGraphIncludeEdges.hashCode() : 0);
        result = 31 * result + (maxEdgeWeight != null ? maxEdgeWeight.hashCode() : 0);
        result = 31 * result + (serviceUrl != null ? serviceUrl.hashCode() : 0);
        result = 31 * result + (fallbackServiceUrl != null ? fallbackServiceUrl.hashCode() : 0);
        result = 31 * result + (serviceKey != null ? serviceKey.hashCode() : 0);
        result = 31 * result + (onlyPrintReachablePoints ? 1 : 0);
        result = 31 * result + (edgeWeightType != null ? edgeWeightType.hashCode() : 0);
        result = 31 * result + (statisticIds != null ? statisticIds.hashCode() : 0);
        result = 31 * result + (statisticGroupId != null ? statisticGroupId.hashCode() : 0);
        result = 31 * result + (statisticServiceUrl != null ? statisticServiceUrl.hashCode() : 0);
        result = 31 * result + (interServiceKey != null ? interServiceKey.hashCode() : 0);
        return result;
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
        builder.append("\n\tmultiGraphAggregation: ");
        builder.append(multiGraphAggregation);
        builder.append("\n\tmultiGraphSerializer: ");
        builder.append(multiGraphSerializer);
        builder.append("\n\tmultiGraphIncludeEdges: ");
        builder.append(multiGraphIncludeEdges);
        builder.append("\n\tmaxEdgeWeight: ");
        builder.append(maxEdgeWeight);
        builder.append("\n\tserviceUrl: ");
        builder.append(serviceUrl);
        builder.append("\n\tstatisticServiceUrl: ");
        builder.append(statisticServiceUrl);
        builder.append("\n\tserviceKey: ");
        builder.append(serviceKey);
        builder.append("\n\tonlyPrintReachablePoints: ");
        builder.append(onlyPrintReachablePoints);
        builder.append("\n\tedgeWeightType: ");
        builder.append(edgeWeightType);
        builder.append("\n\tstatisticIds: ");
        builder.append(statisticIds != null ? toString(statisticIds, maxLen) : null);
        builder.append("\n\tstatisticGroupId: ");
        builder.append(statisticGroupId);
        builder.append("\n}\n");
        builder.append("\n\tinterServiceKey: ");
        builder.append(interServiceKey);
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

    public void setStatisticIds(List<Short> statisticIds) {
        this.statisticIds = statisticIds ;
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

    public MultiGraphSerializerType getMultiGraphSerializer() {
        return multiGraphSerializer;
    }

    public void setMultiGraphSerializer(MultiGraphSerializerType multiGraphSerializer) {
        this.multiGraphSerializer = multiGraphSerializer;
    }

    public MultiGraphAggregationType getMultiGraphAggregation() {
        return multiGraphAggregation;
    }

    public void setMultiGraphAggregation(MultiGraphAggregationType multiGraphAggregation) {
        this.multiGraphAggregation = multiGraphAggregation;
    }

    public Boolean getMultiGraphIncludeEdges() {
        return multiGraphIncludeEdges;
    }

    public void setMultiGraphIncludeEdges(Boolean multiGraphIncludeEdges) {
        this.multiGraphIncludeEdges = multiGraphIncludeEdges;
    }

    public EdgeWeightType getEdgeWeightType() {
        return edgeWeightType;
    }

    public void setEdgeWeightType(final EdgeWeightType edgeWeightType) {
        this.edgeWeightType = edgeWeightType;
    }

    /**
     * 
     * @return Append travel times setting
     */
    public Boolean getAppendTravelTimes() {
        return this.appendTravelTimes;
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

    public void addAllSources(Map<String, Coordinate> inactiveSources) {
        this.sources.putAll(inactiveSources);
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


    public static void main(String[] args) throws JsonProcessingException, Route360ClientException {

        TravelOptions to = new TravelOptions();
        to.addSource(new DefaultSourceCoordinate("sourceid1", 52, 13, TravelType.WALK));
        to.addSource(new DefaultSourceCoordinate("sourceid2", 52, 13));
        to.addTarget(new DefaultTargetCoordinate("target1", 52, 13));
        to.addTarget(new DefaultTargetCoordinate("target2", 52, 13));
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(String.format("%s", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(to)));


        System.out.println(String.format("%s", RequestConfigurator.getConfig(to)));
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
}
