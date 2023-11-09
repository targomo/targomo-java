package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.pojo.LocationProperties;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.targomo.client.api.util.SerializationUtil.travelTypeListToString;

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

	@Column(name = "travel_types")
	private List<TravelType> travelTypes;

	// needed for jackson
	public DefaultSourceCoordinate(){}

	public DefaultSourceCoordinate(String id, double x, double y, List<TravelType> travelTypes, LocationProperties locationProperties) {
		super(id, x, y, locationProperties);
		this.travelTypes = travelTypes;
	}

	/**
	 * Generate Source coordinate with a TravelType as well as ID, X and Y values.
	 * @param id ID to associate with the target coordinate
	 * @param x X value of target
	 * @param y Y value of target
	 * @param travelType TravelType to be associated with the coordinate
	 * @param locationProperties properties for this source
	 */
	public DefaultSourceCoordinate(String id, double x, double y, TravelType travelType, LocationProperties locationProperties) {
		this(id, x, y, travelType == null ? Collections.emptyList() : Collections.singletonList(travelType), locationProperties);
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
		this(id, x, y, Collections.emptyList(), null);
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
		this(id, x, y, Collections.emptyList(), locationProperties);
	}

	/**
	 * Get travel type configuration for the source coordinate.
	 * @return Travel type
	 */
	@Override
	public List<TravelType> getTravelTypes() {
		return travelTypes;
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
		this.travelTypes = travelType == null ? Collections.emptyList() : Collections.singletonList(travelType);
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
		builder.append(", travelTypes: ");
		builder.append(travelTypeListToString(travelTypes));
		builder.append("}");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		DefaultSourceCoordinate that = (DefaultSourceCoordinate) o;

		return Objects.equals(this.travelTypes, that.travelTypes);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (travelTypes != null ? travelTypes.hashCode() : 0);
		return result;
	}
}
