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
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * Created by gerb on 13/02/2017.
 */
@Data @EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticTravelOptions extends TravelOptions {

    @JsonDeserialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapSerializer.class)
    private Map<String,Coordinate> inactiveSources = new HashMap<>();

    @JsonDeserialize(contentAs= DefaultSourceAddress.class, using= DefaultSourceAddressMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceAddress.class, using=DefaultSourceAddressMapSerializer.class)
    private Map<String,DefaultSourceAddress> inactiveSourceAddresses = new HashMap<>();

    @JsonDeserialize(contentAs= DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceGeometry.class, using= DefaultSourceGeometriesMapSerializer.class)
    private Map<String,AbstractGeometry> inactiveGeometrySources = new HashMap<>();

    private boolean getClosestSources = false;

    private boolean omitIndividualStatistics = false;

    private List<Integer> cellIds = new ArrayList<>();

    private Map<String,Short> multiGraphReferencedStatisticIds = null;

    private MultiGraphTravelTimeApproximation multiGraphTravelTimeApproximation = null;

    private Integer multiGraphDomainStatisticGroupId = null;

    private Integer multiGraphDomainStatisticCollectionId = null;

    private Integer statisticCollectionPeriod = null;

    private Boolean multiGraphLayerUnboundedStatistics = null;

    private List<Short> statisticIds;

    private Integer chartInterval;

    private Integer statisticCollectionId;

    private List<CompetingRoutingOption> competingRoutingOptions;

    private RoutingAggregationType routingAggregationType = RoutingAggregationType.MIN;

    private String valuesGeometryAggregation;

    private Double filterStatsValuesByPercentile;

    // Should be true, if the new calculation method will be used which is related to individual reference ids calculation with the virtual statistic.
    private boolean multigraphCalculateGravitationPerReferenceId = true;

    private Boolean returnOriginId = false;

    private Boolean multiGraphIgnoreRoutingErrorMessages = false;

    private String customGeometryCollectionId;

    private List<String> customGeometryFeatureIds = new ArrayList<>();

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(getClass().getName());
        builder.append("\n\tinactiveSources: ");
        builder.append(Arrays.toString(inactiveSources.entrySet().toArray()));
        builder.append("\n\tinactiveGeometrySources: ");
        builder.append(Arrays.toString(inactiveGeometrySources.entrySet().toArray()));
        builder.append("\n\tinactiveSourceAddresses: ");
        builder.append(Arrays.toString(inactiveSourceAddresses.entrySet().toArray()));
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
        builder.append("\n\tstatisticCollectionPeriod: ");
        builder.append(statisticCollectionPeriod);
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
        builder.append("\n\tcustomGeometryCollectionId: ");
        builder.append(customGeometryCollectionId);
        builder.append("\n\tcustomGeometryFeatureIds: ");
        builder.append(customGeometryFeatureIds!= null ? customGeometryFeatureIds.toString() : "[]");
        builder.append("\n\tcompetingRoutingOptions: ");
        builder.append(competingRoutingOptions);
        builder.append("\n\troutingAggregationType: ");
        builder.append(routingAggregationType);
        builder.append("\n\tmultiGraphIgnoreRoutingErrorMessages: ");
        builder.append(multiGraphIgnoreRoutingErrorMessages);
        builder.append("\n\tvaluesGeometryAggregation: ");
        builder.append(valuesGeometryAggregation);
        builder.append("\n\tfilterStatsValuesByPercentile: ");
        builder.append(filterStatsValuesByPercentile);
        builder.append("\n}\n");
        return builder.toString();
    }
}
