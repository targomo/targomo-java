package net.motionintelligence.client.api.request;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.config.RequestConfigurator;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.request.ssl.JerseySslClientGenerator;
import net.motionintelligence.client.api.response.PolygonResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

public class PolygonRequest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PolygonRequest.class);
	
	private Client client;
	private TravelOptions travelOptions;
	private String callback = "callback";
	private String method;
	
	public PolygonRequest(Client client){
		
		this.client	= client;
		this.client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        this.client.property(ClientProperties.READ_TIMEOUT, 100000);
	} 
	
	/**
	 */
	public PolygonRequest(){
		this(JerseySslClientGenerator.initClient());
	}
	
	/**
	 * 
	 * @param travelOptions
	 */
	public PolygonRequest(TravelOptions travelOptions){
		this();
		this.travelOptions = travelOptions;
	}
	
	public PolygonRequest(TravelOptions travelOptions, String method){
		this();
		this.travelOptions = travelOptions;
		this.method 	   = method;
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
	public PolygonResponse get() throws Route360ClientException {
		
		long roundTripTimeMillis = System.currentTimeMillis();
		
		WebTarget request = client.target(travelOptions.getServiceUrl()).path("v1/polygon" + (HttpMethod.POST.equals(this.method) ? "_post" : ""))
			.queryParam("cb", callback)
			.queryParam("key", travelOptions.getServiceKey());
		
		Response response = null;
		String config = RequestConfigurator.getPolygonConfig(travelOptions);
		if ( HttpMethod.GET.equals(this.method) ) {
			
			request  = request.queryParam("cfg", IOUtil.encode(config));
			response = request.request().get();
		}
		else if ( HttpMethod.POST.equals(this.method) ) {
			
			response = request.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
		}
		else 
			throw new Route360ClientException("HTTP Method not supported: " + this.method, null);
		
		roundTripTimeMillis = ( System.currentTimeMillis() - roundTripTimeMillis);
		
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			String resultString = response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", "");
			
			long startParsing = System.currentTimeMillis();
			JSONObject result = JsonUtil.parseString(resultString);
			long parseTime = System.currentTimeMillis() - startParsing;
			
			if ( Constants.EXCEPTION_ERROR_CODE_NO_ROUTE_FOUND.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			if ( Constants.EXCEPTION_ERROR_CODE_COULD_NOT_CONNECT_POINT_TO_NETWORK.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			if ( Constants.EXCEPTION_ERROR_CODE_TRAVEL_TIME_EXCEEDED.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			if ( Constants.EXCEPTION_ERROR_CODE_UNKNOWN_EXCEPTION.equals( JsonUtil.getString(result, "code")) )
				throw new Route360ClientException(result.toString(), null);
			
			return new PolygonResponse(this.travelOptions, result, 
					JsonUtil.getString(result, "code"), 
					JsonUtil.getLong(result, "requestTime"), roundTripTimeMillis, parseTime);
		}
		else {
			
			
			throw new Route360ClientException("Status: " + response.getStatus() + ": " + response.readEntity(String.class), null);
		}
	}

	public static void main(String[] args) throws Route360ClientException {
		
		TravelOptions options = new TravelOptions();
		options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
		options.setTravelType(TravelType.TRANSIT);
		options.addSource(new DefaultSourceCoordinate("id1", -73.976636, 40.608155));
		options.setServiceKey("INSERT_YOUR_KEY_HERE");
		options.setServiceUrl("https://service.route360.net/na_northeast/");
		
		PolygonRequest req = new PolygonRequest(options);
		PolygonResponse polygonResponse = req.get();
		System.out.println(polygonResponse.getRequestTimeMillis() + " " + polygonResponse.getCode());
	}
}
