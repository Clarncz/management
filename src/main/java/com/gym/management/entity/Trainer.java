package com.gym.management.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TRAINOR") // Using your table name as defined
@Data
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    private Integer trainerId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "trainer")
    private Set<TrainingSession> trainingSessions = new HashSet<>();

    // Method to get the count of unique customers trained
    public int getCustomerCount() {
        return (int) trainingSessions.stream()
                .map(TrainingSession::getCustomer)
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

	public Integer getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(Integer trainerId) {
		this.trainerId = trainerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<TrainingSession> getTrainingSessions() {
		return trainingSessions;
	}

	public void setTrainingSessions(Set<TrainingSession> trainingSessions) {
		this.trainingSessions = trainingSessions;
	}
}
