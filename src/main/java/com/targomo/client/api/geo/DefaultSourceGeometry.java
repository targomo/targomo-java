package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Default implementation for storing source geometries.
 * Basically a {@link AbstractGeometry} with TravelType, specialized to be used as a source.
 *
 * @author gideon
 */
@Entity
@Table(name="source_geometry")
public class DefaultSourceGeometry extends AbstractGeometry {

    @Id
    @Column(name = "identifier")
    @GeneratedValue(strategy= GenerationType.TABLE)
    private long identifier;

    @Column(name = "travel_type")
    private TravelType travelType;

    // needed for jackson
    public DefaultSourceGeometry(){}

    /**
     * Generate Source geometry with a TravelType as well as ID, geojson and crs values.
     * @param id ID to associate with the target coordinate
     * @param geojson Geojson String of the source geometry
     * @param crs CRS value used for the geometry
     * @param travelType TravelType to be associated with the coordinate
     */
    public DefaultSourceGeometry(String id, JSONObject geojson, int crs, TravelType travelType) {
        super(id, geojson, crs);
        this.travelType = travelType;
    }

    /**
     * Generate Source geometry with ID, geojson and crs values with no travel type.
     * Travel type will be set to null.
     * @param id ID to associate with the target coordinate
     * @param geojson Geojson String of the source geometry
     * @param crs CRS value used for the geometry
     */
    public DefaultSourceGeometry(String id, JSONObject geojson, int crs) {
        this(id, geojson, crs, null);
    }

    /**
     * Get travel type configuration for the source geometry.
     * @return Travel type
     */
    @Override
    public TravelType getTravelType() {
        return travelType;
    }

    /**
     * The main problem with this identifier is that we need it for hibernate
     * since it's not able to work without an ID. But source geometries have
     * per se no real identifier since the same geometry could come from multiple
     * clients and have different lat/lng/traveltype.
     *
     * @return this database unique identifier of this source point
     */
    public long getIdentifier() { return identifier; }

    public void setIdentifier(long id) { this.identifier = id; }

    /**
     * Specify a travel type for the source geometry.
     * @param travelType TravelType to be associated with the source geometry.
     */
    @Override
    public void setTravelType(final TravelType travelType) {
        this.travelType = travelType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append(" { id: ");
        builder.append(getId());
        builder.append(", geojson: ");
        builder.append(getGeojson());
        builder.append(", y: ");
        builder.append(getCrs());
        builder.append(", travelType: ");
        builder.append(travelType);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DefaultSourceGeometry that = (DefaultSourceGeometry) o;

        return travelType == that.travelType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (travelType != null ? travelType.hashCode() : 0);
        return result;
    }
}
