# r360-java
Java Client API for Route360° web service

    TravelOptions options = new TravelOptions();
	options.setTravelTimes(Arrays.asList(600, 1200, 1800, 2400, 3000, 3600));
	options.setTravelType(TravelType.TRANSIT);
	options.addSource(new Source("id1", 40.608155, -73.976636));
	options.setServiceKey("ENTER YOUR KEY HERE");
	options.setServiceUrl("https://service.route360.net/na_northeast/");
		
	PolygonResponse polygonResponse = new PolygonRequest(options).get();
	System.out.println(polygonResponse.getRequestTimeMillis() + " " + polygonResponse.getCode());
	System.out.println(polygonResponse.getResult());
