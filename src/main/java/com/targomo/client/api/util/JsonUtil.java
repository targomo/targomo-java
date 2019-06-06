package com.targomo.client.api.util;

import com.targomo.client.api.exception.TargomoClientRuntimeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JsonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

	public static void put(JSONArray array, JSONObject object) {
		
		array.put(object);
	}
	
	public static void put(JSONArray jsonArray, JSONArray innerBoundary) {
		
		jsonArray.put(innerBoundary);
	}

	/**
	 * Put array to JSONObject
	 * @param object JSONObject that the array will be insterted into
	 * @param string Key for the array
	 * @param jsonArray The array to be put
     * @return Updated JSONObject
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
	 * Put object inside another JSONObject
	 * @param object Parent object
	 * @param string Key for the child object
	 * @param jsonObject Child object
	 * @return Resulting parent object with child object
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
	 * Put String inside JSONObject
	 * @param object Parent object
	 * @param string Key for the string
	 * @param value The string
     * @return Resulting parent object with child object
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
	 * Parse configuration string
	 * @param config Configuration string
	 * @return Parsed JSONObject
	 * @throws TargomoClientRuntimeException In case of any parse errors
	 */
	public static JSONObject parseString(String config) throws TargomoClientRuntimeException {
		
		try {
			
			return new JSONObject(config);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException("Could not parse configuration string to json object: " + config);
		}
	}
	
	public static JSONArray parseArray(String config) throws TargomoClientRuntimeException {
		
		try {
			
			return new JSONArray(config);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException("Could not parse configuration string to json array: " + config);
		}
	}

	public static Integer getInt(JSONObject jsonConfig, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return jsonConfig.getInt(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static String getString(JSONObject jsonConfig, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return jsonConfig.getString(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static boolean getBoolean(JSONObject jsonConfig, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return jsonConfig.getBoolean(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static JSONObject getJSONObject(JSONObject jsonConfig, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return jsonConfig.getJSONObject(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, jsonConfig));
		}
	}

	public static long getLong(JSONObject object, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return object.getLong(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, object));
		}
	}

	public static JSONArray getJsonArray(JSONObject object, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return object.getJSONArray(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, object));
		}
	}
	
	public static Integer getInt(JSONArray array, int index) throws TargomoClientRuntimeException {
		
		try {
			
			return array.getInt(index);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", index, array));
		}
	}

	public static Double getDouble(JSONObject object, String key) throws TargomoClientRuntimeException {
		
		try {
			
			return object.getDouble(key);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json object: %s", key, object));
		}
	}

	public static JSONObject getJSONObject(JSONArray array, int index) throws TargomoClientRuntimeException {
		
		try {
			
			return array.getJSONObject(index);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not get key '%s' from json array: %s", index, array));
		}
	}

	public static String toString(JSONObject config, int intendation) throws TargomoClientRuntimeException {
		
		try {
			
			return config.toString(intendation);
		} 
		catch (JSONException e) {
			
			throw new TargomoClientRuntimeException(String.format("Could not (pretty) print '%s' with indendation: %s", config, intendation));
		}
	}
	
	public static Set<String> getKeys(JSONObject results) {
		
		Set<String> keys = new HashSet<>();
		
		Iterator<String> iterator = results.keys();
		while ( iterator.hasNext() ) 
			keys.add(iterator.next());
		
		return keys;
	}

	public static List<Integer> getSortedIntKeySet(JSONObject object) {
		
		List<Integer> keysAsInt = new ArrayList<>();
		for ( String key : getKeys(object)) 
			keysAsInt.add(Integer.valueOf(key));
		
		Collections.sort(keysAsInt);
		
		return keysAsInt;
	}
}
