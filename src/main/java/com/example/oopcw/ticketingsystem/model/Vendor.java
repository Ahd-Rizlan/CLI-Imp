package com.example.oopcw.ticketingsystem.model;

public class Vendor extends User implements Runnable {
    private Event event;

    public Vendor(String userId, String userName, Event event) {
        super(userId, userName);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void run() {

    }
}
