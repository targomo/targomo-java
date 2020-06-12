/**
 * 
 */
package com.targomo.client.api.geo;

/**
 * Interface for the geometries used as sources and targets in the core routing.
 *
 * @author gideon
 */
public interface RoutingGeometry extends Location {

	/**
	 * @return the string representation of this geometry
	 */
	String getData();

	/**
	 * @return the coordinate reference system for this geometry
	 */
	Integer getCrs();

	/**
	 * @param data the string representation of this geometry
	 */
	void setData(String data);

	/**
	 * @param crs coordinate reference system for this geometry
	 */
	void setCrs(Integer crs);

}
