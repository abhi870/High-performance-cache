package Cache.adaptor;

import Cache.LinkedList.CacheEntry;
import Cache.db.DbEntry;

public interface DatabaseAdaptor {
    public CacheEntry get(String key);

    public DbEntry save(CacheEntry entry);

    public void remove(String key);
}
