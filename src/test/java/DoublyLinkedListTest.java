import Cache.LinkedList.CacheEntry;
import Cache.LinkedList.DoublyLinkedList;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class DoublyLinkedListTest {
    DoublyLinkedList doublyLinkedList = new DoublyLinkedList();

    @Test
    public void testAddLatestEntry() {
        CacheEntry c = CacheEntry.builder()
                .key("GooglePay")
                .value("GooglePay")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(c);
        CacheEntry d = CacheEntry.builder()
                .key("paytm")
                .value("paytm")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(d);
        CacheEntry e = CacheEntry.builder()
                .key("phonepe")
                .value("phonepe")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(e);

        CacheEntry head = doublyLinkedList.getHead();
        List<String> outputExpected = List.of("phonepe", "paytm", "GooglePay");
        int counter = 0;
        while (head != null) {
            assertEquals(outputExpected.get(counter), head.getValue());
            counter++;
            head = head.getNext();
        }
    }

    @Test
    public void testRemoveLeastUsedEntry() {
        CacheEntry c = CacheEntry.builder()
                .key("GooglePay")
                .value("GooglePay")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(c);
        CacheEntry d = CacheEntry.builder()
                .key("paytm")
                .value("paytm")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(d);
        CacheEntry e = CacheEntry.builder()
                .key("phonepe")
                .value("phonepe")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(e);

        CacheEntry removed = doublyLinkedList.removeLeastUsedEntry();
        assertEquals("GooglePay", removed.getValue());
    }

    @Test
    public void testRemoveHead() {
        CacheEntry c = CacheEntry.builder()
                .key("GooglePay")
                .value("GooglePay")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(c);
        CacheEntry d = CacheEntry.builder()
                .key("paytm")
                .value("paytm")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(d);
        doublyLinkedList.remove(d);
        assertEquals("GooglePay", (String) doublyLinkedList.getHead().getValue());
    }

    @Test
    public void testRemoveRear() {
        CacheEntry c = CacheEntry.builder()
                .key("GooglePay")
                .value("GooglePay")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(c);
        CacheEntry d = CacheEntry.builder()
                .key("paytm")
                .value("paytm")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(d);
        doublyLinkedList.remove(c);
        assertEquals("paytm", (String) doublyLinkedList.getHead().getValue());
    }

    @Test
    public void testRemoveMid() {
        CacheEntry c = CacheEntry.builder()
                .key("GooglePay")
                .value("GooglePay")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(c);
        CacheEntry d = CacheEntry.builder()
                .key("paytm")
                .value("paytm")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(d);
        CacheEntry e = CacheEntry.builder()
                .key("phonepe")
                .value("phonepe")
                .ttl(Instant.now())
                .prev(null)
                .next(null)
                .build();
        doublyLinkedList.addLatestEntry(e);

        boolean isRemoved = doublyLinkedList.remove(d);
        assertTrue(isRemoved);
        doublyLinkedList.print();
    }
}
