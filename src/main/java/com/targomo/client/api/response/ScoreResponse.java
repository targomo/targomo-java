package com.targomo.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

@AllArgsConstructor
@Setter
@Getter
public class ScoreResponse {
    private final JSONObject data;
    private String message;
    private JSONArray errors;
    private DateTime timestamp;
}
