package com.parik.controller;

import com.parik.model.Master;
import com.parik.service.JpaMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления мастерами салона.
 * Предоставляет API endpoints для создания, получения, обновления и удаления мастеров.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@RestController
@RequestMapping("/api/masters")
@Tag(name = "Мастера", description = "API для управления мастерами")
public class MasterController {

    @Autowired
    private JpaMasterService masterService;

    @GetMapping
    @Operation(summary = "Получить всех мастеров")
    public ResponseEntity<List<Master>> getAllMasters() {
        return ResponseEntity.ok(masterService.getAllMasters());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить мастера по ID")
    public ResponseEntity<Master> getMasterById(@PathVariable Integer id) {
        return masterService.getMasterById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить мастера по ID пользователя")
    public ResponseEntity<Master> getMasterByUserId(@PathVariable Integer userId) {
        return masterService.getMasterByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать нового мастера")
    public ResponseEntity<?> createMaster(@RequestBody Master master) {
        try {
            Master created = masterService.createMaster(master);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить мастера")
    public ResponseEntity<?> updateMaster(@PathVariable Integer id, @RequestBody Master master) {
        try {
            Master updated = masterService.updateMaster(id, master);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить мастера")
    public ResponseEntity<?> deleteMaster(@PathVariable Integer id) {
        try {
            masterService.deleteMaster(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

