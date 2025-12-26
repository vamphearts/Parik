package com.parik.controller;

import com.parik.repository.JpaAppointmentRepository;
import com.parik.repository.JpaMasterRepository;
import com.parik.repository.JpaServiceRepository;
import com.parik.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST контроллер для получения статистики системы.
 * Предоставляет API endpoints для получения различных статистических данных.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaMasterRepository masterRepository;

    @Autowired
    private JpaServiceRepository serviceRepository;

    @Autowired
    private JpaAppointmentRepository appointmentRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userRepository.count());
        stats.put("totalMasters", masterRepository.count());
        stats.put("totalServices", serviceRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        
        long completedAppointments = appointmentRepository.findByStatus("Выполнена").size();
        stats.put("completedAppointments", completedAppointments);
        
        long plannedAppointments = appointmentRepository.findByStatus("Запланирована").size();
        stats.put("plannedAppointments", plannedAppointments);
        
        return ResponseEntity.ok(stats);
    }
}

