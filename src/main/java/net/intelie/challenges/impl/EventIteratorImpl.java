package net.intelie.challenges.impl;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

public class EventIteratorImpl implements EventIterator {
    @Override
    public boolean moveNext() {
        return false;
    }

    @Override
    public Event current() {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public void close() throws Exception {

    }
}
