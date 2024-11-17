package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

import java.util.ArrayList;

public class Customer implements Runnable {

    private static final AutoIdGeneration customerAutoIdGeneration = new AutoIdGeneration(0);
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
            //TODO LOG HERE THE BOTH AMOUNTS
            return true;
        } else
            return false;
    }

    private void setPriorityForVipCustomers(boolean isVip) {
        if (this.isVip == true) {
            Thread.currentThread().setPriority(Config.VipPriority);
        } else {
            Thread.currentThread().setPriority(Config.LowPriority);
        }
    }

    @Override
    public void run() {

        boolean isActive = true;
        Thread.currentThread().setName(getCustomerId());
        setPriorityForVipCustomers(this.isVip());

        while (isActive) {
            try {
                if (isActive) {
                    Thread.sleep(retrievalInterval * 1000L);
                }
                int availableTickets = ticketpool.getPoolSize();
                //get Current Pool Size

                if (purchaseTickets(availableTickets)) {
                    ticketpool.removeTicketToTotalCapacity(getTicketsPerPurchase());
                    ticketpool.removeTicket(this, purchasedTickets);
                    System.out.println(Thread.currentThread().getName() + " : " + purchasedTickets.toString());
//                } else {
//                    isActive = false; // Stop if no tickets can be purchased
//                    Thread.currentThread().interrupt();
//                    if (Thread.interrupted()) {
//                        System.out.println(Thread.currentThread().getName() + " : " + "Exiting the System :" + "Insufficient Tickets Available");
//                    }
                }


            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted. Exiting...");
                isActive = false;
            }
        }
    }
}