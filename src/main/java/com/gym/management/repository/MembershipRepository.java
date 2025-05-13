package com.gym.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    Membership findByCustomer(Customer customer);
}