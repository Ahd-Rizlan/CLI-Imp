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

//        config.setTotalTickets(10);
//        config.setCustomerRetrievalRate(20);
//        config.setMaxTicketCapacity(50); // Set this value as it’s missing in the example
//        config.setTicketReleaseRate(34);
//        Configuration config2 = new Configuration();
//        config2.setTotalTickets(12);
//        config2.setCustomerRetrievalRate(43);
//        config2.setMaxTicketCapacity(45); // Set this value as it’s missing in the example
//        config2.setTicketReleaseRate(65);
//
//        ConfigurationService configurationService = new ConfigurationService();
//
//        // Writing to file
//        try {
////            configurationService.writeGson(config);
//            configurationService.writeGson(config2);
//            System.out.println("Configuration written to file successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Reading from file
//
//        try {
//            configurationService.readGson();
//            Configuration configuration= new Configuration();
//            configuration.setTicketReleaseRate(configurationService.readGson().ticketReleaseRate);
//            configuration.setTotalTickets(configurationService.readGson().totalTickets);
//            configuration.setCustomerRetrievalRate(configurationService.readGson().customerRetrievalRate);
//            configuration.setMaxTicketCapacity(configurationService.readGson().maxTicketCapacity);
//            System.out.println(configurationService.readGson());
//            System.out.println("ticketRelese Rate: " + configuration.getTicketReleaseRate());
//            System.out.println("customerRetrievalRate: " + configuration.getCustomerRetrievalRate());
//            System.out.println("maxTicketCapacity: " + configuration.getMaxTicketCapacity());
//            System.out.println("totalTickets: " + configuration.getTotalTickets());
//
//            // Access configuration properties
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println();
//    }
//

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