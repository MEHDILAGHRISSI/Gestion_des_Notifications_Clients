package com.example.notif_backend.service;

import com.example.notif_backend.model.Attachement;
import com.example.notif_backend.repository.AttachementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachementService {
    @Autowired
    private AttachementRepository attachementRepository;

    public List<Attachement> getAll() {
        return attachementRepository.findAll();
    }

    public Attachement getById(Long id) {
        return attachementRepository.findById(id).orElse(null);
    }

    public Attachement create(Attachement attachement) {
        return attachementRepository.save(attachement);
    }

    public void delete(Long id) {
        attachementRepository.deleteById(id);
    }
}
