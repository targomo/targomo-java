package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import net.motionintelligence.client.Constants;

import java.util.Locale;

public enum MultiGraphLayerType {

    NODE    (Constants.KEY_MULTIGRAPH_LAYER_TYPE_NODE,      false,  false),
    EDGE    (Constants.KEY_MULTIGRAPH_LAYER_TYPE_EDGE,      true,   false),
    TILE    (Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILE,      false,  true),
    HEXAGON (Constants.KEY_MULTIGRAPH_LAYER_TYPE_HEXAGON,   false,  true);

    private String key;
    private boolean withEdges;
    private boolean isGeometryMerge;

    MultiGraphLayerType(String key, boolean withEdges, boolean isGeometryMerge) {
        this.key             = key;
        this.withEdges       = withEdges;
        this.isGeometryMerge = isGeometryMerge;
    }

    @JsonCreator
    public static MultiGraphLayerType fromString(String key) {
        return key == null ? null : MultiGraphLayerType.valueOf(key.toUpperCase(Locale.ENGLISH));
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    @JsonIgnore
    public boolean isWithEdges() {
        return withEdges;
    }

    @JsonIgnore
    public boolean isGeometryMerge() {
        return isGeometryMerge;
    }

}
