package net.motion_intelligence.client.api.geo;

public class Target extends R360Point {

	protected String name;
	
	/**
	 * 
	 * @param id
	 * @param latitude
	 * @param longitude
	 */
	public Target(String id, double latitude, double longitude) {
		super(id, latitude, longitude);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Target other = (Target) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Target {\n\tid: " + id + "\n\tlatitude: " + latitude + "\n\tlongitude: " + longitude + "\n}";
	}
}
