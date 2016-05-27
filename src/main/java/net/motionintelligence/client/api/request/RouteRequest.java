package net.motionintelligence.client.api.request;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Source;
import net.motionintelligence.client.api.geo.Target;
import net.motionintelligence.client.api.response.RouteResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;

public class RouteRequest {
	
	private Client client;
	private TravelOptions travelOptions;
	private String callback = "callback";
	
	/**
	 * 
	 * @param traveloptions
	 */
	public RouteRequest(TravelOptions travelOptions){
		
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		
		this.travelOptions = travelOptions;
	}
	
	public RouteRequest(Client client, TravelOptions travelOptions) {
		
		this.client	= client;
		this.travelOptions = travelOptions;
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
			config.put(Constants.PATH_SERIALIZER, this.travelOptions.getPathSerializer().getPathSerializerName());
			
			JSONObject polygon = new JSONObject();
			polygon.put(Constants.POLYGON_VALUES, new JSONArray(this.travelOptions.getTravelTimes()));
			polygon.put(Constants.POLYGON_INTERSECTION_MODE, this.travelOptions.getIntersectionMode());
			polygon.put(Constants.POINT_REDUCTION, this.travelOptions.isPointReduction());
			polygon.put(Constants.MIN_POLYGON_HOLE_SIZE, this.travelOptions.getMinPolygonHoleSize());
			
			config.put(Constants.POLYGON, polygon);
			
			JSONArray sources = new JSONArray();
			for ( Source src : this.travelOptions.getSources().values() ) {
				
				TravelType travelType = travelOptions.getTravelType();
				if ( src.getTravelType() != travelType && src.getTravelType() != TravelType.UNSPECIFIED ) 
					travelType = src.getTravelType();
				
				// "tm":{"transit":{"frame":{"time":55440,"date":"20151208"},"recommendations":5}}}
				
				JSONObject jsonObject = new JSONObject();

				if ( this.travelOptions.getTravelType().isTransit() ) {
					jsonObject.put(Constants.TRANSPORT_MODE_TRANSIT_RECOMMENDATIONS, this.travelOptions.getRecommendations());
					
					JSONObject frame = new JSONObject();
					frame.put(Constants.TRANSPORT_MODE_TRANSIT_FRAME_DATE, this.travelOptions.getDate());
					frame.put(Constants.TRANSPORT_MODE_TRANSIT_FRAME_TIME, this.travelOptions.getTime());
					jsonObject.put(Constants.TRANSPORT_MODE_TRANSIT_FRAME, frame);
				}
				
				sources.put(new JSONObject()
					.put(Constants.ID, src.getId())
					.put(Constants.LATITUDE, src.getLatitude())
					.put(Constants.LONGITUDE, src.getLongitude())
					.put(Constants.TRANSPORT_MODE, new JSONObject().put(travelType.toString(), jsonObject))
				);
			}
			
			config.put(Constants.SOURCES, sources);
			
			JSONArray targets = new JSONArray();
			for ( Target trg : this.travelOptions.getTargets().values() ) {
				
				targets.put(new JSONObject()
					.put(Constants.ID, trg.getId())
					.put(Constants.LATITUDE, trg.getLatitude())
					.put(Constants.LONGITUDE, trg.getLongitude())
				);
			}
			
			config.put(Constants.SOURCES, sources);
			config.put(Constants.TARGETS, targets);
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
	public RouteResponse get() throws Route360ClientException{
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget request = client.target(travelOptions.getServiceUrl()).path("v1/route")
			.queryParam("cb", callback)
			.queryParam("key", travelOptions.getServiceKey())
			.queryParam("cfg", this.getCfg());
		
		// make the request
		Response response = request.request().get();
		
		// compare the HTTP status codes, NOT the route 360 code
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			// consume the results
			JSONObject result = JsonUtil.parseString(response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", ""));
			
			return new RouteResponse(this.travelOptions, JsonUtil.getJsonArray(JsonUtil.getJSONObject(result, "data"), "routes"), JsonUtil.getString(result, "code"), 
					result.has("requestTime") ? JsonUtil.getInt(result, "requestTime") : -1);
		}
		else if ( response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode() ) 
			return new RouteResponse(this.travelOptions, new JSONArray(), "gateway-time-out", System.currentTimeMillis() - requestStart);
		else 
			throw new Route360ClientException(response.readEntity(String.class), null);
	}
}
