package net.motionintelligence.client.api.request;

import net.motionintelligence.client.api.exception.Route360ClientException;

/**
 * Created by David on 18.07.2017.
 */
@FunctionalInterface
public interface GetRequest<T, R> {
    R get(T t) throws Route360ClientException;
}
