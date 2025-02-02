package Cache.adaptor;

import Cache.LinkedList.CacheEntry;
import Cache.db.DataBase;
import Cache.db.DbEntry;
import Cache.exceptions.NotFoundException;
import lombok.Builder;

@Builder
public class KvDatabaseAdaptor implements DatabaseAdaptor {
    private final DataBase database;

    @Override
    public CacheEntry get(String key) {
        DbEntry entry = database.get(key);
        if (entry == null) {
            throw new NotFoundException("entry not found");
        }
        return CacheEntry.builder()
                .key(entry.getKey())
                .value(entry.getValue())
                .ttl(entry.getTtl())
                .prev(null)
                .next(null)
                .build();
    }

    @Override
    public DbEntry save(CacheEntry entry) {

        DbEntry dbEntry = DbEntry.builder()
                .key(entry.getKey())
                .value(entry.getValue())
                .ttl(entry.getTtl())
                .build();
        return database.put(dbEntry);
    }

    @Override
    public void remove(String key) {
        database.remove(key);
    }
}
