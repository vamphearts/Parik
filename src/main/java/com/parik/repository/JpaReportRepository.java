package com.parik.repository;

import com.parik.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByReportDateBetween(LocalDate startDate, LocalDate endDate);
}

