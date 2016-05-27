package net.motionintelligence.client.api.geo;

import net.motionintelligence.client.api.enums.TravelType;

public class Source extends R360Point {

	private final TravelType travelType;
	
	/**
	 * @return the travelType
	 */
	public TravelType getTravelType() {
		return travelType;
	}

	/**
	 * 
	 * @param id
	 * @param travelType
	 * @param latitude
	 * @param longitude
	 */
	public Source(String id, TravelType travelType, double latitude, double longitude) {
		super(id, latitude, longitude);
		
		this.travelType = travelType;
	}
	
	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 */
	public Source(String id, double latitude, double longitude) {
		this(id, TravelType.UNSPECIFIED, latitude, longitude);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((travelType == null) ? 0 : travelType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Source other = (Source) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (travelType != other.travelType)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Source {\n\tid: " + id + "\n\ttravelType: " + travelType + "\n\tlatitude: " + latitude + "\n\tlongitude: " + longitude + "\n}";
	}
}
