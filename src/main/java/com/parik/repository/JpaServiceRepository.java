package com.parik.repository;

import com.parik.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT s FROM Service s WHERE s.name LIKE %:query% OR s.description LIKE %:query%")
    List<Service> search(@Param("query") String query);
}

