package com.parik.controller;

import com.parik.model.Appointment;
import com.parik.service.JpaAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Записи", description = "API для управления записями клиентов")
public class AppointmentController {

    @Autowired
    private JpaAppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "Получить все записи")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись по ID")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Integer id) {
        return appointmentService.getAppointmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Получить записи клиента")
    public ResponseEntity<List<Appointment>> getAppointmentsByClientId(@PathVariable Integer clientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClientId(clientId));
    }

    @GetMapping("/master/{masterId}")
    @Operation(summary = "Получить записи мастера")
    public ResponseEntity<List<Appointment>> getAppointmentsByMasterId(@PathVariable Integer masterId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByMasterId(masterId));
    }

    @GetMapping("/master/{masterId}/date/{date}")
    @Operation(summary = "Получить записи мастера на дату")
    public ResponseEntity<List<Appointment>> getAppointmentsByMasterIdAndDate(
            @PathVariable Integer masterId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByMasterIdAndDate(masterId, date));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Получить записи по статусу")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Создать новую запись")
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment created = appointmentService.createAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить запись")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
        try {
            Appointment updated = appointmentService.updateAppointment(id, appointment);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Отменить запись")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer id) {
        try {
            Appointment cancelled = appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(cancelled);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Отметить запись как выполненную")
    public ResponseEntity<?> completeAppointment(@PathVariable Integer id) {
        try {
            Appointment completed = appointmentService.completeAppointment(id);
            return ResponseEntity.ok(completed);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить запись")
    public ResponseEntity<?> deleteAppointment(@PathVariable Integer id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

