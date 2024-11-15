package com.example.oopcw.ticketingsystem.validation;

public class AutoIdGeneration {
    
    public String generateAutoId(int increamantalValue, String Prefix) {
        increamantalValue = increamantalValue + 1;
        return Prefix + increamantalValue;

    }
}
