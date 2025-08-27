package com.example.notif_backend.service;

import com.example.notif_backend.model.Utilisateur;
import com.example.notif_backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur getById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    public Utilisateur create(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur getByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Utilisateur login(String email, String password) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        if (!utilisateur.getPassword().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return utilisateur;
    }
}
