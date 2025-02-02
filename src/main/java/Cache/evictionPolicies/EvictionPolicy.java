package Cache.evictionPolicies;

import Cache.LinkedList.CacheEntry;

public interface EvictionPolicy {
    void cacheEntryAccessed(CacheEntry entry);

    CacheEntry evictLeastRecentyUsed();

    boolean evictEntry(CacheEntry entry);
    public Integer getCurrentCacheSize();

}
