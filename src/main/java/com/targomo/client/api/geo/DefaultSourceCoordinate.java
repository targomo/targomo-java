package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.api.pojo.LocationProperties;

import javax.persistence.*;

/**
 * Default implementation for storing source coordinates.
 * Basically a {@link AbstractCoordinate} with TravelType, specialized to be used as a target.
 */
@Entity
@Table(name="source_coordinate")
public class DefaultSourceCoordinate extends AbstractCoordinate {

	@Id
	@Column(name = "identifier")
	@GeneratedValue(strategy= GenerationType.TABLE)
	private long identifier;

	@Column(name = "travel_type")
	private TravelType travelType;

	// needed for jackson
	public DefaultSourceCoordinate(){}

	/**
	 * Generate Source coordinate with a TravelType as well as ID, X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 * @param travelType TravelType to be associated with the coordinate
	 * @param locationProperties properties for this source
	 */
	public DefaultSourceCoordinate(String id, double x, double y, TravelType travelType, LocationProperties locationProperties) {
		super(id, x, y, locationProperties);
		this.travelType = travelType;
	}
	/**
	 * Generate Source coordinate with a TravelType as well as ID, X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 * @param travelType TravelType to be associated with the coordinate
	 */
	public DefaultSourceCoordinate(String id, double x, double y, TravelType travelType){
		this(id, x, y, travelType, null);
	}
	
	/**
	 * Generate Source coordinate with ID, X and Y values with no travel type.
	 * Travel type will be set to null.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 */
	public DefaultSourceCoordinate(String id, double x, double y) {
		this(id, x, y, null, null);
	}

	/**
	 * Generate Source coordinate with ID, X and Y values with no travel type.
	 * Travel type will be set to null.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 * @param locationProperties
	 */
	public DefaultSourceCoordinate(String id, double x, double y, LocationProperties locationProperties) {
		this(id, x, y, null, locationProperties);
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
     * The main problem with this identifier is that we need it for hibernate
     * since it's not able to work without an ID. But source coordinates have
     * perse no real identifier since the same coordinate could come from multiple
     * clients and have different lat/lng/traveltype.
     *
     * @return this database unique identifier of this source point
     */
	public long getIdentifier() { return identifier; }

	public void setIdentifier(long id) { this.identifier = id; }

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
		builder.append(getClass().getSimpleName());
		builder.append(" { id: ");
		builder.append(getId());
		builder.append(", x: ");
		builder.append(getX());
		builder.append(", y: ");
		builder.append(getY());
		builder.append(", travelType: ");
		builder.append(travelType);
		builder.append("}");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		DefaultSourceCoordinate that = (DefaultSourceCoordinate) o;

		return travelType == that.travelType;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (travelType != null ? travelType.hashCode() : 0);
		return result;
	}
}
