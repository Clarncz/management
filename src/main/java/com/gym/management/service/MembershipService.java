package com.gym.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Membership;
import com.gym.management.repository.MembershipRepository;

@Service
public class MembershipService {
    
    @Autowired
    private MembershipRepository membershipRepository;
    
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }
    
    public Membership getMembershipById(Integer id) {
        Optional<Membership> membershipOpt = membershipRepository.findById(id);
        return membershipOpt.orElse(null);
    }
    
    public Membership getMembershipByCustomer(Customer customer) {
        return membershipRepository.findByCustomer(customer);
    }
    
    public Membership saveMembership(Membership membership) {
        return membershipRepository.save(membership);
    }
    
    public void deleteMembership(Integer id) {
        membershipRepository.deleteById(id);
    }
}