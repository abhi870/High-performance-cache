package Cache.LinkedList;

import lombok.Getter;

@Getter
public class DoublyLinkedList {
    private CacheEntry head;
    private CacheEntry rear;
    private Integer size;

    public DoublyLinkedList() {
        head = null;
        rear = null;
        this.size = 0;
    }

    public synchronized CacheEntry addLatestEntry(CacheEntry c) {
        if (head == null) {
            // c.prev initialized to null already.
            // c.next initialized to null already.
            head = c;
            rear = c;
        } else {
            // c.prev = null while initializing.
            c.setNext(head);
            head.setPrev(c);
            head = c;
        }
        incrementSize();
        return c;
    }

    public synchronized CacheEntry removeLeastUsedEntry() {
        CacheEntry removed = null;
        if (rear == null) {
            throw new RuntimeException("list is empty");
        } else {
            removed = rear;
            rear = rear.getPrev();
            rear.setNext(null);
            decrementSize();
        }
        return removed;
    }

    public synchronized boolean remove(CacheEntry toBeRemoved) {
        try {
            if (rear == toBeRemoved && head == toBeRemoved) {
                head = null;
                rear = null;
            } else if (head.getNext().equals(rear) && head.equals(toBeRemoved)) {
                CacheEntry temp = head;
                head = rear;
                head.setPrev(null);
                temp.setNext(null);
                temp = null;
            } else if (head.getNext().equals(rear) && rear.equals(toBeRemoved)) {
                removeLeastUsedEntry();
            } else {
                toBeRemoved.getNext().setPrev(toBeRemoved.getPrev());
                toBeRemoved.getPrev().setNext(toBeRemoved.getNext());
                toBeRemoved.setPrev(null);
                toBeRemoved.setNext(null);
            }
            decrementSize();
            return true;
        } catch (Exception e) {
            System.out.println("error occurred while removing entry, :" + e.getMessage());
            return false;
        }
    }

    private synchronized void incrementSize() {
        size++;
    }

    private synchronized void decrementSize() {
        size--;
    }

    public void print() {
        while (head != null) {
            System.out.println(head.getValue());
            head = head.getNext();
        }
    }
}
