package net.intelie.challenges;

import net.intelie.challenges.impl.EventStoreImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTest {

    public EventStoreImpl eventStore = new EventStoreImpl();

    @Test
    public void testInsertEvent() throws Exception {
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

        eventStore.removeAll("test");

        EventIterator iterator = eventStore.query("type", 120L, 125L);

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