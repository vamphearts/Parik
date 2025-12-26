package com.parik.controller;

import com.parik.model.Service;
import com.parik.service.JpaServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления услугами салона.
 * Предоставляет API endpoints для создания, получения, обновления и удаления услуг.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@RestController
@RequestMapping("/api/services")
@Tag(name = "Услуги", description = "API для управления услугами")
public class ServiceController {

    @Autowired
    private JpaServiceService serviceService;

    @GetMapping
    @Operation(summary = "Получить все услуги")
    public ResponseEntity<List<Service>> getAllServices(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(serviceService.searchServices(search));
        }
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить услугу по ID")
    public ResponseEntity<Service> getServiceById(@PathVariable Integer id) {
        return serviceService.getServiceById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать новую услугу")
    public ResponseEntity<?> createService(@RequestBody Service service) {
        try {
            Service created = serviceService.createService(service);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить услугу")
    public ResponseEntity<?> updateService(@PathVariable Integer id, @RequestBody Service service) {
        try {
            Service updated = serviceService.updateService(id, service);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить услугу")
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

