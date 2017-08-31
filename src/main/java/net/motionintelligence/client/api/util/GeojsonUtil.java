package net.motionintelligence.client.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Utils for geojson. Currently supported methods:
 * <ul>
 *     <li>Transformation between geo formats</li>
 *     <li>Visualization of Geojson data in browser</li>
 * </ul>
 */
public class GeojsonUtil {

    private static final Logger LOGGER      = LoggerFactory.getLogger(GeojsonUtil.class);
    private static final String FILE_ENDING = ".geojson";

    private GeojsonUtil() {}

    /**
     * Transforms geojson geometry between different formats. Usage example:
     * <pre>
     * FeatureCollection featureCollectionIn3857 = ... //e.g. from the route service
     * //Preparing transformer
     * CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:3857"); //Web Mercartor
     * CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326"); //WGS84
     * MathTransform transformer = CRS.findMathTransform(sourceCRS, targetCRS);
     * //Do transformation from EPSG:3857 (Web Mercartor) to EPSG:4326 (WGS84)
     * FeatureCollection featureCollectionIn4326 = new FeatureCollection(
     *     GeojsonUtil.transformGeometry(featureCollectionIn3857,transformer).toArray(new Feature[0]));
     * </pre>
     *
     * @param transformer transformer initialized with the correct formats
     * @param featuresToTransform features that need to be transformed into the target format
     * @return Zero or more transformed geometry features
     * @throws TransformException if the geographic projection transformation fails
     */
    private List<Feature> transformGeometry(MathTransform transformer, Feature... featuresToTransform) throws TransformException {

        List<Feature> featureList = new ArrayList<>(featuresToTransform.length);
        for (Feature feature : featuresToTransform) {
            GeoJSONReader reader = new GeoJSONReader();
            Geometry geometry = reader.read(feature.getGeometry());
            geometry = JTS.transform(geometry, transformer);
            GeoJSONWriter writer = new GeoJSONWriter();
            org.wololo.geojson.Geometry json = writer.write(geometry);

            featureList.add(new Feature(json, feature.getProperties()));
        }
        return featureList;
    }

    /**
     * All filename-{@link org.wololo.geojson.Feature} pairs will be displayed in your default browser via the
     * geojson.io website. Requires connection to the internet. (Only EPSG:4326 (WGS84) mode tested)
     *
     * @param featureCollections map of fileNames to their respective contents (i.e. the {@link FeatureCollection})
     * @throws IOException if errors during parsing or execution occurred
     */
    public static void openGeoJsonInBrowserWithGeojsonIO(Map<String,FeatureCollection> featureCollections) throws IOException {
        //(1) Build request
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fileMap = new HashMap<>(featureCollections.size());
        for( Map.Entry<String,FeatureCollection> entry : featureCollections.entrySet() ){
            String fileName = entry.getKey().endsWith(FILE_ENDING) ? entry.getKey() : entry.getKey() + FILE_ENDING;
            String featureCollectionAsString = mapper.writeValueAsString(entry.getValue());
            fileMap.put(fileName, ImmutableMap.builder().put("content",featureCollectionAsString).build());
        }

        //For each tour generate a github gist that can be referenced from geojson.io
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/").path("gists");
        for(Map.Entry<String,Object> geojsonFile : fileMap.entrySet()) {
            //(2) Execute Request
            String requestAsString = mapper.writeValueAsString( ImmutableMap.builder().put("public",true).put("files", geojsonFile).build());
            Response response = target.request().buildPost(Entity.entity(requestAsString, MediaType.APPLICATION_JSON_TYPE)).invoke();
            String responseString = response.readEntity(String.class);

            //(3) Show result in browser
            String idString = Stream.of(responseString.split(","))
                    .filter(val -> val.startsWith("\"id\":\""))
                    .findFirst().orElseThrow(() -> new RuntimeException("Cannot happen"));
            displayURLInBrowser( "http://geojson.io/#id=gist:anonymous/" + idString.substring(6, idString.length() - 1));
        }
    }

    /**
     * All filename-{@link org.wololo.geojson.Feature} pairs will be displayed in your default browser via the
     * api.github.com/gists website. Requires connection to the internet. (Only EPSG:4326 (WGS84) mode tested)
     *
     * Has sometimes display errors - esp. if multiple FeatureCollections are to be shown in one page. In that case it is
     * suggested to use {@link GeojsonUtil#openGeoJsonInBrowserWithGeojsonIO}.
     *
     * @param featureCollections map of fileNames and the respective contents (i.e. the {@link FeatureCollection})
     * @throws IOException if errors during parsing or execution occurred
     */
    public static void openGeoJsonInBrowserWithGitHubGist(Map<String,FeatureCollection> featureCollections) throws IOException {
        //(1) Build request
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fileMap = new HashMap<>(featureCollections.size());
        for( Map.Entry<String,FeatureCollection> entry : featureCollections.entrySet() ){
            String fileName = entry.getKey().endsWith(FILE_ENDING) ? entry.getKey() : entry.getKey() + FILE_ENDING;
            String featureCollectionAsString = mapper.writeValueAsString(entry.getValue());
            fileMap.put(fileName, ImmutableMap.builder().put("content",featureCollectionAsString).build());
        }
        String requestAsString = mapper.writeValueAsString( ImmutableMap.builder().put("public",true).put("files", fileMap).build());

        //(2) Execute Request
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/").path("gists");
        Response response = target.request().buildPost(Entity.entity(requestAsString, MediaType.APPLICATION_JSON_TYPE) ).invoke();
        String responseString = response.readEntity(String.class);

        //(3) Show result in browser
        String url = Stream.of(responseString.split(","))
                .filter( val -> val.startsWith("\"html_url\":\"") )
                .findFirst().orElseThrow(() -> new RuntimeException("Cannot happen"));
        displayURLInBrowser( url.substring(12, url.length()-1) );
    }

    private static void displayURLInBrowser(String url) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (URISyntaxException e) {
                LOGGER.error("Error occurred while trying to open the browser", e);
            }
        } else
            Runtime.getRuntime().exec("xdg-open " + url);
    }
}
