package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

import java.util.ArrayList;

public class Vendor implements Runnable {
    private static final AutoIdGeneration vendorAutoIdGeneration = new AutoIdGeneration();
    private final int frequency;
    private final int totalTicketsToRelease;
    Configuration config;
    Ticket ticket;
    Ticketpool ticketpool;
    private String vendorId;
    private int ticketsPerRelease;
    private ArrayList<Ticket> releasingTickets;


    public Vendor(int totalTicketsToRelease, int ticketsPerRelease) {
        this.vendorId = vendorAutoIdGeneration.generateAutoId("VId");
        this.frequency = config.getTicketReleaseRate();
        this.ticketsPerRelease = ticketsPerRelease;
        this.totalTicketsToRelease = totalTicketsToRelease;
        this.releasingTickets = new ArrayList<>();

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

    @Override
    public void run() {
        if (ticketsPerRelease == 0) {
            System.out.println("No Tickets to Release");
            Thread.currentThread().interrupt();
            System.out.println("Exiting the Vendor Thread");
            //if no ticket found , closing the Thread
        } else {

            int releasableTickets = ticketpool.availabeTotalTicketCapacityCheck(this);

            if (releasableTickets == 0) {
                System.out.println("No Tickets to Release since Maximum Ticket Capacity Reached");
            }

            if (releasableTickets != totalTicketsToRelease && releasableTickets != 0) {
                //TODO LOG AMOUNT OF TICKETS RELEASED
                System.out.println("Only " + releasableTickets + " Tickets Released since Maximum Ticket Capacity Reached");
            }
            //add releasable tickets to TotalTickets
            ticketpool.addTicketToTotalCapacity(releasableTickets);

            for (int i = 0; i < ticketsPerRelease; i++) {
                ticket = new Ticket(Vendor.this);
                //set status to ticket-onPool
                ticket.setStatus(TicketStatus.PENDING);
                releasingTickets.add(0, ticket);
                //cehck availablity
//                    availability yes share
                //Adding Tickets to Customer array
            }

            do {
                if (releasableTickets < ticketsPerRelease) {
                    ticketsPerRelease = releasableTickets;
                }
                releasableTickets = releasableTickets - ticketsPerRelease;

                //      TODO logging the Amount of ticket added amount
                try {

                    Thread.sleep(frequency * 1000L);

                    int releasableTicketToTicketPool = ticketpool.ticketPoolCapacityCheck(releasableTickets);
                    ArrayList<Ticket> ticketsToPool = new ArrayList<>();

                    if (releasableTicketToTicketPool >= ticketsPerRelease) {
                        releasableTicketToTicketPool = ticketsPerRelease;
                        //eventhogh pool have more capacity we have to add maximum of tickets per release
                        // if less we have to add maximum possible vale
                    }
                    //Add Tickets to temporary list while updating the status the ticket in main list
                    for (int i = 0; i < releasableTicketToTicketPool; i++) {
                        if (releasingTickets.get(i).getStatus() == TicketStatus.PENDING) {
                            releasingTickets.get(i).setStatus(TicketStatus.OnPOOL);
                            ticketsToPool.add(0, releasingTickets.get(i));
                        }

                    }

                    ticketpool.addTicket(this, ticketsToPool);
                } catch (InterruptedException e) {
                    System.out.println("Ticket release interrupted for Vendor: " + vendorId);
                    Thread.currentThread().interrupt();
                    break;
                }
            } while (releasableTickets > 0);

        }

    }
}

