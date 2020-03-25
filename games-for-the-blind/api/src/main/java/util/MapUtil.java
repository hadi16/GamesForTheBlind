package util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static <K, V> Map.Entry<K, V> entry(@NotNull K key, @NotNull V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    @SafeVarargs
    public static <K, V> Map<K, V> map(@NotNull Map.Entry<K, V>... entries) {
        final HashMap<K, V> hashMap = new HashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(hashMap);
    }
}
