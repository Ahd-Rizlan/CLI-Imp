package com.example.oopcw.ticketingsystem.model;

public abstract class User {
    protected String userId;
    protected String userName;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    // Abstract run method to common both Vendor and Customer to perform specific task
    public abstract void run();
}