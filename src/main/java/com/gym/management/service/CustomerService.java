package com.gym.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.management.entity.Customer;
import com.gym.management.repository.CustomerRepository;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    public Customer getCustomerById(Integer id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        return customerOpt.orElse(null);
    }
    
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }
}