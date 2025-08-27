package com.example.notif_backend.service;

import com.example.notif_backend.model.Attachement;
import com.example.notif_backend.model.Envoi;
import com.example.notif_backend.model.EtatNotification;
import com.example.notif_backend.model.Notification;
import com.example.notif_backend.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService implements EnvoiSender{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendForEnvoi(Envoi envoi) throws MessagingException {
        sendEmailForEnvoi(envoi);
    }

    @Override
    public boolean supports(String formeEnvoi) {
        return "EMAIL".equalsIgnoreCase(formeEnvoi);
    }

    public void sendEmailForEnvoi(Envoi envoi) throws MessagingException {
        System.out.println(">>> Envoi de l'email pour les notifications de l'envoi #" + envoi.getNumeroEnvoi());


        for (Notification notification : envoi.getNotifications()) {
            if (notification.getEtat().equals(EtatNotification.PLANNED) || notification.getEtat().equals(EtatNotification.FAILED)) {


                var destinataire = notification.getDestinataire();

                if (destinataire == null || destinataire.getEmail() == null) {
                    System.err.println("⚠️ Notification #" + notification.getId() + " sans destinataire ou email null");
                    notification.setEtat(EtatNotification.FAILED);
                    notificationRepository.save(notification);
                    continue;
                }

                System.out.println("➡️ Envoi email à " + destinataire.getEmail() + " pour notification #" + notification.getId());

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(destinataire.getEmail());
                helper.setSubject("Notification : " + notification.getObjet());

                String contenuHtml = notification.getContenu().replace("\n", "<br>");
                helper.setText(contenuHtml, true);

                // Ajout des pièces jointes
                var attachements = notification.getAttachements();
                if (attachements != null && !attachements.isEmpty()) {
                    for (var attachement : attachements) {
                        File file = new File(attachement.getPath());
                        if (file.exists()) {
                            helper.addAttachment(attachement.getFileName(), file);
                        } else {
                            System.err.println("Fichier introuvable : " + attachement.getPath());
                        }
                    }
                }

                mailSender.send(message);
                notification.setEtat(EtatNotification.SENT);
                notificationRepository.save(notification);
                System.out.println("✅ Email envoyé à : " + destinataire.getEmail());
            }
            else {
                System.out.println("EMAIL deja envoye ");

            }
        }


    }}
