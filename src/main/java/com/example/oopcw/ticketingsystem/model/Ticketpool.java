package com.example.oopcw.ticketingsystem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ticketpool {
    private List<Ticket> tickets;

    public Ticketpool() {
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }
}
