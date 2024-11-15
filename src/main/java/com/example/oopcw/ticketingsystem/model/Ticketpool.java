package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {

    private static int currentPoolSize;
    private final List<Ticket> ticketPool;
    private final int maxCapacity;
    private final int poolSize;

    public Ticketpool(Configuration configuration) {
        maxCapacity = configuration.getTotalTickets();
        poolSize = configuration.getTotalTickets();
        this.ticketPool = Collections.synchronizedList(new ArrayList<>(configuration.getMaxTicketCapacity()));

    }

    public synchronized void addTicketToTotalCapacity(int releasableTicketAmount) {
        currentPoolSize = currentPoolSize + releasableTicketAmount;
    }

    public synchronized int availabeTotalTicketCapacityCheck(Vendor vendor) {
        int availableCapacity = maxCapacity - currentPoolSize;
        int vendorTotalTickets = vendor.getTotalTicketsToRelease();
        if (!(vendorTotalTickets > availableCapacity)) {
            return availableCapacity;
        }
        return vendorTotalTickets;
    }

    // to check ticket pool Capacity

    public synchronized int ticketPoolCapacityCheck(int ticketRealeaseAmount) {
        int availableCapacity = poolSize - ticketPool.size();
        if (availableCapacity < ticketRealeaseAmount) {
            System.out.println("Maximum available pool capacity: " + availableCapacity);
            //TODO log above
            return availableCapacity;
        }
        return ticketRealeaseAmount;

    }

    public synchronized void addTicket(Vendor vendor, ArrayList<Ticket> tickets) {
        //TODO update total ticket
        tickets.addAll(ticketPool);
        System.out.println(vendor.getVendorId());
        //TODO LOGG AS TICKET ADDED

    }


//    public int getTotalPoolSize() {
//        return poolTickets.size();
//    }

    public int getTotalPoolCapacity() {
        return poolSize;
    }

    public int getTicketPoolSize() {
        return ticketPool.size();
    }

    public int getTicketPoolCapacity() {
        return maxCapacity;
    }

}

