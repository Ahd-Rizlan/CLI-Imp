package com.example.oopcw.ticketingsystem;

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
                    configurationService.getConfigurationFile("No File Found, Creating a New Configuration File"); //
                    break;
                case "2":
                    configurationService.setConfigurationFile();
                    break;
                case "3":
                    System.out.println("Starting the System here");
                    break;
                case "4":
            }
        }
    }
}
