package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.constant.TicketStatus;
import com.example.oopcw.ticketingsystem.validation.AutoIdGeneration;

public class Ticket {
    private static int ticketIdCounter = 00;
    AutoIdGeneration autoIdGeneration;
    private String ticketId;
    private TicketStatus status;

    public Ticket() {
        this.ticketId = autoIdGeneration.generateAutoId(ticketIdCounter, "Tid");
    }

    public Ticket(Vendor Vendor) {
        this.ticketId = autoIdGeneration.generateAutoId(ticketIdCounter, Vendor.getVendorId() + "Tid");
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
