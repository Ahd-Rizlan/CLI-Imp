package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.Main;
import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

import java.util.ArrayList;

public class Customer implements Runnable {

    private static final AutoIdGeneration customerAutoIdGeneration = new AutoIdGeneration();
    private final Ticketpool ticketpool;
    private final ArrayList<Ticket> purchasedTickets;

    private String customerId;
    private boolean isVip;
    private int ticketsPerPurchase;
    private int retrievalInterval;


    public Customer(boolean isVip, int ticketsPerPurchase, Ticketpool ticketPool, Configuration config) {
        this.customerId = customerAutoIdGeneration.generateAutoId("CId");
        this.retrievalInterval = config.getCustomerRetrievalRate();
        this.ticketsPerPurchase = ticketsPerPurchase;
        this.ticketpool = ticketPool;
        this.purchasedTickets = new ArrayList<>();
        this.isVip = isVip;
    }

    public Ticketpool getTicketpool() {
        return ticketpool;
    }

    public boolean isVip() {
        return isVip;
    }

    public String getCustomerId() {
        return customerId;
    }


    public ArrayList<Ticket> getPurchasedTickets() {
        return purchasedTickets;
    }

    public int getRetrievalInterval() {
        return retrievalInterval;
    }

    public int getTicketsPerPurchase() {
        return ticketsPerPurchase;
    }


    private boolean purchaseTickets(int noOfTicketsInPool) {
        if (noOfTicketsInPool >= getTicketsPerPurchase()) {
            return true;
        } else
            return false;
    }

    private void setPriorityForVipCustomers(boolean isVip) {
        if (this.isVip == true) {
            Thread.currentThread().setPriority(Config.HighPriority);
        } else {
            Thread.currentThread().setPriority(Config.LowPriority);
        }
    }

    @Override
    public void run() {
        boolean IsActive = true;

        Thread.currentThread().setName(getCustomerId());
        setPriorityForVipCustomers(this.isVip());
        do {
            try {
                int availableTickets;
                synchronized (ticketpool) {
                    availableTickets = ticketpool.getCurrentPoolSize();
                    if (purchaseTickets(availableTickets)) {
                        ticketpool.removeTicketToTotalCapacity(availableTickets);
                        ticketpool.removeicket(this, purchasedTickets);
                    } else {
                        IsActive = false;
                        Thread.currentThread().interrupt();
                    }
                }

                Thread.sleep(retrievalInterval * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (IsActive);


    }
}
