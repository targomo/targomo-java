package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.request.config.RequestConfigurator;
import net.motionintelligence.client.api.response.PointOfInterestResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Find reachable openstreetmap pois with this class.
 * Only accepts {@link HttpMethod} POST.
 */
public class PointOfInterestRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointOfInterestRequest.class);

	private Client client;
	private TravelOptions travelOptions;
	private static final String CALLBACK = "callback";

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public PointOfInterestRequest(TravelOptions travelOptions) {

		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public PointOfInterestRequest(Client client, TravelOptions travelOptions){
		
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Execute request
	 * @return point of interest response
	 * @throws Route360ClientException In case of error other than Gateway Timeout
	 */
	public PointOfInterestResponse get() throws Route360ClientException {
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget target = client.target(travelOptions.getPointOfInterestServiceUrl()).path("/reachability")
				.queryParam("cb", CALLBACK)
				.queryParam("key", travelOptions.getServiceKey());

		final Entity<String> entity = Entity.entity(RequestConfigurator.getConfig(travelOptions), MediaType.APPLICATION_JSON_TYPE);

		LOGGER.debug(String.format("Executing reachability request to URI: '%s'", target.getUri()));

		Response response = target.request().post(entity);

		long roundTripTime = System.currentTimeMillis() - requestStart;

		return validateResponse(response, requestStart, roundTripTime);
	}

	/**
	 * Validate HTTP response and return a PointOfInterestResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @param roundTripTime Execution time in milliseconds
	 * @return ReachabilityResponse
	 * @throws Route360ClientException In case of errors other than GatewayTimeout
	 */
	private PointOfInterestResponse validateResponse(final Response response, final long requestStart, final long roundTripTime)
																		throws Route360ClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			// consume the results
			return new PointOfInterestResponse(travelOptions, JsonUtil.parseString(IOUtil.getResultString(response)), requestStart);
		}
		else {
			throw new Route360ClientException(response.readEntity(String.class), null);
		}
	}
}
