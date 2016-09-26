package net.motionintelligence.client.api.request;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONException;
import org.json.JSONObject;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.response.TimeResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;

public class TimeRequest {
	
	private Client client;
	private String method;
	private TravelOptions travelOptions;
	private String callback = "callback";
	
	/**
	 * 
	 * @param traveloptions
	 */
	public TimeRequest(TravelOptions travelOptions, String method){
		
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		
		this.travelOptions = travelOptions;
		this.method = method;
	}
	
	public TimeRequest(Client client, TravelOptions travelOptions, String method){
		
		this.client	= client;
		this.travelOptions = travelOptions;
		this.method = method;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCfg() throws Route360ClientException {
		
		// sources
		StringBuffer sourcesBuffer = new StringBuffer().append("[");
		StringBuffer config = new StringBuffer();
		
		try {
		
			for ( Coordinate src : this.travelOptions.getSources().values() ) {
				
				TravelType travelType = travelOptions.getTravelType();
				if (  src.getTravelType() != null && src.getTravelType() != travelType && src.getTravelType() != TravelType.UNSPECIFIED ) 
					travelType = src.getTravelType();
				
				JSONObject travelMode = new JSONObject();
				if ( TravelType.TRANSIT.equals(travelType) ) {
					travelMode.put("frame", new JSONObject()
						.put("time", travelOptions.getTime())
						.put("date", travelOptions.getDate())
						.put("duration", travelOptions.getFrame()));
				}
				
				if ( TravelType.WALK.equals(travelType) ) {
					travelMode.put("speed", travelOptions.getWalkSpeed());
					travelMode.put("uphill", travelOptions.getWalkDownhill());
					travelMode.put("downhill", travelOptions.getWalkUphill());
				}
				
				if ( TravelType.BIKE.equals(travelType) ) {
					travelMode.put("speed", travelOptions.getBikeSpeed());
					travelMode.put("uphill", travelOptions.getBikeDownhill());
					travelMode.put("downhill", travelOptions.getBikeUphill());
				}
				
				sourcesBuffer.append("{\"")
					.append(Constants.ID).append("\":\"").append(src.getId()).append("\",\"")
					.append(Constants.TRANSPORT_MODE).append("\":").append("{\"").append(travelType.toString()).append("\":"+travelMode.toString()+"},\"")
					.append(Constants.LATITUDE).append("\":\"").append(src.getY()).append("\",\"")
					.append(Constants.LONGITUDE).append("\":\"").append(src.getX()).append("\"},");
			}
			sourcesBuffer.deleteCharAt(sourcesBuffer.length() - 1); 
			sourcesBuffer.append("]");
			
			// targets
			StringBuffer targetsBuffer = new StringBuffer().append("[");
			for ( Coordinate trg : this.travelOptions.getTargets().values() ) {
				
				targetsBuffer.append("{ \"")
					.append(Constants.ID).append("\":\"").append(trg.getId()).append("\",\"")
					.append(Constants.LATITUDE).append("\":\"").append(trg.getY()).append("\",\"")
					.append(Constants.LONGITUDE).append("\":\"").append(trg.getX()).append("\"},");
			}
			targetsBuffer.deleteCharAt(targetsBuffer.length() - 1);
			targetsBuffer.append("]");
			
			config
				.append("{\"" + Constants.MAX_ROUTING_TIME + "\":" + this.travelOptions.getMaxRoutingTime() + ",");
			
			if ( this.travelOptions.isElevationEnabled() != null ) 
				config.append("\"" + Constants.ENABLE_ELEVATION + "\":" + this.travelOptions.isElevationEnabled() + ",");
			
			config.append("\"" + Constants.SOURCES + "\": " + sourcesBuffer.toString() + ",");
			config.append("\"" + Constants.TARGETS + "\": " + targetsBuffer.toString() + "}");
		}
		catch ( Exception e) {
			throw new Route360ClientException("Could not generate r360 config object", e);
		}
		
		
		
		return config.toString();
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public TimeResponse get() throws Route360ClientException {
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/time")
				.queryParam("cb", callback)
				.queryParam("key", travelOptions.getServiceKey());
		
//		try {
//			System.out.println(new JSONObject(getCfg()).toString(4));
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		Response response = null; 
		
		if ( HttpMethod.GET.equals(this.method) ) {
			
			target 	 = target.queryParam("cfg", IOUtil.encode(getCfg()));
			response = target.request().get();
		}
		else if ( HttpMethod.POST.equals(this.method) ) {
			
			try {
				
				response = target.request().post(Entity.entity(this.getCfg(), MediaType.APPLICATION_JSON_TYPE));
			}
			catch ( Exception e) {
				
				e.printStackTrace();
			}
		}
		else 
			throw new Route360ClientException("HTTP Method not supported: " + this.method, null);
		
		// compare the HTTP status codes, NOT the route 360 code
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			String res = response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", "");
			
			// consume the results
			return new TimeResponse(this.travelOptions, JsonUtil.parseString(res), requestStart);
		}
		else if ( response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode() ) 
			return new TimeResponse(this.travelOptions, "gateway-time-out", System.currentTimeMillis() - requestStart, requestStart);
		else 
			throw new Route360ClientException(response.readEntity(String.class), null);
	}
}
