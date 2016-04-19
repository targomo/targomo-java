package net.motion_intelligence.client.api.geo;

public class Wgs84Coordinate {

	protected final double latitude;
	protected final double longitude;
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public Wgs84Coordinate(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Wgs84Coordinate {\n\tlatitude: " + latitude + "\n\tlongitude: " + longitude + "\n}";
	}
}
