package net.motionintelligence.client.api.request.config.builder;

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
		return append(builder, key, "\"" + value + "\"");
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
