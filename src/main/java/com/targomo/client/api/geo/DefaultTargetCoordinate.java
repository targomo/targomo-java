package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientRuntimeException;

/**
 * Default implementation for storing target coordinates.
 * Basically a {@link AbstractCoordinate} specialized to be used as a target.
 */
public class DefaultTargetCoordinate extends AbstractCoordinate {

	/**
	 * Generate Target coordinate with an ID along with X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 */
	public DefaultTargetCoordinate(final String id, final double x, final double y) {
		super(id, x, y);
	}

	/**
	 * Not implemented, will throw exception.
	 * @throws TargomoClientRuntimeException any time this method is called.
	 */
	@Override
	public TravelType getTravelType() {
		throw new TargomoClientRuntimeException("Not implemented.");
	}

	/**
	 * Not implemented, will throw exception.
	 * @param travelType travelType to set
	 * @throws TargomoClientRuntimeException any time this method is called.
	 */
	@Override
	public void setTravelType(final TravelType travelType) {
		throw new TargomoClientRuntimeException("Not implemented.");
	}
}