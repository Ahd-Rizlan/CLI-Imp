package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;

public class RegularCustomer extends Customer {
    private final boolean isVip;

    public RegularCustomer(int ticketsPerPurchase, Ticketpool ticketPool, Configuration config) {

        super(ticketsPerPurchase, ticketPool, config);
        this.isVip = false;
    }


    public boolean isVip() {
        return isVip;
    }

}
