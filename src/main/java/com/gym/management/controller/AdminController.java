package com.gym.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gym.management.entity.Admin;
import com.gym.management.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

	/*
	 * @PostMapping("/login") public String login(@RequestParam String email,
	 * 
	 * @RequestParam String password, HttpSession session, RedirectAttributes
	 * redirectAttrs) {
	 * 
	 * if (adminService.validateAdmin(email, password)) { Admin admin =
	 * adminService.findByEmail(email); session.setAttribute("admin", admin);
	 * 
	 * // 🟢 Redirect to the saved page if it exists String redirectUrl = (String)
	 * session.getAttribute("redirectAfterLogin");
	 * session.removeAttribute("redirectAfterLogin"); // Clean up return
	 * (redirectUrl != null) ? "redirect:" + redirectUrl : "redirect:/dashboard"; }
	 * else { redirectAttrs.addFlashAttribute("error", "Invalid credentials");
	 * return "redirect:/admin/login"; } }
	 */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Admin admin, RedirectAttributes redirectAttrs) {
        try {
            adminService.saveAdmin(admin);
            redirectAttrs.addFlashAttribute("success", "Admin registered successfully");
            return "redirect:/admin/login";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/admin/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.invalidate(); 
        return "redirect:/admin/login";
    }
}

