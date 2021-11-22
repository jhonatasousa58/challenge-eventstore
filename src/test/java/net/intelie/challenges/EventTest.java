package net.intelie.challenges;

import net.intelie.challenges.impl.EventStoreImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    public EventStoreImpl eventStore = new EventStoreImpl();

    public void insertEvent() {
        Event event = new Event("some_type", 123L);
        Event event1 = new Event("type", 121L);
        Event event2 = new Event("test", 122L);
        Event event3 = new Event("test", 124L);
        Event event4 = new Event("type", 125L);
        Event event5 = new Event("test", 126L);
        Event event6 = new Event("some_type", 127L);
        eventStore.insert(event);
        eventStore.insert(event1);
        eventStore.insert(event2);
        eventStore.insert(event3);
        eventStore.insert(event4);
        eventStore.insert(event5);
        eventStore.insert(event6);
    }

    @Test
    public void testInsertEvent() throws Exception {
        insertEvent();
        EventIterator iterator = eventStore.query("some_type", 0L, 0L);
        assertTrue(iterator.moveNext());
    }

    @Test
    public void testRemoveAll() throws Exception {
        insertEvent();
        eventStore.removeAll("test");
        EventIterator iterator = eventStore.query("test", 0L, 0L);
        assertFalse(iterator.moveNext());
    }

    @Test
    public void testQuery() throws Exception {
        insertEvent();
        EventIterator iterator = eventStore.query("type", 120L, 150L);
        assertTrue(iterator.moveNext());
    }

    @Test
    public void testThreadSafe() throws Exception {
        insertEvent();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                EventIterator iterator = eventStore.query("test", 0L, 0L);
                assertTrue(iterator.moveNext());
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                eventStore.removeAll("test");
                EventIterator iterator = eventStore.query("test", 0L, 0L);
                assertFalse(iterator.moveNext());
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                EventIterator iterator = eventStore.query("type", 120L, 150L);
                assertTrue(iterator.moveNext());
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void thisIsAWarning() throws Exception {
        Event event = new Event("some_type", 123L);

        //THIS IS A WARNING:
        //Some of us (not everyone) are coverage freaks.
        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }
}