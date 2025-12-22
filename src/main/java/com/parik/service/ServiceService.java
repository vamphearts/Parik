package com.parik.service;

import com.parik.model.Service;
import com.parik.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<Service> getServiceById(Integer id) {
        return serviceRepository.findById(id);
    }

    public Service createService(Service service) {
        service.setCreatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    public Service updateService(Integer id, Service serviceDetails) {
        Service service = serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Услуга не найдена"));
        
        if (serviceDetails.getName() != null) {
            service.setName(serviceDetails.getName());
        }
        if (serviceDetails.getDescription() != null) {
            service.setDescription(serviceDetails.getDescription());
        }
        if (serviceDetails.getPrice() != null) {
            service.setPrice(serviceDetails.getPrice());
        }
        if (serviceDetails.getDuration() != null) {
            service.setDuration(serviceDetails.getDuration());
        }
        
        return serviceRepository.save(service);
    }

    public void deleteService(Integer id) {
        if (!serviceRepository.findById(id).isPresent()) {
            throw new RuntimeException("Услуга не найдена");
        }
        serviceRepository.deleteById(id);
    }
}

