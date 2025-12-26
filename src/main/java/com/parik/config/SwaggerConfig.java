package com.parik.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger/OpenAPI для документации REST API.
 * Настраивает метаданные API, включая название, версию, описание и контактную информацию.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Создаёт конфигурацию OpenAPI для Swagger UI.
     * 
     * @return объект конфигурации OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Информационно-справочная система парикмахерской")
                .version("1.0.0")
                .description("REST API для управления парикмахерской. " +
                            "Система поддерживает управление пользователями, мастерами, услугами, " +
                            "записями клиентов и формирование отчётов.")
                .contact(new Contact()
                    .name("Разработчик")
                    .email("developer@parik.ru"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}

