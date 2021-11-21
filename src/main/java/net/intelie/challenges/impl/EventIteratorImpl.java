package net.intelie.challenges.impl;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventIteratorImpl implements EventIterator {
    private ConcurrentHashMap<Long, Event> eventList = new ConcurrentHashMap<Long, Event>();
    private Iterator<Map.Entry<Long, Event>> eventIt;
    private Event current;

    public EventIteratorImpl(ConcurrentHashMap<Long, Event> eventList, Iterator<Map.Entry<Long, Event>> eventIt) {
        this.eventList = eventList;
        this.eventIt = eventIt;
    }

    @Override
    public boolean moveNext() {
        if(eventIt.hasNext()){
            current = eventIt.next().getValue();
            return true;
        }
        return false;
    }

    @Override
    public Event current() {
        return current;
    }

    @Override
    public void remove() {
        eventIt.remove();
    }

    @Override
    public void close() throws Exception {
        System.out.println("close");
    }
}
