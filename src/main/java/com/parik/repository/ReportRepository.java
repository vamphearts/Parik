package com.parik.repository;

import com.parik.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Report> reportRowMapper = (rs, rowNum) -> {
        Report report = new Report();
        report.setId(rs.getInt("id"));
        if (rs.getDate("report_date") != null) {
            report.setReportDate(rs.getDate("report_date").toLocalDate());
        }
        report.setTotalClients(rs.getInt("total_clients"));
        report.setTotalIncome(rs.getBigDecimal("total_income"));
        if (rs.getTimestamp("created_at") != null) {
            report.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return report;
    };

    public List<Report> findAll() {
        String sql = "SELECT * FROM reports ORDER BY report_date DESC";
        return jdbcTemplate.query(sql, reportRowMapper);
    }

    public Optional<Report> findById(Integer id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        List<Report> reports = jdbcTemplate.query(sql, reportRowMapper, id);
        return reports.isEmpty() ? Optional.empty() : Optional.of(reports.get(0));
    }

    public List<Report> findByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM reports WHERE report_date BETWEEN ? AND ? ORDER BY report_date DESC";
        return jdbcTemplate.query(sql, reportRowMapper, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    public Report save(Report report) {
        if (report.getId() == null) {
            return insert(report);
        } else {
            return update(report);
        }
    }

    private Report insert(Report report) {
        String sql = "INSERT INTO reports (report_date, total_clients, total_income, created_at) " +
                     "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(
                report.getReportDate() != null ? report.getReportDate() : LocalDate.now()));
            ps.setInt(2, report.getTotalClients() != null ? report.getTotalClients() : 0);
            ps.setBigDecimal(3, report.getTotalIncome() != null ? report.getTotalIncome() : BigDecimal.ZERO);
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(
                report.getCreatedAt() != null ? report.getCreatedAt() : LocalDateTime.now()));
            return ps;
        }, keyHolder);

        report.setId(keyHolder.getKey().intValue());
        return report;
    }

    private Report update(Report report) {
        String sql = "UPDATE reports SET report_date = ?, total_clients = ?, total_income = ? WHERE id = ?";
        jdbcTemplate.update(sql, Date.valueOf(report.getReportDate()), 
                          report.getTotalClients(), report.getTotalIncome(), report.getId());
        return report;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM reports WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

