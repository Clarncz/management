package com.gym.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gym.management.entity.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    
    // Query to find trainers sorted by number of customers trained
    @Query("SELECT t, COUNT(DISTINCT ts.customer) FROM Trainer t " +
           "LEFT JOIN t.trainingSessions ts " +
           "GROUP BY t ORDER BY COUNT(DISTINCT ts.customer) DESC")
    List<Object[]> findTrainersWithCustomerCount();
}