package com.targomo.client.api.geo;

import com.targomo.client.api.enums.TravelType;
import com.targomo.client.api.pojo.AggregationInputParameters;

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

    private DefaultSourceGeometry(){}

    /**
     * Generate Source geometry with a TravelType as well as ID, geojson and crs values.
     * @param id ID to associate with the target coordinate
     * @param geojson String of the geojson of the object
     * @param crs CRS value used for the geometry
     * @param travelType TravelType to be associated with the coordinate
     * @
     */
    public DefaultSourceGeometry(String id, String geojson, int crs, TravelType travelType, AggregationInputParameters aggregationInputParameters) {
        super(id, crs, geojson, aggregationInputParameters);
        this.travelType = travelType;
    }

    /**
     * Generate Source geometry with a TravelType as well as ID, geojson and crs values.
     * @param id ID to associate with the target coordinate
     * @param geojson String of the geojson of the object
     * @param crs CRS value used for the geometry
     * @param travelType TravelType to be associated with the coordinate
     */
    public DefaultSourceGeometry(String id, String geojson, int crs, TravelType travelType) {
        this(id, geojson, crs, travelType, null);
    }

    /**
     * Generate Source geometry with ID, geojson and crs values with no travel type.
     * Travel type will be set to null.
     * @param id ID to associate with the target coordinate
     * @param geojson Geojson String of the source geometry
     * @param crs CRS value used for the geometry
     */
    public DefaultSourceGeometry(String id, String geojson, int crs) {
        this(id, geojson, crs, null, null);
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
        builder.append(", data: ");
        builder.append(getData());
        builder.append(", crs: ");
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
