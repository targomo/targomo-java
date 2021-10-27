package com.targomo.client.api.request.enums;

/**
 * Created by gerb on 01/03/2017.
 */
public enum StatisticMethod {

    CHARTS_DEPENDENT("charts/dependent"),
    CHARTS_INDEPENDENT("charts/independent");

    private final String path;

    StatisticMethod(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
