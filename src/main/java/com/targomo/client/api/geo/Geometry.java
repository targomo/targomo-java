/**
 * 
 */
package com.targomo.client.api.geo;

/**
 * @author gideon 
 *
 */
public interface Geometry extends Location {

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
