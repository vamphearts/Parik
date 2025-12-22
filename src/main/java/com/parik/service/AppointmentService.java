package com.parik.service;

import com.parik.model.Appointment;
import com.parik.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
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
        // Проверка на конфликты времени
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
        
        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        appointment.setStatus("Отменена");
        return appointmentRepository.save(appointment);
    }

    public Appointment completeAppointment(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        appointment.setStatus("Выполнена");
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Integer id) {
        if (!appointmentRepository.findById(id).isPresent()) {
            throw new RuntimeException("Запись не найдена");
        }
        appointmentRepository.deleteById(id);
    }
}

