package net.motionintelligence.client.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wololo.geojson.FeatureCollection;

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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GeojsonUtil {

    private static final Logger LOGGER        = LoggerFactory.getLogger(GeojsonUtil.class);

    public static void openGeoJsonInBrowserWithGeojsonIO(Map<String,FeatureCollection> featureCollections) throws IOException {
        //(1) Build request
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fileMap = new HashMap<>(featureCollections.size());
        for( Map.Entry<String,FeatureCollection> entry : featureCollections.entrySet() ){
            String fileName = entry.getKey().endsWith(".geojson") ? entry.getKey() : entry.getKey() + ".geojson";
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
     * Has sometimes display errors - esp. if multiple FeatureCollections are to be shown in one page. In that case it is
     * suggested to use {@link GeojsonUtil#openGeoJsonInBrowserWithGeojsonIO}.
     *
     * @param featureCollections
     * @throws IOException
     */
    public static void openGeoJsonInBrowserWithGitHubGist(Map<String,FeatureCollection> featureCollections) throws IOException {
        //(1) Build request
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fileMap = new HashMap<>(featureCollections.size());
        for( Map.Entry<String,FeatureCollection> entry : featureCollections.entrySet() ){
            String fileName = entry.getKey().endsWith(".geojson") ? entry.getKey() : entry.getKey() + ".geojson";
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
