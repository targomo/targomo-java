package com.targomo.client.api.geo;

import com.targomo.client.api.pojo.AggregationInputParameters;

import javax.persistence.*;

/**
 * Simple abstract class to use for storing coordinates with IDs and travel types.
 */
@MappedSuperclass
public abstract class AbstractCoordinate implements Coordinate {

	private String id;

	private double x;

	private double y;

	private AggregationInputParameters aggregationInputParameters;

	// needed for jackson
	public AbstractCoordinate(){}

	/**
	 * Generate a Coordinate with an ID along with X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 */
	public AbstractCoordinate(final String id, final double x, final double y, final AggregationInputParameters aggregationInputParameters) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.aggregationInputParameters = aggregationInputParameters;
	}

	/**
	 * Get the ID associated with the coordinate.
	 * @return Coordinate ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Assign an ID to the coordinate
	 * @param id ID to be assigned
	 */
	public void setId(final String id) {
		this.id = id;
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

	/**
	 * Get the aggregation input parameters of a location
	 * @return Location Properties
	 */
	public AggregationInputParameters getAggregationInputParameters() { return this.aggregationInputParameters; }

	/**
	 * Assign aggregation input parameters to a location
	 * @param aggregationInputParameters aggregation input parameters to be assigned
	 */
	public void setAggregationInputParameters(final AggregationInputParameters aggregationInputParameters){ this.aggregationInputParameters = aggregationInputParameters; }

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
