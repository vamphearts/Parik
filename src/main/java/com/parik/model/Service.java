package com.parik.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Модель услуги салона.
 * Представляет сущность услуги, предоставляемой салоном.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer duration;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Конструктор по умолчанию.
     */
    public Service() {
    }

    /**
     * Конструктор с параметрами.
     * 
     * @param id уникальный идентификатор услуги
     * @param name название услуги
     * @param description описание услуги
     * @param price цена услуги
     * @param duration длительность услуги в минутах
     * @param createdAt дата и время создания записи
     */
    public Service(Integer id, String name, String description, BigDecimal price, 
                   Integer duration, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
