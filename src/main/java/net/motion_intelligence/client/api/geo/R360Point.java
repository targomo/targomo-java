package net.motion_intelligence.client.api.geo;

public class R360Point extends Wgs84Coordinate {

	protected String id;
	
	public R360Point(){}
	
	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 */
	public R360Point(String id, double latitude, double longitude) {
		super(latitude, longitude);
		
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
