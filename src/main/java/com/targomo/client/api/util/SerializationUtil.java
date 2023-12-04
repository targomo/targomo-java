package com.targomo.client.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.targomo.client.api.enums.TravelType;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class SerializationUtil {

    public static String travelTypeListToString(List<TravelType> travelTypes) {
        if (CollectionUtils.isEmpty(travelTypes)) {
            return "";
        }
        else if (travelTypes.size() == 1) {
            return travelTypes.get(0).toString();
        }
        else {
            return "[" + travelTypes.stream().map(Enum::toString).collect(Collectors.joining(",")) + "]";
        }
    }

    public static void travelTypeListToJsonGenerator(List<TravelType> travelTypes, JsonGenerator jsonGenerator, String fieldNameList, String fieldNameSingle) throws IOException {
        if (CollectionUtils.isNotEmpty(travelTypes)) {
            if (travelTypes.size() == 1) {
                jsonGenerator.writeStringField(fieldNameSingle, travelTypes.get(0).toString());
            }
            else {
                jsonGenerator.writeFieldName(fieldNameList);
                jsonGenerator.writeStartArray(); // [
                for (TravelType t : travelTypes) {
                    jsonGenerator.writeString(t.toString());
                }
                jsonGenerator.writeEndArray(); // ]
            }
        }
    }

}
