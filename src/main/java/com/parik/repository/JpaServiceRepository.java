package com.parik.repository;

import com.parik.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с услугами в базе данных.
 * Предоставляет методы для поиска и поиска услуг по различным критериям.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Repository
public interface JpaServiceRepository extends JpaRepository<Service, Integer> {
    /**
     * Находит услуги, название которых содержит указанную строку (без учета регистра).
     * 
     * @param name часть названия услуги
     * @return список найденных услуг
     */
    List<Service> findByNameContainingIgnoreCase(String name);
    
    /**
     * Выполняет поиск услуг по названию или описанию.
     * 
     * @param query поисковый запрос
     * @return список найденных услуг
     */
    @Query("SELECT s FROM Service s WHERE s.name LIKE %:query% OR s.description LIKE %:query%")
    List<Service> search(@Param("query") String query);
}

