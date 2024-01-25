package com.targomo.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoundingBoxResponse {

    Double minX;
    Double maxX;
    Double minY;
    Double maxY;

    private final long requestTimeMillis;
}
