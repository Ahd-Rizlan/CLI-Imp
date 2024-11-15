package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.constant.TicketStatus;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

public class Ticket {
    private static final AutoIdGeneration ticketAutoIdGeneration = new AutoIdGeneration();

    private String ticketId;
    private TicketStatus status;

    public Ticket() {
        this.ticketId = ticketAutoIdGeneration.generateAutoId("TId");

    }

    public Ticket(Vendor Vendor) {
        this.ticketId = ticketAutoIdGeneration.generateAutoId("TId");
    }

    public String getTicketId() {
        return ticketId;
    }


    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}


