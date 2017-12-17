package net.motionintelligence.client.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;

import java.util.HashMap;
import java.util.Map;

public class MultiGraph extends BaseGraph {

    private final Map<String,TIntIntMap> layers;

    public MultiGraph(long networkID){
        super(networkID);
        this.layers = new HashMap<>();
    }

    @JsonCreator
    public MultiGraph(@JsonProperty("networkID") long networkID,
                      @JsonProperty("nodes") TIntObjectMap<double[]> nodes,
                      @JsonProperty("layers") Map<String,TIntIntMap> layers,
                      @JsonProperty("edges") TIntObjectMap<int[]> edges,
                      @JsonProperty("supportingPoints") TIntObjectMap<double[][]> supportingPoints) {
        super(networkID,nodes,edges,supportingPoints);
        this.layers = layers;
    }

    public Map<String, TIntIntMap> getLayers() {
        return layers;
    }
}
