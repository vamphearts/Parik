package com.parik.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "masters")
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 100)
    private String specialization;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer experience;
    
    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 0.00")
    private BigDecimal rating;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Master() {
    }

    public Master(Integer id, String name, String specialization, Integer experience, 
                  BigDecimal rating, Integer userId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.rating = rating;
        this.userId = userId;
        this.createdAt = createdAt;
    }


    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

