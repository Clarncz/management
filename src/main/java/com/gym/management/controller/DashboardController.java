package com.gym.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Payment;
import com.gym.management.entity.Trainer;
import com.gym.management.service.CustomerService;
import com.gym.management.service.PaymentService;
import com.gym.management.service.TrainerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/*import jakarta.servlet.http.HttpSession;*/

@Controller
public class DashboardController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private TrainerService trainerService;
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // ✅ Get the logged-in admin using Spring Security
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/admin/login";
        }

        // ✅ Optional: Get the logged-in user's email
        String loggedInEmail = "";
        if (auth.getPrincipal() instanceof UserDetails) {
            loggedInEmail = ((UserDetails) auth.getPrincipal()).getUsername(); 
        }

        // Fetch data for dashboard
        List<Customer> customers = customerService.getAllCustomers();
        List<Trainer> trainers = trainerService.getAllTrainers();
        List<Payment> recentPayments = paymentService.getAllPayments();
        Map<Trainer, Integer> trainerCustomerCounts = trainerService.getTrainersWithCustomerCounts();

        model.addAttribute("customerCount", customers.size());
        model.addAttribute("trainerCount", trainers.size());
        model.addAttribute("trainerCustomerCounts", trainerCustomerCounts);
        model.addAttribute("recentPayments", recentPayments);
        model.addAttribute("loggedInEmail", loggedInEmail); // Pass the admin's email to the dashboard

        return "admin/dashboard"; // Make sure templates/dashboard.html exists
    }
}
