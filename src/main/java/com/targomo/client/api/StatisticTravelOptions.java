package com.targomo.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.enums.MultiGraphTravelTimeApproximation;
import com.targomo.client.api.enums.RoutingAggregationType;
import com.targomo.client.api.geo.*;
import com.targomo.client.api.json.*;
import com.targomo.client.api.pojo.CompetingRoutingOption;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.*;

/**
 * Created by gerb on 13/02/2017.
 */
@Entity @Data
@Table(name = "statistic_travel_option")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticTravelOptions extends TravelOptions {

    @JsonDeserialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapSerializer.class)
    @Transient
    private Map<String,Coordinate> inactiveSources = new HashMap<>();

    @JsonDeserialize(contentAs= DefaultSourceAddress.class, using= DefaultSourceAddressMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceAddress.class, using=DefaultSourceAddressMapSerializer.class)
    @Transient
    private Map<String,DefaultSourceAddress> inactiveSourceAddresses = new HashMap<>();

    @JsonDeserialize(contentAs= DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapSerializer.class)
    @Transient
    private Map<String,AbstractGeometry> inactiveGeometrySources = new HashMap<>();

    @Column(name = "get_closest_sources")
    private boolean getClosestSources = false;

    @Column(name = "omit_individual_statistics")
    private boolean omitIndividualStatistics = false;

    @Transient
    private List<Integer> cellIds = new ArrayList<>();

    @Transient
    private Map<String,Short> multiGraphReferencedStatisticIds = null;

    @Transient
    private MultiGraphTravelTimeApproximation multiGraphTravelTimeApproximation = null;

    @Transient
    private Integer multiGraphDomainStatisticGroupId = null;

    @Transient
    private Integer multiGraphDomainStatisticCollectionId = null;

    @Transient
    private Boolean multiGraphLayerUnboundedStatistics = null;

    @Transient
    private List<Short> statisticIds;

    @Transient
    private Integer chartInterval;

    @Transient
    private Integer statisticCollectionId;

    @Transient
    private List<CompetingRoutingOption> competingRoutingOptions;

    @Transient
    private RoutingAggregationType routingAggregationType = RoutingAggregationType.MIN;

    @Transient
    private String valuesGeometryAggregation;

    // Should be true, if the new calculation method will be used which is related to individual reference ids calculation with the virtual statistic.
    @Transient
    private boolean multigraphCalculateGravitationPerReferenceId = true;

    @Transient
    private Boolean returnOriginId = false;

    @Transient
    private Boolean multiGraphIgnoreRoutingErrorMessages = false;

    @Transient
    private boolean useH3Reachability = false;

    @Transient
    private boolean useStatisticTargets = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticTravelOptions)) return false;

        StatisticTravelOptions that = (StatisticTravelOptions) o;

        return super.equals(o) &&
                Objects.equals(useH3Reachability, that.useH3Reachability) &&
                Objects.equals(useStatisticTargets, that.useStatisticTargets) &&
                Objects.equals(getClosestSources, that.getClosestSources) &&
                Objects.equals(omitIndividualStatistics, that.omitIndividualStatistics) &&
                Objects.equals(inactiveSources, that.inactiveSources) &&
                Objects.equals(cellIds, that.cellIds) &&
                Objects.equals(multiGraphDomainStatisticGroupId, that.multiGraphDomainStatisticGroupId) &&
                Objects.equals(multiGraphDomainStatisticCollectionId, that.multiGraphDomainStatisticCollectionId) &&
                Objects.equals(multiGraphLayerUnboundedStatistics, that.multiGraphLayerUnboundedStatistics) &&
                Objects.equals(multiGraphReferencedStatisticIds, that.multiGraphReferencedStatisticIds) &&
                multiGraphTravelTimeApproximation == that.multiGraphTravelTimeApproximation &&
                Objects.equals(statisticIds, that.statisticIds) &&
                Objects.equals(chartInterval, that.chartInterval) &&
                Objects.equals(statisticCollectionId, that.statisticCollectionId) &&
                Objects.equals(multigraphCalculateGravitationPerReferenceId, that.multigraphCalculateGravitationPerReferenceId)&&
                Objects.equals(returnOriginId, that.returnOriginId)&&
                Objects.equals(competingRoutingOptions, that.competingRoutingOptions) &&
                Objects.equals(routingAggregationType, that.routingAggregationType) &&
                Objects.equals(multiGraphIgnoreRoutingErrorMessages, that.multiGraphIgnoreRoutingErrorMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), inactiveSources, useH3Reachability, useStatisticTargets, getClosestSources,
                omitIndividualStatistics, cellIds, multiGraphDomainStatisticGroupId, multiGraphDomainStatisticCollectionId,
                multiGraphLayerUnboundedStatistics, multiGraphReferencedStatisticIds, multiGraphTravelTimeApproximation,
                statisticIds, chartInterval, statisticCollectionId,
                multigraphCalculateGravitationPerReferenceId, returnOriginId, competingRoutingOptions,
                routingAggregationType.ordinal(), multiGraphIgnoreRoutingErrorMessages);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(getClass().getName());
        builder.append("\n\tinactiveSources: ");
        builder.append(Arrays.toString(inactiveSources.entrySet().toArray()));
        builder.append("\n\tomitIndividualStatistics: ");
        builder.append(omitIndividualStatistics);
        builder.append("\n\tcellIds: ");
        builder.append(cellIds!= null ? cellIds.toString() : "[]");
        builder.append("\n\tmultiGraphReferencedStatisticIds: ");
        builder.append(multiGraphReferencedStatisticIds != null ? multiGraphReferencedStatisticIds.toString() : "[]");
        builder.append("\n\tmultiGraphDomainStatisticGroupId: ");
        builder.append(multiGraphDomainStatisticGroupId);
        builder.append("\n\tmultiGraphDomainStatisticCollectionId: ");
        builder.append(multiGraphDomainStatisticCollectionId);
        builder.append("\n\tmultiGraphLayerUnboundedStatistics: ");
        builder.append(multiGraphLayerUnboundedStatistics);
        builder.append("\n\tmultiGraphTravelTimeApproximation: ");
        builder.append(multiGraphTravelTimeApproximation);
        builder.append("\n\tstatisticIds: ");
        builder.append(statisticIds != null ? statisticIds.toString() : "[]");
        builder.append("\n\tchartInterval: ");
        builder.append(chartInterval);
        builder.append("\n\tstatisticsCollectionId: ");
        builder.append(statisticCollectionId);
        builder.append("\n\tmultigraphCalculateGravitationPerReferenceId: ");
        builder.append(multigraphCalculateGravitationPerReferenceId);
        builder.append("\n\treturnOriginId: ");
        builder.append(returnOriginId);
        builder.append("\n\tcompetingRoutingOptions: ");
        builder.append(competingRoutingOptions);
        builder.append("\n\troutingAggregationType: ");
        builder.append(routingAggregationType);
        builder.append("\n\tmultiGraphIgnoreRoutingErrorMessages: ");
        builder.append(multiGraphIgnoreRoutingErrorMessages);
        builder.append("\n\tuseH3Reachability: ");
        builder.append(useH3Reachability);
        builder.append("\n\tuseStatisticTargets: ");
        builder.append(useStatisticTargets);
        builder.append("\n}\n");
        return builder.toString();
    }
}
