package com.gym.management.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Membership;
import com.gym.management.service.CustomerService;
import com.gym.management.service.MembershipService;
import com.gym.management.dto.CustomerFormDto;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/list")
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("form", new CustomerFormDto());
        return "customers/add";
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute("form") CustomerFormDto form, RedirectAttributes redirectAttrs) {
        try {
            Customer customer = form.getCustomer();
            if (customer.getJoinDate() == null) {
                customer.setJoinDate(new Date());
            }

            Customer savedCustomer = customerService.saveCustomer(customer);

            Membership membership = form.getMembership();
            if (membership != null && membership.getDuration() != null) {
                membership.setCustomer(savedCustomer);
                membershipService.saveMembership(membership);
            }

            redirectAttrs.addFlashAttribute("success", "Customer added successfully");
            return "redirect:/customers/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to add customer: " + e.getMessage());
            return "redirect:/customers/add";
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customers/list";
        }

        Membership membership = membershipService.getMembershipByCustomer(customer);
        if (membership == null) {
            membership = new Membership();
        }

        model.addAttribute("customer", customer);
        model.addAttribute("membership", membership);
        return "customers/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Integer id,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String email,
                                 @RequestParam String phoneNumber,
                                 @RequestParam String address,
                                 @RequestParam(required = false) Integer duration,
                                 @RequestParam(required = false) Double fee,
                                 RedirectAttributes redirectAttrs) {
        try {
            Customer existingCustomer = customerService.getCustomerById(id);
            if (existingCustomer == null) {
                redirectAttrs.addFlashAttribute("error", "Customer not found");
                return "redirect:/customers/list";
            }

            // Update customer details
            existingCustomer.setFirstName(firstName);
            existingCustomer.setLastName(lastName);
            existingCustomer.setEmail(email);
            existingCustomer.setPhoneNumber(phoneNumber);
            existingCustomer.setAddress(address);

            Customer updatedCustomer = customerService.saveCustomer(existingCustomer);

            // Update or create membership
            Membership existingMembership = membershipService.getMembershipByCustomer(updatedCustomer);
            if (existingMembership != null) {
                existingMembership.setDuration(duration);
                existingMembership.setFee(null);
                membershipService.saveMembership(existingMembership);
            } else if (duration != null) {
                Membership newMembership = new Membership();
                newMembership.setDuration(duration);
                newMembership.setFee(null);
                newMembership.setCustomer(updatedCustomer);
                membershipService.saveMembership(newMembership);
            }

            redirectAttrs.addFlashAttribute("success", "Customer updated successfully");
            return "redirect:/customers/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update customer: " + e.getMessage());
            return "redirect:/customers/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            Customer customer = customerService.getCustomerById(id);
            if (customer == null) {
                redirectAttrs.addFlashAttribute("error", "Customer not found");
                return "redirect:/customers/list";
            }

            Membership membership = membershipService.getMembershipByCustomer(customer);
            if (membership != null) {
                membershipService.deleteMembership(membership.getMembershipId());
            }

            customerService.deleteCustomer(id);
            redirectAttrs.addFlashAttribute("success", "Customer deleted successfully");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to delete customer: " + e.getMessage());
        }

        return "redirect:/customers/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewCustomer(@PathVariable Integer id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customers/list";
        }

        Membership membership = membershipService.getMembershipByCustomer(customer);

        // Calculate expiry date
        Date expiryDate = null;
        if (customer.getJoinDate() != null && membership != null && membership.getDuration() != null) {
            // Add duration in months to joinDate
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(customer.getJoinDate());
            calendar.add(java.util.Calendar.MONTH, membership.getDuration());
            expiryDate = calendar.getTime();
        }

        model.addAttribute("customer", customer);
        model.addAttribute("membership", membership);
        model.addAttribute("expiryDate", expiryDate);

        return "customers/view";
    }


    
}
