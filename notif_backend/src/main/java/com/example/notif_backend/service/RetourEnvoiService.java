package com.example.notif_backend.service;

import com.example.notif_backend.model.RetourEnvoi;
import com.example.notif_backend.repository.RetourEnvoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetourEnvoiService {
    @Autowired
    private RetourEnvoiRepository retourEnvoiRepository;

    public List<RetourEnvoi> getAll() {
        return retourEnvoiRepository.findAll();
    }

    public RetourEnvoi getById(Long id) {
        return retourEnvoiRepository.findById(id).orElse(null);
    }

    public RetourEnvoi create(RetourEnvoi retourEnvoi) {
        return retourEnvoiRepository.save(retourEnvoi);
    }

    public List<RetourEnvoi> getByEnvoiId(Long envoiId) {
        return retourEnvoiRepository.findByEnvoiNumeroEnvoi(envoiId);
    }
}
