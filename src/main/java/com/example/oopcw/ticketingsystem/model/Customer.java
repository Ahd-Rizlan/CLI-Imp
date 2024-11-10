package com.example.oopcw.ticketingsystem.model;

public class Customer extends User implements Runnable {
    private Event event;

    public Customer(String userId, String userName) {
        super(userId, userName);
    }

    @Override
    public void run() {

    }
}
