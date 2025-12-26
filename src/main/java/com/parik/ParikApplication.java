package com.parik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения.
 * Точка входа в приложение системы управления салоном красоты.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@SpringBootApplication
public class ParikApplication {
    /**
     * Точка входа в приложение.
     * 
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ParikApplication.class, args);
    }
}

