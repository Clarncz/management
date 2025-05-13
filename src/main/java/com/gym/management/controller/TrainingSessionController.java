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
import com.gym.management.entity.Trainer;
import com.gym.management.entity.TrainingSession;
import com.gym.management.service.CustomerService;
import com.gym.management.service.TrainerService;
import com.gym.management.service.TrainingSessionService;

/*import jakarta.servlet.http.HttpSession;*/

@Controller
@RequestMapping("/training-sessions")
public class TrainingSessionController {

    @Autowired
    private TrainingSessionService trainingSessionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TrainerService trainerService;

    @GetMapping("/list")
    public String listSessions(Model model) {
        List<TrainingSession> sessions = trainingSessionService.getAllTrainingSessions();
        model.addAttribute("sessions", sessions);
        return "training-sessions/list";
    }

    @GetMapping("/add")
    public String addSessionForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Trainer> trainers = trainerService.getAllTrainers();

        model.addAttribute("session", new TrainingSession());
        model.addAttribute("customers", customers);
        model.addAttribute("trainers", trainers);

        return "training-sessions/add";
    }

    @PostMapping("/add")
    public String addSession(@ModelAttribute TrainingSession session, RedirectAttributes redirectAttrs) {
        try {
            // Manually fetch the full Customer and Trainer based on submitted IDs
            Customer customer = customerService.getCustomerById(session.getCustomer().getCustomerId());
            Trainer trainer = trainerService.getTrainerById(session.getTrainer().getTrainerId());

            session.setCustomer(customer);
            session.setTrainer(trainer);

            trainingSessionService.saveTrainingSession(session);
            redirectAttrs.addFlashAttribute("success", "Training session added successfully");
            return "redirect:/training-sessions/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to add session: " + e.getMessage());
            return "redirect:/training-sessions/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editSessionForm(@PathVariable Integer id, Model model) {
        TrainingSession trainingSession = trainingSessionService.getTrainingSessionById(id);
        if (trainingSession == null) {
            return "redirect:/training-sessions/list";
        }

        List<Customer> customers = customerService.getAllCustomers();
        List<Trainer> trainers = trainerService.getAllTrainers();

        model.addAttribute("session", trainingSession);
        model.addAttribute("customers", customers);
        model.addAttribute("trainers", trainers);

        return "training-sessions/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSession(@PathVariable Integer id,
                              @ModelAttribute TrainingSession trainingSession,
                              RedirectAttributes redirectAttrs) {
        try {
            TrainingSession existingSession = trainingSessionService.getTrainingSessionById(id);
            if (existingSession == null) {
                redirectAttrs.addFlashAttribute("error", "Training session not found");
                return "redirect:/training-sessions/list";
            }

            // Manually set the customer and trainer from their IDs
            Customer customer = customerService.getCustomerById(trainingSession.getCustomer().getCustomerId());
            Trainer trainer = trainerService.getTrainerById(trainingSession.getTrainer().getTrainerId());

            existingSession.setCustomer(customer);
            existingSession.setTrainer(trainer);
            existingSession.setSessionDate(trainingSession.getSessionDate());
            existingSession.setDurationMinutes(trainingSession.getDurationMinutes());
            existingSession.setNotes(trainingSession.getNotes());

            trainingSessionService.saveTrainingSession(existingSession);
            redirectAttrs.addFlashAttribute("success", "Training session updated successfully");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update session: " + e.getMessage());
        }
        return "redirect:/training-sessions/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            TrainingSession trainingSession = trainingSessionService.getTrainingSessionById(id);
            if (trainingSession == null) {
                redirectAttrs.addFlashAttribute("error", "Training session not found");
                return "redirect:/training-sessions/list";
            }

            trainingSessionService.deleteTrainingSession(id);
            redirectAttrs.addFlashAttribute("success", "Training session deleted successfully");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to delete session: " + e.getMessage());
        }

        return "redirect:/training-sessions/list";
    }

    @GetMapping("/view/{id}")
    public String viewSession(@PathVariable Integer id, Model model) {
        TrainingSession trainingSession = trainingSessionService.getTrainingSessionById(id);
        if (trainingSession == null) {
            return "redirect:/training-sessions/list";
        }

        model.addAttribute("session", trainingSession);
        return "training-sessions/view";
    }
}
