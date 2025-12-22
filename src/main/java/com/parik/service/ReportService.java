package com.parik.service;

import com.parik.model.Appointment;
import com.parik.model.Report;
import com.parik.model.Service;
import com.parik.repository.AppointmentRepository;
import com.parik.repository.ReportRepository;
import com.parik.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Integer id) {
        return reportRepository.findById(id);
    }

    public List<Report> getReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        return reportRepository.findByDateRange(startDate, endDate);
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public Report generateReportForDate(LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findAll();
        
        List<Appointment> completedAppointments = appointments.stream()
            .filter(a -> a.getDate().equals(date) && "Выполнена".equals(a.getStatus()))
            .collect(Collectors.toList());
        
        Set<Integer> uniqueClients = completedAppointments.stream()
            .map(Appointment::getClientId)
            .collect(Collectors.toSet());
        
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Appointment appointment : completedAppointments) {
            Optional<Service> service = serviceRepository.findById(appointment.getServiceId());
            if (service.isPresent()) {
                totalIncome = totalIncome.add(service.get().getPrice());
            }
        }
        
        Report report = new Report();
        report.setReportDate(date);
        report.setTotalClients(uniqueClients.size());
        report.setTotalIncome(totalIncome);
        
        return reportRepository.save(report);
    }

    public Report updateReport(Integer id, Report reportDetails) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Отчёт не найден"));
        
        if (reportDetails.getReportDate() != null) {
            report.setReportDate(reportDetails.getReportDate());
        }
        if (reportDetails.getTotalClients() != null) {
            report.setTotalClients(reportDetails.getTotalClients());
        }
        if (reportDetails.getTotalIncome() != null) {
            report.setTotalIncome(reportDetails.getTotalIncome());
        }
        
        return reportRepository.save(report);
    }

    public void deleteReport(Integer id) {
        if (!reportRepository.findById(id).isPresent()) {
            throw new RuntimeException("Отчёт не найден");
        }
        reportRepository.deleteById(id);
    }
}

