package net.motionintelligence.client.api.request;

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Source;
import net.motionintelligence.client.api.response.PolygonResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;

public class PolygonRequest {
	
	private Client client;
	private TravelOptions travelOptions;
	private String callback = "callback";
	
	public PolygonRequest(Client client){
		
		this.client	= client;
		this.client.register(GZipEncoder.class);
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
	} 
	
	/**
	 */
	public PolygonRequest(){
		this(ClientBuilder.newClient());
	}
	
	/**
	 * 
	 * @param traveloptions
	 */
	public PolygonRequest(TravelOptions travelOptions){
		this();
		this.travelOptions = travelOptions;
	}
	
	public PolygonRequest(Client client, TravelOptions travelOptions){

		this.client	= client;
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
		this.travelOptions = travelOptions;
	}
	
	/**
	 * 
	 * @param options
	 */
	public void setTravelOptions(TravelOptions options) {
		this.travelOptions = options;
	}
	
	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public String getCfg() throws Route360ClientException {

		String cfg = "";
		
		try {
			
			JSONObject config = new JSONObject();
			
			JSONObject polygon = new JSONObject();
			polygon.put(Constants.BUFFER_IN_METER, this.travelOptions.getBufferInMeter());
			polygon.put(Constants.POLYGON_VALUES, new JSONArray(this.travelOptions.getTravelTimes()));
			polygon.put(Constants.POLYGON_INTERSECTION_MODE, this.travelOptions.getIntersectionMode());
			polygon.put(Constants.SERIALIZER, this.travelOptions.getPolygonSerializerType().getPolygonSerializerName());
			polygon.put(Constants.POINT_REDUCTION, this.travelOptions.isPointReduction());
			polygon.put(Constants.MIN_POLYGON_HOLE_SIZE, this.travelOptions.getMinPolygonHoleSize());
			
			config.put(Constants.POLYGON, polygon);
			
			JSONArray sources = new JSONArray();
			for ( Source src : this.travelOptions.getSources().values() ) {
				
				TravelType travelType = travelOptions.getTravelType();
				if ( src.getTravelType() != travelType && src.getTravelType() != TravelType.UNSPECIFIED ) 
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
				
				JSONObject source = new JSONObject()
					.put(Constants.ID, src.getId())
					.put(Constants.LATITUDE, src.getLatitude())
					.put(Constants.LONGITUDE, src.getLongitude())
					.put(Constants.TRANSPORT_MODE, new JSONObject().put(travelType.toString(), travelMode));
				
				if ( this.travelOptions.getReverse() )
					source.put(Constants.REVERSE, true);
				
				sources.put(source);
			}
			
			config.put(Constants.SOURCES, sources);
			config.put(Constants.ENABLE_ELEVATION, this.travelOptions.isElevationEnabled());
			
			cfg = IOUtil.encode(config.toString());
		}
		catch ( Exception e) {
			throw new Route360ClientException("Could not generate r360 config object", e);
		}
		
		return cfg;
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public PolygonResponse get() throws Route360ClientException {
		
		long roundTripTimeMillis = System.currentTimeMillis();
		
		WebTarget request = client.target(travelOptions.getServiceUrl()).path("v1/polygon")
			.queryParam("cb", callback)
			.queryParam("key", travelOptions.getServiceKey())
			.queryParam("cfg", this.getCfg());
		
		Response response = request.request().get();
		roundTripTimeMillis = ( System.currentTimeMillis() - roundTripTimeMillis);
		
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			String resultString = response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", "");
			
			JSONObject result = JsonUtil.parseString(resultString);
			
			if ( Constants.EXCEPTION_ERROR_CODE_NO_ROUTE_FOUND.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			if ( Constants.EXCEPTION_ERROR_CODE_COULD_NOT_CONNECT_POINT_TO_NETWORK.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			if ( Constants.EXCEPTION_ERROR_CODE_TRAVEL_TIME_EXCEEDED.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			if ( Constants.EXCEPTION_ERROR_CODE_UNKNOWN_EXCEPTION.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			return new PolygonResponse(this.travelOptions, resultString, JsonUtil.getString(result, "code"), JsonUtil.getLong(result, "requestTime"), roundTripTimeMillis);
		}
		else {
			
			throw new Route360ClientException(response.readEntity(String.class), null);
		}
	}

	public static void main(String[] args) throws Route360ClientException {
		
		TravelOptions options = new TravelOptions();
		options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
		options.setTravelType(TravelType.TRANSIT);
		options.addSource(new Source("id1", 40.608155, -73.976636));
		options.setServiceKey("INSERT_YOUR_KEY_HERE");
		options.setServiceUrl("https://service.route360.net/na_northeast/");
		
		PolygonRequest req = new PolygonRequest(options);
		PolygonResponse polygonResponse = req.get();
		System.out.println(polygonResponse.getRequestTimeMillis() + " " + polygonResponse.getCode());
	}
}
