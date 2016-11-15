package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;

/**
 * Simple abstract class to use for storing coordinates with IDs and travel types.
 */
public abstract class AbstractCoordinate implements Coordinate {

	private String id;
	private double x;
	private double y;

	/**
	 * Generate a Coordinate with an ID along with X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 */
	public AbstractCoordinate(final String id, final double x, final double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	/**
	 * Get TravelType of coordinate.
	 * @return Travel type associated with the coordinate
	 */
	public abstract TravelType getTravelType();

	/**
	 * Set a travel type for the coordinate.
	 * @param travelType Travel type to be associated with the coordinate.
	 */
	public abstract void setTravelType(final TravelType travelType);

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
}
