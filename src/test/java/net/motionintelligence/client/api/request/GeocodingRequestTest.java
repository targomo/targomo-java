package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.response.GeocodingResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Arrays;

/**
 * Created by David on 18.07.2017.
 */
public class GeocodingRequestTest extends RequestTest{

    private static final Logger LOGGER      = LoggerFactory.getLogger(GeocodingRequestTest.class);

    //TODO those are system tests - should use the mocking for junit tests
    //TODO make assertions
    //TODO 1) Test with several options
    //TODO 2) Test with Exceptions
    //TODO 3) Test other get requests

    @Test
    public void testSimpleLineRequestSuccess() throws Route360ClientException {
        Client client = ClientBuilder.newClient();
        new GeocodingRequest(client).get( "380 New York St, Redlands, California 92373");
        new GeocodingRequest(client).get( "Chaussee 101 Berlin");
        client.close();
    }

    @Test
    public void testParallelBatchRequestSuccess() throws Route360ClientException {

        Client client = ClientBuilder.newClient();
        final GeocodingRequest geocodingRequest = new GeocodingRequest(client);

        executeBatchRequest(batch2, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(batch13, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(batch26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(batch104, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        client.close();
    }

    @Test
    public void testSequentialBatchRequestSuccess() throws Route360ClientException {
        Client client = ClientBuilder.newClient();
        GeocodingRequest geocodingRequest = new GeocodingRequest(client);

        executeBatchRequest(batch2, geocodingRequest::getBatchSequential );
        executeBatchRequest(batch13, geocodingRequest::getBatchSequential );
        client.close();
    }

    private GeocodingResponse[] executeBatchRequest(String[] input, GetRequest<String[],GeocodingResponse[]> request) throws Route360ClientException{
        long timeBefore = System.currentTimeMillis();
        GeocodingResponse[] responses = request.get( input );
        long timeSequentialFinished = System.currentTimeMillis();
        LOGGER.debug( Arrays.asList(responses).toString() );
        LOGGER.info( "Runtime for " + input.length + " addresses: " + (timeSequentialFinished-timeBefore));
        return responses;
    }

    private static String[] batch2 = new String[]{
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373"};
    private static String[] batch13 = new String[]{
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373",
            "Lehrter Strasse 13 Berlin",
            "oschitzer str 51 schleiz",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße, Berlin"};
    private static String[] batch26 = new String[]{
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373",
            "Lehrter Strasse 13 Berlin",
            "oschitzer str 51 schleiz",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße, Berlin",
            "Chausseestr  101 Berlin",
            "380 New York, Redlands, California 92373",
            "Lehrter Str 13 Berlin",
            "oschitzer strasse 51 schleiz",
            "Ohlauer Strasse 38, 10999 Berlin",
            "Thiemannstrasse 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, Berlin",
            "Wilhelm-Kabus-Str 40, Berlin",
            "Revaler Str. 99, Berlin",
            "Südblock, Admiralstr, Berlin",
            "Großbeerenstraße 2, 12107 Berlin",
            "Böttgerstr 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstr, Berlin"};
    private static String[] batch104 = new String[]{
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373",
            "Lehrter Strasse 13 Berlin",
            "oschitzer str 51 schleiz",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße, Berlin",
            "Chausseestr  101 Berlin",
            "380 New York, Redlands, California 92373",
            "Lehrter Str 13 Berlin",
            "oschitzer strasse 51 schleiz",
            "Ohlauer Strasse 38, 10999 Berlin",
            "Thiemannstrasse 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, Berlin",
            "Wilhelm-Kabus-Str 40, Berlin",
            "Revaler Str. 99, Berlin",
            "Südblock, Admiralstr, Berlin",
            "Großbeerenstraße 2, 12107 Berlin",
            "Böttgerstr 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstr, Berlin",
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373",
            "Lehrter Strasse 13 Berlin",
            "oschitzer str 51 schleiz",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße, Berlin",
            "Chausseestr  101 Berlin",
            "380 New York, Redlands, California 92373",
            "Lehrter Str 13 Berlin",
            "oschitzer strasse 51 schleiz",
            "Ohlauer Strasse 38, 10999 Berlin",
            "Thiemannstrasse 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, Berlin",
            "Wilhelm-Kabus-Str 40, Berlin",
            "Revaler Str. 99, Berlin",
            "Südblock, Admiralstr, Berlin",
            "Großbeerenstraße 2, 12107 Berlin",
            "Böttgerstr 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstr, Berlin",
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373",
            "Lehrter Strasse 13 Berlin",
            "oschitzer str 51 schleiz",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße, Berlin",
            "Chausseestr  101 Berlin",
            "380 New York, Redlands, California 92373",
            "Lehrter Str 13 Berlin",
            "oschitzer strasse 51 schleiz",
            "Ohlauer Strasse 38, 10999 Berlin",
            "Thiemannstrasse 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, Berlin",
            "Wilhelm-Kabus-Str 40, Berlin",
            "Revaler Str. 99, Berlin",
            "Südblock, Admiralstr, Berlin",
            "Großbeerenstraße 2, 12107 Berlin",
            "Böttgerstr 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstr, Berlin",
            "Chaussee 101 Berlin",
            "380 New York St, Redlands, California 92373",
            "Lehrter Strasse 13 Berlin",
            "oschitzer str 51 schleiz",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße, Berlin",
            "Chausseestr  101 Berlin",
            "380 New York, Redlands, California 92373",
            "Lehrter Str 13 Berlin",
            "oschitzer strasse 51 schleiz",
            "Ohlauer Strasse 38, 10999 Berlin",
            "Thiemannstrasse 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, Berlin",
            "Wilhelm-Kabus-Str 40, Berlin",
            "Revaler Str. 99, Berlin",
            "Südblock, Admiralstr, Berlin",
            "Großbeerenstraße 2, 12107 Berlin",
            "Böttgerstr 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstr, Berlin"};
}
