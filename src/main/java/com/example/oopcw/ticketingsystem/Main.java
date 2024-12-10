package com.example.oopcw.ticketingsystem;

import com.example.oopcw.ticketingsystem.constant.Config;
import com.example.oopcw.ticketingsystem.model.*;
import com.example.oopcw.ticketingsystem.service.ConfigurationService;
import com.example.oopcw.ticketingsystem.validation.Validation;

import java.util.ArrayList;
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
        Config.TotalTicketsToRelease = configuration.getTotalTickets();
        Ticketpool ticketpool = new Ticketpool(configuration);
        System.out.println("Welcome to the Ticketing Simulation System");
        while (true) {
            System.out.println("""
                    ---------------------------------------
                    Please enter your choice
                    1. Start Simulation
                    2. Change configuration Values 
                    3. PlaceHolder 1
                    4. PlaceHolder 2
                    5. Exit
                    ----------------------------------------
                    Enter your choice :  """);
            switch (input.nextLine().toLowerCase()) {
                case "1":
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
        ArrayList<Thread> customers = new ArrayList<>();
        ArrayList<Thread> vendors = new ArrayList<>();
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("""
                    -----------------------------------------------
                    1. Configure the number of Vendors to be Added 
                    2. Configure the number of Customers to be Added 
                    3. Save and Run Or Run With Default Settings
                    4. Return to main Screen 
                    Please enter your choice :
                    ----------------------------------------------""");

            switch (input.nextLine()) {
                case "1":
                    createVendors(vendors, input, ticketpool, configuration);
                    break;
                case "2":
                    createCustomers(customers, input, ticketpool, configuration);
                    break;
                case "3":
                    startThePool(customers, vendors, ticketpool, configuration);

                    break;
                case "4":
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }

    }

    private void startThePool(ArrayList<Thread> customers, ArrayList<Thread> vendors, Ticketpool ticketpool, Configuration configuration) {

        for (int i = 0; i < Config.DefaultContacts; i++) {
            Thread defaultVendor = new Thread(new Vendor(Config.TicketsPerRelease, ticketpool, configuration));

            // Config.TotalNumberOfVendors++;
            vendors.add(defaultVendor);
//            defaultVendor.start();
        }
        Config.TotalNumberOfVendors = vendors.size();
        for (Thread vendor : vendors) {
            vendor.start();
        }
        for (int i = 0; i < Config.DefaultContacts; i++) {
            if (i / 2 == 0) {
                Thread defaultCustomer = new Thread(new VipCustomer(Config.TicketsPerPurchase, ticketpool, configuration));
                customers.add(defaultCustomer);
            } else {
                Thread defaultCustomer = new Thread(new RegularCustomer(Config.TicketsPerPurchase, ticketpool, configuration));
                customers.add(defaultCustomer);
            }
        }

        for (Thread customer : customers) {
            customer.start();
        }

    }


    //--------------------------------------------------Dynamically Added Vendors -----------------------------------------------------------

    private void createVendors(ArrayList arrayList, Scanner input, Ticketpool ticketpool, Configuration configuration) {

        Validation validation = new Validation();
        int numberOfVendors = validation.getValidation(input, "Please enter the number of Vendors to be Added : ");
        int ticketsPerRelease = validation.getValidation(input, "Enter the Tickets per Release: ");
        int ticketReleaseRate = validation.getValidation(input, "Enter the Ticket Release Rate(seconds): ");
        for (int i = 0; i < numberOfVendors; i++) {
            Config.TotalNumberOfVendors++;
            Thread vendor = new Thread(new Vendor(ticketsPerRelease, ticketReleaseRate, ticketpool, configuration));
            arrayList.add(vendor);

        }
    }

    //--------------------------------------------------Dynamically Added Customers-----------------------------------------------------------

    private void createCustomers(ArrayList arrayList, Scanner input, Ticketpool ticketpool, Configuration configuration) {

        Validation validation = new Validation();
        int numberOfCustomers = validation.getValidation(input, "Please enter the number of Customers to be Added : ");
        boolean isVIp = validation.getBoolean(input, "Customer is a Vip ((Yes/Y) or (No/N)) : ");
        int ticketsPerPurchase = validation.getValidation(input, "Enter the Tickets per Purchase : ");
        // int ticketPurchaseRate = validation.getValidation(input, "Enter the Ticket Purchase Rate(seconds) : ");
        for (int i = 0; i < numberOfCustomers; i++) {
            if (isVIp) {
                Thread customer = new Thread(new VipCustomer(ticketsPerPurchase, ticketpool, configuration));
                customer.setPriority(Config.VipPriority);
                arrayList.add(customer);
            } else {
                Thread customer = new Thread(new RegularCustomer(ticketsPerPurchase, ticketpool, configuration));
                customer.setPriority(Config.LowPriority);
                arrayList.add(customer);
            }
        }
    }

}
