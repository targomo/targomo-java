package com.targomo.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.geo.DefaultSourceCoordinate;
import com.targomo.client.api.json.DefaultSourceCoordinateMapDeserializer;
import com.targomo.client.api.json.DefaultSourceCoordinateMapSerializer;

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

    @Column(name = "useCache")
    private boolean useCache = true;

    @Column(name = "iFeelLucky")
    private boolean iFeelLucky = false;

    @Column(name = "get_closest_sources")
    private boolean getClosestSources = false;

    @Column(name = "omit_individual_statistics")
    private boolean omitIndividualStatistics = false;

    @Transient
    private List<Integer> cellIds = new ArrayList<>();

    @Transient
    private Map<String,Short> multiGraphReferencedStatisticIds = null;

    @Transient
    private Integer multiGraphDomainStatisticGroupId = null;

    @Transient
    private Boolean multiGraphLayerUnboundedStatistics = null;

    @Transient
    private List<Short> statisticIds;

    public Map<String,Coordinate> getInactiveSources() {
        return this.inactiveSources;
    }

    public void setInactiveSources(Map<String,Coordinate> inactiveSources) {
        this.inactiveSources = inactiveSources;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public boolean isiFeelLucky() {
        return iFeelLucky;
    }

    public void setiFeelLucky(boolean iFeelLucky) {
        this.iFeelLucky = iFeelLucky;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticTravelOptions)) return false;

        StatisticTravelOptions that = (StatisticTravelOptions) o;

        return super.equals(o) &&
                Objects.equals(useCache, that.useCache) &&
                Objects.equals(iFeelLucky, that.iFeelLucky) &&
                Objects.equals(getClosestSources, that.getClosestSources) &&
                Objects.equals(omitIndividualStatistics, that.omitIndividualStatistics) &&
                Objects.equals(inactiveSources, that.inactiveSources) &&
                Objects.equals(cellIds, that.cellIds) &&
                Objects.equals(multiGraphDomainStatisticGroupId, that.multiGraphDomainStatisticGroupId) &&
                Objects.equals(multiGraphLayerUnboundedStatistics, that.multiGraphLayerUnboundedStatistics) &&
                Objects.equals(multiGraphReferencedStatisticIds, that.multiGraphReferencedStatisticIds) &&
                Objects.equals(statisticIds, that.statisticIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), inactiveSources, useCache, iFeelLucky, getClosestSources,
                omitIndividualStatistics, cellIds, multiGraphDomainStatisticGroupId,
                multiGraphLayerUnboundedStatistics, multiGraphReferencedStatisticIds,
                statisticIds);
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

    public Boolean getMultiGraphLayerUnboundedStatistics() {
        return multiGraphLayerUnboundedStatistics;
    }

    public void setMultiGraphLayerUnboundedStatistics(Boolean multiGraphLayerUnboundedStatistics) {
        this.multiGraphLayerUnboundedStatistics = multiGraphLayerUnboundedStatistics;
    }

    public void setStatisticIds(List<Short> statisticIds) {
        this.statisticIds = statisticIds ;
    }

    public List<Short> getStatisticIds() {
        return this.statisticIds;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(getClass().getName());
        builder.append("\n\tinactiveSources: ");
        builder.append(Arrays.toString(inactiveSources.entrySet().toArray()));
        builder.append("\n\tuseCache: ");
        builder.append(useCache);
        builder.append("\n\tiFeelLucky: ");
        builder.append(iFeelLucky);
        builder.append("\n\tomitIndividualStatistics: ");
        builder.append(omitIndividualStatistics);
        builder.append("\n\tcellIds: ");
        builder.append(cellIds!= null ? cellIds.toString() : "[]");
        builder.append("\n\tmultiGraphReferencedStatisticIds: ");
        builder.append(multiGraphReferencedStatisticIds != null ? multiGraphReferencedStatisticIds.toString() : "[]");
        builder.append("\n\tmultiGraphDomainStatisticGroupId: ");
        builder.append(multiGraphDomainStatisticGroupId);
        builder.append("\n\tmultiGraphLayerUnboundedStatistics: ");
        builder.append( multiGraphLayerUnboundedStatistics );
        builder.append("\n\tstatisticIds: ");
        builder.append(statisticIds != null ? statisticIds.toString() : "[]");
        builder.append("\n}\n");
        return builder.toString();
    }

}
