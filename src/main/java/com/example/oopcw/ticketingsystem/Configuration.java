package com.example.oopcw.ticketingsystem;


import java.io.Serial;
import java.io.Serializable;

public class Configuration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int totalTickets;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int ticketReleaseRate;

    public Configuration() {

    }

    public static void main(String[] args) {

    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }
}