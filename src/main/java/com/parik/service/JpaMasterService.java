package com.parik.service;

import com.parik.model.Master;
import com.parik.repository.JpaMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JpaMasterService {

    @Autowired
    private JpaMasterRepository masterRepository;

    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }

    public Optional<Master> getMasterById(Integer id) {
        return masterRepository.findById(id);
    }

    public Optional<Master> getMasterByUserId(Integer userId) {
        return masterRepository.findByUserId(userId);
    }

    public Master createMaster(Master master) {
        if (master.getCreatedAt() == null) {
            master.setCreatedAt(LocalDateTime.now());
        }
        if (master.getRating() == null) {
            master.setRating(BigDecimal.ZERO);
        }
        if (master.getExperience() == null) {
            master.setExperience(0);
        }
        return masterRepository.save(master);
    }

    public Master updateMaster(Integer id, Master masterDetails) {
        Master master = masterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Мастер не найден"));
        
        if (masterDetails.getName() != null) {
            master.setName(masterDetails.getName());
        }
        if (masterDetails.getSpecialization() != null) {
            master.setSpecialization(masterDetails.getSpecialization());
        }
        if (masterDetails.getExperience() != null) {
            master.setExperience(masterDetails.getExperience());
        }
        if (masterDetails.getRating() != null) {
            master.setRating(masterDetails.getRating());
        }
        if (masterDetails.getUserId() != null) {
            master.setUserId(masterDetails.getUserId());
        }
        
        return masterRepository.save(master);
    }

    public void deleteMaster(Integer id) {
        if (!masterRepository.findById(id).isPresent()) {
            throw new RuntimeException("Мастер не найден");
        }
        masterRepository.deleteById(id);
    }
}

