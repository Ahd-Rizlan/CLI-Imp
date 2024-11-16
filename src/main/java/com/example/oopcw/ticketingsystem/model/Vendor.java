package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

import java.util.ArrayList;

public class Vendor implements Runnable {

    private static final AutoIdGeneration vendorAutoIdGeneration = new AutoIdGeneration();
    private final String vendorId;
    private final int frequency;
    private final int totalTicketsToRelease;
    private final ArrayList<Ticket> releasingTickets;
    private final Ticketpool ticketpool;
    private int ticketsPerRelease;
    private boolean IsActive = true;


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
        System.out.println("Unreleased Tickets Count: " + unReleasedTickets);
///////////////
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
            System.out.println("No Tickets to Release");
            Thread.currentThread().interrupt();
            System.out.println("Exiting the Vendor Thread");
            //if no ticket found , closing the Thread
        }
        synchronized (ticketpool) {
            releasableTicketCapacity = ticketpool.availabeTotalTicketCapacityCheck(this);
        }
        ticketpool.addTicketToTotalCapacity(releasableTicketCapacity);

        if (releasableTicketCapacity == 0) {
            System.out.println("Maximum Ticket Capacity Reached");
            Thread.currentThread().interrupt();
            System.out.println("Exiting the Vendor : " + vendorId);
        }
        if (releasableTicketCapacity < totalTicketsToRelease && releasableTicketCapacity > 0) {
            //TODO LOG AMOUNT OF TICKETS RELEASED
            System.out.println(getVendorId() + " - " + "Only " + releasableTicketCapacity + " Tickets Released since Maximum Ticket Capacity Reached");
        }

        //Creating Released Tickets and add it to the Main List

        for (int i = 0; i < releasableTicketCapacity; i++) {
            Ticket ticket = new Ticket(Vendor.this);
            //set status to ticket-onPool
            ticket.setStatus(TicketStatus.PENDING);
            releasingTickets.add(0, ticket);

        }
        System.out.println(releasingTickets.size());
        //TODO LOG
    }

    @Override
    public void run() {
        releasableTicketsToMainPool();
        do {
            try {
                int capacityOfTicketPool;
                ArrayList<Ticket> ticketsToPool = new ArrayList<>();
                synchronized (ticketpool) {
                    capacityOfTicketPool = ticketpool.ticketPoolCapacityCheck();
                    //available TicketCapacity (FreeSpace on Pool)
                    if (capacityOfTicketPool == 0) {
                        System.out.println("Ticket pool is Full");
                        ticketpool.wait();

                    }
                }

                if ((getUnReleasingTickets() >= ticketsPerRelease)) {
                    if (ticketsPerRelease > capacityOfTicketPool) {
                        addToTempListFromVendorList(capacityOfTicketPool, ticketsToPool);
                    } else {
                        addToTempListFromVendorList(ticketsPerRelease, ticketsToPool);
                    }
                } else {
                    addToTempListFromVendorList(getUnReleasingTickets(), ticketsToPool);
                }


                ticketpool.addTicket(this, ticketsToPool);

                if (getUnReleasingTickets() == 0) {
                    System.out.println("Ticket Release is Completed");
                    System.out.println("Total Released Tickets : " + getTotalTickets());
                    IsActive = false;
                    //   Thread.currentThread().interrupt();
                }
                Thread.sleep(frequency * 1000L);

            } catch (InterruptedException e) {
                System.out.println("Ticket release interrupted for Vendor: " + vendorId);
                IsActive = false;
                Thread.currentThread().interrupt();
            }
        } while (IsActive);

        //add releasable tickets to TotalTickets


        //      TODO logging the Amount of ticket added amount


    }
}

