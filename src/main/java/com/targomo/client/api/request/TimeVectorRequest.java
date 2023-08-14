package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.ResponseErrorException;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.response.TimeVectorResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class TimeVectorRequest extends TargomoRequest<TimeVectorResponse> {

    private static final String HTTP_METHOD = HttpMethod.POST; //could also be HttpMethod.GET
    private static final String PATH = "v1/timeVector";

    /**
     * Use custom client with specified travelOptions
     *
     * @param client        Client to be used
     * @param travelOptions Travel options parameters
     */
    public TimeVectorRequest(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers) {
        super(client, travelOptions, PATH, HTTP_METHOD, TimeVectorResponse.class, headers);
    }

    public TimeVectorRequest(Client client, TravelOptions travelOptions) {
        this(client, travelOptions, new MultivaluedHashMap<>());
    }

    public static TimeVectorResponse executeRequest(Client client, TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        return new TimeVectorRequest(client,travelOptions, headers).get();
    }

    public static TimeVectorResponse executeRequest(Client client, TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return  executeRequest(client, travelOptions, new MultivaluedHashMap<>());
    }

    public static TimeVectorResponse executeRequest(TravelOptions travelOptions, MultivaluedMap<String, Object> headers) throws TargomoClientException, ResponseErrorException {
        return TargomoRequest.executeRequest(
                (client,tO) -> new TimeVectorRequest(client,tO, headers),
                travelOptions);
    }

    public static TimeVectorResponse executeRequest(TravelOptions travelOptions) throws TargomoClientException, ResponseErrorException {
        return executeRequest(travelOptions, new MultivaluedHashMap<>());
    }
}
