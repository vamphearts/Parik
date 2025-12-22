package com.parik.repository;

import com.parik.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaMasterRepository extends JpaRepository<Master, Integer> {
    Optional<Master> findByUserId(Integer userId);
}

