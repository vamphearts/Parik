package com.parik.dto;

/**
 * DTO (Data Transfer Object) для передачи данных пользователя.
 * Используется для безопасной передачи данных пользователя без пароля в хешированном виде.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;

    public UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

