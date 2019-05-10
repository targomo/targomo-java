package com.targomo.client.api.util;

import com.targomo.client.api.exception.TargomoClientRuntimeException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class CollectionUtils {
    public static <K, V> Map<K, V> map(Object... objects) {
        if (objects == null || objects.length % 2 != 0) {
            throw new TargomoClientRuntimeException("Key or value is missing");
        }
        Map<K, V> map = new LinkedHashMap<>();
        for (int i = 0; i < objects.length; i += 2) {
            map.put((K) objects[i], (V) objects[i + 1]);
        }
        return map;
    }

    public static <K, V> Map<K, V> map(K[] keys, V[] values) {
        Map<K, V> map = new LinkedHashMap<>(keys.length);
        int index = 0;
        for (K key : keys) {
            if (index < keys.length) {
                V value = values[index];
                map.put(key, value);
            } else {
                map.put(key, null);
            }
            index++;
        }
        return map;
    }

    @SafeVarargs
    public static <V> NavigableSet<V> safeSortedSet(V... array) {
        NavigableSet<V> set = new ConcurrentSkipListSet();
        for (int i = 0; i < array.length; i++) {
            V v = array[i];
            set.add(v);
        }
        return set;
    }
}
