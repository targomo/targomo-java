package com.targomo.client.api.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.EdgeStatisticAggregationType;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.pojo.EdgeStatisticsReachabilityInRadiusOptions;
import com.targomo.client.api.response.EdgeStatisticsReachabilityResponse;
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

@Slf4j
public class EdgeStatisticsReachabilityInRadiusRequest {

    private final Client client;
    private final MultivaluedMap<String, Object> headers;

    int edgeStatisticCollectionId;

    EdgeStatisticsReachabilityInRadiusOptions requestOptions;

    /**
     * Use a custom client implementation with specified options, method, and headers
     * @param client Client implementation to be used
     * @param edgeStatisticCollectionId Id of the statistic collection
     * @param requestOptions The options for the request
     * @param headers List of custom http headers to be used
     */
    public EdgeStatisticsReachabilityInRadiusRequest(Client client, int edgeStatisticCollectionId,
                                                     EdgeStatisticsReachabilityInRadiusOptions requestOptions,
                                                     MultivaluedMap<String, Object> headers) {
        this.client	= client;
        this.headers = headers;
        this.edgeStatisticCollectionId = edgeStatisticCollectionId;
        this.requestOptions = requestOptions;
    }

    public EdgeStatisticsReachabilityInRadiusRequest(Client client, int edgeStatisticCollectionId, Set<Integer> edgeStatisticIds,
                                                     Map<String, List<Integer>> aggregateEdgeStatisticIds, EdgeStatisticAggregationType aggregationType,
                                                     Integer radius, List<Integer> ignoreRoadClasses, TravelOptions routingOptions, boolean ignoreReachability,
                                                     MultivaluedMap<String, Object> headers) {
        this(client, edgeStatisticCollectionId, EdgeStatisticsReachabilityInRadiusOptions.builder()
                .edgeStatisticIds(edgeStatisticIds)
                .aggregateEdgeStatisticIds(aggregateEdgeStatisticIds)
                .aggregationType(aggregationType)
                .radius(radius)
                .ignoreRoadClasses(ignoreRoadClasses)
                .routingOptions(routingOptions)
                .ignoreReachability(ignoreReachability)
                .build(), headers);
    }

    /**
     * Use a custom client implementation with specified options and default headers
     * @see EdgeStatisticsReachabilityRequest#EdgeStatisticsReachabilityRequest(Client, int, Set, TravelOptions, MultivaluedMap)
     */
    public EdgeStatisticsReachabilityInRadiusRequest(Client client, int edgeStatisticCollectionId, Set<Integer> edgeStatisticIds, Integer radius, TravelOptions routingOptions) {
        this(client, edgeStatisticCollectionId, edgeStatisticIds, new HashMap<>(), EdgeStatisticAggregationType.SUM, radius, new ArrayList<>(), routingOptions, false, new MultivaluedHashMap<>());
    }

    /**
     * Use default client implementation with specified options and default headers
     * Default client uses {@link ClientBuilder}
     * @see EdgeStatisticsReachabilityRequest#EdgeStatisticsReachabilityRequest(Client, int, Set, TravelOptions, MultivaluedMap)
     */
    public EdgeStatisticsReachabilityInRadiusRequest(int edgeStatisticCollectionId, Set<Integer> edgeStatisticIds, Integer radius, TravelOptions travelOptions) {
        this(ClientBuilder.newClient(), edgeStatisticCollectionId, edgeStatisticIds, radius, travelOptions);
    }

    /**
     * @return A response object containing a map of location id to a map of edge statistic id to statistic value
     * @throws JsonProcessingException In case the returned response is not parsable
     * @throws TargomoClientException In case of other errors
     */
    public EdgeStatisticsReachabilityResponse get() throws TargomoClientException, JsonProcessingException {

        String path = StringUtils.join(Arrays.asList(String.valueOf(this.edgeStatisticCollectionId), "reachabilityInRadius"), "/");
        WebTarget target = client.target(requestOptions.getRoutingOptions().getServiceUrl()).path(path).queryParam("key", requestOptions.getRoutingOptions().getServiceKey());

        log.debug(String.format("Executing edge statistics reachability in radius request (%s) to URI: '%s'", path, target.getUri()));

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
