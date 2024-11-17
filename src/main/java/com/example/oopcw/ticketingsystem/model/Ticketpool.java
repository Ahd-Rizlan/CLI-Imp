package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {
    //TODO close all when show completion
    private static int currentPoolSize;
    private final List<Ticket> ticketPool;
    private final int totalTickets;
    private final int maxCapacity;

    public Ticketpool(Configuration configuration) {
        totalTickets = configuration.getTotalTickets();
        maxCapacity = configuration.getMaxTicketCapacity();
        this.ticketPool = Collections.synchronizedList(new ArrayList<>(configuration.getMaxTicketCapacity()));

    }

    public synchronized void addTicketToTotalCapacity(int releasableTicketAmount) {
        currentPoolSize = currentPoolSize + releasableTicketAmount;
        if (currentPoolSize == totalTickets) {
//            System.out.println("Pool is full");
        }
        //TODO LOGGING
    }

    public synchronized int availabeTotalTicketCapacityCheck(Vendor vendor) {
        int availableCapacity = getTicketPoolCapacity() - currentPoolSize;
        int vendorTotalTickets = vendor.getTotalTicketsToRelease();
        if (vendorTotalTickets > availableCapacity) {
            return availableCapacity;
        }
        return vendorTotalTickets;
    }

    // to check ticket pool Capacity
    public synchronized int ticketPoolCapacityCheck() {
        int availableCapacity = getTotalPoolCapacity() - getTicketPoolSize();
        return availableCapacity;

    }

    public synchronized void addTicket(Vendor vendor, ArrayList<Ticket> tickets) {
        //TODO update total ticket'
        if (ticketPool.size() == maxCapacity) {
            System.out.println("Ticket pool is Full");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
        ticketPool.addAll(tickets);
        System.out.println(vendor.getVendorId() + " : " + "Added " + tickets.size() + " tickets" + "TicketPool Size :" + ticketPool.size());
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
        if (currentPoolSize == 0) {
            System.out.println("Reservation Full");
            //TODO LOGGING
            Thread.currentThread().interrupt();
        } else {
            currentPoolSize = currentPoolSize - purchasedTicketAmount;
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

    public int getTotalPoolCapacity() {
        return maxCapacity;
    }

    public int getTicketPoolSize() {
        return ticketPool.size();
    }

    public int getTicketPoolCapacity() {
        return totalTickets;
    }

    public int getCurrentPoolSize() {
        return currentPoolSize;
    }
}

