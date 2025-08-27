package com.example.notif_backend.controller;

import com.example.notif_backend.model.RetourEnvoi;
import com.example.notif_backend.service.RetourEnvoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retours")
public class RetourEnvoiController {

    @Autowired
    private RetourEnvoiService retourService;

    @GetMapping
    public List<RetourEnvoi> getAll() {
        return retourService.getAll();
    }

    @GetMapping("/{id}")
    public RetourEnvoi getById(@PathVariable Long id) {
        return retourService.getById(id);
    }

    @PostMapping
    public RetourEnvoi create(@RequestBody RetourEnvoi retour) {
        return retourService.create(retour);
    }

    @GetMapping("/envoi/{id}")
    public List<RetourEnvoi> getByEnvoiId(@PathVariable Long id) {
        return retourService.getByEnvoiId(id);
    }
}
