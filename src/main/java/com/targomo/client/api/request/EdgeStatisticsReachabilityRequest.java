package com.targomo.client.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.json.TravelOptionsSerializer;
import com.targomo.client.api.pojo.EdgeStatisticsReachabilityRequestOptions;
import com.targomo.client.api.response.EdgeStatisticsReachabilityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.util.*;

/**
 * Reachability request to edge statistics service.
 * Returns total statistic values, like traffic, going in and out of the area (i.e. the statistics on the border edges) reachable from each location.
 * Each location is considered individually.
 */
@Slf4j
public class EdgeStatisticsReachabilityRequest {

    private final Client client;
    private final MultivaluedMap<String, Object> headers;

    int edgeStatisticCollectionId;

    EdgeStatisticsReachabilityRequestOptions requestOptions;

    /**
     * Use a custom client implementation with specified options, method, and headers
     * @param client Client implementation to be used
     * @param edgeStatisticCollectionId Id of the statistic collection
     * @param requestOptions The options for the request
     * @param headers List of custom http headers to be used
     */
    public EdgeStatisticsReachabilityRequest(Client client, int edgeStatisticCollectionId,
                                             EdgeStatisticsReachabilityRequestOptions requestOptions,
                                             MultivaluedMap<String, Object> headers) {
        this.client	= client;
        this.headers = headers;
        this.edgeStatisticCollectionId = edgeStatisticCollectionId;
        this.requestOptions = requestOptions;
    }

    public EdgeStatisticsReachabilityRequest(Client client, int edgeStatisticCollectionId, Set<Integer> edgeStatisticIds,
                                             TravelOptions travelOptions, MultivaluedMap<String, Object> headers) {
        this(client, edgeStatisticCollectionId, new EdgeStatisticsReachabilityRequestOptions(edgeStatisticIds, new HashMap<>(), null, travelOptions), headers);
    }

    /**
     * Use a custom client implementation with specified options and default headers
     * @see EdgeStatisticsReachabilityRequest#EdgeStatisticsReachabilityRequest(Client, int, Set, TravelOptions, MultivaluedMap)
     */
    public EdgeStatisticsReachabilityRequest(Client client, int edgeStatisticCollectionId, Set<Integer> edgeStatisticIds, TravelOptions travelOptions) {
        this(client, edgeStatisticCollectionId, edgeStatisticIds, travelOptions, new MultivaluedHashMap<>());
    }

    /**
     * Use default client implementation with specified options and default headers
     * Default client uses {@link ClientBuilder}
     * @see EdgeStatisticsReachabilityRequest#EdgeStatisticsReachabilityRequest(Client, int, Set, TravelOptions, MultivaluedMap)
     */
    public EdgeStatisticsReachabilityRequest(int edgeStatisticCollectionId, Set<Integer> edgeStatisticIds, TravelOptions travelOptions) {
        this(ClientBuilder.newClient(), edgeStatisticCollectionId, edgeStatisticIds, travelOptions);
    }

    /**
     * @return A response object containing a map of location id to a map of edge statistic id to statistic value
     * @throws JsonProcessingException In case the returned response is not parsable
     * @throws TargomoClientException In case of other errors
     */
    public EdgeStatisticsReachabilityResponse get() throws TargomoClientException, JsonProcessingException {

        String path = StringUtils.join(Arrays.asList(String.valueOf(this.edgeStatisticCollectionId), "reachability"), "/");
        WebTarget target = client.target(requestOptions.getRoutingOptions().getServiceUrl()).path(path).queryParam("key", requestOptions.getRoutingOptions().getServiceKey());

        log.debug(String.format("Executing edge statistics reachability request (%s) to URI: '%s'", path, target.getUri()));

        // Execute POST request
        String requestBody = new ObjectMapper().writeValueAsString(requestOptions);
        final Entity<String> entity = Entity.entity(requestBody, MediaType.APPLICATION_JSON_TYPE);
        Response response = target.request().headers(headers).post(entity);
        return parseResponse(response);
    }

    /**
     * Validate HTTP response and return a response object.
     * @param response HTTP response
     * @return A response object containing a map of location id to a map of edge statistic id to statistic value
     * @throws TargomoClientException In case of errors
     */
    private EdgeStatisticsReachabilityResponse parseResponse(final Response response) throws TargomoClientException {

        String responseStr = response.readEntity(String.class);

        // compare the HTTP status codes, NOT the Targomo code
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            // consume the results
            try {
                TypeReference<EdgeStatisticsReachabilityResponse> typeRef = new TypeReference<EdgeStatisticsReachabilityResponse>() {};
                return new ObjectMapper().readValue(responseStr, typeRef);
            }
            catch (JsonProcessingException e){
                throw new TargomoClientRuntimeException("Couldn't parse Edge Statistics reachability response", e);
            }
        }
        else {
            throw new TargomoClientException(responseStr, response.getStatus());
        }
    }
}
