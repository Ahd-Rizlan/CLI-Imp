package com.example.oopcw.ticketingsystem.service;

import com.example.oopcw.ticketingsystem.validation.Validation;
import com.example.oopcw.ticketingsystem.constant.configurationFiles;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.validation.WriteFiles;
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
        WriteFiles writeFiles = new WriteFiles();
        Validation validation = new Validation();
        boolean loop = true;
        while (loop) {
            System.out.println("1. Change Max Ticket pool capacity ");
            System.out.println("2. Change Total Ticket capacity for Vendors ");
            System.out.println("3. Change Release rate ");
            System.out.println("4. Change Purchase rate ");
            System.out.println("5. Change all ");
            System.out.println("6. Exit");
            switch (scanner.nextLine()) {
                case "1":
                    configuration.setMaxTicketCapacity(validation.getValidation(scanner, "Enter Max Ticket pool capacity :"));
                    writeFiles.writeOnGson(configuration);
                    break;
                case "2":
                    configuration.setTotalTickets(validation.getValidation(scanner, "Enter Total Ticket capacity for Vendors : "));
                    writeFiles.writeOnGson(configuration);
                    break;
                case "3":
                    configuration.setTicketReleaseRate(validation.getValidation(scanner, "Enter The Release rate : "));
                    writeFiles.writeOnGson(configuration);
                    break;
                case "4":
                    configuration.setCustomerRetrievalRate(validation.getValidation(scanner, "Enter Purchase rate : "));
                    writeFiles.writeOnGson(configuration);
                    break;
                case "5":
                    configuration.setMaxTicketCapacity(validation.getValidation(scanner, "Enter Max Ticket pool capacity :"));
                    configuration.setTotalTickets(validation.getValidation(scanner, "Enter Total Ticket capacity for Vendors : "));
                    configuration.setTicketReleaseRate(validation.getValidation(scanner, "Enter The Release rate : "));
                    configuration.setCustomerRetrievalRate(validation.getValidation(scanner, "Enter Purchase rate : "));
                    writeFiles.writeOnGson(configuration);
                    break;
                case "6":
                    System.out.println("Goodbye");
                    loop = false;
                    break;

                default:
                    System.out.println("Invalid option");
                    configurationTemplate();


            }
        }
    }

}

