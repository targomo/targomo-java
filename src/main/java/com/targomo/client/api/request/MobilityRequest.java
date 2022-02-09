package com.targomo.client.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.Constants;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.pojo.MobilityRequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MobilityRequest {

	private Client client;
	private MobilityRequestOptions requestOptions;

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param requestOptions Options to be used
	 */
	public MobilityRequest(Client client, MobilityRequestOptions requestOptions) {
		this.client	= client;
		this.requestOptions = requestOptions;
	}

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}
	 * @param requestOptions Options to be used
	 */
	public MobilityRequest(MobilityRequestOptions requestOptions) {
		this(ClientBuilder.newClient(), requestOptions);
	}


	// TODO: remove edge statistics and check parsing
	/**
	 * @param locations Coordinate collection of locations
	 * @return map of location id to edge statistics value
	 * @throws JSONException In case the returned response is not parsable
	 * @throws TargomoClientException In case of other errors
	 */
	public Map<String, Double> get(Collection<Coordinate> locations) throws TargomoClientException, JSONException {

		String path = "staypoints/profile/";
		WebTarget target = client.target(requestOptions.getMobilityServiceUrl()).path(path)
				.queryParam("hour_start", requestOptions.getHourStart())
				.queryParam("hour_end", requestOptions.getHourEnd())
				.queryParam("day_start", requestOptions.getDayStart())
				.queryParam("day_end", requestOptions.getDayEnd())
				.queryParam("day_of_year_start", requestOptions.getDayOfYearStart())
				.queryParam("day_of_year_end", requestOptions.getDayOfYearEnd())
				.queryParam("unique", requestOptions.getUnique())
				.queryParam("return_staypoints", requestOptions.getReturnStaypoints())
				.queryParam("radius", requestOptions.getRadius());


		final Entity<String> entity = Entity.entity(parseLocations(locations), MediaType.APPLICATION_JSON_TYPE);

		log.debug(String.format("Executing mobility request (%s) to URI: '%s'", path, target.getUri()));

		// Execute POST request
		Response response = target.request().post(entity);
		return parseResponse(response);
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
				throw new TargomoClientRuntimeException("Couldn't parse Mobility response", e);
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
				.put(Constants.LATITUDE, l.getY())
				.put("lon", l.getX())
			);
		}
		return locationsJson.toString();
	}
}
