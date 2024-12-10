package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Vendor implements Runnable {

    private static final AutoIdGeneration vendorAutoIdGeneration = new AutoIdGeneration(0);
    private static final Logger logger = LoggerFactory.getLogger(Vendor.class);
    private final Ticketpool ticketpool;
    private final ArrayList<Ticket> releasingTickets;
    private final String vendorId;
    private final int frequency;
    private int totalTicketsToRelease = 0;
    private int ticketsPerRelease;


    public Vendor(int ticketsPerRelease, Ticketpool ticketpool, Configuration configuration) {
        this.vendorId = vendorAutoIdGeneration.generateAutoId("VId");
        this.frequency = configuration.getTicketReleaseRate();
        this.ticketsPerRelease = ticketsPerRelease;
        this.releasingTickets = new ArrayList<>();
        this.ticketpool = ticketpool;

    }

    public Vendor(int ticketsPerRelease, int frequency, Ticketpool ticketpool, Configuration configuration) {
        this.vendorId = vendorAutoIdGeneration.generateAutoId("VId");
        this.frequency = frequency;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releasingTickets = new ArrayList<>();
        this.ticketpool = ticketpool;

    }

    public String getVendorId() {
        return vendorId;
    }

    public int getTotalTicketsToRelease() {
        return totalTicketsToRelease;
    }

    public void setTotalTicketsToRelease(int totalTicketsToRelease) {
        this.totalTicketsToRelease = totalTicketsToRelease;
    }

    public int getTicketsPerRelease() {
        return ticketsPerRelease;
    }

    public void setTicketsPerRelease(int ticketsPerRelease) {
        this.ticketsPerRelease = ticketsPerRelease;
    }
//    public int getTotalTickets() {
//        return releasingTickets.size();
//    }

    @Override
    public String toString() {
        return "Vendor{" +
                "Id =" + vendorId +
                //    "Total Tickets To Release = " + totalTicketsToRelease +
                "Tickets Per Release = " + ticketsPerRelease +
                "Ticket Release Rate =" + frequency +
                '}';
    }
//
//    public boolean getVendorStatus() {
//        boolean status = true;
//        if (getUnReleasingTickets() == 0) {
//            status = false;
//            Thread.currentThread().interrupt();
//            if (Thread.interrupted()) {
//                System.out.println("Vendor " + " - " + Thread.currentThread().getName() + " : " + "Total Released Tickets : " + getTotalTickets());
//                System.out.println("Vendor " + " - " + Thread.currentThread().getName() + " : " + "Ticket Release is Completed");
//            }
//
//        }
//        return status;
//    }

    //    public int getUnReleasingTickets() {
//        int unReleasedTickets = 0;
//        for (Ticket ticket : releasingTickets) {
//            if (ticket.getStatus() == TicketStatus.PENDING) {
//                unReleasedTickets++;
//            }
//        }
//        return unReleasedTickets;
//    }
//    private void addToTempListFromVendorList(Configuration configuration, ArrayList<Ticket> ticketsToPool) {
//        int tickerCount = (configuration.getTotalTickets() / Config.TotalNumberOfVendors);
//        int ChangedTickets = tickerCount;
//        System.out.println(vendorId);
//        for (int i = 0; i < tickerCount; i++) {
//            if (releasingTickets.get(i).getStatus() == TicketStatus.PENDING) {
//                releasingTickets.get(i).setStatus(TicketStatus.OnPOOL);
//                ticketsToPool.add(releasingTickets.get(i));
//                ChangedTickets--;
//                if (ChangedTickets == 0) {
//                    return;
//                }
//            }
//        }
//    }
//    private void addToTempListFromVendorList(int tickerCount, ArrayList<Ticket> ticketsToPool) {
//        int ChangedTickets = tickerCount;
//        for (int i = 0; i < releasingTickets.size(); i++) {
//            if (releasingTickets.get(i).getStatus() == TicketStatus.PENDING) {
//                releasingTickets.get(i).setStatus(TicketStatus.OnPOOL);
//                ticketsToPool.add(releasingTickets.get(i));
//                ChangedTickets--;
//                if (ChangedTickets == 0) {
//                    return;
//                }
//            }
//        }
//    }

//    private boolean releasableTicketsToMainPool() {
//        int releasableTicketCapacity;
//        releasableTicketCapacity = ticketpool.checkVendorEligibility(this);
//        //check the amount of tickets can be added
//        if (releasableTicketCapacity == 0) {
//            return false;
//        } else {
//            //TODO LOG AMOUNT OF TICKETS RELEASED
//            //Creating Released Tickets and add it to the Main List
//            for (int i = 0; i < releasableTicketCapacity; i++) {
//                Ticket ticket = new Ticket(Vendor.this);
//                ticket.setStatus(TicketStatus.PENDING);
//                releasingTickets.add(ticket);
//            }
//            return true;
//            //TODO LOG
//        }
//    }

    @Override
    public void run() {
        Thread.currentThread().setName(getVendorId());
        Thread.currentThread().setPriority(Config.LowPriority);

        // Release Tickets List
        int totalTicketsForRelease;

        //No of TOTAL TICKETS TO BE RELEASED
        totalTicketsForRelease = ticketpool.addTicketsOnMainPool(this);
        // System.out.println("Vendor " + vendorId + " is Releasing " + totalTicketsForRelease + " Tickets");

        //Setting the Total Tickets to be Released
        this.setTotalTicketsToRelease(totalTicketsForRelease);
        logger.debug("Setting the Total Tickets to be Released by {} = {}", vendorId, this.getTotalTicketsToRelease());
        //  System.out.println("Total Tickets to be Released by " + vendorId + " = " + this.getTotalTicketsToRelease());

        // boolean IsActive = true;
        logger.debug("Vendor {} is Releasing Tickets in to the pool until the Maximum Tickets are {} Over , on the rate of {} and in a Interval of {}", vendorId, this.getTotalTicketsToRelease(), this.getTicketsPerRelease(), this.frequency);
        while (totalTicketsForRelease > 0) {
            try {

//                if (this.ticketsPerRelease <= totalTicketsForRelease) {
//                  //  logger.debug("Remaining Tickets in hand is {} and Maximum Tickets that should be released to the pool is {} ");
//                    logger.debug("if Total Tickets in hand = {} lessthan Maximum Tickets Per Release = {} , release all the Tickets in Hand {} ", ticketsPerRelease, totalTicketsForRelease, ticketsPerRelease);
//                    System.out.println(vendorId + "Tickets Per Release = " + ticketsPerRelease);
//                    System.out.println(vendorId + "Tickets that canbe Released  = " + totalTicketsForRelease);
//                    // totalTicketsForRelease = this.ticketsPerRelease;
//                }
                synchronized (ticketpool) {
                    int releasableTickets = Math.min(ticketsPerRelease, (ticketpool.getMaxPoolCapacity() - ticketpool.getTicketPoolSize()));
                    releasableTickets = Math.min(releasableTickets, totalTicketsForRelease);

                    logger.debug("Tickets Per Release = {} / Tickets in Hand = {} /Available Pool Capacity = {}", ticketsPerRelease, totalTicketsForRelease, releasableTickets);
                    logger.debug("Vendor {} is Releasing the minimum amount from the above values {} Tickets to the Pool", vendorId, releasableTickets);
                    logger.info("Tickets Per Release = {} / Tickets in Hand = {} /Available Pool Capacity = {}", ticketsPerRelease, totalTicketsForRelease, releasableTickets);

                    // System.out.println(vendorId + "Tickets Per Release = " + ticketsPerRelease + " / Tickets that canbe Released  = " + totalTicketsForRelease + " /Releasable Tickets = " + releasableTickets);

                    // System.out.println(vendorId + " - Final Relase Tickets " + " = " + releasableTickets);

                    if (releasableTickets == 0) {
                        logger.info("Ticket pool is full , Waiting for the Tickets to be Purchased");

                    } else {
                        logger.info("Vendor {} Released {} Tickets to the Ticket Pool", vendorId, releasableTickets);
                        ticketpool.addTicket(this, releasableTickets);

                        //Substracting the releasable tickets from the total tickets
                        logger.debug("Vendor {} is Substracting the releasable tickets {} from the total tickets {}", vendorId, releasableTickets, totalTicketsForRelease);
                        totalTicketsForRelease -= releasableTickets;
                        logger.debug("Remaining Tickets to Release for Vendor {} is {}", vendorId, totalTicketsForRelease);

                    }
                    if (totalTicketsForRelease == 0) {
                        logger.info("Vendor {} has released all the tickets", vendorId);
                        logger.info("Total Released Tickets by Vendor {} is {}", vendorId, this.getTotalTicketsToRelease());
                        Thread.currentThread().interrupt();
                        if (Thread.interrupted()) {
                            logger.info("Ticket Release is Completed for Vendor {}", vendorId);
                            logger.info("Vendor {} is Released from the Pool ", vendorId);

                        }
                    }

//                System.out.println("---------------------------------------");
//                this.setTicketsPerRelease(totalTicketsForRelease);
//                System.out.println(this.getTotalTicketsToRelease());
//                System.out.println("---------------------------------------");

                }
                //     int capacityOfTicketPool;
                //      capacityOfTicketPool = ticketpool.ticketPoolCapacityCheck();

                // if ((getUnReleasingTickets() >= ticketsPerRelease)) {
                //   if (ticketsPerRelease > capacityOfTicketPool) {
                //      addToTempListFromVendorList(configuration, ticketsForRelease);
                // } else {
                //   addToTempListFromVendorList(ticketsPerRelease, ticketsForRelease);
//                    }
//                } else {
//                    addToTempListFromVendorList(getUnReleasingTickets(), ticketsForRelease);
//                }


                // ADD TICKETS TO MAIN POOL


                // ticketpool.addTicket(this, ticketsForRelease);
                // IsActive = getVendorStatus();

                Thread.sleep(frequency * 1000L);

            } catch (InterruptedException e) {
                logger.error("Ticket release interrupted for Vendor: {}", vendorId);
                System.out.println("Ticket release interrupted for Vendor: " + vendorId);
                Thread.currentThread().interrupt();
                break;

            }
        }

        //add releasable tickets to TotalTickets


        //      TODO logging the Amount of ticket added amount


    }

}

