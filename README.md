# targomo-java
Java Client API for the Targomo web services

## API-Key
Get your API key [here](http://targomo.com/developers/pricing/).

## Add the library to your Maven configuration

```xml
<dependency>
    <groupId>com.targomo</groupId>
    <artifactId>java-client</artifactId>
    <version>0.6.0-SNAPSHOT</version>
</dependency>
```

You also need to add a JAX-RS implementation of your choice. For example Jersey:

```xml
 <dependency>
     <groupId>org.glassfish.jersey.core</groupId>
     <artifactId>jersey-client</artifactId>
     <version>2.6</version>
 </dependency>
```

Please make sure your `jersey-client` version is compatible with your Java version: [here](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/user-guide.html#d0e559)

### Java 11

If you are using Java 11 or more, you might need to add those dependencies:

```
<dependency>
    <groupId>org.glassfish.jersey.inject</groupId>
    <artifactId>jersey-hk2</artifactId>
    <version>2.33</version>
</dependency>
<dependency>
    <groupId>com.sun.activation</groupId>
    <artifactId>jakarta.activation</artifactId>
    <version>1.2.1</version>
</dependency>
```

Note: if you are using `jersey-client`, `jersey-hk2` must have the same version.

## Perform Release
To perform a release simply do: `mvn clean deploy -DperformRelease=true`. There is also manually triggerable jobs to deploy to our 
nexus and to the maven repo (last is only possibly from master).

## Release Notes

### 0.6.0
- adding the logit multigraph aggregations "LOGIT" and "LOGIT_OPTIMIZED"

### 0.5.1
- Revert Jackson version due to incompatibility issues.

### 0.5.0
- added the new aggregationtype "GRAVITATION_HUFF_OPTIMIZED"
- add `routeFromCentroidOption` for routing geometries.

### 0.4.0
- Throw ResponseErrorException if the request failed with an error code

### 0.3.0
- Add POI Gravitation endpoint

### 0.2.0
- Update Readme with POI reachability example

### 0.1.23
- Update osgeo repository

### 0.1.22
- added sourceValuesLowerBound and sourceValuesUpperBound to AggregationConfiguration
- added multiGraphAggregationSourceValuesLowerBound and multiGraphAggregationSourceValuesUpperBound to TravelOptions
- added minResultValueRatio and minResultValue to AggregationConfiguration
- added multiGraphAggregationMinResultValueRatio and multiGraphAggregationMinResultValue to TravelOptions
- Fix a bug: send headers in reachability requests to core

### 0.1.21
- Update Readme

### 0.1.20
- Add option to set headers for requests

### 0.1.19
- Add edge statistics request

### 0.1.18
- Replaced general-image dependency
- Add POI clusterIds field
- Add inter service request type query param
- Add missing travel modes properties
- Remove some of the travel mode defaults
- update JUnit version to 4.13.1

### 0.1.17
- Add POI summary request
- updated geocoding URL
- Added utility classes to decorate jersey client with http headers

### 0.1.16
- Add targetGeohashes parameter
- Update POI reachability response object
- Integrate source geometries parameter
- Integrate turning penalties

### 0.1.15
- Change `elevationEnabled` default to true
- Changed TimeService response format to return both travelTime and length between a source and a target

### 0.1.14
- updated log4j and slf4j versions
- updated the PointOfInterestResponse (since the service return changed slightly)

### 0.1.13
- Added consistent hashCode calculation on Geometry class
- Added new exception types according to core service responses
- Fixes on RequestConfigurator while parsing the input request data
- Some adaptations to use the new poi service
- Improvements for travel time estimation

### 0.1.12
- Change overpass response/request to take an alias rather than take the PoI type from travel options. Used for batch
service requests, other requests will work as before.
- Fixed CurlUtil error that occurred when an immutable list of headers was passed to the method

### 0.1.11
- new aggregation type "count" for multigraph 
- Update to prepare for use of geometries (LineStrings, Polygons and MultiPolygons) as sources.
- added `polygonOrientationRule` parameter to polygon requests
- Updated POIRequest Implementation
- Updated Jackson version to 2.9.9
- Updated the layer type handling - now it is differentiated between layer type and domain type - IMPORTANT: this will only work once a new Targomo core service has been released
- moved elements for statistics requests from TravelOptions into StatisticTravelOptions

### 0.1.10 skipped

### 0.1.9
- Update to RequestConfigurator to correctly parse trafficJunctionPenalty and trafficSignalPenalty
- Removed unused travel options parameter
- Updated dependencies

### 0.1.8
- Added the multigraph attribute multiGraphLayerCustomGeometryMergeAggregation which allows using custom layer aggregation types for tiled serializations. The possible values are defined on MultiGraphGeometryMergeAggregationType.
- New multiGraphReferencedStatisticIds parameter in TravelOptions used in Statistics service
- Updated parameters for MultiGraphAggregationType.MATH
- Renaming all references of ignoreOutlier to ignoreOutliers to make it consistent with the RequestConfigurationParser of the core service

### 0.1.7
- Added the multigraph aggregation type "NONE" for the cases that the response layers aren't merged into one
- Some small aggregation type validations

### 0.1.6
Added attributes for gravitational multigraph, multigraph aggregation pipeline and multigraph math aggregation
- multiGraphAggregationGravitationExponent (exponent to be used on gravitation attraction function)
- multiGraphAggregationPostAggregationFactor (multiplier applied over the multigraph aggregation value)
- multiGraphAggregationInputParameters (set of aggregation input attributes to be used on aggregation pipeline)
- multiGraphPreAggregationPipeline (map containing named aggregations, to be used on multigraph aggregation pipeline. These aggregations should be stored in order of insertion to not affect the pipeline execution) 
- new aggregation type: MATH. To use this new aggregation, one should also define `multiGraphAggregationMathExpression`. An example of mathExpression would be `({POI:00} - {POI:10}) * 2 -  {POI:20}`, where inside `{}` are defined ids of sources (or pre-aggregation ids). Since all the values should be present when calculating the expression, minSourceCount will by default be equal to the number of parameters in the expression (minSourceCount will be 3 by default in the aforementioned example).

### 0.1.5.2
- made avoidTransitRouteTypes in TravelOptions transient
- added maxWalkingTimeFromSource and maxWalkingTimeToTarget to `TravelOptions.equals()` and `TravelOptions.hashCode()`

### 0.1.5.1
- fixed traffic penalty comparison in `TravelOptions.equals()`

### 0.1.5
- added parameter "avoidTransitRouteTypes"
- added parameters for multigraph aggregation pipeline
- added parameters "trafficJunctionPenalty" and "trafficSignalPenalty"

### 0.1.4
- added completeTimeMatrix service request and response
- added lombok library to project
- fixed security warnings + updated build script
- added disable cache parameter to TravelOptions

### 0.1.3
- included multigraph service requests and constants/enums

### 0.0.30
- polygon request's HTTP method can now be set via constructor arg
- statistics request now sets the service url as a url param
- added geometry for server side geometry intersection, also in request builder
- statistic results got some convenience methods to aggregate over returned values

### 0.0.29
- refactored everything to new targomo namespace

### 0.0.28
- this version adds the possibility to generate statistics for specific statistic
 ids. this is especially useful if you don't want to/or you already did
 a travel time analysis and have somehow selected some statistic cell ids client side

### 0.0.27
- adds a small util that rewrites URLs to curl format (post or get)

### 0.0.26

- Geocoding with Authorization against the ESRI service (including access token handling) - batch 
  geocoding with authorization is a lot faster

### 0.0.24

- GeojsonUtils included: they allow displaying your Geojson data in your browser and transforming it between different geo formats
- included toCurl() methods for TimeRequests and RouteRequests
- included "rushHour" in TravelOptions

### 0.0.22

- Removed depricated maxRoutingLength and maxRoutingTime from TravelOptions

### 0.0.21

- Fixed penalties for uphill and downhill

### 0.0.20 

* Implementation to carry out a Geocoding Request (i.e. translate one or more addresses into geo coordinates)

### 0.0.13

The hard dependency to [Jersey](https://jersey.java.net/) was removed. Instead
the user now can and has to provide a JAX-WS implementation of choice as
runtime dependency.

This has the downside that some implementation specific set-up has to be
performed:

* Enabling gzip compression is mandatory when running against Targomo
  servers. A gzip encoder or interceptor has to be registered with the `Client`
  in a library specific way.

* Client timeouts have to be set using library specific properties.

## PolygonService

Create polygon from source point.

    TravelOptions options = new TravelOptions();
    options.setTravelTimes(Arrays.asList(200, 400, 600, 800));
    options.setTravelType(TravelType.TRANSIT);
    options.addSource(new DefaultSourceCoordinate("id1", 40.608155, -73.976636));
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://api.targomo.com/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(GZipEncoder.class); // when using jersey
    // client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    PolygonResponse polygonResponse = new PolygonRequest(client, options).get();
    System.out.println(polygonResponse.getRequestTimeMillis() + " " + polygonResponse.getCode());
    System.out.println(polygonResponse.getResult());

## TimeService

Return travel times from each source to each target point.

    TravelOptions options = new TravelOptions();
    options.setMaxEdgeWeight(900);
    options.setEdgeWeightType(EdgeWeightType.TIME);
    options.addSource(source);
    options.setTargets(targets);
    options.setTravelType(TravelType.CAR);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://api.targomo.com/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(GZipEncoder.class); // when using jersey
    // client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    TimeResponse timeResponse = new TimeRequest(client, options).get();
    // so the api returns all combinations of source and target with the corresponding travel time, or -1 if not reachable
    Map</*Source*/Coordinate, Map</*Target*/Coordinate, Integer>> travelTimes = timeResponse.getTravelTimes();

## ReachabilityService

Return total travel time for each source point to all targets.

    TravelOptions options = new TravelOptions();
    options.setMaxEdgeWeight(900);
    options.setEdgeWeightType(EdgeWeightType.TIME);
    options.addSource(source);
    options.setTargets(targets);
    options.setTravelType(TravelType.CAR);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://api.targomo.com/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(GZipEncoder.class); // when using jersey
    // client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    ReachabilityResponse reachabilityResponse = new ReachabilityRequest(client, options).get();
    // source ID, total travel time or -1 if not reachable
    Map<String, Integer> travelTimes = reachabilityResponse.getTravelTimes();

## RouteService

Return possible route from each source point to each target.

    TravelOptions options = new TravelOptions();
    options.setMaxEdgeWeight(900);
    options.setEdgeWeightType(EdgeWeightType.TIME);
    options.addSource(source);
    options.setTargets(targets);
    options.setTravelType(TravelType.BIKE);
    options.setElevationEnabled(true);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://api.targomo.com/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(GZipEncoder.class); // when using jersey
    // client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    RouteResponse routeResponse = new RouteRequest(client, options).get();
    JSONArray routes = routeResponse.getRoutes();

## GeocodingService

Return possible geocode(s) for each given address.

    Client client = ClientBuilder.newClient();
    GeocodingRequest geocodingRequest = new GeocodingRequest(client);
    
    //Request geocoding for single address
    GeocodingResponse response = geocodingRequest.get( "Lehrter Str. 56, Berlin" );
    DefaultTargetCoordinate geocode = response.getRepresentativeGeocodeOfRequest();
    
    //Request geocoding for many addresses
    GeocodingResponse[] manyGeocodes = geocodingRequest.getBatchParallel( 20, 10, 
            "Ohlauer Str. 38, 10999 Berlin",
            "Thiemannstr 1, Tor 4, 2.Hinterhof, 12059 Berlin",
            "Telegrafenweg 21, 13599 Berlin",
            "Wilhelm-Kabus-Straße 40, Berlin");
    DefaultTargetCoordinate[] geocodes = Arrays.stream(manyGeocodes)
                    .map(GeocodingResponse::getRepresentativeGeocodeOfRequest).toArray(DefaultTargetCoordinate[]::new);

## Poi Reachability service

Return reachable POIs from a list of sources

    TravelOptions options = new TravelOptions();
    options.setMaxEdgeWeight(900);
    options.setEdgeWeightType(EdgeWeightType.TIME);
    options.addSource(new DefaultSourceCoordinate("id1", 40.608155, -73.976636));
    options.setOsmTypes(Collections.singleton(new PoiType("amenity", "restaurant")));
    options.setTravelType(TravelType.CAR);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://api.targomo.com/westcentraleurope/");

    Client client = ClientBuilder.newClient();
    client.register(GZipEncoder.class); // when using jersey
    // client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation

    PointOfInterestRequest request = new PointOfInterestRequest(client, options);
    PointOfInterestResponse poiResponse = request.get();
    // so the api returns all restaurant POIs reachable within 15 min by car.
    
Find a non-exhaustive list of possible OSM types on the [OpenStreetMap wiki](https://wiki.openstreetmap.org/wiki/Map_features)

## Helper to display geojson

You can also display geojson data directly in your browser with the following routine:

    TravelOptions options = new TravelOptions();
    options.setPathSerializer(PathSerializerType.GEO_JSON_PATH_SERIALIZER);
    //set other options
    
    Client client = ClientBuilder.newClient();
    client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    // client.register(GZipEncoder.class); // when using jersey
    RouteResponse routeResponse = new RouteRequest(client, options).get();
    
    CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:3857");
    CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
    this.transform = CRS.findMathTransform(sourceCRS, targetCRS);
    String geometryGeojson = routeResponse.getRoutes().getString(0); //only first one is extracted for the tour to the first target
    FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(geometryGeojson);
    List<Feature> transformedResponse = GeojsonUtil.transformGeometry(this.transform, featureCollection.getFeatures());
    Map<String,FeatureCollection> nameToFeatureCollectionMap = Maps.maps( "testViewer.geojson",
            new FeatureCollection(transformedResponse.toArray(new Feature[transformedResponse.size()])) );
    GeojsonUtil.openGeoJsonInBrowserWithGeojsonIO(fileMap);  //Display the route in the browser
    //alternative method: GeojsonUtil.openGeoJsonInBrowserWithGitHubGist(fileMap);
