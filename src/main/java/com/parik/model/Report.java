package com.parik.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    
    @Column(name = "total_clients", columnDefinition = "INT DEFAULT 0")
    private Integer totalClients;
    
    @Column(name = "total_income", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal totalIncome;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Report() {
    }

    public Report(Integer id, LocalDate reportDate, Integer totalClients, 
                  BigDecimal totalIncome, LocalDateTime createdAt) {
        this.id = id;
        this.reportDate = reportDate;
        this.totalClients = totalClients;
        this.totalIncome = totalIncome;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(Integer totalClients) {
        this.totalClients = totalClients;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

