package jbdl.session02.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enum singleton (Effective Java's recommendation): the JVM guarantees one instance, and it is safe
 * against reflection and serialization, which can create a second instance of the class-based kinds.
 */
public enum AppRegistry {
    INSTANCE;

    // shared by every caller, so it must be thread-safe
    private final Map<String, String> settings = new ConcurrentHashMap<>();

    /** Safe from many threads at once: the map is a ConcurrentHashMap. */
    public void put(String key, String value) {
        settings.put(key, value);
    }

    public String get(String key) {
        return settings.get(key);
    }
}
