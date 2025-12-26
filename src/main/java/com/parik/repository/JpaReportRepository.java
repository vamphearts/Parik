package com.parik.repository;

import com.parik.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с отчётами в базе данных.
 * Предоставляет методы для поиска отчётов по датам.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Repository
public interface JpaReportRepository extends JpaRepository<Report, Integer> {
    /**
     * Находит все отчёты за указанный период.
     * 
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @return список отчётов за период
     */
    List<Report> findByReportDateBetween(LocalDate startDate, LocalDate endDate);
}

