/**
 * 
 */
package com.targomo.client.api.geo;

/**
 * @author gerb
 *
 */
public interface Coordinate extends Location {

	/**
	 * Get the value of coordinate in X-axis.
	 * @return Value in x-axis
	 */
	double getX();

	/**
	 * Set the value of coordinate in X-axis.
	 * @param x X value to be set
	 */
	void setX(final double x);

	/**
	 * Get the value of coordinate in Y-axis.
	 * @return Value in y-axis
	 */
	double getY();

	/**
	 * Set the value of coordinate in Y-axis.
	 * @param y Y value to be set
	 */
	void setY(final double y);
}
