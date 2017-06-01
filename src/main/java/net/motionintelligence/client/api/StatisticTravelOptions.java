package net.motionintelligence.client.api;

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

    /**
     *
     * @return
     */
    public Map<String,Coordinate> getInactiveSources() {
        return this.inactiveSources;
    }

    /**
     *
     * @param inactiveSources
     */
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
        return inactiveSources != null ? inactiveSources.equals(that.inactiveSources) : that.inactiveSources == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (inactiveSources != null ? inactiveSources.hashCode() : 0);
        result = 31 * result + (useCache ? 1 : 0);
        return result;
    }

    public boolean isGetClosestSources() {
        return getClosestSources;
    }

    public void setGetClosestSources(boolean getClosestSources) {
        this.getClosestSources = getClosestSources;
    }
}
