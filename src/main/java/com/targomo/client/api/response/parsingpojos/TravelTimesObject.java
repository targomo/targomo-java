package com.targomo.client.api.response.parsingpojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class TravelTimesObject<A> extends IdObject {

    @Getter @Setter
    private A travelTimes;
}
