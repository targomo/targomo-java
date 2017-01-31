package net.motionintelligence.client.api;

import net.motionintelligence.client.api.enums.EdgeWeightType;
import net.motionintelligence.client.api.enums.PathSerializerType;
import net.motionintelligence.client.api.enums.PolygonIntersectionMode;
import net.motionintelligence.client.api.enums.PolygonSerializerType;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.util.TimeUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Common configuration class for executing all requests.
 * Define source points, target points and other configuration values such as travel times, elevation etc. here.
 * See: {@link net.motionintelligence.client.api.request.PolygonRequest},
 * {@link net.motionintelligence.client.api.request.RouteRequest},
 * {@link net.motionintelligence.client.api.request.TimeRequest},
 * {@link net.motionintelligence.client.api.request.ReachabilityRequest}.
 */
public class TravelOptions {

	private Map<String,Coordinate> sources		= new HashMap<>();
	private Map<String,Coordinate> targets 	    = new HashMap<>();
	
    private double bikeSpeed                         	= 15.0;
    private double bikeUphill                        	= 20.0;
    private double bikeDownhill                      	= -10.0;
    private double walkSpeed                         	= 5.0;
    private double walkUphill                        	= 10.0;
    private double walkDownhill                      	= 0.0;
                 
    private List<Integer> travelTimes                	= Arrays.asList(600, 1200, 1800);
    private TravelType travelType                    	= TravelType.UNSPECIFIED;
    private Boolean elevationEnabled                 	= false;
    private Boolean appendTravelTimes					= false;
    private Boolean pointReduction                   	= true;
    private Boolean reverse                   			= false;
    private Long minPolygonHoleSize                  	= 100000000L;
                 
    private Integer time                                 = TimeUtil.getCurrentTime();
    private Integer date                                 = TimeUtil.getCurrentDate();
    private Integer frame                                = 18000;
    private Integer recommendations                      = 0;
    private Integer srid                      			 = null;

    private Integer bufferInMeter						= null;
    private PolygonIntersectionMode intersectionMode 	= PolygonIntersectionMode.UNION;
    private PathSerializerType pathSerializer        	= PathSerializerType.COMPACT_PATH_SERIALIZER;
    private PolygonSerializerType polygonSerializerType = PolygonSerializerType.JSON_POLYGON_SERIALIZER;
    private Integer maxRoutingTime                      = 3600;
    private Integer maxRoutingLength                    = 100000;
    private String serviceUrl                        	= "";
    private String statisticServiceUrl                 	= "https://service.route360.net/statistics/";
    private String serviceKey                        	= "";
	private boolean onlyPrintReachablePoints			= true;
	private EdgeWeightType edgeWeightType               = EdgeWeightType.TIME;

	private List<Short> statisticIds;
	private String statisticGroupId;
	
	/**
	 * 
	 * @return source coordinates array
	 */
	public double[][] getSourceCoordinates() {
		return getCoordinates(this.sources);
	}
	
	/**
	 * 
	 * @return target coordinates array
	 */
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
	 * @return the maxRoutingTime
	 */
	public int getMaxRoutingTime() {
		return maxRoutingTime;
	}

	/**
	 * @param maxRoutingTime the maxRoutingTime to set
	 */
	public void setMaxRoutingTime(int maxRoutingTime) {
		this.maxRoutingTime = maxRoutingTime;
	}

	/**
	 * @return the maxRoutingLength
	 */
	public Integer getMaxRoutingLength() {
		return maxRoutingLength;
	}

	/**
	 * @param maxRoutingLength the maxRoutingLength to set
	 */
	public void setMaxRoutingLength(Integer maxRoutingLength) {
		this.maxRoutingLength = maxRoutingLength;
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
	 * @param source
	 */
	public void addSource(Coordinate source) {
		this.sources.put(source.getId(), source);
	}
	
	/**
	 * @param target
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
		builder.append("\n\tbufferInMeter: ");
		builder.append(bufferInMeter);
		builder.append("\n\tintersectionMode: ");
		builder.append(intersectionMode);
		builder.append("\n\tpathSerializer: ");
		builder.append(pathSerializer);
		builder.append("\n\tpolygonSerializerType: ");
		builder.append(polygonSerializerType);
		builder.append("\n\tmaxRoutingTime: ");
		builder.append(maxRoutingTime);
		builder.append("\n\tmaxRoutingLength: ");
		builder.append(maxRoutingLength);
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
		return builder.toString();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Coordinate getSource(String id) {
		return this.sources.get(id);
	}
	
	/**
	 * 
	 * @param id
	 * @return
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

	public Integer getBufferInMeter() {
		return bufferInMeter;
	}

	public void setBufferInMeter(Integer bufferInMeter) {
		this.bufferInMeter = bufferInMeter;
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

	public EdgeWeightType getEdgeWeightType() {
		return edgeWeightType;
	}

	public void setEdgeWeightType(final EdgeWeightType edgeWeightType) {
		this.edgeWeightType = edgeWeightType;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getAppendTravelTimes() {
		return this.appendTravelTimes;
	}
	
	/**
	 * 
	 */
	public void setAppendTravelTimes(Boolean appendTravelTimes) {
		this.appendTravelTimes = appendTravelTimes;
	}

	public String getStatisticServiceUrl() {
		return statisticServiceUrl;
	}

	public void setStatisticServiceUrl(String statisticServiceUrl) {
		this.statisticServiceUrl = statisticServiceUrl;
	}

	public String getStatisticGroupId() {
		return statisticGroupId;
	}

	public void setStatisticGroupId(String statisticGroupId) {
		this.statisticGroupId = statisticGroupId;
	}
}
