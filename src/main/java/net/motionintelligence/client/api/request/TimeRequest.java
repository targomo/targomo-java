package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.TravelOptions;
import net.motionintelligence.client.api.config.RequestConfigurator;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.TimeResponse;
import net.motionintelligence.client.api.util.IOUtil;
import net.motionintelligence.client.api.util.JsonUtil;
import org.glassfish.jersey.message.GZipEncoder;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TimeRequest {
	
	private Client client;
	private String method;
	private TravelOptions travelOptions;
	private String callback = "callback";
	
	/**
	 * 
	 * @param travelOptions
	 */
	public TimeRequest(TravelOptions travelOptions, String method){
		
		this.client	= ClientBuilder.newClient();
		client.register(GZipEncoder.class);
		
		this.travelOptions = travelOptions;
		this.method = method;
	}
	
	public TimeRequest(Client client, TravelOptions travelOptions, String method){
		
		this.client	= client;
		this.travelOptions = travelOptions;
		this.method = method;
	}

	/**
	 * 
	 * @return
	 * @throws Route360ClientException 
	 */
	public TimeResponse get() throws Route360ClientException, ProcessingException {
		
		long requestStart = System.currentTimeMillis();
		
		WebTarget target = client.target(travelOptions.getServiceUrl()).path("v1/time")
				.queryParam("cb", callback)
				.queryParam("key", travelOptions.getServiceKey());
		
		Response response = null; 
		String config = RequestConfigurator.getTimeConfig(travelOptions);
		if (HttpMethod.GET.equals(this.method)) {
			target 	 = target.queryParam("cfg", IOUtil.encode(config));
			response = target.request().get();
		}
		else if (HttpMethod.POST.equals(this.method)) {
			response = target.request().post(
					Entity.entity(config, MediaType.APPLICATION_JSON_TYPE));
		}
		else 
			throw new Route360ClientException("HTTP Method not supported: " + this.method, null);
		
		// compare the HTTP status codes, NOT the route 360 code
		if ( response.getStatus() == Response.Status.OK.getStatusCode() ) {
			
			String res = response.readEntity(String.class).replace(callback + "(", "").replaceAll("\\)$", "");
			
			// consume the results
			return new TimeResponse(this.travelOptions, JsonUtil.parseString(res), requestStart);
		}
		else if ( response.getStatus() == Response.Status.GATEWAY_TIMEOUT.getStatusCode() ) 
			return new TimeResponse(this.travelOptions, "gateway-time-out", System.currentTimeMillis() - requestStart, requestStart);
		else 
			throw new Route360ClientException(response.readEntity(String.class), null);
	}
}
