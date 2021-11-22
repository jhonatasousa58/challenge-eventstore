package net.intelie.challenges.impl;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class responsible for iterating over registered events, either walking among them,
 * removing or returning the current event.
 */
public class EventIteratorImpl implements EventIterator {
    private ConcurrentHashMap<Long, Event> eventList = new ConcurrentHashMap<Long, Event>();
    private Iterator<Map.Entry<Long, Event>> eventIt;
    private Event current;

    /**
     * Constructor of the EventIteratorImpl class that receives a list of events and an iterator.
     * @param eventList
     * @param eventIt
     */
    public EventIteratorImpl(ConcurrentHashMap<Long, Event> eventList, Iterator<Map.Entry<Long, Event>> eventIt) {
        this.eventList = eventList;
        this.eventIt = eventIt;
    }

    /**
     * Responsible method for going to the next event through the iterator.
     * At each iteration it checks if there is a next event, if it exists,
     * saves the next event in the current variable and returns true, otherwise it returns false
     * @return
     */
    @Override
    public boolean moveNext() {
        if(eventIt.hasNext()){
            current = eventIt.next().getValue();
            return true;
        }
        return false;
    }

    /**
     * Method responsible for returning the current event.
     * @return current event.
     */
    @Override
    public Event current() {
        return current;
    }

    /**
     * Method responsible for removing an event, in this case the current event.
     */
    @Override
    public void remove() {
        eventIt.remove();
    }

    @Override
    public void close() throws Exception {
        System.out.println("close");
    }
}
