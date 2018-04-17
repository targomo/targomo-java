# r360-java
Java Client API for Route360° web service

## API-Key
Get your API key [here](https://developers.route360.net/signup/free).

## Add the library to your Maven configuration

     <dependency>
         <groupId>net.motionintelligence</groupId>
         <artifactId>r360-java-client</artifactId>
         <version>0.0.28</version>
     </dependency>

You also need to add a JAX-RS implementation of your choice. For example Jersey:

```
 <dependency>
     <groupId>org.glassfish.jersey.core</groupId>
     <artifactId>jersey-client</artifactId>
     <version>2.6</version>
 </dependency>
```

## Release Notes

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

* Enabling gzip compression is mandatory when running against Route360°
  servers. A gzip encoder or interceptor has to be registered with the `Client`
  in a library specific way.

* Client timeouts have to be set using library specific properties.

## PolygonService

Create polygon from source point.

    TravelOptions options = new TravelOptions();
    options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
    options.setTravelType(TravelType.TRANSIT);
    options.addSource(new DefaultSourceCoordinate("id1", 40.608155, -73.976636));
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://service.route360.net/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    // client.register(GZipEncoder.class); // when using jersey
    PolygonResponse polygonResponse = new PolygonRequest(client, options).get();
    System.out.println(polygonResponse.getRequestTimeMillis() + " " + polygonResponse.getCode());
    System.out.println(polygonResponse.getResult());

## TimeService

Return travel times from each source to each target point.

    TravelOptions options = new TravelOptions();
    options.setMaxRoutingTime(3600);
    options.addSource(source);
    options.setTargets(targets);
    options.setTravelType(TravelType.CAR);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://service.route360.net/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    // client.register(GZipEncoder.class); // when using jersey
    TimeResponse timeResponse = new TimeRequest(client, options).get();
    // so the api returns all combinations of source and target with the corresponding travel time, or -1 if not reachable
    Map</*Source*/Coordinate, Map</*Target*/Coordinate, Integer>> travelTimes = timeResponse.getTravelTimes();

## ReachabilityService

Return total travel time for each source point to all targets.

    TravelOptions options = new TravelOptions();
    options.setMaxRoutingTime(3600);
    options.addSource(source);
    options.setTargets(targets);
    options.setTravelType(TravelType.CAR);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://service.route360.net/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    // client.register(GZipEncoder.class); // when using jersey
    ReachabilityResponse reachabilityResponse = new ReachabilityRequest(client, options).get();
    // source ID, total travel time or -1 if not reachable
    Map<String, Integer> travelTimes = reachabilityResponse.getTravelTimes();

## RouteService

Return possible route from each source point to each target.

    TravelOptions options = new TravelOptions();
    options.setMaxRoutingTime(3600);
    options.addSource(source);
    options.setTargets(targets);
    options.setTravelType(TravelType.BIKE);
    options.setElevationEnabled(true);
    options.setServiceKey("ENTER YOUR KEY HERE");
    options.setServiceUrl("https://service.route360.net/germany/");
    
    Client client = ClientBuilder.newClient();
    client.register(new GZIPDecodingInterceptor(10_000_000)); // specific to JAX-RS implementation
    // client.register(GZipEncoder.class); // when using jersey
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
