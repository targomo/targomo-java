# r360-java
Java Client API for Route360° web service

## API-Key
Get your API key [here](https://developers.route360.net/signup/free).

## Add the library to your Maven configuration

     <dependency>
         <groupId>net.motionintelligence</groupId>
         <artifactId>r360-java-client</artifactId>
         <version>0.0.12</version>
     </dependency>

You also need to add a JAX-RS implementation of your choice.

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
    RouteResponse routeResponse = new RouteRequest(client, options).get();
    JSONArray routes = routeResponse.getRoutes();

## Release Notes

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
