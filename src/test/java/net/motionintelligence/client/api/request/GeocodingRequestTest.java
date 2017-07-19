package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.Address;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.geo.DefaultTargetCoordinate;
import net.motionintelligence.client.api.response.GeocodingResponse;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.ToDoubleFunction;

import static org.mockito.Mockito.when;

/**
 * Tests of the Geocoding request methods. This class contains:
 * (1) JUnit tests and
 * (2) System tests (actually calling the REST service)
 *
 * Created by David on 18.07.2017.
 */
public class GeocodingRequestTest extends RequestTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(GeocodingRequestTest.class);
    private static final double DELTA  = 0.005; //Allowed delta when comparing the coordinates

    private static Client client;

    /****************************************************************************************************************
     *  JUnit tests
     ***************************************************************************************************************/

    @Test(expected = Route360ClientException.class)
    public void testExceptionResponse() throws Route360ClientException {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        new GeocodingRequest(mockClient).get(batch2[0]);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testServiceUnavailableResponse() throws Route360ClientException {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
        new GeocodingRequest(mockClient).get(batch2[0]);
    }

    @Test(expected = Route360ClientException.class) //caused by service unavailable
    public void testExceptionBatchResponse() throws Route360ClientException {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
        new GeocodingRequest(mockClient).getBatchParallel(10,2,batch2);
    }

    @Test
    public void testSuccessResponse() throws Route360ClientException {
        when(sampleResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        // Get sample json when success response is queried
        String sampleJson = "{\n" +
                " \"spatialReference\": {\n" +
                "  \"wkid\": 4326,\n" +
                "  \"latestWkid\": 4326\n" +
                " },\n" +
                " \"candidates\": [\n" +
                "  {\n" +
                "   \"address\": \"Chausseestraße 101, 10115, Mitte, Berlin\",\n" +
                "   \"location\": {\n" +
                "    \"x\": 13.380707532171671,\n" +
                "    \"y\": 52.532420302239096\n" +
                "   },\n" +
                "   \"score\": 98.290000000000006,\n" +
                "   \"attributes\": {\n" +
                "    \n" +
                "   },\n" +
                "   \"extent\": {\n" +
                "    \"xmin\": 13.379457500000006,\n" +
                "    \"ymin\": 52.531325000000017,\n" +
                "    \"xmax\": 13.381457500000005,\n" +
                "    \"ymax\": 52.533325000000012\n" +
                "   }\n" +
                "  }\n" +
                " ]\n" +
                "}";
        when(sampleResponse.readEntity(String.class)).thenReturn(sampleJson);
        GeocodingResponse response =  new GeocodingRequest(mockClient).get(batch2[0]);

        Assert.assertEquals( 98.29, response.getRepresentativeCandidate().getScore(), DELTA);
        Assert.assertEquals( 13.380707532171671, response.getRepresentativeGeocodeOfRequest().getX(), DELTA);
        Assert.assertEquals( 52.532420302239096, response.getRepresentativeGeocodeOfRequest().getY(), DELTA);
    }

    /****************************************************************************************************************
     *  System tests
     ***************************************************************************************************************/

    @BeforeClass
    public static void classSetUp() {
        //Make sure it's the Jersey Client. For other clients a connection pool for concurrent requests is required
        //otherwise the parallel batch requests will fail
        client = JerseyClientBuilder.createClient();
    }

    @AfterClass
    public static void classTearDown() {
        if( client != null )
            client.close();
    }

    @Test(expected = Route360ClientException.class)
    public void testSimpleLineRequestFailed() throws Route360ClientException {
        new GeocodingRequest(client).get( "" );
    }

    @Test
    public void testSimpleLineRequestSuccess() throws Route360ClientException {

        GeocodingResponse response = new GeocodingRequest(client).get( batch2[0] );
        GeocodingResponse response2 = new GeocodingRequest(client).get( batch2[1] );

        Assert.assertEquals( coordinates2[0].getX(), response.getRepresentativeGeocodeOfRequest().getX(), DELTA);
        Assert.assertEquals( coordinates2[0].getY(), response.getRepresentativeGeocodeOfRequest().getY(), DELTA);
        Assert.assertEquals( coordinates2[1].getX(), response2.getRepresentativeGeocodeOfRequest().getX(), DELTA);
        Assert.assertEquals( coordinates2[1].getY(), response2.getRepresentativeGeocodeOfRequest().getY(), DELTA);
    }

    @Test
    public void testAddressRequestSuccess() throws Route360ClientException {

        GeocodingResponse response = new GeocodingRequest(client).get(batchAdd2[0]);
        GeocodingResponse response2 = new GeocodingRequest(client).get(batchAdd2[1]);

        Assert.assertEquals( coordinatesAdd2[0].getX(), response.getRepresentativeGeocodeOfRequest().getX(), DELTA);
        Assert.assertEquals( coordinatesAdd2[0].getY(), response.getRepresentativeGeocodeOfRequest().getY(), DELTA);
        Assert.assertEquals( coordinatesAdd2[1].getX(), response2.getRepresentativeGeocodeOfRequest().getX(), DELTA);
        Assert.assertEquals( coordinatesAdd2[1].getY(), response2.getRepresentativeGeocodeOfRequest().getY(), DELTA);
    }

    @Test
    public void testParallelBatchRequestSuccess() throws Route360ClientException {

        final GeocodingRequest geocodingRequest = new GeocodingRequest(client);

        executeBatchRequest(coordinates2, batch2, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(coordinates13, batch13, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(coordinates26, batch26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(coordinates104, batch104, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
    }

    @Test
    public void testParallelAddressBatchRequestSuccess() throws Route360ClientException {

        final GeocodingRequest geocodingRequest = new GeocodingRequest(client);

        executeBatchRequest(coordinatesAdd2, batchAdd2, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(coordinatesAdd18, batchAdd18, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
        executeBatchRequest(coordinatesAdd26, batchAdd26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
    }

    @Test
    public void testParallelBatchWithOptionsRequestSuccess() throws Route360ClientException {

        //Add Option source country: Germany
        EnumMap<GeocodingRequest.Option,String> options = new EnumMap<>(GeocodingRequest.Option.class);
        options.put(GeocodingRequest.Option.SOURCE_COUNTRY,"DEU");
        final GeocodingRequest geocodingRequest = new GeocodingRequest(client,options);

        LOGGER.info("Single Line batch of 26; 10 Threads; Source Country Germany");
        executeBatchRequest(null, batch26, batch -> geocodingRequest.getBatchParallel(10,10,batch) );
//        LOGGER.info("Address batch of 26; 10 Threads; Source Country Germany");
//        executeBatchRequest(null, batchAdd26, batch -> geocodingRequest.getBatchParallel(10,10,batch) );
//        LOGGER.info("Single Line batch of 26; 20 Threads; Source Country Germany");
//        executeBatchRequest(null, batch26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
//        LOGGER.info("Address batch of 26; 20 Threads; Source Country Germany");
//        executeBatchRequest(null, batchAdd26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );

        //Add option search extent: Berlin
        options.put(GeocodingRequest.Option.SEARCH_EXTENT,"12.9803466797,52.3420516364,13.8153076172,52.716330936");
        LOGGER.info("Single Line batch of 26; 10 Threads; Source Country Germany; Search Extent Berlin");
        executeBatchRequest(null, batch26, batch -> geocodingRequest.getBatchParallel(10,10,batch) );
//        LOGGER.info("Address batch of 26; 10 Threads; Source Country Germany; Search Extent Berlin");
//        executeBatchRequest(null, batchAdd26, batch -> geocodingRequest.getBatchParallel(10,10,batch) );
//        LOGGER.info("Single Line batch of 26; 20 Threads; Source Country Germany; Search Extent Berlin");
//        executeBatchRequest(null, batch26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );
//        LOGGER.info("Address batch of 26; 20 Threads; Source Country Germany; Search Extent Berlin");
//        executeBatchRequest(null, batchAdd26, batch -> geocodingRequest.getBatchParallel(20,10,batch) );

        //Other tests excluded since they were only for testing performance of configurations

        //Note: Test results seem to indicate the options don't seem to have a notable effect on the search performance
        //There seems to be fixed limit from the server about max number of parallel requests from one source
    }

    @Test
    public void testSequentialAddressBatchRequestSuccess() throws Route360ClientException {
        GeocodingRequest geocodingRequest = new GeocodingRequest(client);

        executeBatchRequest(coordinatesAdd2, batchAdd2, geocodingRequest::getBatchSequential );
        executeBatchRequest(coordinatesAdd26, batchAdd26, geocodingRequest::getBatchSequential );
    }

    @Test
    public void testSequentialBatchRequestSuccess() throws Route360ClientException {
        GeocodingRequest geocodingRequest = new GeocodingRequest(client);

        executeBatchRequest(coordinates2, batch2, geocodingRequest::getBatchSequential );
        executeBatchRequest(coordinates26, batch26, geocodingRequest::getBatchSequential );
    }

    /****************************************************************************************************************
     *  Private helper test methods
     ***************************************************************************************************************/

    /**
     * Private Method to execute and assert the results of a batch call.
     *
     * @param expectedOutput
     * @param input
     * @param request
     * @param <A> input type, e.g. String or {@link Address}
     * @throws Route360ClientException
     */
    private <A> void executeBatchRequest(DefaultTargetCoordinate[] expectedOutput,
                            A[] input, GetRequest<A[],GeocodingResponse[]> request) throws Route360ClientException{
        long timeBefore = System.currentTimeMillis();
        GeocodingResponse[] responses = request.get( input );
        long timeSequentialFinished = System.currentTimeMillis();
        if( expectedOutput == null ) //print output
            LOGGER.debug( Arrays.asList(responses).toString() );
        else {
            assertCoordinate(expectedOutput, responses, DefaultTargetCoordinate::getX);
            assertCoordinate(expectedOutput, responses, DefaultTargetCoordinate::getY);
        }
        LOGGER.info( "Runtime for " + input.length + " addresses: " + (timeSequentialFinished-timeBefore));
    }

    /**
     * Private method to assert either the x or y coordinates
     *
     * @param expectedOutput
     * @param actual
     * @param extractor
     */
    private void assertCoordinate(DefaultTargetCoordinate[] expectedOutput, GeocodingResponse[] actual,
                         ToDoubleFunction<DefaultTargetCoordinate> extractor) {
        Assert.assertArrayEquals(
                Arrays.stream(expectedOutput).mapToDouble(extractor).toArray(),
                Arrays.stream(actual)
                        .map(GeocodingResponse::getRepresentativeGeocodeOfRequest)
                        .mapToDouble(extractor).toArray(), DELTA);
    }

    /****************************************************************************************************************
     *  Test data
     ***************************************************************************************************************/

    private static DefaultTargetCoordinate[] coordinates2 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,13.441245879393026, 52.51692758612766)};

    private static DefaultTargetCoordinate[] coordinates13 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.361320000000035, 52.52729000000005)};

    private static DefaultTargetCoordinate[] coordinates26 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,13.362665579577191, 52.52980624536893),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,13.441245879393026, 52.51692758612766),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.361320000000035, 52.52729000000005),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,13.441245879393026, 52.51692758612766),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.363161067430415, 52.52770892091282)};

    private static DefaultTargetCoordinate[] coordinates104 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.361320000000035, 52.52729000000005),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.363161067430415, 52.52770892091282),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.361320000000035, 52.52729000000005),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.363161067430415, 52.52770892091282),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.361320000000035, 52.52729000000005),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.363161067430415, 52.52770892091282),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.361320000000035, 52.52729000000005),
            new DefaultTargetCoordinate( null,13.380707532171671, 52.532420302239096),
            new DefaultTargetCoordinate( null,-117.1956703176181, 34.05648811930892),
            new DefaultTargetCoordinate( null,13.36338603846139, 52.528955248687126),
            new DefaultTargetCoordinate( null,11.798012235789622, 50.57337987731554),
            new DefaultTargetCoordinate( null,13.429426548416057, 52.4947124804374),
            new DefaultTargetCoordinate( null,13.452820377171328, 52.47879201471518),
            new DefaultTargetCoordinate( null,13.223142404784014, 52.54285768495032),
            new DefaultTargetCoordinate( null,13.367250787420156, 52.48227046872783),
            new DefaultTargetCoordinate( null,13.451970945726917, 52.50836470716731),
            new DefaultTargetCoordinate( null,13.417159980524335, 52.49833993782109),
            new DefaultTargetCoordinate( null,13.38140487535365, 52.439042828207),
            new DefaultTargetCoordinate( null,13.382203029545938, 52.548776739915894),
            new DefaultTargetCoordinate( null,13.363161067430415, 52.52770892091282)};

    private static String[] batch2 = new String[]{
            "Chaussee 101 Berlin",
            "Karl-Marx-Allee 90a, 10243 Berlin"};
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
            "DAV Kletterzentrum Berlin, Seydlitzstraße 1, 10557 Berlin"};
    private static String[] batch26 = new String[]{
            "Chaussee 101 Berlin",
            "Lehrter Str. 22, berlin",
            "Lehrter Strasse 13 Berlin",
            "Karl-Marx-Allee 90a, 10243 Berlin",
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin",
            "Revaler Str. 99, 10245 Berlin",
            "Südblock, Admiralstraße, Berlin",
            "Großbeerenstraße 2-10/4, 12107 Berlin",
            "Böttgerstraße 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstraße 1, 10557 Berlin",
            "Chausseestr  101 Berlin",
            "Karl-Marx-Allee 90a, 10243 Berlin",
            "Lehrter Str 13 Berlin",
            "Lehrter Strasse 13 Berlin",
            "Ohlauer Strasse 38, 10999 Berlin",
            "Thiemannstrasse 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, Berlin",
            "Wilhelm-Kabus-Str 40, Berlin",
            "Revaler Str. 99, Berlin",
            "Südblock, Admiralstr, Berlin",
            "Großbeerenstraße 2, 12107 Berlin",
            "Böttgerstr 20, Berlin",
            "DAV Kletterzentrum Berlin, Seydlitzstr 1, 10557 Berlin"};
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
            "DAV Kletterzentrum Berlin, Seydlitzstraße 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstr 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstraße 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstr 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstraße 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstr 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstraße 1, 10557 Berlin",
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
            "DAV Kletterzentrum Berlin, Seydlitzstr 1, 10557 Berlin"};

    //Address test cases
    private static DefaultTargetCoordinate[] coordinatesAdd2 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate(null,13.3180633,52.5085717),
            new DefaultTargetCoordinate(null,13.4291491,52.5124634)};

    private static DefaultTargetCoordinate[] coordinatesAdd18 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate(null, 13.3180633, 52.5085717),
            new DefaultTargetCoordinate(null, 13.4291491, 52.5124634),
            new DefaultTargetCoordinate(null, 13.4291491, 52.5124634),
            new DefaultTargetCoordinate(null, 13.4587813, 52.4886732),
            new DefaultTargetCoordinate(null, 13.3025913, 52.5205732),
            new DefaultTargetCoordinate(null, 13.5257713, 52.5285532),
            new DefaultTargetCoordinate(null, 13.3084218, 52.4999538),
            new DefaultTargetCoordinate(null, 13.4533853, 52.5134245),
            new DefaultTargetCoordinate(null, 13.39751, 52.5270387),
            new DefaultTargetCoordinate(null, 13.3841061, 52.5147475),
            new DefaultTargetCoordinate(null, 13.396931, 52.5317154),
            new DefaultTargetCoordinate(null, 13.4053438, 52.5362021),
            new DefaultTargetCoordinate(null, 13.3932513, 52.5185632),
            new DefaultTargetCoordinate(null, 13.4105467, 52.5180071),
            new DefaultTargetCoordinate(null, 13.4374066, 52.5169631),
            new DefaultTargetCoordinate(null, 13.433486, 52.4937527),
            new DefaultTargetCoordinate(null, 13.396931, 52.5317154),
            new DefaultTargetCoordinate(null, 13.4142261, 52.521256)};

    private static DefaultTargetCoordinate[] coordinatesAdd26 = new DefaultTargetCoordinate[]{
            new DefaultTargetCoordinate(null, 13.3180633, 52.5085717),
            new DefaultTargetCoordinate(null, 13.4291491, 52.5124634),
            new DefaultTargetCoordinate(null, 13.4587813, 52.4886732),
            new DefaultTargetCoordinate(null, 13.3025913, 52.5205732),
            new DefaultTargetCoordinate(null, 13.5257713, 52.5285532),
            new DefaultTargetCoordinate(null, 13.3084218, 52.4999538),
            new DefaultTargetCoordinate(null, 13.4533853, 52.5134245),
            new DefaultTargetCoordinate(null, 13.39751, 52.5270387),
            new DefaultTargetCoordinate(null, 13.3841061, 52.5147475),
            new DefaultTargetCoordinate(null, 13.396931, 52.5317154),
            new DefaultTargetCoordinate(null, 13.4053438, 52.5362021),
            new DefaultTargetCoordinate(null, 13.3932513, 52.5185632),
            new DefaultTargetCoordinate(null, 13.4105467, 52.5180071),
            new DefaultTargetCoordinate(null, 13.4374066, 52.5169631),
            new DefaultTargetCoordinate(null, 13.433486, 52.4937527),
            new DefaultTargetCoordinate(null, 13.396931, 52.5317154),
            new DefaultTargetCoordinate(null, 13.4142261, 52.521256),
            new DefaultTargetCoordinate(null, 13.3180633, 52.5085717),
            new DefaultTargetCoordinate(null, 13.4291491, 52.5124634),
            new DefaultTargetCoordinate(null, 13.4587813, 52.4886732),
            new DefaultTargetCoordinate(null, 13.3025913, 52.5205732),
            new DefaultTargetCoordinate(null, 13.5257713, 52.5285532),
            new DefaultTargetCoordinate(null, 13.3084218, 52.4999538),
            new DefaultTargetCoordinate(null, 13.4533853, 52.5134245),
            new DefaultTargetCoordinate(null, 13.39751, 52.5270387),
            new DefaultTargetCoordinate(null, 13.3841061, 52.5147475)};

    private static Address[] batchAdd2 = new Address[]{
            new Address("Grolmanstraße 141",null,"Berlin","10623", "DEU"),
            new Address("Andreasstr. 10",null,"Berlin","10243", "DEU")};

    private static Address[] batchAdd18 = new Address[]{
            new Address("Grolmanstraße 141","","Berlin","10623", "DEU"),
            new Address("Andreasstr. 10","","Berlin","10243", "DEU"),
            new Address("Andreasstr. 10","","Berlin","10243", "DEU"),
            new Address("Moosdorfstrasse 2","","Berlin","12435", "DEU"),
            new Address("Charlottenburger Ufer 16B","","Berlin","10587", "DEU"),
            new Address("Beilsteiner Str.121","","Berlin","12681", "DEU"),
            new Address("Kurfürstendamm 177","","Berlin","10707", "DEU"),
            new Address("Boxhagener Str. 18","","Berlin","10245", "DEU"),
            new Address("Gipsstr. 5","","Berlin","10119", "DEU"),
            new Address("Glinkastraße 40","","Berlin","10117", "DEU"),
            new Address("Brunnenstr. 181","","Berlin","10119", "DEU"),
            new Address("Kastanienallee 74","","Berlin","10435", "DEU"),
            new Address("Am Festungsgraben 1","","Berlin","10117", "DEU"),
            new Address("Klosterstraße 71","","Berlin","10179", "DEU"),
            new Address("Karl-Marx-Allee 90a","","Berlin","10243", "DEU"),
            new Address("Reichenberger Straße 80","","Berlin","10999", "DEU"),
            new Address("Brunnenstr. 181","","Berlin","10119", "DEU"),
            new Address("Alexanderstraße 9","","Berlin","10178", "DEU")};

    private static Address[] batchAdd26 = new Address[]{
            new Address("Grolmanstraße 141","","Berlin","10623", "DEU"),
            new Address("Andreasstr. 10","","Berlin","10243", "DEU"),
            new Address("Moosdorfstrasse 2","","Berlin","12435", "DEU"),
            new Address("Charlottenburger Ufer 16B","","Berlin","10587", "DEU"),
            new Address("Beilsteiner Str.121","","Berlin","12681", "DEU"),
            new Address("Kurfürstendamm 177","","Berlin","10707", "DEU"),
            new Address("Boxhagener Str. 18","","Berlin","10245", "DEU"),
            new Address("Gipsstr. 5","","Berlin","10119", "DEU"),
            new Address("Glinkastraße 40","","Berlin","10117", "DEU"),
            new Address("Brunnenstr. 181","","Berlin","10119", "DEU"),
            new Address("Kastanienallee 74","","Berlin","10435", "DEU"),
            new Address("Am Festungsgraben 1","","Berlin","10117", "DEU"),
            new Address("Klosterstraße 71","","Berlin","10179", "DEU"),
            new Address("Karl-Marx-Allee 90a","","Berlin","10243", "DEU"),
            new Address("Reichenberger Straße 80","","Berlin","10999", "DEU"),
            new Address("Brunnenstr. 181","","Berlin","10119", "DEU"),
            new Address("Alexanderstraße 9","","Berlin","10178", "DEU"),
            new Address("Grolmanstraße 141","","Berlin","10623", "DEU"),
            new Address("Andreasstr. 10","","Berlin","10243", "DEU"),
            new Address("Moosdorfstrasse 2","","Berlin","12435", "DEU"),
            new Address("Charlottenburger Ufer 16B","","Berlin","10587", "DEU"),
            new Address("Beilsteiner Str.121","","Berlin","12681", "DEU"),
            new Address("Kurfürstendamm 177","","Berlin","10707", "DEU"),
            new Address("Boxhagener Str. 18","","Berlin","10245", "DEU"),
            new Address("Gipsstr. 5","","Berlin","10119", "DEU"),
            new Address("Glinkastraße 40","","Berlin","10117", "DEU")};
}
