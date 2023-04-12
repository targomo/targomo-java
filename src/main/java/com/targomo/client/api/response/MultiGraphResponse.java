package com.targomo.client.api.response;

import com.targomo.client.api.pojo.MultiGraph;
import org.wololo.geojson.FeatureCollection;

import java.util.Map;

/**
 * Since two options are available both can be used accordingly. <p>
 * For Geojson
 * <pre>
 *     MultiGraphResponse{@literal <}FeatureCollection{@literal >} response = ...
 * </pre>
 * For json:
 * <pre>
 *     MultiGraphResponse{@literal <}MultiGraph{@literal >} response = ...
 * </pre>
 *
 * Not implemented so far is a Response for {@link com.targomo.client.api.enums.MultiGraphSerializationFormat#MAPBOX_VECTOR_TILES}
 * since it is commonly not used by java clients.
 * @param <R>
 */
public class MultiGraphResponse<R> extends DefaultResponse<R,R> {

    @Override
    protected R parseData(R jacksonData) {
        return jacksonData;
    }

    ////////////////////////////////////// Response Classes ///////////////////////////////////////////////

    public static class MultiGraphJsonResponse extends MultiGraphResponse<MultiGraph> {}

    public static class MultiGraphGeoJsonResponse extends MultiGraphResponse<FeatureCollection> {}

    public static class MultiGraphH3JsonResponse<T> extends MultiGraphResponse<Map<T, Map<String, Double>>> {}
}
