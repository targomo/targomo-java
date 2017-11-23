package net.motionintelligence.client.api.response.refactored;

import java.util.Map;

public class MultiGraphResponse extends DefaultResponse<Map<String,Object>,Map<String,Object>> {

    @Override
    protected Map<String,Object> parseData(Map<String, Object> jacksonData) {
        return jacksonData;
    }
}
