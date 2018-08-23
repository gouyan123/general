package com.listener;

public class Event {
    private Object eventSource;
    private String eventState;
    public Event(Object eventSource,String eventState) {
        this.eventSource = eventSource;
        this.eventState = eventState;
    }

    public Object getEventSource() {
        return eventSource;
    }

    public String getState() {
        return this.eventState;
    }
}
