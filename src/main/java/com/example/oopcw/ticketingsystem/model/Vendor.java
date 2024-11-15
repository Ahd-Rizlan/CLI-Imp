package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

import java.util.ArrayList;

public class Vendor implements Runnable {

    private static int vendorIdCounter = 00;
    private final int totalTicketsToRelease;
    private final int frequency;
    Configuration config;
    Ticket ticket;
    AutoIdGeneration autoIdGeneration;
    Ticketpool ticketpool;
    private String vendorId;
    private int ticketsPerRelease;
    private ArrayList<Ticket> releasingTickets;

    public Vendor(int totalTicketsToRelease, int ticketsPerRelease, Ticket ticket) {
        this.vendorId = autoIdGeneration.generateAutoId(vendorIdCounter, "VenId");
        this.frequency = config.getTicketReleaseRate();
        this.ticketsPerRelease = ticketsPerRelease;
        this.totalTicketsToRelease = totalTicketsToRelease;
        this.releasingTickets = new ArrayList<>();
        this.ticket = ticket;
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
            return;
        } else {

            int releasableTickets = ticketpool.availabeCapacityCheck(this);
            //amount of space available to complete totalpoolSize
            if (releasableTickets == 0) {
                System.out.println("No Tickets to Release since Maximum Ticket Capacity Reached");
                return;
            }
            if (releasableTickets != totalTicketsToRelease && releasableTickets != 0) {
                //TODO LOG AMOUNT OF TICKETS RELEASED
                System.out.println("Only " + releasableTickets + " Tickets Released since Maximum Ticket Capacity Reached");
            }
            do {
                releasableTickets = releasableTickets - ticketsPerRelease;
                if (releasableTickets < ticketsPerRelease) {
                    ticketsPerRelease = releasableTickets;
                }

                for (int i = 0; i < ticketsPerRelease; i++) {
                    ticket = new Ticket(Vendor.this);
                    ticket.setStatus(TicketStatus.OnPOOL);
                    releasingTickets.add(0, ticket);
                    //Adding Tickets to Customer array
                }
                //      TODO logging the Amount of ticket added amount

            } while (releasableTickets > 0);

            //geting the gap, so I can add only ticket that need to fill the gap
//        int availableCapcity = (ticketpool.getTotalPoolCapacity() - ticketpool.getTotalPoolSize());
//        if (totalTicketsToRelease <= availableCapcity) {
//            //Add the AMount that coming from the Equation
//            //Add Tickets
//            for (int i = 0; i < totalTicketsToRelease; i++) {
//                ticket = new Ticket(Vendor.this);
//                //Add Tickets
            //       }

            //     }

        }


    }
}