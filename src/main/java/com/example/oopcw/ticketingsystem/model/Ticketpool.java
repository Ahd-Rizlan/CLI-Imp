package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {


    private static int currentPoolSize = 0;
    private final List<Ticket> ticketPool;
    private final int poolSize;
    private final List<Ticket> poolTickets;
    Configuration configuration;
    private ArrayList<Ticket> soldTickets;

    public Ticketpool() {
        this.ticketPool = Collections.synchronizedList(new ArrayList<>(configuration.getMaxTicketCapacity()));
        this.poolTickets = Collections.synchronizedList(new ArrayList<>(configuration.getTotalTickets()));
        poolSize = configuration.getTotalTickets();
    }

    public synchronized int availabeCapacityCheck(Vendor vendor) {
        int availableCapacity = poolSize - currentPoolSize;
        int vendorTotalTickets = vendor.getTotalTicketsToRelease();
        if (!(vendorTotalTickets > availableCapacity)) {
            return availableCapacity;
        }
        return vendorTotalTickets;
    }

    public synchronized void addTicket(Vendor vendor) {
        if (vendor.getReleasingTickets().size() > 0) {
            vendor.getReleasingTickets().addAll(ticketPool);
            System.out.println(vendor.getVendorId());
            //TODO LOGG AS TICKET ADDED

        } else {
            System.out.println("No capacity to add tickets.");
        }
    }


    public int getTotalPoolSize() {
        return poolTickets.size();
    }

    public int getTotalPoolCapacity() {
        return configuration.getTotalTickets();
    }

    public int getTicketPoolSize() {
        return ticketPool.size();
    }

    public int getTicketPoolCapacity() {
        return configuration.getMaxTicketCapacity();
    }

}
