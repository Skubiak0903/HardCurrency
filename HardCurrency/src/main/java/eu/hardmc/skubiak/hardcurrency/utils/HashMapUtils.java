package eu.hardmc.skubiak.hardcurrency.utils;

import java.util.Map;

public class HashMapUtils {
	
    public static <K, V> K getKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; // No key found for the given value
    }
}
