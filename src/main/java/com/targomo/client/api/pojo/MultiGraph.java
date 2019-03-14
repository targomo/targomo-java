package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.map.TIntObjectMap;
import com.targomo.client.api.enums.MultiGraphLayerType;

import java.util.HashMap;
import java.util.Map;

/**
 * The multigraph object that is returned when doing a Multigraph request with JSON serialization.
 * It extends the BaseGraph with values for either the nodes or the edges, depending on the layerType (NODE or EDGE).
 */
public class MultiGraph extends BaseGraph {

    private final Map<String, TIntFloatMap> layers;

    // the type of the layer: values per vertex or per edge
    private final MultiGraphLayerType layerType;

    public MultiGraph(long networkID, MultiGraphLayerType multiGraphLayerType){
        super(networkID);
        this.layerType = multiGraphLayerType;
        this.layers = new HashMap<>();
    }

    @JsonCreator
    public MultiGraph(@JsonProperty("networkID") long networkID,
                      @JsonProperty("nodes") TIntObjectMap<double[]> nodes,
                      @JsonProperty("layers") Map<String,TIntFloatMap> layers,
                      @JsonProperty("edges") TIntObjectMap<int[]> edges,
                      @JsonProperty("supportingPoints") TIntObjectMap<double[][]> supportingPoints,
                      @JsonProperty("layerType") MultiGraphLayerType layerType) {
        super(networkID,nodes,edges,supportingPoints);
        this.layers = layers;
        this.layerType = layerType;
    }

    public Map<String, TIntFloatMap> getLayers() {
        return layers;
    }

    public MultiGraphLayerType getLayerType() {
        return layerType;
    }
}
