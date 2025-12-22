package com.parik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.parik.model.Appointment;
import com.parik.model.Master;
import com.parik.model.Report;
import com.parik.model.Service;
import com.parik.model.User;
import com.parik.service.AppointmentService;
import com.parik.service.MasterService;
import com.parik.service.ReportService;
import com.parik.service.ServiceService;
import com.parik.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/export-import")
@Tag(name = "Экспорт/Импорт", description = "API для экспорта и импорта данных в JSON и CSV форматах")
public class ExportImportController {

    @Autowired
    private UserService userService;

    @Autowired
    private MasterService masterService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ReportService reportService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========== EXPORT JSON ==========

    @GetMapping("/export/users/json")
    @Operation(summary = "Экспорт пользователей в JSON")
    public ResponseEntity<byte[]> exportUsersJson() {
        try {
            List<User> users = userService.getAllUsers();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/masters/json")
    @Operation(summary = "Экспорт мастеров в JSON")
    public ResponseEntity<byte[]> exportMastersJson() {
        try {
            List<Master> masters = masterService.getAllMasters();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(masters);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=masters.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/services/json")
    @Operation(summary = "Экспорт услуг в JSON")
    public ResponseEntity<byte[]> exportServicesJson() {
        try {
            List<Service> services = serviceService.getAllServices();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(services);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=services.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/appointments/json")
    @Operation(summary = "Экспорт записей в JSON")
    public ResponseEntity<byte[]> exportAppointmentsJson() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(appointments);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=appointments.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/reports/json")
    @Operation(summary = "Экспорт отчётов в JSON")
    public ResponseEntity<byte[]> exportReportsJson() {
        try {
            List<Report> reports = reportService.getAllReports();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reports);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== EXPORT CSV ==========

    @GetMapping("/export/users/csv")
    @Operation(summary = "Экспорт пользователей в CSV")
    public ResponseEntity<byte[]> exportUsersCsv() {
        try {
            List<User> users = userService.getAllUsers();
            String csv = convertUsersToCsv(users);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/masters/csv")
    @Operation(summary = "Экспорт мастеров в CSV")
    public ResponseEntity<byte[]> exportMastersCsv() {
        try {
            List<Master> masters = masterService.getAllMasters();
            String csv = convertMastersToCsv(masters);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=masters.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/services/csv")
    @Operation(summary = "Экспорт услуг в CSV")
    public ResponseEntity<byte[]> exportServicesCsv() {
        try {
            List<Service> services = serviceService.getAllServices();
            String csv = convertServicesToCsv(services);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=services.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/appointments/csv")
    @Operation(summary = "Экспорт записей в CSV")
    public ResponseEntity<byte[]> exportAppointmentsCsv() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            String csv = convertAppointmentsToCsv(appointments);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=appointments.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/reports/csv")
    @Operation(summary = "Экспорт отчётов в CSV")
    public ResponseEntity<byte[]> exportReportsCsv() {
        try {
            List<Report> reports = reportService.getAllReports();
            String csv = convertReportsToCsv(reports);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== IMPORT JSON ==========

    @PostMapping("/import/users/json")
    @Operation(summary = "Импорт пользователей из JSON")
    public ResponseEntity<?> importUsersJson(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            User[] users = objectMapper.readValue(content, User[].class);
            
            for (User user : users) {
                try {
                    userService.createUser(user);
                } catch (Exception e) {
                    // Пропускаем дубликаты
                }
            }
            return ResponseEntity.ok("Импортировано пользователей: " + users.length);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка импорта: " + e.getMessage());
        }
    }

    @PostMapping("/import/services/json")
    @Operation(summary = "Импорт услуг из JSON")
    public ResponseEntity<?> importServicesJson(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            Service[] services = objectMapper.readValue(content, Service[].class);
            
            for (Service service : services) {
                try {
                    serviceService.createService(service);
                } catch (Exception e) {
                    // Пропускаем ошибки
                }
            }
            return ResponseEntity.ok("Импортировано услуг: " + services.length);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка импорта: " + e.getMessage());
        }
    }

    // ========== CSV Conversion Helpers ==========

    private String convertUsersToCsv(List<User> users) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeNext(new String[]{"ID", "Username", "Role", "Email", "Phone"});
        for (User user : users) {
            csvWriter.writeNext(new String[]{
                String.valueOf(user.getId()),
                user.getUsername(),
                user.getRole(),
                user.getEmail(),
                user.getPhone() != null ? user.getPhone() : ""
            });
        }
        csvWriter.close();
        return writer.toString();
    }

    private String convertMastersToCsv(List<Master> masters) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeNext(new String[]{"ID", "Name", "Specialization", "Experience", "Rating"});
        for (Master master : masters) {
            csvWriter.writeNext(new String[]{
                String.valueOf(master.getId()),
                master.getName(),
                master.getSpecialization() != null ? master.getSpecialization() : "",
                String.valueOf(master.getExperience()),
                master.getRating() != null ? master.getRating().toString() : "0"
            });
        }
        csvWriter.close();
        return writer.toString();
    }

    private String convertServicesToCsv(List<Service> services) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeNext(new String[]{"ID", "Name", "Description", "Price", "Duration"});
        for (Service service : services) {
            csvWriter.writeNext(new String[]{
                String.valueOf(service.getId()),
                service.getName(),
                service.getDescription() != null ? service.getDescription() : "",
                service.getPrice().toString(),
                String.valueOf(service.getDuration())
            });
        }
        csvWriter.close();
        return writer.toString();
    }

    private String convertAppointmentsToCsv(List<Appointment> appointments) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeNext(new String[]{"ID", "Client ID", "Master ID", "Service ID", "Date", "Time", "Status"});
        for (Appointment appointment : appointments) {
            csvWriter.writeNext(new String[]{
                String.valueOf(appointment.getId()),
                String.valueOf(appointment.getClientId()),
                String.valueOf(appointment.getMasterId()),
                String.valueOf(appointment.getServiceId()),
                appointment.getDate().toString(),
                appointment.getTime().toString(),
                appointment.getStatus()
            });
        }
        csvWriter.close();
        return writer.toString();
    }

    private String convertReportsToCsv(List<Report> reports) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeNext(new String[]{"ID", "Report Date", "Total Clients", "Total Income"});
        for (Report report : reports) {
            csvWriter.writeNext(new String[]{
                String.valueOf(report.getId()),
                report.getReportDate().toString(),
                String.valueOf(report.getTotalClients()),
                report.getTotalIncome().toString()
            });
        }
        csvWriter.close();
        return writer.toString();
    }
}

