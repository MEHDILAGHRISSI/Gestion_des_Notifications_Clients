package com.example.notif_backend.controller;

import com.example.notif_backend.dto.NotificationDashboardDTO;
import com.example.notif_backend.dto.NotificationDetailDTO;
import com.example.notif_backend.dto.NotificationRequestDTO;
import com.example.notif_backend.model.EtatNotification;
import com.example.notif_backend.model.Notification;
import com.example.notif_backend.repository.NotificationRepository;
import com.example.notif_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> getAll() {
        return notificationService.getAll();
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable String id) {
        return notificationService.getById(id);
    }

    @PostMapping
    public Notification create(@RequestBody Notification notification) {
        return notificationService.create(notification);
    }

    @PutMapping("/{id}")
    public Notification update(@PathVariable String id, @RequestBody Notification n) {
        return notificationService.update(id, n);
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable Long id) {
        notificationService.delete(String.valueOf(id));
    }

    @GetMapping("/dashboard")
    public List<NotificationDashboardDTO> getDashboardNotifications() {
        return notificationService.getDashboardNotifications();
    }


    @GetMapping("/{id}/details")
    public ResponseEntity<NotificationDetailDTO> getNotificationDetails(@PathVariable Long id) {
        NotificationDetailDTO details = notificationService.getNotificationDetails(id);
        return ResponseEntity.ok(details);
    }


    @PostMapping("/generer")
    public ResponseEntity<Map<String, String>> genererNotifications(@RequestBody NotificationRequestDTO request) {
        notificationService.genererNotifications(
                request.getTemplateId(),
                request.getDestinataires(),
                request.getUtilisateurId(),
                request.getAttachementIds()
        );
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notifications générées avec succès.");
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{notificationId}/lier-envoi/{envoiId}")
    public ResponseEntity<Notification> lierNotificationAEnvoi(
            @PathVariable Long notificationId,
            @PathVariable Long envoiId) {

        Notification notification = notificationService.lierNotificationAEnvoi(notificationId, envoiId);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/pending")
    public List<Notification> getNotificationsEnAttente() {
        return notificationRepository.findByEtatIn(List.of(EtatNotification.CREATED, EtatNotification.PLANNED));
    }

    /**
     * Générer et télécharger le PDF d'une notification de type courrier physique
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadNotificationPdf(@PathVariable Long id) {
        try {
            byte[] pdfBytes = notificationService.generatePdfForNotification(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "notification_" + id + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lister toutes les notifications de type courrier physique
     */
    @GetMapping("/courrier-physique")
    public List<Notification> getNotificationsCourrierPhysique() {
        return notificationRepository.findByTypeNotification( "Courrier physique");
    }

    @GetMapping("/pending/type/{type}")
    public List<Notification> getNotificationsEnAttenteByType(@PathVariable String type) {
        return notificationService.findByTemplateTypeAndEtat(type, EtatNotification.CREATED);
    }



}
