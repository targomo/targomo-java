package net.motionintelligence.client.api.request;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.message.GZipEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.request.ssl.JerseySslClientGenerator;
import net.motionintelligence.client.api.response.ReachabilityResponse;
import net.motionintelligence.client.api.response.StatisticsResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;

public class StatisticsRequest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsRequest.class);
	private Client client;
	private TravelOptions travelOptions;
	
	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder} with a {@link GZipEncoder} attached.
	 * @param travelOptions Options to be used
	 */
	public StatisticsRequest(TravelOptions travelOptions) {
		
		this.client	= ClientBuilder.newClient();
		this.client.register(GZipEncoder.class);
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public StatisticsRequest(Client client, TravelOptions travelOptions){
		
		this.client	= client;
		this.travelOptions = travelOptions;
	}
	
	/**
	 * 
	 * @param sources
	 * @param serviceUrl
	 * @param travelType
	 * @param travelTime
	 * @param travelDate
	 * @param travelTimes
	 * @param statistics
	 * @return
	 * @throws JSONException
	 * @throws Route360ClientException 
	 */
	public StatisticsResponse get() throws Route360ClientException, JSONException {
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget target = JerseySslClientGenerator.initClient()
				.target(this.travelOptions.getStatisticServiceUrl())
				.path("/calculate")
				.queryParam("travelType", this.travelOptions.getTravelType())
				.queryParam("travelTime", this.travelOptions.getTime())
				.queryParam("travelDate", this.travelOptions.getDate())
				.queryParam("reverse", this.travelOptions.getReverse())
				.queryParam("frame", this.travelOptions.getFrame())
				.queryParam("walkSpeed", this.travelOptions.getWalkSpeed())
				.queryParam("bikeSpeed", this.travelOptions.getBikeSpeed())
				.queryParam("serviceUrl", this.travelOptions.getServiceUrl())
				.queryParam("serviceKey", this.travelOptions.getServiceKey())
				.queryParam("intersectionMode", this.travelOptions.getIntersectionMode())
				.queryParam("appendTravelTimes", this.travelOptions.getAppendTravelTimes())
				.queryParam("maxRoutingTime", this.travelOptions.getMaxRoutingTime())
				.queryParam("statisticGroupId", this.travelOptions.getStatisticGroupId());
		
		for ( Integer stat : this.travelOptions.getStatisticIds()) 
			target = target.queryParam("statistics", stat);
		
		System.out.println(target.getUri().toString());
		
		// Execute POST request
		Response response = target.request().post(Entity.entity(parseSources(this.travelOptions.getSources()), MediaType.APPLICATION_JSON_TYPE));

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);
	}
	
	public static void main(String[] args) throws Route360ClientException, JSONException {
		
		TravelOptions options = new TravelOptions();
		options.setMaxRoutingTime(1800);
		options.setTravelType(TravelType.WALK);
		options.addSource(new DefaultSourceCoordinate("ashjdkahw", 7.4474, 46.9480));
		options.setServiceUrl("https://service.route360.net/switzerland/");
		options.setStatisticServiceUrl("http://localhost:8081/");
		options.setServiceKey("uhWrWpUhyZQy8rPfiC7X");
		options.setDate(20150812);
		options.setTime(43200);
		options.setStatisticIds(new HashSet<>(Arrays.asList(0,1)));
		options.setStatisticGroupId("switzerland_statistics");
		
		StatisticsResponse response   = new StatisticsRequest(options).get();
		System.out.println(response.getStatisticResult().getResult());
	}
	
	/**
	 * Validate HTTP response and return a ReachabilityResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws Route360ClientException In case of errors other than GatewayTimeout
	 */
	private StatisticsResponse validateResponse(final Response response, final long requestStart, final long roundTripTime)
			throws Route360ClientException {
		
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			
			// consume the results
			return new StatisticsResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart);
		} 
		else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new StatisticsResponse(travelOptions, "gateway-time-out", roundTripTime, requestStart);
		} 
		else {
			throw new Route360ClientException(response.readEntity(String.class), null);
		}
	}

	/**
	 * @param sources
	 * @param travelType
	 * @return
	 * @throws JSONException
	 */
	private static String parseSources(Map<String,Coordinate> sources) throws JSONException {
		
		JSONArray sourcesJson = new JSONArray();
		for ( Coordinate src : sources.values() ) {
			sourcesJson.put(new JSONObject()
				.put(Constants.ID, src.getId())
				.put(Constants.Y, src.getY())
				.put(Constants.X, src.getX())
			);
		}
		
		return sourcesJson.toString();
	}
}
