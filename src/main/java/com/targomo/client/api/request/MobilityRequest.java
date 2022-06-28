package com.targomo.client.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.pojo.MobilityRequestOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

@Slf4j
public class MobilityRequest {

	public static final String ID         = "id";
	public static final String LATITUDE   = "lat";
	public static final String LONGITUDE  = "lon";

	private final Client client;
	private final MobilityRequestOptions requestOptions;
	private final MultivaluedMap<String, Object> headers;
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param requestOptions Options to be used
	 */
	public MobilityRequest(Client client, MobilityRequestOptions requestOptions) {
		this(client, requestOptions, new MultivaluedHashMap<>());
	}

	/**
	 * Use default client implementation with specified options and method
	 * Default client uses {@link ClientBuilder}
	 * @param requestOptions Options to be used
	 */
	public MobilityRequest(MobilityRequestOptions requestOptions) {
		this(ClientBuilder.newClient(), requestOptions);
	}

	/**
	 * Use a custom client implementation with specified options and method
	 * @param client Client implementation to be used
	 * @param requestOptions Options to be used
	 * @param headers List of custom http headers to be used
	 */
	public MobilityRequest(Client client, MobilityRequestOptions requestOptions, MultivaluedMap<String, Object> headers) {
		this.client	= client;
		this.requestOptions = requestOptions;
		this.headers = headers;
	}

	/**
	 * @param locations Coordinate collection of locations
	 * @return list of mobility result
	 * @throws JSONException In case the returned response is not parsable
	 * @throws TargomoClientException In case of other errors
	 */
	public List<MobilityResult> get(Collection<Coordinate> locations) throws TargomoClientException, JSONException {

		String path = "staypoints/profile/";
		WebTarget target = client.target(requestOptions.getMobilityServiceUrl()).path(path)
				.queryParam("min_duration", requestOptions.getMinDuration())
				.queryParam("max_duration", requestOptions.getMaxDuration())
				.queryParam("hour_start", requestOptions.getHourStart())
				.queryParam("hour_end", requestOptions.getHourEnd())
				.queryParam("day_start", requestOptions.getDayStart())
				.queryParam("day_end", requestOptions.getDayEnd())
				.queryParam("day_of_year_start", requestOptions.getDayOfYearStart())
				.queryParam("day_of_year_end", requestOptions.getDayOfYearEnd())
				.queryParam("unique", requestOptions.getUnique())
				.queryParam("exact", requestOptions.getExact())
				.queryParam("exclude_night_locations", requestOptions.getExcludeNightLocations())
				.queryParam("radius", requestOptions.getRadius())
				.queryParam("api_key", requestOptions.getApiKey())
				.queryParam("format", "json");

		final Entity<String> entity = Entity.entity(parseLocations(locations), MediaType.APPLICATION_JSON_TYPE);

		log.debug(String.format("Executing mobility request (%s) to URI: '%s'", path, target.getUri()));

		// Execute POST request
		Response response = target.request().headers(headers).post(entity);
		return parseResponse(response);
	}

	/**
	 * Validate HTTP response and return a result map
	 * @param response HTTP response
	 * @return map of location id to edge statistics value
	 * @throws TargomoClientException In case of errors
	 */
	private List<MobilityResult> parseResponse(final Response response)
			throws TargomoClientException {

		// compare the HTTP status codes
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {

			// consume the results
			try {
				TypeReference<List<MobilityResult>> typeRef = new TypeReference<List<MobilityResult>>() {};
				return OBJECT_MAPPER.readValue(response.readEntity(String.class), typeRef);
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
				.put(ID, l.getId())
				.put(LATITUDE, l.getY())
				.put(LONGITUDE, l.getX())
			);
		}
		return locationsJson.toString();
	}

	@Setter
	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MobilityResult{
		private String id;
		private Double lat;
		private Integer lon;
		private Integer radius;
		@JsonProperty(value = "day_start")
		private Integer dayStart;
		@JsonProperty(value = "day_end")
		private Integer dayEnd;
		@JsonProperty(value = "hour_start")
		private Integer hourStart;
		@JsonProperty(value = "hour_end")
		private Integer hourEnd;
		@JsonProperty(value = "day_of_year_start")
		private Integer dayOfYearStart;
		@JsonProperty(value = "day_of_year_end")
		private Integer dayOfYearEnd;
		@JsonProperty(value = "min_duration")
		private Integer minDuration;
		@JsonProperty(value = "max_duration")
		private Integer maxDuration;
		private Boolean unique;
		private Boolean exact;
		@JsonProperty(value = "exclude_night_locations")
		private Boolean excludeNightLocations;
		private Integer count;
	}
}
