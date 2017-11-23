package net.motionintelligence.client.api.request.refactored;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.refactored.MultiGraphResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import java.util.Map;

/**
 * TODO update documentation
 */
public class MultiGraphRequest extends R360Request<Map<String,Object>,Map<String,Object>,MultiGraphResponse> {

    private static final String HTTP_METHOD = HttpMethod.GET; //could also be HttpMethod.POST
    private static final String PATH = "v1/multigraph";

    /**
     * Use custom client with specified travelOptions
     * @param client Client to be used
     * @param travelOptions Travel options parameters
     */
    public MultiGraphRequest(Client client, TravelOptions travelOptions) {
        super(client,travelOptions,PATH,HTTP_METHOD,MultiGraphResponse.class);
    }

    /**
     * Not recommended since a heavy client object is constructed and destroyed with every call. Also a GZIPEncoder
     * needs to be registered usually to the client to receive results.
     *
     * @param travelOptions
     * @return result of the request
     */
    public static MultiGraphResponse executeRequest(TravelOptions travelOptions) throws Route360ClientException {
        return R360Request.executeRequest(MultiGraphRequest::new,travelOptions);
    }
}