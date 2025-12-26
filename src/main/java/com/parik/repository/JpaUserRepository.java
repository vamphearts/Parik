package com.parik.repository;

import com.parik.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями в базе данных.
 * Предоставляет методы для поиска пользователей по различным критериям.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    /**
     * Находит пользователя по имени пользователя (логину).
     * 
     * @param username имя пользователя
     * @return Optional с пользователем, если найден
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Находит пользователя по электронной почте.
     * 
     * @param email электронная почта
     * @return Optional с пользователем, если найден
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Находит всех пользователей с указанной ролью.
     * 
     * @param role роль пользователей
     * @return список пользователей с указанной ролью
     */
    java.util.List<User> findByRole(String role);
}

