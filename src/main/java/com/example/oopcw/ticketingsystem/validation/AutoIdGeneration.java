package com.example.oopcw.ticketingsystem.validation;

import java.util.concurrent.atomic.AtomicInteger;

public class AutoIdGeneration {

    private static AtomicInteger incrementalValue = new AtomicInteger(0); // Atomic Integer for thread-safe increments

    // Constructor (initializing value, but it's not really necessary since we start from 0)


    // Generates a unique ID with the specified prefix
    public String generateAutoId(String prefix) {
        int id = incrementalValue.incrementAndGet(); // Atomically increment the value
        return prefix + id;
    }
}
