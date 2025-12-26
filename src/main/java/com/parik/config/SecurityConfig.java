package com.parik.config;

import com.parik.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

/**
 * Конфигурация безопасности Spring Security.
 * Определяет правила доступа для различных ролей пользователей и настраивает аутентификацию.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth/login", "/auth/registration", "/auth/register", "/about", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Пользователи: GET для всех авторизованных, остальное только для админа
                .requestMatchers(HttpMethod.GET, "/api/users").authenticated()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                // Мастера: GET для всех авторизованных, остальное только для админа
                .requestMatchers(HttpMethod.GET, "/api/masters").authenticated()
                .requestMatchers("/api/masters/**").hasRole("ADMIN")
                // Отчёты: только для админа и мастера
                .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "MASTER")
                .requestMatchers("/api/export-import/**").hasRole("ADMIN")
                // Записи: доступ для всех авторизованных
                .requestMatchers("/api/appointments/**").hasAnyRole("ADMIN", "MASTER", "CLIENT")
                // Услуги: просмотр для всех, редактирование только для админа (контроль через @PreAuthorize)
                .requestMatchers("/api/services/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Для REST API
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

