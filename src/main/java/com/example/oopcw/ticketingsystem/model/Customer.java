package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

public class Customer implements Runnable {

    private static final AutoIdGeneration customerAutoIdGeneration = new AutoIdGeneration();

    private String customerId;
    private boolean isVip;
    private int frequency;
    private int retrievalInterval;
    //customerReterivalRate


    public Customer(String customerId, int frequency, boolean isVip, int retrievalInterval) {
        this.customerId = customerAutoIdGeneration.generateAutoId("CId");
        this.frequency = frequency;
        this.isVip = isVip;
        this.retrievalInterval = retrievalInterval;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getRetrievalInterval() {
        return retrievalInterval;
    }

    public void setRetrievalInterval(int retrievalInterval) {
        this.retrievalInterval = retrievalInterval;
    }


    @Override
    public void run() {

    }
}
