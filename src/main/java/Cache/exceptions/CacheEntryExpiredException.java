package Cache.exceptions;

public class CacheEntryExpiredException extends RuntimeException {
    public CacheEntryExpiredException(final String message) {
        super(message);
    }
}
