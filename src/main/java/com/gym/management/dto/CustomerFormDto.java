package com.gym.management.dto;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Membership;

public class CustomerFormDto {

    private Customer customer;
    private Membership membership;

    public CustomerFormDto() {
        this.customer = new Customer();
        this.membership = new Membership();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
