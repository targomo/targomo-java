package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.targomo.client.api.enums.MultiGraphDomainType;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.map.TIntObjectMap;

import java.util.HashMap;
import java.util.Map;

/**
 * The multigraph object that is returned when doing a Multigraph request with JSON serialization.
 * It extends the BaseGraph with values for either the nodes or the edges, depending on the domainType (NODE or EDGE).
 */
public class MultiGraph extends BaseGraph {

    private final Map<String, TIntFloatMap> layers;

    // the type of the layer: values per vertex or per edge
    private final MultiGraphDomainType domainType;

    public MultiGraph(long networkID, MultiGraphDomainType domainType){
        super(networkID);
        this.domainType = domainType;
        this.layers = new HashMap<>();
    }

    @JsonCreator
    public MultiGraph(@JsonProperty("networkID") long networkID,
                      @JsonProperty("nodes") TIntObjectMap<double[]> nodes,
                      @JsonProperty("layers") Map<String,TIntFloatMap> layers,
                      @JsonProperty("edges") TIntObjectMap<int[]> edges,
                      @JsonProperty("supportingPoints") TIntObjectMap<double[][]> supportingPoints,
                      @JsonProperty("domainType") MultiGraphDomainType domainType) {
        super(networkID,nodes,edges,supportingPoints);
        this.layers = layers;
        this.domainType = domainType;
    }

    public Map<String, TIntFloatMap> getLayers() {
        return layers;
    }

    @JsonIgnore
    public String[] getLayerKeys() {
        return getLayers().keySet().toArray(new String[0]);
    }

    public MultiGraphDomainType getDomainType() {
        return domainType;
    }
}
