package com.gym.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.management.entity.Trainer;
import com.gym.management.repository.TrainerRepository;
import com.gym.management.repository.TrainingSessionRepository;

@Service
public class TrainerService {
    
    @Autowired
    private TrainerRepository trainerRepository;
    
    @Autowired
    private TrainingSessionRepository trainingSessionRepository;
    
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }
    
    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }
    
    public Trainer getTrainerById(Integer id) {
        Optional<Trainer> trainerOpt = trainerRepository.findById(id);
        return trainerOpt.orElse(null);
    }
    
    public void deleteTrainer(Integer id) {
        trainerRepository.deleteById(id);
    }
    
    public Map<Trainer, Integer> getTrainersWithCustomerCounts() {
        Map<Trainer, Integer> trainerCustomerCounts = new HashMap<>();
        
        List<Object[]> results = trainingSessionRepository.findTrainersWithCustomerCounts();
        
        for (Object[] result : results) {
            Trainer trainer = (Trainer) result[0];
            Long count = (Long) result[1];
            trainerCustomerCounts.put(trainer, count.intValue());
        }
        
        return trainerCustomerCounts;
    }
    
    public Integer getCustomerCountForTrainer(Trainer trainer) {
        return trainingSessionRepository.countCustomersByTrainer(trainer);
    }
}