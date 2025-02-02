package Cache.db;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class DbEntry {
    private String key;
    private Object value;
    private Instant ttl;
}
