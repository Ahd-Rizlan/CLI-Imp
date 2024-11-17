package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

import java.util.ArrayList;

public class Vendor implements Runnable {

    private static final AutoIdGeneration vendorAutoIdGeneration = new AutoIdGeneration(0);
    private final Ticketpool ticketpool;
    private final ArrayList<Ticket> releasingTickets;

    private final String vendorId;
    private final int frequency;
    private final int totalTicketsToRelease;
    private int ticketsPerRelease;


    public Vendor(int totalTicketsToRelease, int ticketsPerRelease, Ticketpool ticketpool, Configuration config) {
        this.vendorId = vendorAutoIdGeneration.generateAutoId("VId");
        this.frequency = config.getTicketReleaseRate();
        this.ticketsPerRelease = ticketsPerRelease;
        this.totalTicketsToRelease = totalTicketsToRelease;
        this.releasingTickets = new ArrayList<>();
        this.ticketpool = ticketpool;

    }

    public String getVendorId() {
        return vendorId;
    }

    public int getTotalTicketsToRelease() {
        return totalTicketsToRelease;
    }

    public int getTicketsPerRelease() {
        return ticketsPerRelease;
    }

    public void setTicketsPerRelease(int ticketsPerRelease) {
        this.ticketsPerRelease = ticketsPerRelease;
    }

    public int getFrequency() {
        return frequency;
    }

    public ArrayList<Ticket> getReleasingTickets() {
        return releasingTickets;
    }

    public int getTotalTickets() {
        return releasingTickets.size();
    }

    public int getUnReleasingTickets() {
        int unReleasedTickets = 0;
        for (Ticket ticket : releasingTickets) {
            if (ticket.getStatus() == TicketStatus.PENDING) {
                unReleasedTickets++;
            }
        }
        return unReleasedTickets;
    }

    private void addToTempListFromVendorList(int tickerCount, ArrayList<Ticket> ticketsToPool) {
        int ChangedTickets = tickerCount;
        for (int i = 0; i < releasingTickets.size(); i++) {
            if (releasingTickets.get(i).getStatus() == TicketStatus.PENDING) {
                releasingTickets.get(i).setStatus(TicketStatus.OnPOOL);
                ticketsToPool.add(0, releasingTickets.get(i));
                ChangedTickets--;
                if (ChangedTickets == 0) {
                    return;
                }
            }
        }
    }

    private void releasableTicketsToMainPool() {
        int releasableTicketCapacity;
        if (ticketsPerRelease == 0) {
            System.out.println(Thread.currentThread().getName() + " : " + "No Tickets to Release");
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                System.out.println(this.vendorId + " : " + "Exiting the Vendor Thread");
            }
            //TODO
            //if no ticket found , closing the Thread
        }
        releasableTicketCapacity = ticketpool.availabeTotalTicketCapacityCheck(this);
        if (releasableTicketCapacity == 0) {
            System.out.println("Maximum Event Ticket Capacity Reached");
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                System.out.println("");
                System.out.println("Exiting the Vendor : " + vendorId);
            }
        }

        if (releasableTicketCapacity < totalTicketsToRelease && releasableTicketCapacity > 0) {
            //TODO LOG AMOUNT OF TICKETS RELEASED
            System.out.println(getVendorId() + " - " + "Only " + releasableTicketCapacity + " Tickets Released since Maximum Ticket Capacity Reached");
            for (int i = 0; i < releasableTicketCapacity; i++) {
                Ticket ticket = new Ticket(Vendor.this);
                //set status to ticket-onPool
                ticket.setStatus(TicketStatus.PENDING);
                releasingTickets.add(0, ticket);
            }
            //Creating Released Tickets and add it to the Main List


        }
        ticketpool.addTicketToTotalCapacity(releasableTicketCapacity);
        //TODO LOG

    }

    @Override
    public void run() {
        Thread.currentThread().setName(getVendorId());
        Thread.currentThread().setPriority(Config.HighPriority);
        releasableTicketsToMainPool();
        boolean IsActive = true;
        //check weather the vendor have any tickets to release ; if not remove the Vendor
        while (IsActive) {
            try {
                Thread.sleep(frequency * 1000L);
                int capacityOfTicketPool;
                ArrayList<Ticket> ticketsToPool = new ArrayList<>();
                capacityOfTicketPool = ticketpool.ticketPoolCapacityCheck();

                //Math.min(getUnReleasingTickets(),ticketsPerRelease);
                //Math.min(totalTicketsToRelease, capacityOfTicketPool);
                //Required Amount of Tickets Released
                addToTempListFromVendorList(Math.min(getUnReleasingTickets(), Math.min(totalTicketsToRelease, capacityOfTicketPool)), ticketsToPool);

                ticketpool.addTicket(this, ticketsToPool);

                if (getUnReleasingTickets() == 0) {
                    System.out.println(Thread.currentThread().getName() + " : " + "Total Released Tickets : " + getTotalTickets());
                    System.out.println(Thread.currentThread().getName() + " : " + "Ticket Release is Completed");
                    IsActive = false;
                    Thread.currentThread().interrupt();
                }


            } catch (InterruptedException e) {
                System.out.println("Ticket release interrupted for Vendor: " + vendorId);
                Thread.currentThread().interrupt();
                break;

            }
        }

        //add releasable tickets to TotalTickets


        //      TODO logging the Amount of ticket added amount


    }
}

