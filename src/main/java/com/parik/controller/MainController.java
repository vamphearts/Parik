package com.parik.controller;

import com.parik.model.*;
import com.parik.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Основной контроллер для обработки веб-запросов.
 * Отвечает за отображение главной страницы и страницы "Об авторе".
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Controller
public class MainController {

    @Autowired
    private JpaServiceService serviceService;

    @Autowired
    private JpaMasterService masterService;

    @Autowired
    private JpaAppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JpaReportService reportService;

    /**
     * Обрабатывает запрос на главную страницу приложения.
     * Загружает данные об услугах, мастерах, пользователях, записях и отчётах.
     * Поддерживает поиск, сортировку и фильтрацию по ролям пользователей.
     * 
     * @param model модель для передачи данных в представление
     * @param search поисковый запрос (опционально)
     * @param sortBy поле для сортировки (опционально)
     * @param order порядок сортировки (asc/desc, по умолчанию asc)
     * @param tab активная вкладка (services/masters/appointments/reports, по умолчанию services)
     * @return имя представления "index"
     */
    @GetMapping("/")
    public String index(Model model,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false) String sortBy,
                       @RequestParam(required = false, defaultValue = "asc") String order,
                       @RequestParam(required = false, defaultValue = "services") String tab) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            currentUser = userService.getUserByUsername(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        // Услуги
        List<Service> services;
        if (search != null && !search.isEmpty() && tab.equals("services")) {
            services = serviceService.searchServices(search);
        } else if (tab.equals("services")) {
            services = serviceService.getAllServices(sortBy != null ? sortBy : "name", order);
        } else {
            services = serviceService.getAllServices();
        }
        model.addAttribute("services", services);

        // Мастера
        List<Master> masters = masterService.getAllMasters();
        model.addAttribute("masters", masters);

        // Пользователи
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        // Записи
        List<Appointment> appointments;
        // Если пользователь - клиент, показываем только его записи
        if (currentUser != null && "Клиент".equals(currentUser.getRole())) {
            appointments = appointmentService.getAppointmentsByClientId(currentUser.getId());
        } else if (search != null && !search.isEmpty() && tab.equals("appointments")) {
            appointments = appointmentService.searchAppointments(search);
        } else if (tab.equals("appointments") && sortBy != null) {
            appointments = appointmentService.getAllAppointments(sortBy, order);
        } else {
            appointments = appointmentService.getAllAppointments();
        }
        model.addAttribute("appointments", appointments);
        
        // Создаем мапы для быстрого поиска имен по ID
        Map<Integer, String> usersMap = users.stream()
            .collect(Collectors.toMap(User::getId, User::getUsername, (a, b) -> a));
        Map<Integer, String> mastersMap = masters.stream()
            .collect(Collectors.toMap(Master::getId, Master::getName, (a, b) -> a));
        Map<Integer, String> servicesMap = services.stream()
            .collect(Collectors.toMap(Service::getId, Service::getName, (a, b) -> a));
        
        model.addAttribute("usersMap", usersMap);
        model.addAttribute("mastersMap", mastersMap);
        model.addAttribute("servicesMap", servicesMap);

        // Отчёты
        List<Report> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);

        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        model.addAttribute("activeTab", tab);

        return "index";
    }

    /**
     * Обрабатывает запрос на страницу "Об авторе".
     * 
     * @param model модель для передачи данных в представление
     * @return имя представления "about"
     */
    @GetMapping("/about")
    public String about(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User currentUser = userService.getUserByUsername(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }
        return "about";
    }
}

