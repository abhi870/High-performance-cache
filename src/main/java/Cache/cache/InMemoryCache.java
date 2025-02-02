package Cache.cache;

import Cache.LinkedList.CacheEntry;
import Cache.adaptor.DatabaseAdaptor;
import Cache.db.DataBase;
import Cache.exceptions.CacheEntryExpiredException;
import Cache.evictionPolicies.EvictionPolicy;
import Cache.exceptions.InvalidKeyException;
import Cache.expiration.ExpirationStrategy;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Builder
public class InMemoryCache implements Cache {
    private final ConcurrentHashMap<String, CacheEntry> storage;
    private final EvictionPolicy evictionPolicy;
    private final DatabaseAdaptor databaseAdaptor;
    private final ExpirationStrategy expirationStrategy;
    private final Integer capacity;

    @Override
    public boolean put(String key, String value) {
        try {
            validateKey(key);
            CacheEntry newEntry = buildCacheEntry(key, value);
            Object o = new Object();

                // evict if capacity full
                if (evictionPolicy.getCurrentCacheSize().equals(capacity)) {
                    CacheEntry leastRecentlyUsed = evictionPolicy.evictLeastRecentyUsed();
                    storage.remove(leastRecentlyUsed.getKey());
                    databaseAdaptor.remove(leastRecentlyUsed.getKey());
                }

            storage.put(key, newEntry);
            databaseAdaptor.save(newEntry);
            evictionPolicy.cacheEntryAccessed(newEntry);
            return true;
        } catch (Exception e) {
            System.out.println("Error occurred while adding to cache.");
            return false;
        }
    }

    @Override
    public String get(String key) {
        try {
            validateKey(key);
            CacheEntry entry = storage.get(key);

            if (entry == null) {
                entry = databaseAdaptor.get(key);
            }
            if (expirationStrategy.isExpired(entry.getTtl())) {
                entry = databaseAdaptor.get(key);
            }

            evictionPolicy.cacheEntryAccessed(entry);
            return (String) entry.getValue();
        } catch (Exception e) {
            System.out.println("Error occurred while fetching value : " + e.getMessage());
            return null;
        }

    }


    @Override
    public boolean remove(String key) {
        try {
            validateKey(key);
            CacheEntry entry = storage.remove(key);
            databaseAdaptor.remove(key);
            evictionPolicy.evictEntry(entry);
            return true;
        } catch (Exception e) {
            System.out.println("Error while removing entry Cache. + " + e.getMessage());
            return false;
        }
    }

    private void validateKey(String key) {
        if (StringUtils.isBlank(key)) {
            throw new InvalidKeyException("Invalid key");
        }
    }

    private CacheEntry buildCacheEntry(final String key, final String value) {
        return CacheEntry.builder()
                .key(key)
                .value(value)
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
    }
}
