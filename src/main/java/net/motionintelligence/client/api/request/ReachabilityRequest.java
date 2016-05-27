package net.motionintelligence.client.api.request;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.message.GZipEncoder;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Source;
import net.motionintelligence.client.api.geo.Target;
import net.motionintelligence.client.api.response.ReachabilityResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;

public class ReachabilityRequest {
	
	private Client client;
	private String method;
	private TravelOptions travelOptions;
	private String callback = "callback";
	
	/**
	 * 
	 * @param traveloptions
	 */
	public ReachabilityRequest(TravelOptions travelOptions, String method){
		
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		
		this.travelOptions = travelOptions;
		this.method = method;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCfg() {
		
		StringBuffer polygonBuffer = new StringBuffer();
		
		// polygon
		polygonBuffer
			.append("{\""+ Constants.POLYGON_VALUES + "\":[" + StringUtils.join(this.travelOptions.getTravelTimes(), ",") + "]," )
			.append("\""+ Constants.POLYGON_INTERSECTION_MODE +"\":\"" + this.travelOptions.getIntersectionMode()+"\",")
			.append("\""+ Constants.POLYGON_SERIALIZER+"\":\"" + this.travelOptions.getPolygonSerializerType().getPolygonSerializerName()+"\", ")
			.append("\""+ Constants.POINT_REDUCTION+"\":" + this.travelOptions.isPointReduction()+",")
			.append("\""+ Constants.MIN_POLYGON_HOLE_SIZE+"\":" + this.travelOptions.getMinPolygonHoleSize()+"}");
		
		// sources
		StringBuffer sourcesBuffer = new StringBuffer().append("[");
		for ( Source src : this.travelOptions.getSources().values() ) {
			
			TravelType travelType = travelOptions.getTravelType();
			if ( src.getTravelType() != travelType && src.getTravelType() != TravelType.UNSPECIFIED ) 
				travelType = src.getTravelType();
			
			sourcesBuffer.append("{\"")
				.append(Constants.ID).append("\":\"").append(src.getId()).append("\",\"")
				.append(Constants.TRANSPORT_MODE).append("\":").append("{\"").append(travelType.toString()).append("\":{}},\"")
				.append(Constants.LATITUDE).append("\":\"").append(src.getLatitude()).append("\",\"")
				.append(Constants.LONGITUDE).append("\":\"").append(src.getLongitude()).append("\"},");
		}
		sourcesBuffer.deleteCharAt(sourcesBuffer.length() - 1); 
		sourcesBuffer.append("]");
		
		// targets
		StringBuffer targetsBuffer = new StringBuffer().append("[");
		for ( Target trg : this.travelOptions.getTargets().values() ) {
			
			targetsBuffer.append("{\"")
				.append(Constants.ID).append("\":\"").append(trg.getId()).append("\",\"")
				.append(Constants.LATITUDE).append("\":\"").append(trg.getLatitude()).append("\",\"")
				.append(Constants.LONGITUDE).append("\":\"").append(trg.getLongitude()).append("\"},");
		}
		if ( !this.travelOptions.getTargets().isEmpty() ) targetsBuffer.deleteCharAt(targetsBuffer.length() - 1);
		targetsBuffer.append("]");
		
		StringBuffer config = new StringBuffer()
			.append("{\"" + Constants.MAX_ROUTING_TIME + "\":" + this.travelOptions.getMaxRoutingTime() + ",")
			.append("\"" + Constants.ENABLE_ELEVATION + "\":" + this.travelOptions.isElevationEnabled() + ",")
			.append("\"" + Constants.POLYGON + "\": " + polygonBuffer.toString() + ",")
			.append("\"" + Constants.SOURCES + "\": " + sourcesBuffer.toString() + ",")
			.append("\"" + Constants.TARGETS + "\": " + targetsBuffer.toString() + "}");
		
		return config.toString();
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public ReachabilityResponse get() throws Route360ClientException {
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/reachability")
				.queryParam("cb", callback)
				.queryParam("key", travelOptions.getServiceKey());
		
		Response response = null; 
		
		if ( HttpMethod.GET.equals(this.method) ) {
			
			target 	 = target.queryParam("cfg", IOUtil.encode(getCfg()));
			response = target.request().get();
		}
		else if ( HttpMethod.POST.equals(this.method) ) {
			
			response = target.request().post(Entity.entity(getCfg(), MediaType.APPLICATION_JSON_TYPE));
		}
		else 
			throw new Route360ClientException("HTTP Method not supported: " + this.method, null);
		
		// compare the HTTP status codes, NOT the route 360 code
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			// consume the results
			String res = response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", "");
			
			return new ReachabilityResponse(this.travelOptions, JsonUtil.parseString(res), requestStart);
		}
		else if ( response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode() ) 
			return new ReachabilityResponse(this.travelOptions, "gateway-time-out", System.currentTimeMillis() - requestStart, requestStart);
		else 
			throw new Route360ClientException(response.readEntity(String.class), null);
	}
}
