package com.targomo.client.api.quality.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targomo.client.api.pojo.LocationProperties;
import com.targomo.client.api.quality.Location;
import com.targomo.core.exception.RequestConfigurationException;
import com.targomo.core.gis.projection.ProjectionUtil;
import lombok.SneakyThrows;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * Custom deserializer for the Location object. It has three potential constructors
 *
 * In the case that just a lat/lng is given the location is considered a point.
 * In the case that just geometry and crs is given the location is a geometry and uses the geometry centroid for lat/lng.
 * In the that all parameters are set then the location is still treated as a geometry but uses the the lat lng for all
 * quality service requirements (e.g. cache keys or haversine distance calculations)
 */
public class LocationDeserializer extends JsonDeserializer<Location> {

    private static final int DEFAULT_CRS = 4326;
    private static final int DEFAULT_DECIMAL_PRECISION = 8;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    @Override
    public Location deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {

        JsonNode locationJson = jsonParser.getCodec().readTree(jsonParser);
        String id = locationJson.get("id").asText();
        Location.LocationBuilder<?, ?> builder = Location.builder();
        builder.id(id);

        JsonNode latJson = locationJson.get("lat");
        JsonNode lngJson = locationJson.get("lng");
        JsonNode geometryJson = locationJson.get("geometry");

        if (geometryJson != null) {
            int crs = locationJson.get("crs") == null ? DEFAULT_CRS : locationJson.get("crs").asInt();
            deserializeGeometry(builder, id, latJson, lngJson, geometryJson, crs);
        } else {
            deserializePoint(builder, id, latJson, lngJson);
        }

        LocationProperties properties = locationJson.get("properties") == null ? null :
                MAPPER.readValue(locationJson.get("properties").toString(), LocationProperties.class);

        return builder.properties(properties).build();
    }

    private void deserializePoint(Location.LocationBuilder<?, ?> builder, String id, JsonNode latJson, JsonNode lngJson) {
        if (latJson == null || lngJson == null)
            throw new RequestConfigurationException(String.format(
                    "Location with id '%s' has neither geometry nor valid lat/lng pair defined", id));

        builder.lat(latJson.asDouble()).lng(lngJson.asDouble())
                .point(true);
    }

    private void deserializeGeometry(Location.LocationBuilder<?, ?> builder, String id, JsonNode latJson, JsonNode lngJson, JsonNode geometryJson, int crs) throws java.io.IOException, TransformException, FactoryException {
        String geojsonString = geometryJson.toString();

        Geometry geom = new GeometryJSON(DEFAULT_DECIMAL_PRECISION).read(geometryJson.toString());
        //If geometry is not in 4326, transform it and update the crs
        if (DEFAULT_CRS == crs) {
            geom = ProjectionUtil.transformGeometry(geom, crs, DEFAULT_CRS);
            geojsonString = new GeometryJSON(DEFAULT_DECIMAL_PRECISION).toString(geom);
            crs = DEFAULT_CRS;
        }

        //If lat/lng not set get from geometry centroid otherwise use lat/lng set in json
        if (latJson == null || lngJson == null) {
            builder.lat(geom.getCentroid().getY()).lng(geom.getCentroid().getX());
        } else {
            builder.lat(latJson.asDouble()).lng(lngJson.asDouble());
        }

        GeoJsonObject geometry = MAPPER.readValue(geojsonString, GeoJsonObject.class);
        if (!validGeometryType(geometry))
            throw new RequestConfigurationException(String.format(
                    "Location with id '%s' has geometry of invalid type. Should be only LineString, Polygon " +
                            "or MultiPolygon", id));
        builder.geometry(geometry).crs(crs).point(false);
    }

    /**
     * Returns true if the geometry is one of the valid types for locations
     *
     * Valid types: LineString, Polygon, Multipolygon
     * @param geometry geometry to be checked.
     * @return true if geometry is a LineString, Polygon or Multipolygon
     */
    private boolean validGeometryType(GeoJsonObject geometry) {
        return LineString.class.equals(geometry.getClass()) ||
                Polygon.class.equals(geometry.getClass()) ||
                MultiPolygon.class.equals(geometry.getClass());
    }
}
