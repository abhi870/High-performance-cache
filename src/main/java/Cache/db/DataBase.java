package Cache.db;

import Cache.LinkedList.CacheEntry;

public interface DataBase {
    DbEntry get(String key);

    DbEntry put(DbEntry entry);

    void remove(String key);
}
