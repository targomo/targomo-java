package com.targomo.client.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class POJOUtil {

    /**
     * Prints all fields and their values of the specified java object. If the containing objects are not native but
     * class instances, their contents will be printed as well with indents.
     *
     * @param plainOldJavaObject the java object that is to be printed to a pretty String (no array)
     * @return the pretty print String
     */
    public static String prettyPrintPOJO(Object plainOldJavaObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(plainOldJavaObject);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
