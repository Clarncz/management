package com.gym.management.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Trainer;
import com.gym.management.entity.TrainingSession;
import com.gym.management.repository.TrainingSessionRepository;

@Service
public class TrainingSessionService {
    
    @Autowired
    private TrainingSessionRepository trainingSessionRepository;
    
    public List<TrainingSession> getAllTrainingSessions() {
        return trainingSessionRepository.findAll();
    }
    
    public TrainingSession saveTrainingSession(TrainingSession trainingSession) {
        return trainingSessionRepository.save(trainingSession);
    }
    
    public TrainingSession getTrainingSessionById(Integer id) {
        Optional<TrainingSession> sessionOpt = trainingSessionRepository.findById(id);
        return sessionOpt.orElse(null);
    }
    
    public List<TrainingSession> getSessionsByTrainer(Trainer trainer) {
        return trainingSessionRepository.findByTrainer(trainer);
    }
    
    public List<TrainingSession> getSessionsByCustomer(Customer customer) {
        return trainingSessionRepository.findByCustomer(customer);
    }
    
    public List<TrainingSession> getSessionsBetweenDates(Date startDate, Date endDate) {
        return trainingSessionRepository.findBySessionDateBetween(startDate, endDate);
    }
    
    public void deleteTrainingSession(Integer id) {
        trainingSessionRepository.deleteById(id);
    }
}