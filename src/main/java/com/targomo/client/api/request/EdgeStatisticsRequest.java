package com.targomo.client.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.pojo.EdgeStatisticsRequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EdgeStatisticsRequest {

	private final Client client;
	private final MultivaluedMap<String, Object> headers;

	String serviceUrl;
	String serviceKey;

	int edgeStatisticCollectionId;
	private final EdgeStatisticsRequestOptions requestOptions;

	/**
	 * Use a custom client implementation with specified options and default headers
	 * @see EdgeStatisticsRequest#EdgeStatisticsRequest(Client, String, String, int, EdgeStatisticsRequestOptions, MultivaluedMap)
	 */
	public EdgeStatisticsRequest(Client client, String serviceUrl, String serviceKey, int edgeStatisticCollectionId,
								 EdgeStatisticsRequestOptions requestOptions) {
		this(client, serviceUrl, serviceKey, edgeStatisticCollectionId, requestOptions, new MultivaluedHashMap<>());
	}

	/**
	 * Use default client implementation with specified options and default headers
	 * Default client uses {@link ClientBuilder}
	 * @see EdgeStatisticsRequest#EdgeStatisticsRequest(Client, String, String, int, EdgeStatisticsRequestOptions, MultivaluedMap)
	 */
	public EdgeStatisticsRequest(String serviceUrl, String serviceKey, int edgeStatisticCollectionId,
								 EdgeStatisticsRequestOptions requestOptions) {
		this(ClientBuilder.newClient(), serviceUrl, serviceKey, edgeStatisticCollectionId, requestOptions);
	}

	/**
	 * Use a custom client implementation with specified options, method, and headers
	 * @param client Client implementation to be used
	 * @param serviceUrl The url for the service
	 * @param serviceKey The api key
	 * @param edgeStatisticCollectionId Id of the statistic collection
	 * @param requestOptions Options to be used
	 * @param headers List of custom http headers to be used
	 */
	public EdgeStatisticsRequest(Client client, String serviceUrl, String serviceKey, int edgeStatisticCollectionId,
								 EdgeStatisticsRequestOptions requestOptions, MultivaluedMap<String, Object> headers) {
		this.client	= client;
		this.requestOptions = requestOptions;
		this.headers = headers;
		this.serviceUrl = serviceUrl;
		this.serviceKey = serviceKey;
		this.edgeStatisticCollectionId = edgeStatisticCollectionId;
	}

	/**
	 * @return map of location id to a map of edge statistic id to statistic value
	 * @throws JsonProcessingException In case the returned response is not parsable
	 * @throws TargomoClientException In case of other errors
	 */
	public Map<String, Map<String, Double>> get() throws TargomoClientException, JsonProcessingException {

		String path = StringUtils.join(Arrays.asList(String.valueOf(this.edgeStatisticCollectionId), "locations"), "/");
		WebTarget target = client.target(serviceUrl).path(path).queryParam("key", serviceKey);

		log.debug(String.format("Executing edge statistics request (%s) to URI: '%s'", path, target.getUri()));

		// Execute POST request
		final Entity<String> entity = Entity.entity(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY).writeValueAsString(requestOptions), MediaType.APPLICATION_JSON_TYPE);
		Response response = target.request().headers(headers).post(entity);
		return parseResponse(response);
	}

	/**
	 * Validate HTTP response and return a result map
	 * @param response HTTP response
	 * @return map of location id to edge statistics value
	 * @throws TargomoClientException In case of errors
	 */
	private Map<String, Map<String, Double>> parseResponse(final Response response)
			throws TargomoClientException {

		String responseStr = response.readEntity(String.class);

		// compare the HTTP status codes, NOT the Targomo code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {

			// consume the results
			try {
				TypeReference<HashMap<String, Map<String, Double>>> typeRef = new TypeReference<HashMap<String, Map<String, Double>>>() {};
				return new ObjectMapper().readValue(responseStr, typeRef);
			}
			catch (JsonProcessingException e){
				throw new TargomoClientRuntimeException("Couldn't parse Edge Statistics response", e);
			}
		}
		else {
			throw new TargomoClientException(responseStr, response.getStatus());
		}
	}
}
