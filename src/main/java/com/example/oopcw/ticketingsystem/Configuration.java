package com.example.oopcw.ticketingsystem;

import com.example.oopcw.ticketingsystem.service.ConfigurationService;

import java.io.IOException;


public class Configuration {
    private int totalTickets;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int ticketReleaseRate;

    public Configuration() {
    }

    public static void main(String[] args) {
        Configuration config = new Configuration();

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