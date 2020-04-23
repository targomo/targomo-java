package com.targomo.client.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class is responsible for storing the distance and time taken to route between a source and target
 * @author Mario Freitas
 */
@Getter
@AllArgsConstructor
public class TravelWeight {

    private final int travelDistance;
    private final int travelTime;

}