package com.targomo.client.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.geo.Coordinate;
import com.targomo.client.api.response.parsingpojos.ElementWithTargets;
import com.targomo.client.api.response.parsingpojos.TransitTravelTimes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TimeVectorResponse extends DefaultResponse<List<ElementWithTargets<TransitTravelTimes>>, List<ElementWithTargets<TransitTravelTimes>>>{

    @JsonIgnore
    private Map<Coordinate,Map<Coordinate,TransitTravelTimes>> travelTimeVectors = null;

    @Override
    protected List<ElementWithTargets<TransitTravelTimes>> parseData(List<ElementWithTargets<TransitTravelTimes>> jacksonData) {
        return jacksonData;
    }

    public Map<Coordinate,Map<Coordinate,TransitTravelTimes>> getTravelTimeVectors(){
        if(travelTimeVectors != null)
            return travelTimeVectors;

        travelTimeVectors = new HashMap<>();
        if(data != null) {
            final TravelOptions travelOptions = this.getTravelOptions();
            for (ElementWithTargets<TransitTravelTimes> travelTimesOfSource : data) {

                Map<Coordinate,TransitTravelTimes> mapOfTravelTimesOfSource =
                        travelTimesOfSource.getTargets().stream().collect(Collectors.toMap(
                                tTO -> travelOptions.getTarget(tTO.getId()), //key becomes coordinate
                                Function.identity() )); //value is the travel time vector result

                //adding the targets to the map
                travelTimeVectors.put(travelOptions.getSource(travelTimesOfSource.getId()), mapOfTravelTimesOfSource);
            }
        }
        return travelTimeVectors;
    }
}
