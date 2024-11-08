package com.example.oopcw.ticketingsystem.validation;

import com.example.oopcw.ticketingsystem.Configuration;
import com.example.oopcw.ticketingsystem.service.ConfigurationService;

import java.io.IOException;

public class WriteFiles {

    public void writeOnGson(Configuration configuration) {
        ConfigurationService configurationService = new ConfigurationService();
        try {
            configurationService.writeGson(configuration);
        } catch (IOException e) {
            System.out.println("Unexpected issue occurred while writing to the Gson file ");
            e.printStackTrace();
        }
    }
}
