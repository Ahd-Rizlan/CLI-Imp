package com.example.oopcw.ticketingsystem;

import com.example.oopcw.ticketingsystem.model.Customer;
import com.example.oopcw.ticketingsystem.model.Ticketpool;
import com.example.oopcw.ticketingsystem.model.Vendor;
import com.example.oopcw.ticketingsystem.service.ConfigurationService;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Main main = new Main();
        main.mainTemplate();
    }


    public void mainTemplate() {
        Scanner input = new Scanner(System.in);
        ConfigurationService configurationService = new ConfigurationService();
        Configuration configuration = configurationService.readGson();
        Ticketpool ticketpool = new Ticketpool(configuration);
        System.out.println("Welcome to the Ticketing Simulation System\n");
        while (true) {
            System.out.println("Please enter your choice");
            System.out.println("1. Start System");
            System.out.println("2. Change configuration Values");
            System.out.println("3. Change Number of Buyers and Sellers");
            System.out.println("3. Exit");
            switch (input.nextLine().toLowerCase()) {
                case "1":
                    System.out.println("Starting The Simulation");
                    System.out.println("Starting the System here");
                    simulation(ticketpool, configuration);
                    break;
                case "2":
                    configurationService.setConfigurationFile();
                    break;
                case "3":

                    break;
                case "4":
            }
        }
    }

    public void simulation(Ticketpool ticketpool, Configuration configuration) {
        Thread vendor2 = new Thread(new Vendor(200, 100, ticketpool, configuration));
        Thread vendor3 = new Thread(new Vendor(100, 50, ticketpool, configuration));
        Thread vendor4 = new Thread(new Vendor(50, 20, ticketpool, configuration));
        Thread customer = new Thread(new Customer(true, 50, ticketpool, configuration));
        Thread customer2 = new Thread(new Customer(true, 100, ticketpool, configuration));

        // Start vendors in sequence with a slight delay or control if needed
        vendor2.start();
        vendor3.start();
//        vendor3.start();
//        vendor4.start();

        // Start customers
        customer.start();
        customer2.start();

//        try {
//            vendor2.join();
//            vendor3.join();
//            vendor4.join();
//            customer.join();
//        } catch (InterruptedException e) {
//            System.err.println("Thread interrupted: " + e.getMessage());
//        }

        System.out.println("Final Ticket Count: " + ticketpool.getPoolSize());
    }

//
//    public void simulation(Ticketpool ticketpool, Configuration configuration) {
//        //  Vendor vendor2= new Vendor(200, 100, ticketpool, configuration);
//
//        //Thread vendor1 = new Thread();
//        Thread vendor2 = new Thread(new Vendor(200, 100, ticketpool, configuration));
//        Thread vendor3 = new Thread(new Vendor(100, 50, ticketpool, configuration));
//        Thread vendor4 = new Thread(new Vendor(50, 20, ticketpool, configuration));
//        Thread customer = new Thread(new Customer(true, 100, ticketpool, configuration));
//        Thread customer1 = new Thread(new Customer(false, 50, ticketpool, configuration));
//        Thread customer2 = new Thread(new Customer(false, 20, ticketpool, configuration));
//        Thread customer3 = new Thread(new Customer(false, 10, ticketpool, configuration));
//
//        // vendor1.start();
//        vendor2.start();
//        vendor3.start();
//        vendor4.start();
//
//        customer.start();
//        customer1.start();
//        customer2.start();
//        customer3.start();
//
//        try {
////            vendor1.join();
//            vendor2.join();
//            vendor3.join();
//            vendor4.join();
////            vendor5.join();
//            customer1.join();
//            customer2.join();
//            customer3.join();
////            customer4.join();
////            customer5.join();
////            customer6.join();
//        } catch (InterruptedException e) {
//            System.err.println("Thread interrupted: " + e.getMessage());
//        }
//
//        System.out.println("Final Ticket Count: " + ticketpool.getPoolSize());
//    }
//
}
