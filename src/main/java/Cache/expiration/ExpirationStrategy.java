package Cache.expiration;

import java.time.Instant;

public interface ExpirationStrategy {

    boolean isExpired(Instant entryTime);
}
