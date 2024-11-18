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

    public boolean isVip() {
        return isVip;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getTicketsPerPurchase() {
        return ticketsPerPurchase;
    }


//    private synchronized boolean purchaseTickets(int noOfTicketsInPool) {
//
//        System.out.println("Available Tickets  = " + noOfTicketsInPool);
//        System.out.println(Thread.currentThread().getName() + " Amount To be Purchased = " + getTicketsPerPurchase());
//
//        if (noOfTicketsInPool >= getTicketsPerPurchase()) {
//            //TODO LOG HERE THE BOTH AMOUNTS
//            return true;
//        } else {
//            System.out.println("Customer : " + customerId + " Insufficient amount of tickets");
//            System.out.println("TickwtPool : " + ticketpool.getTicketPoolSize());
//            notifyAll();
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            return false;
//        }
//    }

    private void setPriorityForVipCustomers(boolean isVip) {
        if (this.isVip == true) {
            Thread.currentThread().setPriority(Config.VipPriority);
        } else {
            Thread.currentThread().setPriority(Config.LowPriority);
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName(getCustomerId());
        setPriorityForVipCustomers(this.isVip());
        boolean isActive = true;


        while (isActive) {
            try {
                if (isActive) {
                    Thread.sleep(retrievalInterval * 1000L);
                    synchronized (ticketpool) {
                        int availableTickets = ticketpool.getCurrentPoolSizePoolSize();
                        System.out.println("------------------------------------------------------------------------------------------");
                        System.out.println("Available Tickets  = " + availableTickets);
                        System.out.println(Thread.currentThread().getName() + " Amount To be Purchased = " + getTicketsPerPurchase());

                        if (availableTickets >= getTicketsPerPurchase()) {
                            ticketpool.removeTicketToTotalCapacity(getTicketsPerPurchase());
                            ticketpool.removeTicket(this, purchasedTickets);
                            System.out.println("---------------------------------------------------------------------------------");
                            //TODO LOG HERE THE BOTH AMOUNTS
                        } else {
                            System.out.println("Customer : " + customerId + " Insufficient amount of tickets");
                            System.out.println("TickwtPool : " + ticketpool.getTicketPoolSize());
                            ticketpool.wait();
                            ticketpool.notifyAll();
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted. Exiting...");
                isActive = false;
            }
        }
    }
}