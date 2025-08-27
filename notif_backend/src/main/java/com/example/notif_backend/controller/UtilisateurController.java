package com.example.notif_backend.controller;

import com.example.notif_backend.dto.LoginRequest;
import com.example.notif_backend.model.Utilisateur;
import com.example.notif_backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public List<Utilisateur> getAll() {
        return utilisateurService.getAll();
    }

    @GetMapping("/{id}")
    public Utilisateur getById(@PathVariable Long id) {
        return utilisateurService.getById(id);
    }

    @PostMapping
    public Utilisateur create(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.create(utilisateur);
    }

    @GetMapping("/email/{email}")
    public Utilisateur getByEmail(@PathVariable String email) {
        return utilisateurService.getByEmail(email);
    }

    @PostMapping("/login")
    public Utilisateur login(@RequestBody LoginRequest loginRequest) {
        return utilisateurService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

}
