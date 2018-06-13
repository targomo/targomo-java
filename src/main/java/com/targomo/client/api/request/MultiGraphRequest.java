package com.targomo.client.api.request;

import com.targomo.client.Constants;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.jackson.datatype.trove.TroveModule;
import com.targomo.client.api.enums.MultiGraphSerializationFormat;
import com.targomo.client.api.response.MultiGraphResponse;
import com.targomo.client.api.response.MultiGraphResponse.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;

/**
 *  Class to create and execute a multigraph request.
 *
 * @param <R> Response Type of the MultigraphRequest, e.g. {@link MultiGraphJsonResponse}, {@link MultiGraphGeoJsonResponse}
 */
public class MultiGraphRequest<R extends MultiGraphResponse<?>> extends TargomoRequest<R> {

    private static final String HTTP_METHOD = HttpMethod.GET; //could also be HttpMethod.POST
    private static final String PATH = "v1/multigraph";

    static {
        MAPPER.registerModule(new TroveModule(Constants.NO_VALUE_ENTRY_TROVE_MAPS)); //required to properly deserialize MultiGraphs
    }

    /**
     * Use custom client with specified travelOptions
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     */
    public MultiGraphRequest(Client client, TravelOptions travelOptions, Class<R> responseClass) {
        super(client,travelOptions,PATH,HTTP_METHOD,responseClass);

        //validation check
        if( (responseClass == MultiGraphJsonResponse.class &&
                !MultiGraphSerializationFormat.JSON.equals( travelOptions.getMultiGraphSerializationFormat() )) ||
                (responseClass == MultiGraphGeoJsonResponse.class &&
                        !MultiGraphSerializationFormat.GEOJSON.equals( travelOptions.getMultiGraphSerializationFormat() )))
            throw new IllegalArgumentException("MultiGraph serialization type JSON must be requested to expect MultiGraphJsonResponse");
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions Travel options parameters of this request
     * @return result of the request
     * @throws TargomoClientException id error occurred during request
     */
    public static MultiGraphJsonResponse executeRequestJson(TravelOptions travelOptions) throws TargomoClientException {
        return TargomoRequest.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphJsonResponse.class),
                travelOptions);
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions Travel options parameters of this request
     * @return result of the request
     * @throws TargomoClientException id error occurred during request
     */
    public static MultiGraphGeoJsonResponse executeRequestGeoJson(TravelOptions travelOptions) throws TargomoClientException {
        return TargomoRequest.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphGeoJsonResponse.class),
                travelOptions);
    }

    public static MultiGraphJsonResponse executeRequestJson(Client client, TravelOptions travelOptions) throws TargomoClientException {
        if(!MultiGraphSerializationFormat.JSON.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type JSON must be requested to expect MultiGraphJsonResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphJsonResponse.class).get();
    }

    public static MultiGraphGeoJsonResponse executeRequestGeoJson(Client client, TravelOptions travelOptions) throws TargomoClientException {
        if(!MultiGraphSerializationFormat.GEOJSON.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type GEOJSON must be requested to expect MultiGraphGeoJsonResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphGeoJsonResponse.class).get();
    }
}