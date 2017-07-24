package net.motionintelligence.client.api.util;

import org.boon.primitive.CharBuf;

public class POJOUtil {

    /**
     * Prints all fields and their values of the specified java object. If the containing objects are not native but
     * class instances, their contents will be printed as well with indents.
     *
     * @param plainOldJavaObject the java object that is to be printed to a pretty String (no array)
     * @return the pretty print String
     */
    public static String prettyPrintPOJO(Object plainOldJavaObject) {
        try (CharBuf buf = CharBuf.createCharBuf()) {
            return buf.prettyPrintBean(plainOldJavaObject).toString();
        }
    }
}
