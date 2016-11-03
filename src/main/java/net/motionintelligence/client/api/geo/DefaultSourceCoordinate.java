package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;

/**
 * Default implementation for storing source coordinates.
 * Basically a {@link Coordinate} with TravelType, specialized to be used as a target.
 */
public class DefaultSourceCoordinate extends Coordinate {

	private TravelType travelType;
	
	/**
	 * Generate Source coordinate with a TravelType as well as ID, X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 * @param travelType TravelType to be associated with the coordinate
	 */
	public DefaultSourceCoordinate(String id, double x, double y, TravelType travelType) {
		super(id, x, y);
		this.travelType = travelType;
	}
	
	/**
	 * Generate Source coordinate with ID, X and Y values with no travel type.
	 * Travel type will be set to null.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 */
	public DefaultSourceCoordinate(String id, double x, double y) {
		this(id, x, y, null);
	}

	/**
	 * Get travel type configuration for the source coordinate.
	 * @return Travel type
	 */
	@Override
	public TravelType getTravelType() {
		return travelType;
	}

	/**
	 * Specify a travel type for the source coordinate.
	 * @param travelType TravelType to be associated with the source coordinate.
	 */
	@Override
	public void setTravelType(final TravelType travelType) {
		this.travelType = travelType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append(" {\n\tid: ");
		builder.append(getId());
		builder.append("\n\tx: ");
		builder.append(getX());
		builder.append("\n\ty: ");
		builder.append(getY());
		builder.append("\n\ttravelType: ");
		builder.append(travelType);
		builder.append("\n}\n");
		return builder.toString();
	}
}
