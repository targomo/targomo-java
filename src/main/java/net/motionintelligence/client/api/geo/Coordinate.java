/**
 * 
 */
package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;

/**
 * @author gerb
 *
 */
public interface Coordinate {

	public double getX();
	public void setX(double x);
	
	public double getY();
	public void setY(double y);
	
	public String getId();
	public void setId(String id);
	
	public TravelType getTravelType();
	public void setTravelType(TravelType travelType);
}
