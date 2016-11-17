package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;
import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;

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
	 * @throws Route360ClientRuntimeException
	 */
	@Override
	public TravelType getTravelType() {
		throw new Route360ClientRuntimeException("Not implemented.");
	}

	/**
	 * Not implemented, will throw exception.
	 * @param travelType travelType to set
	 * @throws Route360ClientRuntimeException
	 */
	@Override
	public void setTravelType(final TravelType travelType) {
		throw new Route360ClientRuntimeException("Not implemented.");
	}
}