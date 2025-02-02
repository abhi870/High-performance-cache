package Cache.db;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class KvDatabase implements DataBase {
    private final Map<String, DbEntry> map = new HashMap<>();

    @Override
    public DbEntry get(String key) {
        return map.get(key);
    }

    @Override
    public DbEntry put(DbEntry entry) {
        map.put(entry.getKey(), entry);
        return entry;
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }
}
