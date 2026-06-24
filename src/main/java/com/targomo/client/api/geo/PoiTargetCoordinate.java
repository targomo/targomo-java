package com.targomo.client.api.geo;

import com.targomo.client.api.statistic.PoiType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by gerb on 24/03/2017.
 */
@Getter @Setter
public class PoiTargetCoordinate extends DefaultTargetCoordinate {

    private PoiType type;

    public PoiTargetCoordinate(String id, double x, double y) {
        super(id,x,y);
    }
}
