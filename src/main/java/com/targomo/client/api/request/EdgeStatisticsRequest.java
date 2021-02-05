package com.targomo.client.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.pojo.EdgeStatisticsRequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EdgeStatisticsRequest {

	private Client client;
	private EdgeStatisticsRequestOptions requestOptions;

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param requestOptions Options to be used
	 */
	public EdgeStatisticsRequest(Client client, EdgeStatisticsRequestOptions requestOptions) {
		this.client	= client;
		this.requestOptions = requestOptions;
	}

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}
	 * @param requestOptions Options to be used
	 */
	public EdgeStatisticsRequest(EdgeStatisticsRequestOptions requestOptions) {
		this(ClientBuilder.newClient(), requestOptions);
	}

	/**
	 * @param locations Coordinate collection of locations
	 * @return map of location id to edge statistics value
	 * @throws JSONException In case the returned response is not parsable
	 * @throws TargomoClientException In case of other errors
	 */
	public Map<String, Double> get(Collection<Coordinate> locations) throws TargomoClientException, JSONException {

		String path = buildLocationsPath(requestOptions.getEdgeStatisticGroupId(), requestOptions.getEdgeStatisticId());
		WebTarget target = client.target(requestOptions.getServiceUrl()).path(path)
				.queryParam("key", requestOptions.getServiceKey())
				.queryParam("radius", requestOptions.getRadius())
				.queryParam("travelType", requestOptions.getTravelType())
				.queryParam("direction", requestOptions.getDirection());

		if (requestOptions.getIgnoreRoadClasses() != null) {
			for (Integer clazz : requestOptions.getIgnoreRoadClasses()) {
				target = target.queryParam("ignoreRoadClass", clazz);
			}
		}

		final Entity<String> entity = Entity.entity(parseLocations(locations), MediaType.APPLICATION_JSON_TYPE);

		log.debug(String.format("Executing edge statistics request (%s) to URI: '%s'", path, target.getUri()));

		// Execute POST request
		Response response = target.request().post(entity);
		return parseResponse(response);
	}

	private static String buildLocationsPath(Integer edgeStatisticGroupId, Integer edgeStatisticId) {
		return StringUtils.join(Arrays.asList("locations", String.valueOf(edgeStatisticGroupId), String.valueOf(edgeStatisticId)), "/");
	}

	/**
	 * Validate HTTP response and return a result map
	 * @param response HTTP response
	 * @return map of location id to edge statistics value
	 * @throws TargomoClientException In case of errors
	 */
	private Map<String, Double> parseResponse(final Response response)
			throws TargomoClientException {

		// compare the HTTP status codes, NOT the route 360 code
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {

			// consume the results
			try {
				TypeReference<HashMap<String, Double>> typeRef = new TypeReference<HashMap<String, Double>>() {};
				return new ObjectMapper().readValue(response.readEntity(String.class), typeRef);
			}
			catch (JsonProcessingException e){
				throw new TargomoClientRuntimeException("Couldn't parse Edge Statistics response", e);
			}
		}
		else {
			throw new TargomoClientException(response.readEntity(String.class), response.getStatus());
		}
	}

	/**
	 * @param locations Coordinate collection of locations to be parsed
	 * @return Locations parsed into JSON
	 * @throws JSONException In case something cannot be parsed
	 */
	private static String parseLocations(Collection<Coordinate> locations) throws JSONException {

		JSONArray locationsJson = new JSONArray();
		for (Coordinate l : locations) {
			locationsJson.put(new JSONObject()
				.put(Constants.ID, l.getId())
				.put(Constants.Y, l.getY())
				.put(Constants.X, l.getX())
			);
		}
		return locationsJson.toString();
	}
}
