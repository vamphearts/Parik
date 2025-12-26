package com.parik.controller;

import com.parik.model.Report;
import com.parik.service.JpaReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST контроллер для управления отчётами.
 * Предоставляет API endpoints для создания, получения, обновления и удаления отчётов.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Отчёты", description = "API для управления отчётами")
public class ReportController {

    @Autowired
    private JpaReportService reportService;

    /**
     * Получает все отчёты.
     * 
     * @return ResponseEntity со списком всех отчётов
     */
    @GetMapping
    @Operation(summary = "Получить все отчёты")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    /**
     * Получает отчёт по идентификатору.
     * 
     * @param id идентификатор отчёта
     * @return ResponseEntity с отчётом или 404, если не найден
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить отчёт по ID")
    public ResponseEntity<Report> getReportById(@PathVariable Integer id) {
        return reportService.getReportById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает отчёты за указанный период.
     * 
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @return ResponseEntity со списком отчётов за период
     */
    @GetMapping("/range")
    @Operation(summary = "Получить отчёты за период")
    public ResponseEntity<List<Report>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getReportsByDateRange(startDate, endDate));
    }

    /**
     * Создаёт новый отчёт.
     * 
     * @param report объект отчёта для создания
     * @return ResponseEntity с созданным отчётом или сообщением об ошибке
     */
    @PostMapping
    @Operation(summary = "Создать новый отчёт")
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        try {
            Report created = reportService.createReport(report);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Генерирует отчёт за указанную дату.
     * Автоматически подсчитывает количество клиентов и общий доход.
     * 
     * @param date дата для генерации отчёта
     * @return ResponseEntity с созданным отчётом или сообщением об ошибке
     */
    @PostMapping("/generate/{date}")
    @Operation(summary = "Сгенерировать отчёт за дату")
    public ResponseEntity<?> generateReportForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            Report report = reportService.generateReportForDate(date);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Обновляет существующий отчёт.
     * 
     * @param id идентификатор отчёта для обновления
     * @param report объект с новыми данными отчёта
     * @return ResponseEntity с обновлённым отчётом или сообщением об ошибке
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить отчёт")
    public ResponseEntity<?> updateReport(@PathVariable Integer id, @RequestBody Report report) {
        try {
            Report updated = reportService.updateReport(id, report);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удаляет отчёт по идентификатору.
     * 
     * @param id идентификатор отчёта для удаления
     * @return ResponseEntity без содержимого или сообщением об ошибке
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отчёт")
    public ResponseEntity<?> deleteReport(@PathVariable Integer id) {
        try {
            reportService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

