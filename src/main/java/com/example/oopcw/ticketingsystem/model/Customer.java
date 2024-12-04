package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;

public class Customer implements Runnable {

    private static final AutoIdGeneration customerAutoIdGeneration = new AutoIdGeneration(0);
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);
    private final Ticketpool ticketpool;

    private final ArrayList<Ticket> purchasedTickets;
    private final String customerId;
    private final boolean isVip;
    private final int ticketsPerPurchase;
    private final int retrievalInterval;


    public Customer(boolean isVip, int ticketsPerPurchase, Ticketpool ticketPool, Configuration config) {
        this.customerId = customerAutoIdGeneration.generateAutoId("CId");
        this.retrievalInterval = config.getCustomerRetrievalRate();
        this.ticketsPerPurchase = ticketsPerPurchase;
        this.ticketpool = ticketPool;
        this.purchasedTickets = new ArrayList<>();
        this.isVip = isVip;
    }

    public Customer(boolean isVip, int ticketsPerPurchase, int retrievalInterval, Ticketpool ticketPool, Configuration config) {
        this.customerId = customerAutoIdGeneration.generateAutoId("CId");
        this.retrievalInterval = retrievalInterval;
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


    @Override
    public String toString() {
        return "Customer {" +
                "Id =" + customerId +
                "Tickets Per Purchase = " + ticketsPerPurchase +
                "Ticket Purchase Rate =" + retrievalInterval +
                '}';
    }


    private void setPriorityForVipCustomers(boolean isVip) {
        if (this.isVip == true) {
            logger.info(" Customer {} , Identified as VIP and Setting Priority as Vip Customer", customerId);
            logger.debug("Setting Priority for Vip Customer , Higher Priority TO Thread {}", customerId);
            Thread.currentThread().setPriority(Config.VipPriority);
        } else {
            logger.info(" Customer {} , Not Identified as VIP ", customerId);
            logger.debug("Setting up Normal Priority for Customer {}", customerId);
            Thread.currentThread().setPriority(Config.LowPriority);
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName(getCustomerId());
        logger.debug("thread Renamed to Cusomer Id");
        setPriorityForVipCustomers(this.isVip());
        boolean isActive = true;


        while (isActive) {
            try {
                if (isActive) {
                    Thread.sleep(retrievalInterval * 1000L);

                    //  int purchaseableTickets = ticketpool.getCurrentPoolSizePoolSize();
                    // System.out.println("Available Tickets  = " + purchaseableTickets);
                    //   System.out.println(" Amount To be Purchased = " + getTicketsPerPurchase());
                    logger.info("Customer {} , Checking for Available Tickets", customerId);
                    ticketpool.removeTicket(this, purchasedTickets);
                    synchronized (ticketpool) {
                        if (ticketpool.getLargePoolSize() < this.getTicketsPerPurchase()) {
//                            Thread.currentThread().interrupt();

//                            System.out.println("-----------------------------------------------------===  " + ticketpool.getLargePoolSize());
//                            System.out.println("Insufficient Tickets Available---------------------------------------------------------=======");
//                            System.out.println(Thread.currentThread().getName() + "===========");
                            Thread.currentThread().interrupt();
                            logger.info("Customer {} , Insufficient Tickets Available , Customer is Exited from the Pool", customerId);
                            logger.debug("Customer thread is Interrupted ");
                            if (Thread.interrupted()) {
                                logger.info("TicketPool Size {} , LargePool Size {} ", ticketpool.getTicketPoolSize(), ticketpool.getLargePoolSize());
                                logger.debug("TicketPool Size {} , LargePool Size {} ", ticketpool.getTicketPoolSize(), ticketpool.getLargePoolSize());

                                isActive = false;

                            }

                        }
                    }


//                    if (purchaseableTickets == 0) {
//                        Thread.currentThread().interrupt();
//                        if (Thread.interrupted()) {
//                            System.out.println("Exitning The Customer " + Thread.currentThread().getName() + " Total Tickets Purchased : " + this.getPurchasedTickets() + " : Tickets Sold-out");
//                            isActive = false;
//                        }
                }
//                    synchronized (ticketpool) {
//
//
//
////
////                        System.out.println("------------------------------------------------------------------------------------------");
////                        System.out.println("Available Tickets  = " + availableTickets);
////                        System.out.println(Thread.currentThread().getName() + " Amount To be Purchased = " + getTicketsPerPurchase());
////                        if (availableTickets == 0) {
////                            Thread.currentThread().interrupt();
////                            if (Thread.interrupted()) {
////                                System.out.println("Exitning The Customer " + Thread.currentThread().getName() + " Total Tickets Purchased : " + this.getPurchasedTickets() + " : Tickets Sold-out");
////                                isActive = false;
////                            }
////                        } else if (availableTickets >= getTicketsPerPurchase()) {
////                            // ticketpool.removeTicketToTotalCapacity(getTicketsPerPurchase());
////                            ticketpool.removeTicket(this, purchasedTickets);
////                            //System.out.println("---------------------------------------------------------------------------------");
////                            //TODO LOG HERE THE BOTH AMOUNTS
////                        } else {
////                            System.out.println("Customer : " + customerId + " Insufficient amount of tickets");
//////                            System.out.println("TicktPool : " + ticketpool.getTicketPoolSize());
////                            ticketpool.wait();
////                            ticketpool.notifyAll();
////                        }
//                    }

            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted. Exiting...");
                isActive = false;
            }
        }
    }
}