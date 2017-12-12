package net.motionintelligence.client.api.enums;

import net.motionintelligence.client.Constants;
import net.motionintelligence.client.api.exception.Route360ClientException;
import net.motionintelligence.client.api.exception.Route360ClientRuntimeException;

/**
 * @author gerb
 *
 */
public enum PathSerializerType {
	
	TRAVEL_TIME_PATH_SERIALIZER, 
	COMPACT_PATH_SERIALIZER,
	GEO_JSON_PATH_SERIALIZER;
	
	public static PathSerializerType getPathSerializer(String key) throws Route360ClientException {
		
		switch (key) {
		
			case Constants.TRAVEL_TIME_PATH_SERIALIZER:
				return TRAVEL_TIME_PATH_SERIALIZER;
				
			case Constants.COMPACT_PATH_SERIALIZER:
				return COMPACT_PATH_SERIALIZER;
				
			case Constants.GEO_JSON_PATH_SERIALIZER:
				return GEO_JSON_PATH_SERIALIZER;
				
			default: throw new Route360ClientException(String.format("No path serializer available for key '%s'.", key));
		}
	}
	
	public String getPathSerializerName() throws Route360ClientRuntimeException {
		
		switch (this) {
		
			case COMPACT_PATH_SERIALIZER:
				return Constants.COMPACT_PATH_SERIALIZER;
				
			case GEO_JSON_PATH_SERIALIZER:
				return Constants.GEO_JSON_PATH_SERIALIZER;
				
			default: throw new Route360ClientRuntimeException(String.format("No polygon serializer name available for key '%s'.", this));
		}
	}
}
