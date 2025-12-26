package com.parik.controller;

import com.parik.model.User;
import com.parik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для обработки запросов аутентификации и регистрации.
 * Управляет страницами входа и регистрации пользователей.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Отображает страницу входа в систему.
     * 
     * @param error параметр ошибки (если есть)
     * @param logout параметр выхода (если есть)
     * @param model модель для передачи данных в представление
     * @return имя представления "auth/login"
     */
    @GetMapping("/auth/login")
    public String loginPage(@RequestParam(required = false) String error, 
                           @RequestParam(required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        return "auth/login";
    }

    /**
     * Отображает страницу регистрации нового пользователя.
     * 
     * @return имя представления "auth/registration"
     */
    @GetMapping("/auth/registration")
    public String registrationPage() {
        return "auth/registration";
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     * Создаёт нового пользователя с указанными данными.
     * 
     * @param username имя пользователя (логин)
     * @param password пароль пользователя
     * @param email электронная почта
     * @param phone телефон (опционально)
     * @param role роль пользователя (по умолчанию "Клиент")
     * @param model модель для передачи данных в представление
     * @return редирект на страницу входа при успехе или страницу регистрации при ошибке
     */
    @PostMapping("/auth/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String email,
                          @RequestParam(required = false) String phone,
                          @RequestParam(defaultValue = "Клиент") String role,
                          Model model) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(password);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);
            
            userService.createUser(user);
            return "redirect:/auth/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/registration";
        }
    }
}

