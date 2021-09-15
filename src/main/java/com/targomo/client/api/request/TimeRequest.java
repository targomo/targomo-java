package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.request.config.RequestConfigurator;
import com.targomo.client.api.response.TimeResponse;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.util.IOUtil;
import com.targomo.client.api.util.JsonUtil;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Calculates travel times from each source point to each target.
 * Only accepts {@link HttpMethod} POST.
 */
public class TimeRequest {

	private static final String CALLBACK = "callback";

	private Client client;
	private TravelOptions travelOptions;

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}.
	 * @param travelOptions Options to be used
	 */
	public TimeRequest(TravelOptions travelOptions) {
		this.client	= ClientBuilder.newClient();
		this.travelOptions = travelOptions;
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param travelOptions Options to be used
	 */
	public TimeRequest(Client client, TravelOptions travelOptions) {
		this.client	= client;
		this.travelOptions = travelOptions;
	}

	/**
	 * Execute request
	 * @return Time response
	 * @throws TargomoClientException In case of error other than Gateway Timeout
	 */
	public TimeResponse get() throws TargomoClientException, ProcessingException, ResponseErrorException {

		long requestStart = System.currentTimeMillis();

        WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/time")
                .queryParam("cb", CALLBACK)
                .queryParam("key", travelOptions.getServiceKey());

        if (travelOptions.getInterServiceKey() != null && !travelOptions.getInterServiceKey().isEmpty()) {
            target = target.queryParam(Constants.INTER_SERVICE_KEY, travelOptions.getInterServiceKey());
        }

		if (travelOptions.getInterServiceRequestType() != null && !travelOptions.getInterServiceRequestType().isEmpty()) {
			target = target.queryParam(Constants.INTER_SERVICE_REQUEST, travelOptions.getInterServiceRequestType());
		}

		Response response;
		String config = RequestConfigurator.getConfig(travelOptions);
		// Execute POST request
		response = target.request().post(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));

		return validateResponse(response, requestStart);
	}

	/**
	 * For debugging.
	 *
	 * @return the request as curl String
     * @throws TargomoClientException when error occurred during parsing of the travel options
	 */
	public String toCurl() throws TargomoClientException {
		String url = travelOptions.getServiceUrl().endsWith("/") ?
				travelOptions.getServiceUrl() : travelOptions.getServiceUrl() + "/";
		return "curl -X POST '" +
                    url + "v1/time" +
                    "?cb=" + CALLBACK +
                    "&key=" + travelOptions.getServiceKey() + "' " +
                "-H 'content-type: application/json' " +
                "-d '" + RequestConfigurator.getConfig(travelOptions) + "'";
	}


	/**
	 * Validate HTTP response and return a TimeResponse
	 * @param response HTTP response
	 * @param requestStart Beginning of execution in milliseconds
	 * @return TimeResponse
	 * @throws TargomoClientException In case of errors other than GatewayTimeout
	 */
	private TimeResponse validateResponse(final Response response, final long requestStart)
			throws TargomoClientException, ResponseErrorException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {

			String res = IOUtil.getResultString(response);

			// consume the results
			return new TimeResponse(travelOptions, JsonUtil.parseString(res), requestStart);
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}
}
