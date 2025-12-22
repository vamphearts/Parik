package com.parik.repository;

import com.parik.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Appointment> appointmentRowMapper = (rs, rowNum) -> {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setClientId(rs.getInt("client_id"));
        appointment.setMasterId(rs.getInt("master_id"));
        appointment.setServiceId(rs.getInt("service_id"));
        if (rs.getDate("date") != null) {
            appointment.setDate(rs.getDate("date").toLocalDate());
        }
        if (rs.getTime("time") != null) {
            appointment.setTime(rs.getTime("time").toLocalTime());
        }
        appointment.setStatus(rs.getString("status"));
        if (rs.getTimestamp("created_at") != null) {
            appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            appointment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return appointment;
    };

    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments ORDER BY date DESC, time DESC";
        return jdbcTemplate.query(sql, appointmentRowMapper);
    }

    public Optional<Appointment> findById(Integer id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        List<Appointment> appointments = jdbcTemplate.query(sql, appointmentRowMapper, id);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments.get(0));
    }

    public List<Appointment> findByClientId(Integer clientId) {
        String sql = "SELECT * FROM appointments WHERE client_id = ? ORDER BY date DESC, time DESC";
        return jdbcTemplate.query(sql, appointmentRowMapper, clientId);
    }

    public List<Appointment> findByMasterId(Integer masterId) {
        String sql = "SELECT * FROM appointments WHERE master_id = ? ORDER BY date, time";
        return jdbcTemplate.query(sql, appointmentRowMapper, masterId);
    }

    public List<Appointment> findByMasterIdAndDate(Integer masterId, LocalDate date) {
        String sql = "SELECT * FROM appointments WHERE master_id = ? AND date = ? ORDER BY time";
        return jdbcTemplate.query(sql, appointmentRowMapper, masterId, Date.valueOf(date));
    }

    public List<Appointment> findByStatus(String status) {
        String sql = "SELECT * FROM appointments WHERE status = ? ORDER BY date DESC, time DESC";
        return jdbcTemplate.query(sql, appointmentRowMapper, status);
    }

    public Appointment save(Appointment appointment) {
        if (appointment.getId() == null) {
            return insert(appointment);
        } else {
            return update(appointment);
        }
    }

    private Appointment insert(Appointment appointment) {
        String sql = "INSERT INTO appointments (client_id, master_id, service_id, date, time, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, appointment.getClientId());
            ps.setInt(2, appointment.getMasterId());
            ps.setInt(3, appointment.getServiceId());
            ps.setDate(4, Date.valueOf(appointment.getDate()));
            ps.setTime(5, Time.valueOf(appointment.getTime()));
            ps.setString(6, appointment.getStatus());
            LocalDateTime now = LocalDateTime.now();
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(now));
            ps.setTimestamp(8, java.sql.Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        appointment.setId(keyHolder.getKey().intValue());
        return appointment;
    }

    private Appointment update(Appointment appointment) {
        String sql = "UPDATE appointments SET client_id = ?, master_id = ?, service_id = ?, " +
                     "date = ?, time = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, appointment.getClientId(), appointment.getMasterId(), 
                          appointment.getServiceId(), Date.valueOf(appointment.getDate()),
                          Time.valueOf(appointment.getTime()), appointment.getStatus(), appointment.getId());
        return appointment;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

