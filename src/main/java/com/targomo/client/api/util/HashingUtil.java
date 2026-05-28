package com.targomo.client.api.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author gideon
 */
public class HashingUtil {

    /**
     * For a given class, create a hash from the names of all declared fields
     * @param clazz The class to be hashed
     * @return The hash of all delcared field names
     */
    public static int hashFieldNames(Class clazz) {
        String[] fieldNames = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .sorted(String::compareTo)
                .collect(Collectors.toList())
                .toArray(new String[]{});
        return Arrays.hashCode(fieldNames);
    }
}
