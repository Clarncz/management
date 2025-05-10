package com.gym.management.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Payment;
import com.gym.management.repository.PaymentRepository;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    public Payment getPaymentById(Integer id) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        return paymentOpt.orElse(null);
    }
    
    public List<Payment> getPaymentsByCustomer(Customer customer) {
        return paymentRepository.findByCustomer(customer);
    }
    
    public List<Payment> getPaymentsBetweenDates(Date startDate, Date endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }
    
    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }
}