package com.gym.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gym.management.entity.Customer;
import com.gym.management.entity.Trainer;
import com.gym.management.entity.TrainingSession;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {
    
    List<TrainingSession> findByTrainer(Trainer trainer);
    
    List<TrainingSession> findByCustomer(Customer customer);
    
    List<TrainingSession> findBySessionDateBetween(Date startDate, Date endDate);
    
    @Query("SELECT COUNT(DISTINCT ts.customer) FROM TrainingSession ts WHERE ts.trainer = :trainer")
    Integer countCustomersByTrainer(@Param("trainer") Trainer trainer);
    
    @Query("SELECT ts.trainer, COUNT(DISTINCT ts.customer) as customerCount FROM TrainingSession ts GROUP BY ts.trainer ORDER BY customerCount DESC")
    List<Object[]> findTrainersWithCustomerCounts();
}