package net.motion_intelligence.client.api.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.motion_intelligence.client.api.exception.Route360ClientRuntimeException;

public class JsonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

	public static void put(JSONArray array, JSONObject object) {
		
		array.put(object);
	}
	
	public static void put(JSONArray jsonArray, JSONArray innerBoundary) {
		
		jsonArray.put(innerBoundary);
	}

	/**
	 * 
	 * @param response
	 * @param string
	 * @param jsonArray
	 */
	public static JSONObject put(JSONObject object, String string, JSONArray jsonArray) {
		
		try {
			
			object.put(string, jsonArray);
		} 
		catch (JSONException e) {
			
			LOGGER.error("Could not put json in json object", e);
		}
		
		return object;
	}
	
	public static JSONObject put(JSONObject object, String string, int value) {
		
		try {
			
			object.put(string, value);
		} 
		catch (JSONException e) {
			
			LOGGER.error("Could not put json in json object", e);
		}
		return object;
	}
	
	public static JSONObject put(JSONObject object, String string, double value) {
		
		try {
			
			object.put(string, value);
		} 
		catch (JSONException e) {
			
			LOGGER.error("Could not put json in json object", e);
		}
		return object;
	}
	
	/**
	 * 
	 * @param response
	 * @param string
	 * @param jsonObject
	 */
	public static JSONObject put(JSONObject object, String string, JSONObject jsonObject) {
		
		try {
			
			object.put(string, jsonObject);
		} 
		catch (JSONException e) {
			
			LOGGER.error("Could not put json in json object", e);
		}
		return object;
	}

	/**
	 * 
	 * @param object
	 * @param string
	 * @param value
	 */
	public static JSONObject put(JSONObject object, String string, String value) {
		
		try {
			
			object.put(string, value);
		} 
		catch (JSONException e) {
			
			LOGGER.error("Could not put json in json object", e);
		}
		return object;
	}

	/**
	 * 
	 * @param config
	 * @return
	 * @throws Route360ClientRuntimeException
	 */
	public static JSONObject parseString(String config) throws Route360ClientRuntimeException {
		
		try {
			
			return new JSONObject(config);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException("Could not parse configuration string to json object: " + config);
		}
	}
	
public static JSONArray parseArray(String config) throws Route360ClientRuntimeException {
		
		try {
			
			return new JSONArray(config);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException("Could not parse configuration string to json array: " + config);
		}
	}

	public static Integer getInt(JSONObject jsonConfig, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return jsonConfig.getInt(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static String getString(JSONObject jsonConfig, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return jsonConfig.getString(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static boolean getBoolean(JSONObject jsonConfig, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return jsonConfig.getBoolean(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static JSONObject getJSONObject(JSONObject jsonConfig, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return jsonConfig.getJSONObject(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static long getLong(JSONObject object, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return object.getLong(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, object));
		}
	}

	public static JSONArray getJsonArray(JSONObject object, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return object.getJSONArray(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, object));
		}
	}
	
	public static Integer getInt(JSONArray array, int index) throws Route360ClientRuntimeException {
		
		try {
			
			return array.getInt(index);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", index, array));
		}
	}

	public static Double getDouble(JSONObject object, String key) throws Route360ClientRuntimeException {
		
		try {
			
			return object.getDouble(key);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, object));
		}
	}

	public static JSONObject getJSONObject(JSONArray array, int index) throws Route360ClientRuntimeException {
		
		try {
			
			return array.getJSONObject(index);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not get key '%s' from json array: %s", index, array));
		}
	}

	public static String toString(JSONObject config, int intendation) throws Route360ClientRuntimeException {
		
		try {
			
			return config.toString(intendation);
		} 
		catch (JSONException e) {
			
			throw new Route360ClientRuntimeException(String.format("Could not (pretty) print '%s' with indendation: %s", config, intendation));
		}
	}
}
