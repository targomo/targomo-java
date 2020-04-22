package com.targomo.client.api.pojo;

/**
 * @author Mario Freitas
 */

import lombok.AllArgsConstructor;

/**
 * This class is responsible for storing the distance and time taken to route between a source and target
 * @author Mario Freitas
 */
@AllArgsConstructor
public class TravelWeight {

    public final int travelDistance;
    public final int travelTime;

}