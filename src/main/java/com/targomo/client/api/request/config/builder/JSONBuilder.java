package com.targomo.client.api.request.config.builder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to help generate JSON Strings using StringBuilders
 *
 */
public final class JSONBuilder {

	private JSONBuilder() {
	}

	/**
	 * Adds beginning descriptor "{" for JSON objects
	 * @param builder Builder to append the descriptor to
	 * @return Builder after appending
	 */
	public static StringBuilder beginJson(final StringBuilder builder) {
		return builder.append("{");
	}

	/**
	 * Appends Object as String, by adding quotes
	 * @param builder Builder to append the value to
	 * @param key Field name
	 * @param value Value object
	 * @return Builder after appending
	 */
	public static StringBuilder appendString(final StringBuilder builder, final String key, final Object value) {
		return builder.append("\"").append(key).append("\":\"").append(value).append("\",");
	}

	/**
	 * Appends Object directly, without adding quotes
	 * @param builder Builder to append the value to
	 * @param key Field name
	 * @param value Value object
	 * @return Builder after appending
	 */
	public static StringBuilder append(final StringBuilder builder, final String key, final Object value) {
		return builder.append("\"").append(key).append("\":").append(value).append(",");
	}

	/**
	 * Appends a list of objects adding quotes and comma separated
	 * @param builder Builder to append the value to
	 * @param key Field name
	 * @param list List of objects
	 * @return Builder after appending
	 */
	public static <T> StringBuilder appendStringList(final StringBuilder builder, final String key, final List<T> list) {
		return append(builder, key, list.stream().map(val -> "\"" + val.toString()  + "\"").collect(Collectors.toList()));
	}


	/**
	 * Appends Object directly and puts JSON object end descriptor "}" to the end
	 * @param builder Builder to append the descriptor
	 * @param key Field name
	 * @param value Value object
	 * @return Builder after appending
	 */
	public static StringBuilder appendAndEnd(final StringBuilder builder, final String key, final Object value) {
		return builder.append("\"").append(key).append("\":").append(value).append("}");
	}
}
