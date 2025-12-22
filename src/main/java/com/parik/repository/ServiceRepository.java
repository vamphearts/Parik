package com.parik.repository;

import com.parik.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Service> serviceRowMapper = (rs, rowNum) -> {
        Service service = new Service();
        service.setId(rs.getInt("id"));
        service.setName(rs.getString("name"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getBigDecimal("price"));
        service.setDuration(rs.getInt("duration"));
        if (rs.getTimestamp("created_at") != null) {
            service.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return service;
    };

    public List<Service> findAll() {
        String sql = "SELECT * FROM services ORDER BY id";
        return jdbcTemplate.query(sql, serviceRowMapper);
    }

    public Optional<Service> findById(Integer id) {
        String sql = "SELECT * FROM services WHERE id = ?";
        List<Service> services = jdbcTemplate.query(sql, serviceRowMapper, id);
        return services.isEmpty() ? Optional.empty() : Optional.of(services.get(0));
    }

    public Service save(Service service) {
        if (service.getId() == null) {
            return insert(service);
        } else {
            return update(service);
        }
    }

    private Service insert(Service service) {
        String sql = "INSERT INTO services (name, description, price, duration, created_at) " +
                     "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, service.getName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getPrice());
            ps.setInt(4, service.getDuration());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(
                service.getCreatedAt() != null ? service.getCreatedAt() : LocalDateTime.now()));
            return ps;
        }, keyHolder);

        service.setId(keyHolder.getKey().intValue());
        return service;
    }

    private Service update(Service service) {
        String sql = "UPDATE services SET name = ?, description = ?, price = ?, duration = ? WHERE id = ?";
        jdbcTemplate.update(sql, service.getName(), service.getDescription(), 
                          service.getPrice(), service.getDuration(), service.getId());
        return service;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM services WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

