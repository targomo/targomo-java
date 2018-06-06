<<<<<<< HEAD:src/main/java/net/motionintelligence/client/api/enums/PathSerializerType.java
package net.motionintelligence.client.api.enums;
=======
/**
 * 
 */
package com.targomo.client.api.enums;
>>>>>>> develop:src/main/java/com/targomo/client/api/enums/PathSerializerType.java

import com.targomo.client.api.exception.TargomoClientException;
import com.targomo.client.api.exception.TargomoClientRuntimeException;
import com.targomo.client.Constants;

/**
 * @author gerb
 *
 */
public enum PathSerializerType {
	
	TRAVEL_TIME_PATH_SERIALIZER, 
	COMPACT_PATH_SERIALIZER,
	GEO_JSON_PATH_SERIALIZER;
	
	public static PathSerializerType getPathSerializer(String key) throws TargomoClientException {
		
		switch (key) {
		
			case Constants.TRAVEL_TIME_PATH_SERIALIZER:
				return TRAVEL_TIME_PATH_SERIALIZER;
				
			case Constants.COMPACT_PATH_SERIALIZER:
				return COMPACT_PATH_SERIALIZER;
				
			case Constants.GEO_JSON_PATH_SERIALIZER:
				return GEO_JSON_PATH_SERIALIZER;
				
			default: throw new TargomoClientException(String.format("No path serializer available for key '%s'.", key));
		}
	}
	
	public String getPathSerializerName() throws TargomoClientRuntimeException {
		
		switch (this) {
		
			case COMPACT_PATH_SERIALIZER:
				return Constants.COMPACT_PATH_SERIALIZER;
				
			case GEO_JSON_PATH_SERIALIZER:
				return Constants.GEO_JSON_PATH_SERIALIZER;
				
			default: throw new TargomoClientRuntimeException(String.format("No polygon serializer name available for key '%s'.", this));
		}
	}
}
