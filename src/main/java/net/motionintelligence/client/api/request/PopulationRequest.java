package net.motionintelligence.client.api.request;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Source;
import net.motionintelligence.client.api.request.ssl.JerseySslClientGenerator;
import net.motionintelligence.client.api.response.PopulationResponse;
import net.motionintelligence.client.api.util.JsonUtil;

public class PopulationRequest {
	
	private Client client = JerseySslClientGenerator.initClient();
	private TravelOptions travelOptions;
	
	public PopulationRequest(Client client){
		
		this.client	= client;
		this.client.register(GZipEncoder.class);
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
	} 
	
	/**
	 */
	public PopulationRequest(){
		this(ClientBuilder.newClient());
	}
	
	/**
	 * 
	 * @param traveloptions
	 */
	public PopulationRequest(TravelOptions travelOptions){
		this();
		this.travelOptions = travelOptions;
	}
	
	public PopulationRequest(Client client, TravelOptions travelOptions){

		this.client	= client;
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
		this.travelOptions = travelOptions;
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 * @throws JSONException 
	 */
	public String getSources() throws Route360ClientException {
		
		try {
			
			JSONArray sources = new JSONArray();
			for ( Map.Entry<String,Source> source : this.travelOptions.getSources().entrySet() ) {
				sources.put(new JSONObject()
						.put("lat", source.getValue().getLatitude())
						.put("lng", source.getValue().getLongitude())
						.put("id", source.getValue().getId()));
			}
			
			return sources.toString();
		}
		catch ( JSONException e ) {
			throw new Route360ClientException("Could not build json sources", e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public PopulationResponse get() throws Route360ClientException {
		
		long requestStart = System.currentTimeMillis();

		WebTarget target = client.target(travelOptions.getServiceUrl()).path("population")
				.queryParam("maxRoutingTime", travelOptions.getMaxRoutingTime())
				.queryParam("travelDate", travelOptions.getDate())
				.queryParam("travelTime", travelOptions.getTime())
				.queryParam("travelType", travelOptions.getTravelType().toString())
				.queryParam("key", travelOptions.getServiceKey())
				.queryParam("useBoundingBox", false)
				.queryParam("statistics", travelOptions.getStatistics().toArray(new String[travelOptions.getStatistics().size()]));
		
//		System.out.println(target.getUri());
		
		Response response = target.request().post(Entity.entity(this.getSources(), MediaType.APPLICATION_JSON_TYPE));
		
		try {
			
			if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
				
				// consume the results
				JSONArray result = JsonUtil.parseArray(response.readEntity(String.class));
				
				return new PopulationResponse(this.travelOptions, result, requestStart);
			}
			else 
				throw new Route360ClientException("Reponse not working: " + response.readEntity(String.class), null);
		}
		catch ( JSONException e) {
			
			throw new Route360ClientException("Could not parse JSON response", e);
		}
	}
}
