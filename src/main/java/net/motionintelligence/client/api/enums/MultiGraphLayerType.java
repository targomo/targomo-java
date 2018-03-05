package net.motionintelligence.client.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import net.motionintelligence.client.Constants;

import java.util.Locale;

public enum MultiGraphLayerType {

    NODES(Constants.KEY_MULTIGRAPH_LAYER_TYPE_NODES,false),
    EDGES(Constants.KEY_MULTIGRAPH_LAYER_TYPE_EDGES,true),
    TILES(Constants.KEY_MULTIGRAPH_LAYER_TYPE_TILES,false);

    private String key;
    private boolean withEdges;

    MultiGraphLayerType(String key, boolean withEdges) {
        this.key = key;
        this.withEdges = withEdges;
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

}
