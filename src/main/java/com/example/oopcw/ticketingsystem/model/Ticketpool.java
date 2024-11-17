package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {

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
            System.out.println("Pool is full");
        } else {
            System.out.println("Total Tickets Released : " + currentPoolSize);
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
        //TODO update total ticket
        ticketPool.addAll(tickets);
        System.out.println(vendor.getVendorId());
        System.out.println(vendor.getVendorId() + "Added " + tickets.size() + " tickets" + "TicketPool Size :" + ticketPool.size());
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

}

