package com.parik.service;

import com.parik.model.Appointment;
import com.parik.repository.JpaAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с записями клиентов.
 * Предоставляет методы для управления записями, включая создание,
 * обновление, удаление, поиск и фильтрацию по различным критериям.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Service
public class JpaAppointmentService {

    @Autowired
    private JpaAppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll(Sort.by("date").descending().and(Sort.by("time").descending()));
    }

    public List<Appointment> getAllAppointments(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        return appointmentRepository.findAll(sort);
    }

    public List<Appointment> searchAppointments(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return appointmentRepository.findByClientOrMaster(id, id);
        } catch (NumberFormatException e) {
            return appointmentRepository.findAll();
        }
    }

    public Optional<Appointment> getAppointmentById(Integer id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByClientId(Integer clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    public List<Appointment> getAppointmentsByMasterId(Integer masterId) {
        return appointmentRepository.findByMasterId(masterId);
    }

    public List<Appointment> getAppointmentsByMasterIdAndDate(Integer masterId, LocalDate date) {
        return appointmentRepository.findByMasterIdAndDate(masterId, date);
    }

    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    public Appointment createAppointment(Appointment appointment) {
        List<Appointment> existingAppointments = appointmentRepository
            .findByMasterIdAndDate(appointment.getMasterId(), appointment.getDate());
        
        for (Appointment existing : existingAppointments) {
            if (!existing.getStatus().equals("Отменена") && 
                existing.getTime().equals(appointment.getTime())) {
                throw new RuntimeException("Это время уже занято");
            }
        }
        
        if (appointment.getStatus() == null) {
            appointment.setStatus("Запланирована");
        }
        if (appointment.getCreatedAt() == null) {
            appointment.setCreatedAt(LocalDateTime.now());
        }
        if (appointment.getUpdatedAt() == null) {
            appointment.setUpdatedAt(LocalDateTime.now());
        }
        
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Integer id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        
        if (appointmentDetails.getClientId() != null) {
            appointment.setClientId(appointmentDetails.getClientId());
        }
        if (appointmentDetails.getMasterId() != null) {
            appointment.setMasterId(appointmentDetails.getMasterId());
        }
        if (appointmentDetails.getServiceId() != null) {
            appointment.setServiceId(appointmentDetails.getServiceId());
        }
        if (appointmentDetails.getDate() != null) {
            appointment.setDate(appointmentDetails.getDate());
        }
        if (appointmentDetails.getTime() != null) {
            appointment.setTime(appointmentDetails.getTime());
        }
        if (appointmentDetails.getStatus() != null) {
            appointment.setStatus(appointmentDetails.getStatus());
        }
        appointment.setUpdatedAt(LocalDateTime.now());
        
        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        appointment.setStatus("Отменена");
        appointment.setUpdatedAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        appointment.setStatus("Выполнена");
        appointment.setUpdatedAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Integer id) {
        if (!appointmentRepository.findById(id).isPresent()) {
            throw new RuntimeException("Запись не найдена");
        }
        appointmentRepository.deleteById(id);
    }
}

