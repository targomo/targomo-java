package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.enums.MultiGraphSerializationFormat;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.response.MultiGraphResponse;
import com.targomo.client.api.response.MultiGraphResponse.MultiGraphGeoJsonResponse;
import com.targomo.client.api.response.MultiGraphResponse.MultiGraphJsonResponse;
import com.targomo.client.api.response.MultiGraphResponse.MultiGraphTileHashResponse;
import com.targomo.jackson.datatype.trove.TroveModule;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 *  Class to create and execute a multigraph request.
 *
 * @param <R> Response Type of the MultigraphRequest, e.g. {@link MultiGraphJsonResponse}, {@link MultiGraphGeoJsonResponse}
 */
public class MultiGraphRequest<R extends MultiGraphResponse<?>> extends TargomoRequest<R> {

    private static final String HTTP_METHOD = HttpMethod.POST; //could also be HttpMethod.GET
    private static final String PATH = "v1/multigraph";

    static {
        MAPPER.registerModule(new TroveModule(Constants.NO_VALUE_ENTRY_INT_TROVE_MAPS,
                Constants.NO_VALUE_ENTRY_FLOAT_TROVE_MAPS)); //required to properly deserialize MultiGraphs
    }

    /**
     * Use custom client with specified travelOptions
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     */
    public MultiGraphRequest(Client client, TravelOptions travelOptions, Class<R> responseClass, MultivaluedMap<String, Object> headers) {
        super(client,travelOptions,PATH,HTTP_METHOD,responseClass, headers);

        //validation check
        if( (responseClass == MultiGraphJsonResponse.class &&
                !MultiGraphSerializationFormat.JSON.equals( travelOptions.getMultiGraphSerializationFormat() )) ||
                (responseClass == MultiGraphGeoJsonResponse.class &&
                        !MultiGraphSerializationFormat.GEOJSON.equals( travelOptions.getMultiGraphSerializationFormat() )) ||
                (responseClass == MultiGraphTileHashResponse.class &&
                        !MultiGraphSerializationFormat.TILEHASH.equals( travelOptions.getMultiGraphSerializationFormat() ))
        )
            throw new IllegalArgumentException("MultiGraph serialization type JSON must be requested to expect MultiGraphJsonResponse");
    }

    public MultiGraphRequest(Client client, TravelOptions travelOptions, Class<R> responseClass) {
        this(client, travelOptions, responseClass, new MultivaluedHashMap<>());
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions Travel options parameters of this request
     * @return result of the request
     * @throws TargomoClientException id error occurred during request
     */
    public static MultiGraphJsonResponse executeRequestJson(TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        return TargomoRequest.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphJsonResponse.class, headers),
                travelOptions);
    }

    public static MultiGraphJsonResponse executeRequestJson(TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequestJson(travelOptions, new MultivaluedHashMap<>());
    }

    public static MultiGraphTileHashResponse executeRequestTileHash(TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        return TargomoRequest.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphTileHashResponse.class, headers),
                travelOptions);
    }

    public static MultiGraphTileHashResponse executeRequestTileHash(TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequestTileHash(travelOptions,  new MultivaluedHashMap<>());
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions Travel options parameters of this request
     * @return result of the request
     * @throws TargomoClientException id error occurred during request
     */
    public static MultiGraphGeoJsonResponse executeRequestGeoJson(TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        return TargomoRequest.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphGeoJsonResponse.class, headers),
                travelOptions);
    }

    public static MultiGraphGeoJsonResponse executeRequestGeoJson(TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequestGeoJson(travelOptions,  new MultivaluedHashMap<>());
    }

    public static MultiGraphJsonResponse executeRequestJson(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        if(!MultiGraphSerializationFormat.JSON.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type JSON must be requested to expect MultiGraphJsonResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphJsonResponse.class, headers).get();
    }

    public static MultiGraphJsonResponse executeRequestJson(Client client, TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequestJson(client, travelOptions, new MultivaluedHashMap<>());
    }

    public static MultiGraphTileHashResponse executeRequestTileHash(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        if(!MultiGraphSerializationFormat.TILEHASH.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type TILEHASH must be requested to expect MultiGraphTileHashResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphTileHashResponse.class, headers).get();
    }

    public static MultiGraphTileHashResponse executeRequestTileHash(Client client, TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequestTileHash(client, travelOptions, new MultivaluedHashMap<>());
    }

    public static MultiGraphGeoJsonResponse executeRequestGeoJson(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        if(!MultiGraphSerializationFormat.GEOJSON.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type GEOJSON must be requested to expect MultiGraphGeoJsonResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphGeoJsonResponse.class, headers).get();
    }

    public static MultiGraphGeoJsonResponse executeRequestGeoJson(Client client, TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequestGeoJson(client, travelOptions, new MultivaluedHashMap<>());
    }
}