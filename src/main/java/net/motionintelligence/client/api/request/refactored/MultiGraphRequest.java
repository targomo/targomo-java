package net.motionintelligence.client.api.request.refactored;

import com.targomo.jackson.datatype.trove.TroveModule;
import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.enums.MultiGraphSerializationFormat;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.refactored.MultiGraphResponse;
import net.motionintelligence.client.api.response.refactored.MultiGraphResponse.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;

/**
 * TODO update documentation
 */
public class MultiGraphRequest<R, MR extends MultiGraphResponse<R>> extends R360Request<R,R,MR> {

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
    public MultiGraphRequest(Client client, TravelOptions travelOptions,Class<MR> responseClass) {
        super(client,travelOptions,PATH,HTTP_METHOD,responseClass);
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions Travel options parameters of this request
     * @return result of the request
     * @throws Route360ClientException id error occurred during request
     */
    public static MultiGraphJsonResponse executeRequestJson(TravelOptions travelOptions) throws Route360ClientException, ProcessingException {
        return R360Request.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphJsonResponse.class),
                travelOptions);
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions Travel options parameters of this request
     * @return result of the request
     * @throws Route360ClientException id error occurred during request
     */
    public static MultiGraphGeoJsonResponse executeRequestGeoJson(TravelOptions travelOptions) throws Route360ClientException, ProcessingException {
        return R360Request.executeRequest(
                (client,tO) -> new MultiGraphRequest<>(client,tO,MultiGraphGeoJsonResponse.class),
                travelOptions);
    }

    public static MultiGraphJsonResponse executeRequestJson(Client client, TravelOptions travelOptions) throws Route360ClientException, ProcessingException {
        if(!MultiGraphSerializationFormat.JSON.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type JSON must be requested to expect MultiGraphJsonResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphJsonResponse.class).get();
    }

    public static MultiGraphGeoJsonResponse executeRequestGeoJson(Client client, TravelOptions travelOptions) throws Route360ClientException, ProcessingException {
        if(!MultiGraphSerializationFormat.GEOJSON.equals( travelOptions.getMultiGraphSerializationFormat() ))
            throw new IllegalArgumentException("MultiGraph serialization type GEOJSON must be requested to expect MultiGraphGeoJsonResponse");
        return new MultiGraphRequest<>(client,travelOptions,MultiGraphGeoJsonResponse.class).get();
    }
}