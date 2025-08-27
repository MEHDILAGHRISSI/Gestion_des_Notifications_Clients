package com.example.notif_backend.service;

import com.example.notif_backend.model.Destinataire;
import com.example.notif_backend.model.Envoi;
import com.example.notif_backend.model.EtatNotification;
import com.example.notif_backend.model.Notification;
import com.example.notif_backend.repository.DestinataireRepository;
import com.example.notif_backend.repository.EnvoiRepository;
import com.example.notif_backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@Service
public class EnvoiService {

    private final EnvoiRepository envoiRepository;

    @Autowired
    private EnvoiSenderFactory envoiSenderFactory;

    @Autowired
    private final NotificationRepository notificationRepository;
    private final DestinataireRepository destinataireRepository;
    private final EmailService emailService;

    public EnvoiService(
            EnvoiRepository envoiRepository,
            NotificationRepository notificationRepository,
            DestinataireRepository destinataireRepository,
            EmailService emailService) {
        this.envoiRepository = envoiRepository;
        this.notificationRepository = notificationRepository;
        this.destinataireRepository = destinataireRepository;
        this.emailService = emailService;
    }

    public List<Envoi> getAll() {
        return envoiRepository.findAll();
    }

    public Envoi getByNumero(Long numero) {
        return envoiRepository.findById(numero).orElse(null);
    }

   public List<Envoi> getByNotificationId(Long notificationId) {
       return envoiRepository.findByNotificationsId(notificationId);
  }

    public Envoi create(Envoi envoi) {
        // On ignore les notifications envoyées lors de la création
        envoi.setNotifications(null); // Optionnel : au cas où elles seraient passées dans le JSON

        // Sauvegarde simple de l'envoi
        return envoiRepository.save(envoi);
    }


    public Envoi update(Long numero, Envoi updated) {
        if (!envoiRepository.existsById(numero)) return null;
        updated.setNumeroEnvoi(numero);
        return envoiRepository.save(updated);
    }

    public void delete(Long numero) {
        envoiRepository.deleteById(numero);
    }

    public List<Notification> getNotificationsByEnvoiId(Long numeroEnvoi) {
        return envoiRepository.findById(numeroEnvoi)
                .map(Envoi::getNotifications)
                .orElse(Collections.emptyList());
    }


    public List<Destinataire> getDestinatairesByEnvoiId(Long numeroEnvoi) {
        return envoiRepository.findById(numeroEnvoi)
                .map(envoi -> envoi.getNotifications().stream()
                        .map(Notification::getDestinataire)  // getDestinataire retourne un seul destinataire
                        .filter(dest -> dest != null)        // on filtre les notifications sans destinataire (par sécurité)
                        .distinct()                         // on évite les doublons si un destinataire est lié à plusieurs notifications
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }



    public Notification ajouterNotificationAEnvoi(Long numeroEnvoi, Notification notification) {

        Notification notif = notificationRepository.findById(notification.getId())
                .orElseThrow(() -> new RuntimeException("Notification introuvable avec l'id: " + notification.getId()));


        System.out.println("maintenant je vais verifier la condition de CREATED ");
        if (notif.getEtat()== EtatNotification.CREATED) {
            System.out.println("j'ai bien verifier la condition de CREATED ");
            Envoi envoi = envoiRepository.findById(numeroEnvoi)
                    .orElseThrow(() -> new RuntimeException("Envoi non trouvé avec l'id: " + numeroEnvoi));

            notif.setEnvoi(envoi); // Lier la notif à l’envoi
            notif.setEtat(EtatNotification.PLANNED);
            return notificationRepository.save(notif); // Sauvegarde
        }
        System.out.println("notification deja envoye");
        return notif;
    }



    @Transactional
    public void processPendingEnvois() {
        LocalDate now = LocalDate.now();
        List<Envoi> envois = envoiRepository.findReadyEnvois(now);

        System.out.println("Traitement des envois en attente...");
        System.out.println("Nombre d'envois à traiter : " + envois.size());

        for (Envoi envoi : envois) {
            System.out.println("Envoi #" + envoi.getNumeroEnvoi() + " avec notifications :");
            if (envoi.getNotifications() == null || envoi.getNotifications().isEmpty()) {
                System.out.println("  Aucune notification associée");
            } else {
                for (Notification notif : envoi.getNotifications()) {
                    System.out.println("  - Notification #" + notif.getId() + ": " + notif.getObjet());
                }
            }
        }

        for (Envoi envoi : envois) {
            try {
                // Utilisation de la Factory Pattern (seul changement !)
                EnvoiSender sender = envoiSenderFactory.getSender(envoi.getForme());
                sender.sendForEnvoi(envoi);

                envoi.setDateEnvoi(now);
                envoiRepository.save(envoi);

            } catch (IllegalArgumentException e) {
                System.err.println("Type d'envoi non supporté: " + envoi.getForme());
            } catch (Exception e) {
                System.err.println("Erreur d'envoi : " + e.getMessage());
            }
        }
    }
}
