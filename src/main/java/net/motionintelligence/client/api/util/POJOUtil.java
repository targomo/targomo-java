package net.motionintelligence.client.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class POJOUtil {

    private static final Gson         GSON    = new GsonBuilder().create();
    private static final ObjectMapper MAPPER  = new ObjectMapper();

    /**
     * Prints all fields and their values of the specified java object. If the containing objects are not native but
     * class instances, their contents will be printed as well with indents.
     *
     * Notice: For this a JSON Object is serialized and converted back to a String.
     *
     * @param plainOldJavaObject
     * @return
     */
    public static String prettyPrintPOJO(Object plainOldJavaObject) {
        try {
            Object jsonObject = MAPPER.readValue( GSON.toJson(plainOldJavaObject), Object.class );
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString( jsonObject );
        } catch (IOException e) {  e.printStackTrace(); }
        //return null pointer if failed
        return null;
    }
}
