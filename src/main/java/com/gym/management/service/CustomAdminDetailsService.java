package com.gym.management.service;

import com.gym.management.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        System.out.println("Attempting login with: " + emailAddress);
        Admin admin = adminService.findByEmail(emailAddress);
        if (admin == null) {
            System.out.println("No admin found for: " + emailAddress);
            throw new UsernameNotFoundException("No admin found with email: " + emailAddress);
        }
        System.out.println("Admin found: " + admin.getEmailAddress());
        return User.builder()
                .username(admin.getEmailAddress())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }

}
