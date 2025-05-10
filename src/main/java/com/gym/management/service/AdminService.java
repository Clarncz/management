package com.gym.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym.management.entity.Admin;
import com.gym.management.repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin findByEmail(String emailAddress) {
        return adminRepository.findByEmailAddress(emailAddress);
    }

    public boolean validateAdmin(String emailAddress, String password) {
        Admin admin = findByEmail(emailAddress);
        if (admin != null) {
            return passwordEncoder.matches(password, admin.getPassword());
        }
        return false;
    }

    public Admin saveAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
}
