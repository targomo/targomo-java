package com.targomo.client.api.request;

import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.response.TimeVectorResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;

public class TimeVectorRequest extends TargomoRequest<TimeVectorResponse> {

    private static final String HTTP_METHOD = HttpMethod.POST; //could also be HttpMethod.GET
    private static final String PATH = "v1/timeVector";

    /**
     * Use custom client with specified travelOptions
     *
     * @param client        Client to be used
     * @param travelOptions Travel options parameters
     */
    public TimeVectorRequest(Client client, TravelOptions travelOptions) {
        super(client, travelOptions, PATH, HTTP_METHOD, TimeVectorResponse.class);
    }

    public static TimeVectorResponse executeRequest(Client client, TravelOptions travelOptions) throws TargomoClientException {
        return new TimeVectorRequest(client,travelOptions).get();
    }

    public static TimeVectorResponse executeRequest(TravelOptions travelOptions) throws TargomoClientException {
        return TargomoRequest.executeRequest( TimeVectorRequest::new, travelOptions );
    }
}
