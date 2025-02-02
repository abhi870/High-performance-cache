package Cache.evictionPolicies;

import Cache.LinkedList.CacheEntry;
import Cache.LinkedList.DoublyLinkedList;
import lombok.Builder;

@Builder
public class LRUEvictionPolicy implements EvictionPolicy {
    private final DoublyLinkedList doublyLinkedList;

    @Override
    public void cacheEntryAccessed(CacheEntry entry) {
        if (doublyLinkedList.getSize() != 0) {
            doublyLinkedList.remove(entry);
        }
        doublyLinkedList.addLatestEntry(entry);
    }

    @Override
    public CacheEntry evictLeastRecentyUsed() {
        if (doublyLinkedList.getSize() != 0) {
            return doublyLinkedList.removeLeastUsedEntry();
        }
        return null;
    }

    @Override
    public boolean evictEntry(CacheEntry entry) {
        if (doublyLinkedList.getSize() != 0) {
            return doublyLinkedList.remove(entry);
        }
        return true;
    }

    public Integer getCurrentCacheSize() {
        return doublyLinkedList.getSize();
    }
}
