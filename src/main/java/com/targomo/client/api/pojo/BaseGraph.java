package com.targomo.client.api.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.io.Serializable;

/**
 * The basegraph is a POJO representation of an unweighted directed graph, it consists nodes and directed edges between them.
 */
public class BaseGraph implements Serializable {

    private static final BaseGraph emptyImmutableBaseGraph = new BaseGraph(0L);

    public static BaseGraph getEmptyImmutableBaseGraph() {
        return emptyImmutableBaseGraph;
    }

    private final long networkID;
    protected final TLongObjectMap<double[]> nodes;
    protected final TLongObjectMap<long[]> edges;
    protected final TLongObjectMap<double[][]> supportingPoints;

    public BaseGraph(long networkID) {
        this.networkID = networkID;
        this.nodes = new TLongObjectHashMap<>();
        this.edges = new TLongObjectHashMap<>();
        this.supportingPoints = new TLongObjectHashMap<>();
    }

    public BaseGraph(BaseGraph source) {
        this(source.getNetworkID(),
                new TLongObjectHashMap<>(source.getNodes()),
                new TLongObjectHashMap<>(source.getEdges()),
                new TLongObjectHashMap<>(source.getSupportingPoints()));
    }

    @JsonCreator
    public BaseGraph(@JsonProperty("networkID") long networkID,
                     @JsonProperty("nodes") TLongObjectMap<double[]> nodes,
                     @JsonProperty("edges") TLongObjectMap<long[]> edges,
                     @JsonProperty("supportingPoints") TLongObjectMap<double[][]> supportingPoints) {
        this.networkID = networkID;
        this.nodes = nodes;
        this.edges = edges;
        this.supportingPoints = supportingPoints;
    }

    public long getNetworkID() {
        return networkID;
    }

    public TLongObjectMap<double[]> getNodes() {
        return nodes;
    }

    public TLongObjectMap<long[]> getEdges() {
        return edges;
    }

    public TLongObjectMap<double[][]> getSupportingPoints() {
        return supportingPoints;
    }
}
