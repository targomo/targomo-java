package net.motionintelligence.client.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * JSON Format:
 * <pre>{@code
 *   {
 *       "networkID": 1,
 *       'nodes' : { 1:[12.345,52.13], 2:[12.345, 52.13], ... , 13534:[12.345,52.13] }, // {id:[x,y]}
 *       'edges' : {1: [n1, n2, ec], 2:[n1, n2, ec], ...,108264:[ n1, n2, ec ]}, // optional: {id: [sourceNodeId, targetNodeId, edgeClass]}
 *       'supportingPoints' : {                                  // optional: edgeId:[[x,y]]
 *           0:[[12.345, 52.13],[ 12.345, 52.13 ]],
 *           ..
 *       }
 *   }}</pre>
 */
public class BaseGraph implements Serializable {

    private final long networkID;
    protected final TIntObjectMap<double[]> nodes;
    protected final TIntObjectMap<int[]> edges;
    protected final TIntObjectMap<double[][]> supportingPoints;

    public BaseGraph(long networkID) {
        this.networkID = networkID;
        this.nodes = new TIntObjectHashMap<>();
        this.edges = new TIntObjectHashMap<>();
        this.supportingPoints = new TIntObjectHashMap<>();
    }

    @JsonCreator
    public BaseGraph(@JsonProperty("networkID") long networkID,
                     @JsonProperty("nodes") TIntObjectMap<double[]> nodes,
                     @JsonProperty("edges") TIntObjectMap<int[]> edges,
                     @JsonProperty("supportingPoints") TIntObjectMap<double[][]> supportingPoints) {
        this.networkID = networkID;
        this.nodes = nodes;
        this.edges = edges;
        this.supportingPoints = supportingPoints;
    }

    public long getNetworkID() {
        return networkID;
    }

    public TIntObjectMap<double[]> getNodes() {
        return nodes;
    }

    public TIntObjectMap<int[]> getEdges() {
        return edges;
    }

    public TIntObjectMap<double[][]> getSupportingPoints() {
        return supportingPoints;
    }
}
