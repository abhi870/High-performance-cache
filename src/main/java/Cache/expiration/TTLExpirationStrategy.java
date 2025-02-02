package Cache.expiration;

import lombok.Builder;

import java.time.Duration;
import java.time.Instant;

@Builder
public class TTLExpirationStrategy implements ExpirationStrategy {
    private final Duration duration;

    @Override
    public boolean isExpired(Instant entryTime) {
        Instant ttl = entryTime.plus(duration);
        return ttl.isBefore(Instant.now());
    }
}
