package com.example.notif_backend.controller;

import com.example.notif_backend.model.Envoi;
import com.example.notif_backend.model.Notification;
import com.example.notif_backend.service.EnvoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envois")
public class EnvoiController {

    @Autowired
    private EnvoiService envoiService;

    @GetMapping
    public List<Envoi> getAll() {
        return envoiService.getAll();
    }

    @GetMapping("/{numero}")
    public Envoi getByNumero(@PathVariable Long numero) {
        return envoiService.getByNumero(Long.valueOf(numero));
    }

    @GetMapping("/notification/{id}")
    public List<Envoi> getByNotificationId(@PathVariable Long id) {
        return envoiService.getByNotificationId(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Envoi create(@RequestBody Envoi envoi) {
        return envoiService.create(envoi);
    }

    @PutMapping("/{numero}")
    public Envoi update(@PathVariable Long numero, @RequestBody Envoi envoi) {
        return envoiService.update(numero, envoi);
    }

    @DeleteMapping("/{numero}")
    public void delete(@PathVariable Long numero) {
        envoiService.delete(numero);
    }

    @GetMapping("/{numeroEnvoi}/notifications")
    public List<Notification> getNotificationsByEnvoi(@PathVariable Long numeroEnvoi) {
        return envoiService.getNotificationsByEnvoiId(numeroEnvoi);
    }

    @PostMapping("/send-pending")
    public ResponseEntity<Void> sendPending() {
        envoiService.processPendingEnvois();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{numeroEnvoi}/notifications")
    public ResponseEntity<Notification> ajouterNotificationAEnvoi(
            @PathVariable Long numeroEnvoi,
            @RequestBody Notification notification) {

        Notification notificationCreee = envoiService.ajouterNotificationAEnvoi(numeroEnvoi, notification);
        return ResponseEntity.ok(notificationCreee);
    }

}
