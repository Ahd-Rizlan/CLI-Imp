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

    public Vendor(int totalTicketsToRelease, int ticketsPerRelease, int frequency, Ticketpool ticketpool, Configuration config) {
        this.vendorId = vendorAutoIdGeneration.generateAutoId("VId");
        this.frequency = frequency;
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

    public int getTotalTickets() {
        return releasingTickets.size();
    }

    public String toString() {
        return "Vendor{" +
                "Id =" + vendorId +
                "Total Tickets To Release = " + totalTicketsToRelease +
                "Tickets Per Release = " + ticketsPerRelease +
                "Ticket Release Rate =" + frequency +
                '}';
    }

    public boolean getVendorStatus() {
        boolean status = true;
        if (getUnReleasingTickets() == 0) {
            status = false;
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                System.out.println(Thread.currentThread().getName() + " : " + "Total Released Tickets : " + getTotalTickets());
                System.out.println(Thread.currentThread().getName() + " : " + "Ticket Release is Completed");
            }

        }
        return status;
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
                ticketsToPool.add(releasingTickets.get(i));
                ChangedTickets--;
                if (ChangedTickets == 0) {
                    return;
                }
            }
        }
    }

    private boolean releasableTicketsToMainPool() {
        int releasableTicketCapacity;
        releasableTicketCapacity = ticketpool.checkVendorEligibility(this);
        //check the amount of tickets can be added
        if (releasableTicketCapacity == 0) {
            return false;
        } else {
            //TODO LOG AMOUNT OF TICKETS RELEASED
            //Creating Released Tickets and add it to the Main List
            for (int i = 0; i < releasableTicketCapacity; i++) {
                Ticket ticket = new Ticket(Vendor.this);
                ticket.setStatus(TicketStatus.PENDING);
                releasingTickets.add(ticket);
            }
            return true;
            //TODO LOG
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName(getVendorId());
        Thread.currentThread().setPriority(Config.HighPriority);

        boolean IsActive = releasableTicketsToMainPool();
        while (IsActive) {
            try {
                int capacityOfTicketPool;
                capacityOfTicketPool = ticketpool.ticketPoolCapacityCheck();
                ArrayList<Ticket> ticketsForRelease = new ArrayList<>();

                if ((getUnReleasingTickets() >= ticketsPerRelease)) {
                    if (ticketsPerRelease > capacityOfTicketPool) {
                        addToTempListFromVendorList(capacityOfTicketPool, ticketsForRelease);
                    } else {
                        addToTempListFromVendorList(ticketsPerRelease, ticketsForRelease);
                    }
                } else {
                    addToTempListFromVendorList(getUnReleasingTickets(), ticketsForRelease);
                }

                ticketpool.addTicket(this, ticketsForRelease);
                IsActive = getVendorStatus();

                Thread.sleep(frequency * 1000L);

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

