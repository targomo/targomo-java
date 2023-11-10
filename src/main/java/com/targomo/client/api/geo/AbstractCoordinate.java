package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.pojo.LocationProperties;

import javax.persistence.MappedSuperclass;
import java.util.Collections;

/**
 * Simple abstract class to use for storing coordinates with IDs and travel types.
 */
@MappedSuperclass
public abstract class AbstractCoordinate extends AbstractLocation implements Coordinate {

	private double x;

	private double y;

	// needed for jackson
	public AbstractCoordinate(){
		super();
	}

	/**
	 * Generate a Coordinate with an ID along with X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of coordinate
	 * @param y Y value of coordinate
	 * @param locationProperties properties of coordinate
	 */
	public AbstractCoordinate(final String id, final double x, final double y, final LocationProperties locationProperties) {
		super(id, locationProperties);
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the value of coordinate in X-axis.
	 * @return Value in x-axis
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the value of coordinate in X-axis.
	 * @param x X value to be set
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Get the value of coordinate in Y-axis.
	 * @return Value in y-axis
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the value of coordinate in Y-axis.
	 * @param y Y value to be set
	 */
	public void setY(final double y) {
		this.y = y;
	}

	@Override
	public void setTravelType(final TravelType travelType) {
		setTravelTypes(travelType == null ? Collections.emptyList() : Collections.singletonList(travelType));
	}

	/**
	 * Returns a JSON String representation of the Coordinate with ID, x and y values.
	 * @return JSON representation of the coordinate
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append(" {\n\tid: ");
		builder.append(id);
		builder.append("\n\tx: ");
		builder.append(x);
		builder.append("\n\ty: ");
		builder.append(y);
		builder.append("\n}\n");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AbstractCoordinate that = (AbstractCoordinate) o;

		if (Double.compare(that.x, x) != 0) return false;
		if (Double.compare(that.y, y) != 0) return false;
		return id != null ? id.equals(that.id) : that.id == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = id != null ? id.hashCode() : 0;
		temp = Double.doubleToLongBits(x);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
