package com.targomo.client.api.geo;

import com.targomo.client.api.statistic.PoiType;

/**
 * Created by gerb on 24/03/2017.
 */
public class PoiTargetCoordinate extends DefaultTargetCoordinate {

    private PoiType type;

    public PoiTargetCoordinate(String id, double x, double y) {
        super(id,x,y);
    }

    public PoiType getType() {
        return type;
    }

    public void setType(PoiType type) {
        this.type = type;
    }
}
