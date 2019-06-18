/**
 * 
 */
package com.targomo.client.api.geo;

/**
 * @author gerb
 *
 */
public abstract class Coordinate implements Location {

	/**
	 * Get the value of coordinate in X-axis.
	 * @return Value in x-axis
	 */
	public abstract double getX();

	/**
	 * Set the value of coordinate in X-axis.
	 * @param x X value to be set
	 */
	public abstract void setX(final double x);

	/**
	 * Get the value of coordinate in Y-axis.
	 * @return Value in y-axis
	 */
	public abstract double getY();

	/**
	 * Set the value of coordinate in Y-axis.
	 * @param y Y value to be set
	 */
	public abstract void setY(final double y);
}
