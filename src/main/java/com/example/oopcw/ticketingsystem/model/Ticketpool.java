package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.constant.TicketStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {
    private final List<Ticket> ticketPool;
    private final int totalTickets;
    private final int maxCapacity;
    private int currentSizeOfLargePool;

    private int LargePoolSize = Config.TotalTicketsToRelease;

    public Ticketpool(Configuration configuration) {
        totalTickets = configuration.getTotalTickets();
        maxCapacity = configuration.getMaxTicketCapacity();
        this.ticketPool = Collections.synchronizedList(new ArrayList<>(configuration.getMaxTicketCapacity()));

    }


//    public synchronized int checkVendorEligibility(Vendor vendor) {
//
//        int availableCapacity = this.getTicketPoolCapacity() - this.getPoolSize();
//        int vendorTotalTickets = vendor.getTotalTicketsToRelease();
//
//        //currentPoolSize ==  no change only increase
//        if (availableCapacity == 0) {
//            //TODO LOG ABOUT CONDITION
//            Thread.currentThread().interrupt();
//            if (Thread.interrupted()) {
//                System.out.println("Maximum Event Ticket Capacity Reached");
//                System.out.println("Vendor : " + vendor.getVendorId() + " is Removed");
//            }
//        } else {
//            //TODO LOG AVAILABILTY
//            System.out.println(Math.min(vendorTotalTickets, availableCapacity));
//            int releasableTicketAmount = Math.min(vendorTotalTickets, availableCapacity);
//            PoolSize = PoolSize + releasableTicketAmount;
//            currentPoolSize = currentPoolSize + releasableTicketAmount;
//            if (vendorTotalTickets > availableCapacity) {
//                System.out.println("Vendor " + " - " + vendor.getVendorId() + " is Released " + availableCapacity + " Tickets : Since Maximum Event Ticket Capacity Reached");
//                //TODO LOG ABOUT CONDITION
//
//            } else {
//                System.out.println("Vendor " + " - " + vendor.getVendorId() + " is Released " + vendorTotalTickets + " Tickets.");
//                //TODO LOG ABOUT CONDITION
//            }
//        }
//        return Math.min(vendorTotalTickets, availableCapacity);
//    }

//----------------------------------

//----------------------------------

    // to check ticket pool Capacity
//    public synchronized int ticketPoolCapacityCheck() {
//        return getMaxPoolCapacity() - getTicketPoolSize();
//
//    }


    public int getLargePoolSize() {
        return LargePoolSize;
    }

    public void setLargePoolSize(int largePoolSize) {
        LargePoolSize = largePoolSize;
    }

    public synchronized int addTicketsOnMainPool(Vendor vendor) {
        int tickerCount = (Config.TotalTicketsToRelease / Config.TotalNumberOfVendors);

        //updating the total number of tickets
        //   currentSizeOfLargePool = currentSizeOfLargePool + tickerCount;

        System.out.println("Vendor trying to add " + tickerCount);
        //  ArrayList<Ticket> TotalTicketsToBeReleased = new ArrayList<>();
        int TotalTicketsToBeReleased = tickerCount;
        System.out.println(vendor.getVendorId());
        System.out.println("Vendor Added " + tickerCount + " Tickets");
        return TotalTicketsToBeReleased;
    }

//    public synchronized int addTicketsOnMainPool(Configuration configuration) {
//        int tickerCount = (Config.TotalTicketsToRelease / Config.TotalNumberOfVendors);
//        System.out.println("Vendor trying to add " + tickerCount);
//        //  ArrayList<Ticket> TotalTicketsToBeReleased = new ArrayList<>();
//        int TotalTicketsToBeReleased = tickerCount;
//        System.out.println(configuration.getVendorId());
//        System.out.println("Vendor Added " + tickerCount + " Tickets");
////        TotalTicketsToBeReleased.size();
////        return TotalTicketsToBeReleased;
//
//    }


//    public synchronized void addTicket(Vendor vendor, ArrayList<Ticket> tickets) {
//        //TODO update total ticket'
//
//        if (ticketPool.size() == maxCapacity) {
//            System.out.println("TicketPool - " + "Maximum Pool Capacity Reached " + ticketPool.size());
//            try {
//                wait();
//                //TODO
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            ticketPool.addAll(tickets);
//            System.out.println("Vendor" + " : " + vendor.getVendorId() + " Added " + tickets.size() + " tickets : " + "Updated TicketPool Size :" + ticketPool.size());
//            notifyAll();
//            //TODO LOGG AS TICKET ADDED
//        }
//    }

    public synchronized void addTicket(Vendor vendor, int TotalTicketsToBeReleased) {
//        if (TotalTicketsToBeReleased == 0) {
//            Thread.currentThread().interrupt();
//            if (Thread.interrupted()) {
//                System.out.println("Vendor " + vendor.getVendorId() + " : " + "Tickets are Sold Out--------------------------------------------------------------------------------------------------------------------------");
//            }
//        }
        //TODO update total ticket'
        ArrayList<Ticket> tickets = new ArrayList<>();
        if (ticketPool.size() == maxCapacity) {
            System.out.println("TicketPool - " + "Maximum Pool Capacity Reached " + ticketPool.size());
            try {
                wait();
                //TODO
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            for (int i = 0; i < TotalTicketsToBeReleased; i++) {
                Ticket ticket = new Ticket(vendor);
                ticket.setStatus(TicketStatus.PENDING);
                tickets.add(ticket);
                currentSizeOfLargePool++;
            }
            ticketPool.addAll(tickets);
            //  currentSizeOfLargePool = currentSizeOfLargePool + TotalTicketsToBeReleased;
            System.out.println("Vendor" + " : " + vendor.getVendorId() + " Added " + tickets.size() + " tickets : " + "Updated TicketPool Size :" + ticketPool.size());
            notifyAll();

            //TODO LOGG AS TICKET ADDED
        }
    }

    private synchronized ArrayList<Ticket> changeTicketStatusToSold(int tickerCount, ArrayList<Ticket> soldTickets) {
        synchronized (ticketPool) {
            for (int i = ticketPool.size() - 1; i >= 0 && tickerCount > 0; i--) {
                if (ticketPool.get(i).getStatus() == TicketStatus.OnPOOL) {
                    ticketPool.get(i).setStatus(TicketStatus.ACCQUIRED);
                    soldTickets.add(ticketPool.get(i));
                    ticketPool.remove(i); // Safe to remove when iterating backward
                    tickerCount--; // Decrement tickets to process
                }
            }
            return soldTickets;
        }


    }


//    public synchronized void removeTicketToTotalCapacity(int purchasedTicketAmount) {
//        if (currentPoolSize >= purchasedTicketAmount) {
//            currentPoolSize -= purchasedTicketAmount;
//        }
//    }


    //TODO LOGGING
    public synchronized void removeTicket2(Customer customer, ArrayList<Ticket> purchasedTickets) {
        int requiredTickets = customer.getTicketsPerPurchase();
        if (ticketPool.isEmpty() && currentSizeOfLargePool == 0) {
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                System.out.println("Tickets Are Sold Out");
            }

        }

        if (ticketPool.size() < customer.getTicketsPerPurchase()) {
//            System.out.println("-----------------------------------------------------===  " + LargePoolSize);
//            if (LargePoolSize < customer.getTicketsPerPurchase()) {
//                System.out.println("Insufficient Tickets Available---------------------------------------------------------=======");
//                System.out.println(Thread.currentThread().getName());
//
//                Thread.currentThread().interrupt();
//                if (Thread.interrupted()) {
//                    System.out.println(Thread.currentThread().getName());
//                    System.out.println("Tickets Are Sold Out");
//                }
//
//            }

            System.out.println("TicketPool : Please wait while tickets are being updated.");
            System.out.println("TicketPool Available Tickets : " + ticketPool.size());

            //TODO Log
            notifyAll();
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            // currentSizeOfLargePool = currentSizeOfLargePool - requiredTickets;
            for (int i = 0; i < requiredTickets; i++) {
                ticketPool.get(0).setStatus(TicketStatus.ACCQUIRED);
                purchasedTickets.add(ticketPool.get(0));
                ticketPool.remove(0);
                LargePoolSize--;
            }
            System.out.println("Customer" + customer.getCustomerId() + " - " + " Purchased " + customer.getTicketsPerPurchase() + " tickets ;" + "Remaining Tickets Available :" + ticketPool.size());

        }


    }

    //    public synchronized void removeTicket(Customer customer, ArrayList<Ticket> purchasedTickets) {
//        int requiredTickets = customer.getTicketsPerPurchase();
//        purchasedTickets.addAll(changeTicketStatusToSold(requiredTickets, new ArrayList<>()));
//        System.out.println("Customer " + " - " + customer.getCustomerId() + " : " + " Purchased " + customer.getTicketsPerPurchase() + " tickets ;" + "Remaining Tickets Available :" + ticketPool.size());
//        if (ticketPool.size() == 0 && currentSizeOfLargePool == 0) {
//            Thread.currentThread().interrupt();
//            if (Thread.interrupted()) {
//                System.out.println("Tickets Are Sold Out");
//            }
//        }
//        if (ticketPool.size() < requiredTickets) {
//            System.out.println("TicketPool : Please wait while tickets are being updated.");
//            System.out.println("TicketPool Available Tickets : " + ticketPool.size());
//            //TODO Log
//            notifyAll();
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
    public int getMaxPoolCapacity() {
        return maxCapacity;
    }

    public int getTicketPoolSize() {
        return ticketPool.size();
    }

    public int getTicketPoolCapacity() {
        return totalTickets;
    }

    public int getCurrentSizeOfLargePool() {
        return currentSizeOfLargePool;
    }

    public int getCurrentPoolSizePoolSize() {
        return currentSizeOfLargePool;
    }
}

