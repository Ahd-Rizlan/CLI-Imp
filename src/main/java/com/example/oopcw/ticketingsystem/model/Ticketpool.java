package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.Main;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {
    private final List<Ticket> ticketPool;
    private final int totalTickets;
    private final int maxCapacity;
    private int currentPoolSize;
    private int PoolSize;

    public Ticketpool(Configuration configuration) {
        totalTickets = configuration.getTotalTickets();
        maxCapacity = configuration.getMaxTicketCapacity();
        this.ticketPool = Collections.synchronizedList(new ArrayList<>(configuration.getMaxTicketCapacity()));

    }


    public synchronized int checkVendorEligibility(Vendor vendor) {

        int availableCapacity = this.getTicketPoolCapacity() - this.getPoolSize();
        int vendorTotalTickets = vendor.getTotalTicketsToRelease();


        //Test Records
        System.out.println("--------------------------------------------");
        System.out.println("getTicketPoolCapacity " + getTicketPoolCapacity());
        System.out.println("availableCapacity " + availableCapacity);
        System.out.println("vendorTotalTickets " + vendorTotalTickets);
        System.out.println(Math.min(vendorTotalTickets, availableCapacity));

        System.out.println("--------------------------------------------");


        //currentPoolSize ==  no change only increase
        if (availableCapacity == 0) {
            //TODO LOG ABOUT CONDITION
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                System.out.println("Maximum Event Ticket Capacity Reached");
                System.out.println("Vendor : " + vendor.getVendorId() + " is Removed");
            }
        } else {
            //TODO LOG AVAILABILTY
            System.out.println(Math.min(vendorTotalTickets, availableCapacity));
            int releasableTicketAmount = Math.min(vendorTotalTickets, availableCapacity);
            PoolSize = PoolSize + releasableTicketAmount;
            currentPoolSize = currentPoolSize + releasableTicketAmount;
            if (vendorTotalTickets > availableCapacity) {
                System.out.println("Vendor " + " - " + vendor.getVendorId() + " is Released " + availableCapacity + " Tickets : Since Maximum Event Ticket Capacity Reached");
                //TODO LOG ABOUT CONDITION

            } else {
                System.out.println("Vendor " + " - " + vendor.getVendorId() + " is Released " + vendorTotalTickets + " Tickets.");
                //TODO LOG ABOUT CONDITION
            }
        }
        return Math.min(vendorTotalTickets, availableCapacity);
    }

//----------------------------------

//----------------------------------

    // to check ticket pool Capacity
    public synchronized int ticketPoolCapacityCheck() {
        return getMaxPoolCapacity() - getTicketPoolSize();

    }

    public synchronized void addTicket(Vendor vendor, ArrayList<Ticket> tickets) {
        //TODO update total ticket'

        if (ticketPool.size() == maxCapacity) {
            System.out.println("TicketPool - " + "Maximum Pool Capacity Reached " + ticketPool.size());
            try {
                wait();
                //TODO
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            ticketPool.addAll(tickets);
            System.out.println("Vendor" + " : " + vendor.getVendorId() + " Added " + tickets.size() + " tickets : " + "Updated TicketPool Size :" + ticketPool.size());
            notifyAll();
            //TODO LOGG AS TICKET ADDED
        }
    }

    private synchronized ArrayList<Ticket> changeTicketStatusToSold(int tickerCount, ArrayList<Ticket> soldTickets) {
        System.out.println("Before Ticket Capacity = " + ticketPool.size());
        System.out.println("Before Current Pool Size = " + currentPoolSize);

        int RizCount = 0;
        synchronized (ticketPool) {
            for (int i = ticketPool.size() - 1; i >= 0 && tickerCount > 0; i--) {
                if (ticketPool.get(i).getStatus() == TicketStatus.OnPOOL) {
                    ticketPool.get(i).setStatus(TicketStatus.ACCQUIRED);
                    soldTickets.add(ticketPool.get(i));
                    ticketPool.remove(i); // Safe to remove when iterating backward
                    RizCount++;
                    tickerCount--; // Decrement tickets to process
                }
            }
            System.out.println("RizCount " + RizCount);
            System.out.println("Ingathn " + ticketPool.size());
            System.out.println("Before Ticket Capacity = " + ticketPool.size());
            System.out.println("Solds Ticket Count = " + soldTickets.size());
            System.out.println("After Ticket Capacity = " + ticketPool.size());
            System.out.println("RizCount = " + RizCount);
            return soldTickets;
        }


    }


    public synchronized void removeTicketToTotalCapacity(int purchasedTicketAmount) {
        System.out.println("Current Pool Cpacity on Removal ticket Capacity = " + currentPoolSize);
        if (currentPoolSize >= purchasedTicketAmount) {
            currentPoolSize -= purchasedTicketAmount;
        }
        System.out.println("Current Pool Cpacity on Removal ticket Capacity  After = " + currentPoolSize);
    }
    //TODO LOGGING


    public synchronized void removeTicket(Customer customer, ArrayList<Ticket> purchasedTickets) {
        int requiredTickets = customer.getTicketsPerPurchase();
        purchasedTickets.addAll(changeTicketStatusToSold(requiredTickets, new ArrayList<>()));
        System.out.println(customer.getCustomerId() + " : " + " Purchased " + purchasedTickets.size() + " tickets ;" + "Remaining Tickets Available :" + ticketPool.size());
        if (ticketPool.size() < requiredTickets) {
            System.out.println("TicketPool : Please wait while tickets are being updated.");
            System.out.println("TicketPool Available Tickets : " + ticketPool.size());
            //TODO Log
            notifyAll();
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getMaxPoolCapacity() {
        return maxCapacity;
    }

    public int getTicketPoolSize() {
        return ticketPool.size();
    }

    public int getTicketPoolCapacity() {
        return totalTickets;
    }

    public int getPoolSize() {
        return PoolSize;
    }

    public int getCurrentPoolSizePoolSize() {
        return currentPoolSize;
    }
}

