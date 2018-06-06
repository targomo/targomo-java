package com.targomo.client.api.request;

import com.targomo.client.api.exception.TargomoClientException;

/**
 * Created by David on 18.07.2017.
 */
@FunctionalInterface
public interface GetRequest<T, R> {
    R get(T t) throws TargomoClientException;
}
