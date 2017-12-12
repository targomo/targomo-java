package net.motionintelligence.client.api.request.refactored;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.refactored.PolygonResponse;
import org.json.JSONArray;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import java.util.List;
import java.util.Map;

/**
 * Creates polygons for the source points with specified travel times in minutes.
 * In case of GeoJson output, Polygons will be buffered, simplified and transformed
 * according to the respective values in {@link net.motionintelligence.client.api.TravelOptions}.
 * Buffer should be given in meters or in degrees, depending on the output CRS's unit.
 */
public class PolygonRequest extends R360Request<JSONArray,List<Map<String,?>>,PolygonResponse>{

    private static final String HTTP_METHOD = HttpMethod.GET; //could also be HttpMethod.POST
    private static final String PATH = "v1/polygon";

    /**
     * Use custom client with specified travelOptions
     *
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     */
    public PolygonRequest(Client client, TravelOptions travelOptions) {
        super(client,travelOptions,PATH,HTTP_METHOD,PolygonResponse.class);
    }

    public PolygonRequest(Client client, TravelOptions travelOptions, String httpMethod) {
        super(client,travelOptions,PATH,httpMethod,PolygonResponse.class);
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions the travel options of this request
     * @return result of the request
     */
    public static PolygonResponse executeRequest(TravelOptions travelOptions) throws Route360ClientException {
        return R360Request.executeRequest(PolygonRequest::new, travelOptions);
    }
}
