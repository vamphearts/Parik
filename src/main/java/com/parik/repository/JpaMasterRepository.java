package com.parik.repository;

import com.parik.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с мастерами в базе данных.
 * Предоставляет методы для поиска мастеров по различным критериям.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Repository
public interface JpaMasterRepository extends JpaRepository<Master, Integer> {
    /**
     * Находит мастера по идентификатору пользователя.
     * 
     * @param userId идентификатор пользователя
     * @return Optional с мастером, если найден
     */
    Optional<Master> findByUserId(Integer userId);
}

