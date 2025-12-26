package com.parik.repository;

import com.parik.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с записями клиентов в базе данных.
 * Предоставляет методы для поиска записей по различным критериям.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Repository
public interface JpaAppointmentRepository extends JpaRepository<Appointment, Integer> {
    /**
     * Находит все записи клиента.
     * 
     * @param clientId идентификатор клиента
     * @return список записей клиента
     */
    List<Appointment> findByClientId(Integer clientId);
    
    /**
     * Находит все записи мастера.
     * 
     * @param masterId идентификатор мастера
     * @return список записей мастера
     */
    List<Appointment> findByMasterId(Integer masterId);
    
    /**
     * Находит все записи мастера на указанную дату.
     * 
     * @param masterId идентификатор мастера
     * @param date дата записи
     * @return список записей мастера на дату
     */
    List<Appointment> findByMasterIdAndDate(Integer masterId, LocalDate date);
    
    /**
     * Находит все записи с указанным статусом.
     * 
     * @param status статус записи
     * @return список записей с указанным статусом
     */
    List<Appointment> findByStatus(String status);
    
    /**
     * Находит все записи клиента или мастера.
     * 
     * @param clientId идентификатор клиента
     * @param masterId идентификатор мастера
     * @return список записей клиента или мастера
     */
    @Query("SELECT a FROM Appointment a WHERE a.clientId = :clientId OR a.masterId = :masterId")
    List<Appointment> findByClientOrMaster(@Param("clientId") Integer clientId, @Param("masterId") Integer masterId);
}

