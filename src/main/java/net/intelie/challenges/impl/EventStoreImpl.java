package net.intelie.challenges.impl;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventStoreImpl implements EventStore {
    ConcurrentHashMap<Long, Event> eventList = new ConcurrentHashMap<Long, Event>();

    @Override
    public void insert(Event event){
        eventList.put(event.timestamp(), event);
    }

    @Override
    public void removeAll(String type) {
        EventIterator iterator = new EventIteratorImpl(eventList, eventList.entrySet().iterator());
        while (iterator.moveNext()) {
            if (type.equals(iterator.current().type())) {
                iterator.remove();
            }
        }
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        EventIterator iterator = new EventIteratorImpl(eventList, eventList.entrySet().iterator());
        ConcurrentHashMap<Long, Event> eventQuery = new ConcurrentHashMap<Long, Event>();
        while (iterator.moveNext()) {
            if (type.equals(iterator.current().type()) && iterator.current().timestamp() >= startTime && iterator.current().timestamp() < endTime) {
                eventQuery.put(iterator.current().timestamp(), iterator.current());
            }
        }
        EventIterator it = new EventIteratorImpl(eventQuery, eventQuery.entrySet().iterator());
        return it;
    }
}
