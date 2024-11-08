package com.example.oopcw.ticketingsystem.service;

import com.example.oopcw.ticketingsystem.validation.Validation;
import com.example.oopcw.ticketingsystem.constant.configurationFiles;

import com.example.oopcw.ticketingsystem.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Scanner;

public class ConfigurationService {

    public void writeGson(Configuration configuration) throws IOException {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        File file = new File(configurationFiles.configurationFile);
        Writer writer = new FileWriter(file);
        gson.toJson(configuration, writer);
        writer.close();


    }

    public Configuration readGson() throws IOException {
        File configFile = new File(configurationFiles.configurationFile);

        if (!configFile.exists()) {
            System.out.println("Configuration file does not exist");
            System.out.println("Creating new configuration file");

        } else {
            Gson gson = new Gson();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configurationFiles.configurationFile));
            Configuration configuration = gson.fromJson(bufferedReader, Configuration.class);
            bufferedReader.close();
            return configuration;
        }
        return null;
    }

    public void configurationTemplate() {

        Scanner scanner = new Scanner(System.in);
        Configuration configuration = new Configuration();
        Validation validation = new Validation();
        boolean loop = true;
        while (loop) {
            System.out.println("1. Change Max Ticket pool capacity ");
            System.out.println("2. Change Total Ticket capacity for Vendors ");
            System.out.println("3. Change Release rate ");
            System.out.println("4. Change Purchase rate ");
            System.out.println("5. Change all ");
            switch (scanner.nextLine()) {
                case "1":

                    configuration.setMaxTicketCapacity(validation.getValidation(scanner, "Enter Max Ticket pool capacity :"));
                    break;

                case "2":
                    System.out.println("Enter Total Ticket capacity for Vendors :");
                    configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));
                    break;
                case "3":
                    System.out.println("Enter THE Release rate :");
                    int ReleaseRate = scanner.nextInt();
                    configuration.setMaxTicketCapacity(ReleaseRate);
                    break;

                case "4":
                    System.out.println("Enter Purchase rate :");
                    scanner.nextLine();
                    configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));
                    break;
                case "5":
                    System.out.println("Enter Max Ticket pool capacity :");
                    scanner.nextLine();
                    configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));
                    System.out.println("Enter Total Ticket capacity for Vendors :");
                    scanner.nextLine();
                    configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));
                    System.out.println("Enter THE Release rate :");
                    scanner.nextLine();
                    configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));
                    System.out.println("Enter Purchase rate :");
                    scanner.nextLine();
                    configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));
                    break;

                default:
                    System.out.println("Invalid option");
                    configurationTemplate();


            }
        }
    }

}

