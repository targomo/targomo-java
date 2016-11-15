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

	/**
	 * Get TravelType of coordinate.
	 * @return Travel type associated with the coordinate
	 */
	public TravelType getTravelType();

	/**
	 * Set a travel type for the coordinate.
	 * @param travelType Travel type to be associated with the coordinate.
	 */
	public void setTravelType(final TravelType travelType);

	/**
	 * Get the ID associated with the coordinate.
	 * @return Coordinate ID
	 */
	public String getId(); 

	/**
	 * Assign an ID to the coordinate
	 * @param id ID to be assigned
	 */
	public void setId(final String id); 

	/**
	 * Get the value of coordinate in X-axis.
	 * @return Value in x-axis
	 */
	public double getX();

	/**
	 * Set the value of coordinate in X-axis.
	 * @param x X value to be set
	 */
	public void setX(final double x);

	/**
	 * Get the value of coordinate in Y-axis.
	 * @return Value in y-axis
	 */
	public double getY();

	/**
	 * Set the value of coordinate in Y-axis.
	 * @param y Y value to be set
	 */
	public void setY(final double y);
}
