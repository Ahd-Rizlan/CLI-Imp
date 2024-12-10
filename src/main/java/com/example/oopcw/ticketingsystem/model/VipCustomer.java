package com.example.oopcw.ticketingsystem.model;

import com.example.oopcw.ticketingsystem.Configuration;

public class VipCustomer extends Customer {
    private final boolean isVip;

    public VipCustomer(int ticketsPerPurchase, Ticketpool ticketPool, Configuration config) {

        super(ticketsPerPurchase, ticketPool, config);
        this.isVip = true;
        super.setCustomerId("V" + super.getCustomerId());
    }


    public boolean isVip() {
        return isVip;
    }


}
