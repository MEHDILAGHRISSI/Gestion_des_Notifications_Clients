package com.example.notif_backend.controller;

import com.example.notif_backend.model.Destinataire;
import com.example.notif_backend.service.DestinataireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinataires")
public class DestinataireController {

    @Autowired
    private DestinataireService destinataireService;

    @GetMapping
    public List<Destinataire> getAll() {
        return destinataireService.getAll();
    }

    @GetMapping("/{id}")
    public Destinataire getById(@PathVariable Long id) {
        return destinataireService.getById(id);
    }

    @PostMapping
    public Destinataire create(@RequestBody Destinataire destinataire) {
        return destinataireService.create(destinataire);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        destinataireService.delete(id);
    }

    @PutMapping("/{id}")
    public Destinataire update(@PathVariable Long id, @RequestBody Destinataire destinataire) {
        return destinataireService.update(id, destinataire);
    }

}
