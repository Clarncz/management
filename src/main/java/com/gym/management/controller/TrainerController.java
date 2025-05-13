package com.gym.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gym.management.entity.Trainer;
import com.gym.management.entity.TrainingSession;
import com.gym.management.service.TrainerService;
import com.gym.management.service.TrainingSessionService;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingSessionService trainingSessionService;

    @GetMapping("/list")
    public String listTrainers(Model model) {
        List<Trainer> trainers = trainerService.getAllTrainers();
        model.addAttribute("trainers", trainers);
        model.addAttribute("trainerCustomerCounts", trainerService.getTrainersWithCustomerCounts());
        return "trainers/list";
    }

    @GetMapping("/add")
    public String addTrainerForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "trainers/add";
    }

    @PostMapping("/add")
    public String addTrainer(@ModelAttribute Trainer trainer, RedirectAttributes redirectAttrs) {
        try {
            trainerService.saveTrainer(trainer);
            redirectAttrs.addFlashAttribute("success", "Trainer added successfully");
            return "redirect:/trainers/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to add trainer: " + e.getMessage());
            return "redirect:/trainers/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTrainerForm(@PathVariable Integer id, Model model) {
        Trainer trainer = trainerService.getTrainerById(id);
        if (trainer == null) {
            return "redirect:/trainers/list";
        }
        model.addAttribute("trainer", trainer);
        return "trainers/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTrainer(@PathVariable Integer id, @ModelAttribute Trainer trainer, RedirectAttributes redirectAttrs) {
        try {
            Trainer existingTrainer = trainerService.getTrainerById(id);
            if (existingTrainer == null) {
                redirectAttrs.addFlashAttribute("error", "Trainer not found");
                return "redirect:/trainers/list";
            }

            existingTrainer.setName(trainer.getName());
            existingTrainer.setEmail(trainer.getEmail());
            existingTrainer.setPhoneNumber(trainer.getPhoneNumber());

            trainerService.saveTrainer(existingTrainer);
            redirectAttrs.addFlashAttribute("success", "Trainer updated successfully");
            return "redirect:/trainers/list";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update trainer: " + e.getMessage());
            return "redirect:/trainers/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            Trainer trainer = trainerService.getTrainerById(id);
            if (trainer == null) {
                redirectAttrs.addFlashAttribute("error", "Trainer not found");
                return "redirect:/trainers/list";
            }

            List<TrainingSession> sessions = trainingSessionService.getSessionsByTrainer(trainer);
            if (!sessions.isEmpty()) {
                redirectAttrs.addFlashAttribute("error", "Cannot delete trainer with assigned sessions. Delete sessions first.");
                return "redirect:/trainers/list";
            }

            trainerService.deleteTrainer(id);
            redirectAttrs.addFlashAttribute("success", "Trainer deleted successfully");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to delete trainer: " + e.getMessage());
        }

        return "redirect:/trainers/list";
    }

    @GetMapping("/view/{id}")
    public String viewTrainer(@PathVariable Integer id, Model model) {
        Trainer trainer = trainerService.getTrainerById(id);
        if (trainer == null) {
            return "redirect:/trainers/list";
        }

        List<TrainingSession> sessions = trainingSessionService.getSessionsByTrainer(trainer);
        Integer customerCount = trainerService.getCustomerCountForTrainer(trainer);

        model.addAttribute("trainer", trainer);
        model.addAttribute("sessions", sessions);
        model.addAttribute("customerCount", customerCount);
        return "trainers/view";
    }
}
