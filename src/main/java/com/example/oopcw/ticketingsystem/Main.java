package com.example.oopcw.ticketingsystem;

import com.example.oopcw.ticketingsystem.model.Customer;
import com.example.oopcw.ticketingsystem.model.Ticketpool;
import com.example.oopcw.ticketingsystem.model.Vendor;
import com.example.oopcw.ticketingsystem.service.ConfigurationService;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Main main = new Main();
        main.minTemplate();
    }


    public void minTemplate() {
        Scanner input = new Scanner(System.in);
        ConfigurationService configurationService = new ConfigurationService();
        Configuration configuration = configurationService.readGson();

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
                    Ticketpool ticketpool = new Ticketpool(configuration);
                    System.out.println("Starting the System here");
                    Vendor vendor = new Vendor(50, 5, ticketpool, configuration);
                    Vendor vendor2 = new Vendor(200, 10, ticketpool, configuration);
                    Customer customer = new Customer(false, 50, ticketpool, configuration);
                    Customer customer2 = new Customer(true, 100, ticketpool, configuration);

                    Thread thread = new Thread(vendor);
                    Thread thread2 = new Thread(vendor2);
                    Thread thread3 = new Thread(customer);
                    Thread thread4 = new Thread(customer2);
                    thread.start();
                    thread2.start();
                    thread3.start();
                    thread4.start();


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
}
