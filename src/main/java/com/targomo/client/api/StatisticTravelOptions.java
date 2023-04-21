package com.targomo.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.enums.MultiGraphTravelTimeApproximation;
import com.targomo.client.api.enums.RoutingAggregationType;
import com.targomo.client.api.geo.AbstractGeometry;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.geo.DefaultSourceGeometry;
import com.targomo.client.api.json.DefaultSourceCoordinateMapDeserializer;
import com.targomo.client.api.json.DefaultSourceCoordinateMapSerializer;
import com.targomo.client.api.json.DefaultSourceGeometriesMapDeserializer;
import com.targomo.client.api.json.DefaultSourceGeometriesMapSerializer;
import com.targomo.client.api.pojo.CompetingRoutingOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.*;

/**
 * Created by gerb on 13/02/2017.
 */
@Entity
@Table(name = "statistic_travel_option")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticTravelOptions extends TravelOptions {

    @JsonDeserialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapDeserializer.class)
    @JsonSerialize(contentAs=DefaultSourceCoordinate.class, using=DefaultSourceCoordinateMapSerializer.class)
    @Transient
    private Map<String,Coordinate> inactiveSources = new HashMap<>();

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

    public Map<String,Coordinate> getInactiveSources() {
        return this.inactiveSources;
    }

    public void setInactiveSources(Map<String,Coordinate> inactiveSources) {
        this.inactiveSources = inactiveSources;
    }

    public boolean isUseH3Reachability() {
        return useH3Reachability;
    }

    public void setUseH3Reachability(boolean useH3Reachability) {
        this.useH3Reachability = useH3Reachability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticTravelOptions)) return false;

        StatisticTravelOptions that = (StatisticTravelOptions) o;

        return super.equals(o) &&
                Objects.equals(useH3Reachability, that.useH3Reachability) &&
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
        return Objects.hash(super.hashCode(), inactiveSources, useH3Reachability, getClosestSources,
                omitIndividualStatistics, cellIds, multiGraphDomainStatisticGroupId, multiGraphDomainStatisticCollectionId,
                multiGraphLayerUnboundedStatistics, multiGraphReferencedStatisticIds, multiGraphTravelTimeApproximation,
                statisticIds, chartInterval, statisticCollectionId,
                multigraphCalculateGravitationPerReferenceId, returnOriginId, competingRoutingOptions,
                routingAggregationType.ordinal(), multiGraphIgnoreRoutingErrorMessages);
    }

    public boolean isGetClosestSources() {
        return getClosestSources;
    }

    public void setGetClosestSources(boolean getClosestSources) {
        this.getClosestSources = getClosestSources;
    }

    public List<Integer> getCellIds() {
        return cellIds;
    }

    public void setCellIds(List<Integer> cellIds) {
        this.cellIds = cellIds;
    }

    public boolean isOmitIndividualStatistics() {
        return omitIndividualStatistics;
    }

    public void setOmitIndividualStatistics(boolean omitIndividualStatistics) {
        this.omitIndividualStatistics = omitIndividualStatistics;
    }

    public Map<String, Short> getMultiGraphReferencedStatisticIds() {
        return multiGraphReferencedStatisticIds;
    }

    public void setMultiGraphReferencedStatisticIds(Map<String, Short> multiGraphReferencedStatisticIds) {
        this.multiGraphReferencedStatisticIds = multiGraphReferencedStatisticIds;
    }

    public Integer getMultiGraphDomainStatisticGroupId() {
        return multiGraphDomainStatisticGroupId;
    }

    public void setMultiGraphDomainStatisticGroupId(Integer multiGraphDomainStatisticGroupId) {
        this.multiGraphDomainStatisticGroupId = multiGraphDomainStatisticGroupId;
    }

    public List<CompetingRoutingOption> getCompetingRoutingOptions() {
        return competingRoutingOptions;
    }

    public void setCompetingRoutingOptions(List<CompetingRoutingOption> competingRoutingOptions) {
        this.competingRoutingOptions = competingRoutingOptions;
    }

    public Integer getMultiGraphDomainStatisticCollectionId() {
        return multiGraphDomainStatisticCollectionId;
    }

    public void setMultiGraphDomainStatisticCollectionId(Integer multiGraphDomainStatisticCollectionId) {
        this.multiGraphDomainStatisticCollectionId = multiGraphDomainStatisticCollectionId;
    }

    public Boolean getMultiGraphLayerUnboundedStatistics() {
        return multiGraphLayerUnboundedStatistics;
    }

    public void setMultiGraphLayerUnboundedStatistics(Boolean multiGraphLayerUnboundedStatistics) {
        this.multiGraphLayerUnboundedStatistics = multiGraphLayerUnboundedStatistics;
    }

    public MultiGraphTravelTimeApproximation getMultiGraphTravelTimeApproximation() {
        return multiGraphTravelTimeApproximation;
    }

    public void setMultiGraphTravelTimeApproximation(MultiGraphTravelTimeApproximation multiGraphTravelTimeApproximation) {
        this.multiGraphTravelTimeApproximation = multiGraphTravelTimeApproximation;
    }

    public void setStatisticIds(List<Short> statisticIds) {
        this.statisticIds = statisticIds ;
    }

    public List<Short> getStatisticIds() {
        return this.statisticIds;
    }

    public void setChartInterval(Integer chartInterval) {
        this.chartInterval = chartInterval;
    }

    public Integer getChartInterval() {
        return this.chartInterval;
    }

    public Integer getStatisticCollectionId() {
        return statisticCollectionId;
    }

    public void setStatisticCollectionId(Integer statisticCollectionId) {
        this.statisticCollectionId = statisticCollectionId;
    }

    public boolean isMultigraphCalculateGravitationPerReferenceId() {
        return multigraphCalculateGravitationPerReferenceId;
    }

    public void setMultigraphCalculateGravitationPerReferenceId(boolean multigraphCalculateGravitationPerReferenceId) {
        this.multigraphCalculateGravitationPerReferenceId = multigraphCalculateGravitationPerReferenceId;
    }

    public boolean isReturnOriginId() {
        return returnOriginId;
    }

    public void setReturnOriginId(boolean returnOriginId) {
        this.returnOriginId = returnOriginId;
    }

    public void setValuesGeometryAggregation(String valuesGeometryAggregation) {
        this.valuesGeometryAggregation = valuesGeometryAggregation;
    }

    public String getValuesGeometryAggregation() {
        return this.valuesGeometryAggregation;
    }

    public void setRoutingAggregationType(RoutingAggregationType routingAggregationType) {
        this.routingAggregationType = routingAggregationType;
    }

    public RoutingAggregationType getRoutingAggregationType() { return this.routingAggregationType; }

    public Boolean getMultiGraphIgnoreRoutingErrorMessages() {
        return multiGraphIgnoreRoutingErrorMessages;
    }

    public void setMultiGraphIgnoreRoutingErrorMessages(Boolean multiGraphIgnoreRoutingErrorMessages) {
        this.multiGraphIgnoreRoutingErrorMessages = multiGraphIgnoreRoutingErrorMessages;
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
        builder.append("\n}\n");
        return builder.toString();
    }
}
