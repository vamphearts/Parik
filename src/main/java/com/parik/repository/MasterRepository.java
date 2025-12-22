package com.parik.repository;

import com.parik.model.Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MasterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Master> masterRowMapper = (rs, rowNum) -> {
        Master master = new Master();
        master.setId(rs.getInt("id"));
        master.setName(rs.getString("name"));
        master.setSpecialization(rs.getString("specialization"));
        master.setExperience(rs.getInt("experience"));
        master.setRating(rs.getBigDecimal("rating"));
        master.setUserId(rs.getInt("user_id"));
        if (rs.getTimestamp("created_at") != null) {
            master.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return master;
    };

    public List<Master> findAll() {
        String sql = "SELECT * FROM masters ORDER BY id";
        return jdbcTemplate.query(sql, masterRowMapper);
    }

    public Optional<Master> findById(Integer id) {
        String sql = "SELECT * FROM masters WHERE id = ?";
        List<Master> masters = jdbcTemplate.query(sql, masterRowMapper, id);
        return masters.isEmpty() ? Optional.empty() : Optional.of(masters.get(0));
    }

    public Optional<Master> findByUserId(Integer userId) {
        String sql = "SELECT * FROM masters WHERE user_id = ?";
        List<Master> masters = jdbcTemplate.query(sql, masterRowMapper, userId);
        return masters.isEmpty() ? Optional.empty() : Optional.of(masters.get(0));
    }

    public Master save(Master master) {
        if (master.getId() == null) {
            return insert(master);
        } else {
            return update(master);
        }
    }

    private Master insert(Master master) {
        String sql = "INSERT INTO masters (name, specialization, experience, rating, user_id, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, master.getName());
            ps.setString(2, master.getSpecialization());
            ps.setInt(3, master.getExperience() != null ? master.getExperience() : 0);
            ps.setBigDecimal(4, master.getRating() != null ? master.getRating() : BigDecimal.ZERO);
            ps.setInt(5, master.getUserId());
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(
                master.getCreatedAt() != null ? master.getCreatedAt() : LocalDateTime.now()));
            return ps;
        }, keyHolder);

        master.setId(keyHolder.getKey().intValue());
        return master;
    }

    private Master update(Master master) {
        String sql = "UPDATE masters SET name = ?, specialization = ?, experience = ?, " +
                     "rating = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, master.getName(), master.getSpecialization(), 
                          master.getExperience(), master.getRating(), master.getUserId(), master.getId());
        return master;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM masters WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

