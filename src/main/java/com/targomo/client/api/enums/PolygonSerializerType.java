/**
 * 
 */
package com.targomo.client.api.enums;

import com.targomo.client.Constants;
import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;

/**
 * @author gerb
 *
 */
public enum PolygonSerializerType {
	
	JSON_POLYGON_SERIALIZER, 
	GEO_JSON_POLYGON_SERIALIZER;
	
	public static PolygonSerializerType getPolygonSerializer(String key) throws TargomoClientException {
		
		switch (key) {
		
			case Constants.JSON_POLYGON_SERIALIZER:
				return PolygonSerializerType.JSON_POLYGON_SERIALIZER;
				
			case Constants.GEO_JSON_POLYGON_SERIALIZER:
				return PolygonSerializerType.GEO_JSON_POLYGON_SERIALIZER;
				
			default: throw new TargomoClientException(String.format("No polygon serializer available for key '%s'.", key));
		}
	}
	
	public String getPolygonSerializerName() throws TargomoClientRuntimeException {
		
		switch (this) {
		
			case JSON_POLYGON_SERIALIZER:
				return Constants.JSON_POLYGON_SERIALIZER;
				
			case GEO_JSON_POLYGON_SERIALIZER:
				return Constants.GEO_JSON_POLYGON_SERIALIZER;
				
			default: throw new TargomoClientRuntimeException(String.format("No polygon serializer name available for key '%s'.", this));
		}
	}
}
