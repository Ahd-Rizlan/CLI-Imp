package com.example.oopcw.ticketingsystem;

import com.example.oopcw.ticketingsystem.service.ConfigurationService;

import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Main main = new Main();
        main.minTemplate();
    }


     public void minTemplate(){
         Scanner input = new Scanner(System.in);
         ConfigurationService configurationService = new ConfigurationService();
        while (true){
        System.out.println("Welcome to the Ticketing Simulation System");
            System.out.println("Please enter your choice");
            System.out.println("1. Start System");
            System.out.println("2. Change configuration Values");
            System.out.println("3. Change Number of Buyers and Sellers");
            System.out.println("3. Exit");
        switch (input.nextLine().toLowerCase()){
            case "1":
                System.out.println("Starting The Simulation");
                break;
            case "2":
                System.out.println("Change Values configurations come here");
                configurationService.configurationTemplate();
                break;
                case "3":
                    System.out.println("Starting the System here");
                    break;
                    case "4":
     }}
}}
