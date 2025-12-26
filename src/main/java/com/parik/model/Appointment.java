package com.parik.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Модель записи на услугу.
 * Представляет сущность записи клиента к мастеру на определенную услугу.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "client_id", nullable = false)
    private Integer clientId;
    
    @Column(name = "master_id", nullable = false)
    private Integer masterId;
    
    @Column(name = "service_id", nullable = false)
    private Integer serviceId;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private LocalTime time;
    
    @Column(nullable = false, length = 20)
    private String status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Конструктор по умолчанию.
     */
    public Appointment() {
    }

    /**
     * Конструктор с параметрами.
     * 
     * @param id уникальный идентификатор записи
     * @param clientId идентификатор клиента
     * @param masterId идентификатор мастера
     * @param serviceId идентификатор услуги
     * @param date дата записи
     * @param time время записи
     * @param status статус записи (Запланирована, Выполнена, Отменена)
     * @param createdAt дата и время создания записи
     * @param updatedAt дата и время последнего обновления
     */
    public Appointment(Integer id, Integer clientId, Integer masterId, Integer serviceId, 
                       LocalDate date, LocalTime time, String status, 
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.clientId = clientId;
        this.masterId = masterId;
        this.serviceId = serviceId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
