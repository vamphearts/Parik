package com.parik.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель пользователя системы.
 * Представляет сущность пользователя с информацией об учетных данных и роли.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(nullable = false, length = 20)
    private String role;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(length = 20)
    private String phone;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Конструктор по умолчанию.
     */
    public User() {
    }

    /**
     * Конструктор с параметрами.
     * 
     * @param id уникальный идентификатор пользователя
     * @param username имя пользователя (логин)
     * @param passwordHash хеш пароля пользователя
     * @param role роль пользователя (Администратор, Мастер, Клиент)
     * @param email электронная почта пользователя
     * @param phone телефон пользователя
     * @param createdAt дата и время создания записи
     */
    public User(Integer id, String username, String passwordHash, String role, 
                String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    /**
     * Получает уникальный идентификатор пользователя.
     * 
     * @return идентификатор пользователя
     */
    public Integer getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор пользователя.
     * 
     * @param id идентификатор пользователя
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Получает имя пользователя (логин).
     * 
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя (логин).
     * 
     * @param username имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получает хеш пароля пользователя.
     * 
     * @return хеш пароля
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Устанавливает хеш пароля пользователя.
     * 
     * @param passwordHash хеш пароля
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Получает роль пользователя.
     * 
     * @return роль пользователя (Администратор, Мастер, Клиент)
     */
    public String getRole() {
        return role;
    }

    /**
     * Устанавливает роль пользователя.
     * 
     * @param role роль пользователя
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Получает электронную почту пользователя.
     * 
     * @return электронная почта
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает электронную почту пользователя.
     * 
     * @param email электронная почта
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Получает телефон пользователя.
     * 
     * @return телефон пользователя
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Устанавливает телефон пользователя.
     * 
     * @param phone телефон пользователя
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Получает дату и время создания записи.
     * 
     * @return дата и время создания
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Устанавливает дату и время создания записи.
     * 
     * @param createdAt дата и время создания
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

