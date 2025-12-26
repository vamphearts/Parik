package com.parik.service;

import com.parik.model.User;
import com.parik.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для управления пользователями, включая создание,
 * обновление, удаление и поиск. Обеспечивает хеширование паролей с помощью BCrypt.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Service
public class UserService {

    @Autowired
    private JpaUserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Получает всех пользователей из базы данных.
     * 
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получает пользователя по идентификатору.
     * 
     * @param id идентификатор пользователя
     * @return Optional с пользователем, если найден
     */
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Получает пользователя по имени пользователя (логину).
     * 
     * @param username имя пользователя
     * @return Optional с пользователем, если найден
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Получает пользователя по электронной почте.
     * 
     * @param email электронная почта
     * @return Optional с пользователем, если найден
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Создаёт нового пользователя.
     * Хеширует пароль с помощью BCrypt и проверяет уникальность имени и email.
     * 
     * @param user объект пользователя для создания
     * @return созданный пользователь
     * @throws RuntimeException если пользователь с таким именем или email уже существует
     */
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    /**
     * Обновляет данные существующего пользователя.
     * Хеширует новый пароль, если он указан. Проверяет уникальность имени и email.
     * 
     * @param id идентификатор пользователя для обновления
     * @param userDetails объект с новыми данными пользователя
     * @return обновлённый пользователь
     * @throws RuntimeException если пользователь не найден или имя/email уже заняты
     */
    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        if (userDetails.getUsername() != null) {
            Optional<User> existingUser = userRepository.findByUsername(userDetails.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("Пользователь с таким именем уже существует");
            }
            user.setUsername(userDetails.getUsername());
        }
        
        if (userDetails.getEmail() != null) {
            Optional<User> existingUser = userRepository.findByEmail(userDetails.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
        }
        
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        
        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя по идентификатору.
     * 
     * @param id идентификатор пользователя для удаления
     * @throws RuntimeException если пользователь не найден
     */
    public void deleteUser(Integer id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new RuntimeException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }

    /**
     * Получает всех пользователей с указанной ролью.
     * 
     * @param role роль пользователей (Администратор, Мастер, Клиент)
     * @return список пользователей с указанной ролью
     */
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}

