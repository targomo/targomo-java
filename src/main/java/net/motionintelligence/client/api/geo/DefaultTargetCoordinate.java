package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;

public class DefaultTargetCoordinate implements Coordinate {
	
	protected String id;
	protected double x, y;
	
	/**
	 * 
	 * @param id
	 * @param x
	 * @param y
	 */
	public DefaultTargetCoordinate(String id, double x, double y) {
		this.id = id;
		this.x  = x;
		this.y  = y;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public TravelType getTravelType() {
		throw new Route360ClientRuntimeException("not implemented");
	}

	@Override
	public void setTravelType(TravelType travelType) {
		throw new Route360ClientRuntimeException("not implemented");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
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