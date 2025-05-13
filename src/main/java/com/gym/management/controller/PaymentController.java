package com.gym.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Payment;
import com.gym.management.service.CustomerService;
import com.gym.management.service.PaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/list")
    public String listPayments(Model model) {
        // Check if admin is logged in
		/*
		 * if (session.getAttribute("admin") == null) { return "redirect:/admin/login";
		 * }
		 */
        
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "payments/list";
    }
    
    @GetMapping("/add")
    public String addPaymentForm(Model model, HttpSession session) {
        // Check if admin is logged in
		/*
		 * if (session.getAttribute("admin") == null) { return "redirect:/admin/login";
		 * }
		 */
        
        List<Customer> customers = customerService.getAllCustomers();
        
        model.addAttribute("payment", new Payment());
        model.addAttribute("customers", customers);
        
        return "payments/add";
    }
    
    @PostMapping("/add")
    public String addPayment(@ModelAttribute Payment payment, RedirectAttributes redirectAttrs) {
        try {
            paymentService.savePayment(payment);
            redirectAttrs.addFlashAttribute("success", "Payment added successfully");
            return "redirect:/payments/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to add payment: " + e.getMessage());
            return "redirect:/payments/add";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String editPaymentForm(@PathVariable Integer id, Model model, HttpSession session) {
        // Check if admin is logged in
		/*
		 * if (session.getAttribute("admin") == null) { return "redirect:/admin/login";
		 * }
		 */
        
        Payment payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return "redirect:/payments/list";
        }
        
        List<Customer> customers = customerService.getAllCustomers();
        
        model.addAttribute("payment", payment);
        model.addAttribute("customers", customers);
        
        return "payments/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String updatePayment(@PathVariable Integer id, @ModelAttribute Payment payment, RedirectAttributes redirectAttrs) {
        try {
            Payment existingPayment = paymentService.getPaymentById(id);
            if (existingPayment == null) {
                redirectAttrs.addFlashAttribute("error", "Payment not found");
                return "redirect:/payments/list";
            }
            
            existingPayment.setCustomer(payment.getCustomer());
            existingPayment.setAmount(payment.getAmount());
            existingPayment.setPaymentDate(payment.getPaymentDate());
            
            paymentService.savePayment(existingPayment);
            redirectAttrs.addFlashAttribute("success", "Payment updated successfully");
            return "redirect:/payments/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update payment: " + e.getMessage());
            return "redirect:/payments/edit/" + id;
        }
    }
    
    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Integer id, RedirectAttributes redirectAttrs, HttpSession session) {
        // Check if admin is logged in
		/*
		 * if (session.getAttribute("admin") == null) { return "redirect:/admin/login";
		 * }
		 */
        
        try {
            Payment payment = paymentService.getPaymentById(id);
            if (payment == null) {
                redirectAttrs.addFlashAttribute("error", "Payment not found");
                return "redirect:/payments/list";
            }
            
            paymentService.deletePayment(id);
            redirectAttrs.addFlashAttribute("success", "Payment deleted successfully");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to delete payment: " + e.getMessage());
        }
        
        return "redirect:/payments/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewPayment(@PathVariable Integer id, Model model, HttpSession session) {
        // Check if admin is logged in
		/*
		 * if (session.getAttribute("admin") == null) { return "redirect:/admin/login";
		 * }
		 */
        
        Payment payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return "redirect:/payments/list";
        }
        
        model.addAttribute("payment", payment);
        return "payments/view";
    }
}