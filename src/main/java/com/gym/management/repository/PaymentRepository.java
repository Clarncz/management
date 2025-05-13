package com.gym.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCustomer(Customer customer);
    List<Payment> findByPaymentDateBetween(Date startDate, Date endDate);
}