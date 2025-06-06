package com.gym.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gym.management.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findByEmailAddress(String emailAddress);
}