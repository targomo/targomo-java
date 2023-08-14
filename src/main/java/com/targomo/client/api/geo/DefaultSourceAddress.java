package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.pojo.LocationProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * AbstractClass for dealing with Source/Target H3 Addresses
 */
@MappedSuperclass @Getter @Setter
public class DefaultSourceAddress extends AbstractLocation implements Location {

	@Id
	@Column(name = "identifier")
	@GeneratedValue(strategy= GenerationType.TABLE)
	private long identifier;

	@Column(name = "travel_type")
	private TravelType travelType;

	private String h3Address;

	// needed for jackson
	public DefaultSourceAddress(){
		super();
	}

	public DefaultSourceAddress(final String h3Address, TravelType travelType, LocationProperties locationProperties) {
		super(null, locationProperties);
		this.h3Address = h3Address;
		this.travelType = travelType;
	}

	public DefaultSourceAddress(final String h3Address, final LocationProperties locationProperties) {
		this(h3Address, null, locationProperties);
	}

	public DefaultSourceAddress(String h3Address, TravelType travelType){
		this(h3Address, travelType, null);
	}

	public DefaultSourceAddress(String h3Address) {
		this(h3Address, null, null);
	}

	/**
	 * Returns a JSON String representation of the Coordinate with ID, x and y values.
	 * @return JSON representation of the coordinate
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append(" {\n\th3Address: ");
		builder.append(h3Address);
		builder.append("\n}\n");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DefaultSourceAddress that = (DefaultSourceAddress) o;

		return Objects.equals(h3Address, that.h3Address);
	}

	@Override
	public int hashCode() {
		return h3Address.hashCode();
	}
}
