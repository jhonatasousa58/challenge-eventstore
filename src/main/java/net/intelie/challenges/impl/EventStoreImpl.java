package net.intelie.challenges.impl;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

public class EventStoreImpl implements EventStore {
    @Override
    public void insert(Event event){

    }

    @Override
    public void removeAll(String type) {

    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        return null;
    }
}
