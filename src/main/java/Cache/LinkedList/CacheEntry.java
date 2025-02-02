package Cache.LinkedList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Builder
@Getter
@Setter
public class CacheEntry {
    private String key;
    private Object value;
    private Instant ttl;
    private CacheEntry next;
    private CacheEntry prev;
}
