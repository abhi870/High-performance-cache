import Cache.LinkedList.CacheEntry;
import Cache.LinkedList.DoublyLinkedList;
import Cache.adaptor.DatabaseAdaptor;
import Cache.adaptor.KvDatabaseAdaptor;
import Cache.cache.InMemoryCache;
import Cache.db.DataBase;
import Cache.db.DbEntry;
import Cache.db.KvDatabase;
import Cache.evictionPolicies.EvictionPolicy;
import Cache.evictionPolicies.LRUEvictionPolicy;
import Cache.expiration.ExpirationStrategy;
import Cache.expiration.TTLExpirationStrategy;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryCacheTest {


    @Test
    public void testGet_noValuePresent() {
        DataBase database = KvDatabase.builder()
                .build();
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap(10);

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        String value = inMemoryCache.get("abc");
        assertNull(value);
    }

    @Test
    public void testGet_valuePresent() {
        DataBase database = KvDatabase.builder()
                .build();
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap(10);

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        inMemoryCache.put("phonepe", "upi");
        String value = inMemoryCache.get("phonepe");
        assertEquals("upi", value);
    }

    @Test
    public void testGet_updateValue() {
        DataBase database = KvDatabase.builder()
                .build();
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap(10);

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        inMemoryCache.put("phonepe", "upi");
        inMemoryCache.put("phonepe", "ipu");
        String value = inMemoryCache.get("phonepe");
        assertEquals("ipu", value);
    }

    @Test
    public void test_cacheMiss_dbHit() {
        DataBase database = KvDatabase.builder()
                .build();
        database.put(DbEntry.builder()
                .key("paytm")
                .value("upi")
                .ttl(Instant.now())
                .build());
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap(10);

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        assertEquals("upi", inMemoryCache.get("paytm"));
    }

    @Test
    public void test_valueExpired_fetchFromDb() {
        DataBase database = KvDatabase.builder()
                .build();
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap(10);

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        inMemoryCache.put("paytm", "100");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        database.put(DbEntry.builder()
                .key("paytm")
                .value("10000000")
                .ttl(Instant.now())
                .build());
        assertEquals("10000000", inMemoryCache.get("paytm"));
    }

    @Test
    public void test_valueNotExpiredBeforeExpiryTime() {
        DataBase database = KvDatabase.builder()
                .build();
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap(10);

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        inMemoryCache.put("paytm", "100");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        database.put(DbEntry.builder()
                .key("paytm")
                .value("10000000")
                .ttl(Instant.now())
                .build());
        assertEquals("100", inMemoryCache.get("paytm"));
    }


    @Test
    public void test_putMoreThanMaxSizeConcurrent() {
        DataBase database = KvDatabase.builder()
                .build();
        DatabaseAdaptor databaseAdaptor = KvDatabaseAdaptor.builder()
                .database(database)
                .build();
        EvictionPolicy evictionPolicy = LRUEvictionPolicy.builder()
                .doublyLinkedList(new DoublyLinkedList())
                .build();
        ExpirationStrategy expirationStrategy = TTLExpirationStrategy.builder()
                .duration(Duration.of(5, ChronoUnit.SECONDS))
                .build();
        ConcurrentHashMap<String, CacheEntry> concurrentHashMap = new ConcurrentHashMap();

        InMemoryCache inMemoryCache = InMemoryCache.builder()
                .storage(concurrentHashMap)
                .evictionPolicy(evictionPolicy)
                .databaseAdaptor(databaseAdaptor)
                .expirationStrategy(expirationStrategy)
                .capacity(5)
                .build();
        Runnable r1 = () -> {
            inMemoryCache.put("r1", "r1Value");
        };
        Runnable r2 = () -> {
            inMemoryCache.put("r2", "r2Value");
        };
        Runnable r3 = () -> {
            inMemoryCache.put("r3", "r3Value");
        };
        Runnable r4 = () -> {
            inMemoryCache.put("r4", "r4Value");
        };
        Runnable r5 = () -> {
            inMemoryCache.put("r5", "r2Value");
        };
        Runnable r6 = () -> {
            inMemoryCache.put("r6", "r6Value");
        };
        Runnable r7 = () -> {
            inMemoryCache.put("r7", "r7Value");
        };
        Runnable r8 = () -> {
            inMemoryCache.put("r8", "r8Value");
        };
        Runnable r9 = () -> {
            inMemoryCache.put("r9", "r9Value");
        };
        Runnable r10 = () -> {
            inMemoryCache.put("r10", "r10Value");
        };

        Runnable r100 = () -> {
            inMemoryCache.put("r100", "r100Value");
        };
        Runnable r20 = () -> {
            inMemoryCache.put("r20", "r20Value");
        };
        Runnable r30 = () -> {
            inMemoryCache.put("r30", "r30Value");
        };
        Runnable r40 = () -> {
            inMemoryCache.put("r40", "r40Value");
        };
        Runnable r50 = () -> {
            inMemoryCache.put("r50", "r50Value");
        };
        Runnable r60 = () -> {
            inMemoryCache.put("r60", "r60Value");
        };
        Runnable r70 = () -> {
            inMemoryCache.put("r70", "r70Value");
        };
        Runnable r80 = () -> {
            inMemoryCache.put("r80", "r80Value");
        };
        Runnable r90 = () -> {
            inMemoryCache.put("r90", "r90Value");
        };
        Runnable r101 = () -> {
            inMemoryCache.put("r101", "r101Value");
        };

        r1.run();
        r2.run();
        r3.run();
        r4.run();
        r5.run();
        r6.run();
        r7.run();
        r8.run();
        r9.run();
        r10.run();
        r100.run();
        r101.run();
        r20.run();
        r30.run();
        r40.run();
        r50.run();
        r60.run();
        r70.run();
        r80.run();
        r90.run();
        r3.run();
        r4.run();
        r5.run();

        assertEquals(Integer.valueOf(5), evictionPolicy.getCurrentCacheSize());
    }

}
