package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {
    private static int currentPoolSize;
    private final List<Ticket> ticketPool;
    private final int totalTickets;
    private final int maxCapacity;
    private int PoolSize;

    public Ticketpool(Configuration configuration) {
        totalTickets = configuration.getTotalTickets();
        maxCapacity = configuration.getMaxTicketCapacity();
        this.ticketPool = Collections.synchronizedList(new ArrayList<>(configuration.getMaxTicketCapacity()));

    }


    public synchronized int checkVendorEligibility(Vendor vendor) {
        int availableCapacity = getTicketPoolCapacity() - getPoolSize();
        //currentPoolSize ==  no change only increase
        int vendorTotalTickets = vendor.getTotalTicketsToRelease();
        if (availableCapacity == 0) {
            //TODO LOG ABOUT CONDITION
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                System.out.println("Maximum Event Ticket Capacity Reached");
                System.out.println("Vendor : " + vendor.getVendorId() + " is Removed");
            }
        } else {
            if (vendorTotalTickets >= availableCapacity) {
                //TODO LOG AVAILABILTY
                System.out.println("availableCapacity  " + availableCapacity);
                return availableCapacity;
            }
            System.out.println("vendorTotalTickets" + vendorTotalTickets);
            return vendorTotalTickets;
        }
        return vendorTotalTickets;
        //TODO LOG AVAILABILTY


    }

//----------------------------------

    public synchronized void updatePoolSize(int releasableTicketAmount) {
        PoolSize = PoolSize + releasableTicketAmount;
        if (PoolSize == totalTickets) {
            //   System.out.println("Maximum Event Ticket Capacity Reached");
        }
        //TODO LOGGING
    }

//----------------------------------

    // to check ticket pool Capacity
    public synchronized int ticketPoolCapacityCheck() {
        int availableCapacity = getMaxPoolCapacity() - getTicketPoolSize();
        return availableCapacity;

    }

    public synchronized void addTicket(Vendor vendor, ArrayList<Ticket> tickets) {
        //TODO update total ticket'
        if (ticketPool.size() == maxCapacity) {
            System.out.println("TicketPool - " + "Maximum Pool Capacity Reached");
            try {
                wait();
                //TODO
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ticketPool.addAll(tickets);
        System.out.println("Vendor" + " : " + vendor.getVendorId() + " Added " + tickets.size() + " tickets : " + "Updated TicketPool Size :" + ticketPool.size());
        notifyAll();
        //TODO LOGG AS TICKET ADDED

    }

    private ArrayList changeTicetStatusToSold(int tickerCount, ArrayList<Ticket> soldTickets) {
        int ChangedTickets = tickerCount;
        for (int i = 0; i < ticketPool.size(); i++) {
            if (ticketPool.get(i).getStatus() == TicketStatus.OnPOOL) {
                ticketPool.get(i).setStatus(TicketStatus.ACCQUIRED);
                soldTickets.add(0, ticketPool.get(i));
                ChangedTickets--;
                if (ChangedTickets == 0) {
                    break;
                }
            }
        }
        return soldTickets;
    }


    public synchronized void removeTicketToTotalCapacity(int purchasedTicketAmount) {
        if (PoolSize == 0) {
            System.out.println("Reservation Full");
            //TODO LOGGING
            Thread.currentThread().interrupt();
        } else {
            PoolSize = PoolSize - purchasedTicketAmount;
            //TODO LOGGING
        }
    }

    public synchronized void removeTicket(Customer customer, ArrayList<Ticket> purchasedTickets) {
        int requiredTickets = customer.getTicketsPerPurchase();
        if (ticketPool.size() < requiredTickets) {
            System.out.println("Please wait while tickets are being updated.");
            //TODO Log
            notifyAll();
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        purchasedTickets.addAll(changeTicetStatusToSold(requiredTickets, new ArrayList<>()));
        System.out.println(customer.getCustomerId() + " : " + " Purchased " + purchasedTickets.size() + " tickets ;" + "Remaining Tickets Available :" + ticketPool.size());
        //TODO LOGG AS TICKET ADDED

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
}

