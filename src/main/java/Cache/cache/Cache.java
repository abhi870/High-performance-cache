package Cache.cache;

import Cache.LinkedList.CacheEntry;

public interface Cache {
    boolean  put(final String key, final String value);
    String get(final String key);
    boolean remove(final String key);
}
