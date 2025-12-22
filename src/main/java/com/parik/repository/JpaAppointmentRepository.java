package com.parik.repository;

import com.parik.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByClientId(Integer clientId);
    List<Appointment> findByMasterId(Integer masterId);
    List<Appointment> findByMasterIdAndDate(Integer masterId, LocalDate date);
    List<Appointment> findByStatus(String status);
    
    @Query("SELECT a FROM Appointment a WHERE a.clientId = :clientId OR a.masterId = :masterId")
    List<Appointment> findByClientOrMaster(@Param("clientId") Integer clientId, @Param("masterId") Integer masterId);
}

