/**
 * 
 */
package net.motionintelligence.client.api.enums;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;

/**
 * @author gerb
 *
 */
public enum PolygonSerializerType {
	
	JSON_POLYGON_SERIALIZER, 
	GEO_JSON_POLYGON_SERIALIZER;
	
	public static PolygonSerializerType getPolygonSerializer(String key) throws Route360ClientException {
		
		switch (key) {
		
			case Constants.JSON_POLYGON_SERIALIZER:
				return PolygonSerializerType.JSON_POLYGON_SERIALIZER;
				
			case Constants.GEO_JSON_POLYGON_SERIALIZER:
				return PolygonSerializerType.GEO_JSON_POLYGON_SERIALIZER;
				
			default: throw new Route360ClientException(String.format("No polygon serializer available for key '%s'.", key));
		}
	}
	
	public String getPolygonSerializerName() throws Route360ClientRuntimeException {
		
		switch (this) {
		
			case JSON_POLYGON_SERIALIZER:
				return Constants.JSON_POLYGON_SERIALIZER;
				
			case GEO_JSON_POLYGON_SERIALIZER:
				return Constants.GEO_JSON_POLYGON_SERIALIZER;
				
			default: throw new Route360ClientRuntimeException(String.format("No polygon serializer name available for key '%s'.", this));
		}
	}
}
