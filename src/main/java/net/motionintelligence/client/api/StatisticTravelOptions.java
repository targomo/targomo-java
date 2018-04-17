package net.motionintelligence.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.motionintelligence.client.api.geo.Coordinate;
import net.motionintelligence.client.api.geo.DefaultSourceCoordinate;
import net.motionintelligence.client.api.json.DefaultSourceCoordinateMapDeserializer;
import net.motionintelligence.client.api.json.DefaultSourceCoordinateMapSerializer;

import javax.persistence.*;
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

    @Transient
    private List<Integer> cellIds = new ArrayList<>();

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
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StatisticTravelOptions that = (StatisticTravelOptions) o;

        if (useCache != that.useCache) return false;
        if (iFeelLucky != that.iFeelLucky) return false;
        if (getClosestSources != that.getClosestSources) return false;
        if (inactiveSources != null ? !inactiveSources.equals(that.inactiveSources) : that.inactiveSources != null)
            return false;
        return cellIds != null ? cellIds.equals(that.cellIds) : that.cellIds == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (inactiveSources != null ? inactiveSources.hashCode() : 0);
        result = 31 * result + (useCache ? 1 : 0);
        result = 31 * result + (iFeelLucky ? 1 : 0);
        result = 31 * result + (getClosestSources ? 1 : 0);
        result = 31 * result + (cellIds != null ? cellIds.hashCode() : 0);
        return result;
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
}
