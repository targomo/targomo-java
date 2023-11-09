package com.targomo.client.api.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientRuntimeException;

import java.util.Collections;
import java.util.List;

/**
 * Default implementation for storing target coordinates.
 * Basically a {@link AbstractCoordinate} specialized to be used as a target.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultTargetCoordinate extends AbstractCoordinate {

	/**
	 * Generate Target coordinate with an ID along with X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 */
	@JsonCreator
	public DefaultTargetCoordinate(@JsonProperty("id") final String id, @JsonProperty("x") final double x, @JsonProperty("y") final double y) {
		super(id, x, y, null);
	}

	/**
	 * Not implemented, will throw exception.
	 * @throws TargomoClientRuntimeException any time this method is called.
	 */
	@Override
	public List<TravelType> getTravelTypes() {
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