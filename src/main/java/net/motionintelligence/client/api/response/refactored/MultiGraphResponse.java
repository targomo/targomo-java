package net.motionintelligence.client.api.response.refactored;

import net.motionintelligence.client.api.model.MultiGraph;
import org.wololo.geojson.FeatureCollection;

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
 * @param <R>
 */
public class MultiGraphResponse<R> extends DefaultResponse<R,R> {

    @Override
    protected R parseData(R jacksonData) {
        return jacksonData;
    }

    public static class MultiGraphJsonResponse extends MultiGraphResponse<MultiGraph> {}

    public static class MultiGraphGeoJsonResponse extends MultiGraphResponse<FeatureCollection> {}
}
