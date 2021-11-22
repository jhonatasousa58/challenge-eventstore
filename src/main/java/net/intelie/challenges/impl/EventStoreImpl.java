package net.intelie.challenges.impl;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of the EventStore interface with all the methods necessary for its execution.
 * after a lot of research, I ended up using ConcurrentHashMap, as it is thread-safe,
 * that is, ConcurrentHashMap itself makes the synchronization of processes/threads during execution through locks.
 * Ensuring mutual exclusion and avoiding race conditions.
 * I use timestamp as a key and event itself as a value inside the map.
*/
public class EventStoreImpl implements EventStore {
    ConcurrentHashMap<Long, Event> eventList = new ConcurrentHashMap<Long, Event>();

    /**
     * method responsible for inserting an event inside the ConcurrentHashMap,
     * when an event is being inserted it is blocked until the operation is finished,
     * and this is a property of ConcurrentHashMap itself because it is thread safe.
     * @param event
     */
    @Override
    public void insert(Event event){
        eventList.put(event.timestamp(), event);
    }

    /**
     * method responsible for removing all events of a specific type.
     * It creates an iterator to move inside the ConcurrentHashMap,
     * it has a "while" that iterates through the entire ConcurrentHashMap
     * checking if the current event type is equal to the event type passed for deletion,
     * if it is the same, the event is deleted.
     * @param type
     */
    @Override
    public void removeAll(String type) {
        EventIterator iterator = new EventIteratorImpl(eventList, eventList.entrySet().iterator());
        while (iterator.moveNext()) {
            if (type.equals(iterator.current().type())) {
                iterator.remove();
            }
        }
    }

    /**
     * method responsible for searching between the dates passed by parameters all events of a specific type.
     * Receives the type, start time, end time and returns an iterator with the search result.
     * I assumed that if you elapse the start time and end time with 0, the locator should return all events of the specified type, start to finish.
     * And since there is inclusive and exclusive in the entity, I assumed that it has a start time, but it may not have an end time in the search,
     * so if the start time is greater than 0 and the end time equal to 0, search all events of a specific type from the start date to the last recorded event.
     * The worst-case search cost is O(n), as it traverses all elements of the ConcurrentHashMap.
     * @param type      The type we are querying for.
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return an iterator with the search result.
     */
    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        EventIterator iterator = new EventIteratorImpl(eventList, eventList.entrySet().iterator());
        ConcurrentHashMap<Long, Event> eventQuery = new ConcurrentHashMap<Long, Event>();
        if(startTime == 0 && endTime == 0) {
            while (iterator.moveNext()) {
                if (type.equals(iterator.current().type())) {
                    eventQuery.put(iterator.current().timestamp(), iterator.current());
                }
            }
            EventIterator it = new EventIteratorImpl(eventQuery, eventQuery.entrySet().iterator());
            return it;
        } else if (startTime >= 0 && endTime == 0) {
            while (iterator.moveNext() && iterator.current().timestamp() >= startTime) {
                if (type.equals(iterator.current().type())) {
                    eventQuery.put(iterator.current().timestamp(), iterator.current());
                }
            }
            EventIterator it = new EventIteratorImpl(eventQuery, eventQuery.entrySet().iterator());
            return it;
        } else {
            while (iterator.moveNext() && iterator.current().timestamp() >= startTime && iterator.current().timestamp() < endTime) {
                if (type.equals(iterator.current().type())) {
                    eventQuery.put(iterator.current().timestamp(), iterator.current());
                }
            }
            EventIterator it = new EventIteratorImpl(eventQuery, eventQuery.entrySet().iterator());
            return it;
        }
    }
}
