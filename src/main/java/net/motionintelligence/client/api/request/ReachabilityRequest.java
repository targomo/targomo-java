package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.response.ReachabilityResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;
import org.glassfish.jersey.message.GZipEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ReachabilityRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReachabilityRequest.class);
	
	private Client client;
	private String method;
	private TravelOptions travelOptions;
	private static final String CALLBACK = "callback";

	/**
	 * Use default client implementation with specified options & method
	 * @param travelOptions Options to be used
	 * @param method HTTP Method (GET or POST)
	 */
	public ReachabilityRequest(TravelOptions travelOptions, String method){
		
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		this.travelOptions = travelOptions;
		this.method = method;
	}

	/**
	 * Use a custom client implementation with specified options & method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 * @param method HTTP Method (GET or POST)
	 */
	public ReachabilityRequest(Client client, TravelOptions travelOptions, String method){
		
		this.client	= client;
		this.travelOptions = travelOptions;
		this.method = method;
	}

	/**
	 * Execute request
	 * @return Reachability response
	 * @throws Route360ClientException In case of error other than Gateway Timeout
	 */
	public ReachabilityResponse get() throws Route360ClientException {
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/reachability")
				.queryParam("cb", CALLBACK)
				.queryParam("key", travelOptions.getServiceKey());
		
		Response response;
		String config = RequestConfigurator.getConfig(travelOptions);
		if ( HttpMethod.GET.equals(this.method) ) {
			
			target 	 = target.queryParam("cfg", IOUtil.encode(config));
			response = target.request().get();
		}
		else if ( HttpMethod.POST.equals(this.method) ) {
			
			response = target.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
		}
		else 
			throw new Route360ClientException("HTTP Method not supported: " + this.method, null);

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);


	}

	/**
	 * Validate HTTP response & return a ReachabilityResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws Route360ClientException In case of errors other than GatewayTimeout
	 */
	private ReachabilityResponse validateResponse(final Response response,
	                                              final long requestStart, final long roundTripTime)
			throws Route360ClientException {
		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			String res = IOUtil.getResultString(response);
			return new ReachabilityResponse(travelOptions, JsonUtil.parseString(res), requestStart);
		} else if (response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode()) {
			return new ReachabilityResponse(travelOptions, "gateway-time-out", roundTripTime, requestStart);
		} else {
			throw new Route360ClientException(response.readEntity(String.class), null);
		}
	}
}
