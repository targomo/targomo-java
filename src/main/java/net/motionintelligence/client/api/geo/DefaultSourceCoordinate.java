package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;

public class DefaultSourceCoordinate implements Coordinate {
	
	protected String id;
	protected double x, y;
	private TravelType travelType;
	
	/**
	 * 
	 * @param id
	 * @param x
	 * @param y
	 */
	public DefaultSourceCoordinate(String id, double x, double y, TravelType travelType) {
		this.id = id;
		this.x  = x;
		this.y  = y;
		this.travelType = travelType;
	}
	
	/**
	 * 
	 * @param id
	 * @param x
	 * @param y
	 */
	public DefaultSourceCoordinate(String id, double x, double y) {
		this(id,x,y,null);
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

	/**
	 * 
	 */
	public TravelType getTravelType() {
		return this.travelType;
	}
	
	/**
	 * 
	 */
	public void setTravelType(TravelType travelType) {
		this.travelType = travelType;
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
		builder.append("\n\ttravelType: ");
		builder.append(travelType);
		builder.append("\n}\n");
		return builder.toString();
	}
}
