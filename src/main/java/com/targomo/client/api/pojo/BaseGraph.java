package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.Serializable;

/**
 * The basegraph is a POJO representation of an unweighted directed graph, it consists nodes and directed edges between them.
 */
public class BaseGraph implements Serializable {

    private static BaseGraph emptyImmutableBaseGraph = new BaseGraph(0L);

    public static BaseGraph getEmptyImmutableBaseGraph() {
        return emptyImmutableBaseGraph;
    }

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

    public BaseGraph(BaseGraph source) {
        this(source.getNetworkID(),
                new TIntObjectHashMap<>(source.getNodes()),
                new TIntObjectHashMap<>(source.getEdges()),
                new TIntObjectHashMap<>(source.getSupportingPoints()));
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
