package com.example.notif_backend.service;

import com.example.notif_backend.dto.*;
import com.example.notif_backend.model.*;
import com.example.notif_backend.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.notif_backend.repository.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class NotificationService {
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private DestinataireRepository destinataireRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AttachementRepository attachementRepository;

    @Autowired
    private EnvoiRepository envoiRepository;

    @Autowired
    private PdfService pdfService;

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public Notification getById(String id) {
        return notificationRepository.findById(Long.valueOf(id)).orElse(null);
    }

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification update(String id, Notification updated) {
        if (!notificationRepository.existsById(Long.valueOf(id))) return null;
        updated.setId(Long.valueOf(id));
        return notificationRepository.save(updated);
    }

    public void delete(String id) {
        notificationRepository.deleteById(Long.valueOf(id));
    }

    public List<NotificationDashboardDTO> getDashboardNotifications() {
        return notificationRepository.fetchDashboardNotifications();
    }

    public NotificationDetailDTO getNotificationDetails(Long id) {
        Notification n = notificationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification non trouvée"));

        NotificationDetailDTO dto = new NotificationDetailDTO();
        dto.setId(n.getId());
        dto.setObjet(n.getObjet());
        dto.setContenu(n.getContenu());
        dto.setDateCreation(n.getDateCreation());
        dto.setSignataire(n.getSignataire());

        Utilisateur u = n.getUtilisateur();
        UtilisateurDTO uDto = null;

        if (u != null) {
            uDto = new UtilisateurDTO();
            uDto.setId(u.getId());
            uDto.setNom(u.getNom());
            uDto.setEmail(u.getEmail());
            uDto.setDepartement(u.getDepartement());
            uDto.setTel(u.getTel());
        }

        dto.setUtilisateur(uDto);

        Envoi envoi = n.getEnvoi();
        List<EnvoiDTO> envoiDTOs = new ArrayList<>();

        if (envoi != null) {
            EnvoiDTO eDto = new EnvoiDTO();
            eDto.setNumeroEnvoi(envoi.getNumeroEnvoi());
            eDto.setDateEnvoi(envoi.getDateEnvoi() != null ? envoi.getDateEnvoi().toString() : null);
            eDto.setDatePrevueEnvoi(envoi.getDatePrevueEnvoi() != null ? envoi.getDatePrevueEnvoi().toString() : null);
            eDto.setForme(envoi.getForme());

            envoiDTOs.add(eDto);
        }

        dto.setEnvois(envoiDTOs);

        return dto;
    }

    public void genererNotifications(
            String templateId,
            List<DestinataireParamDTO> destinataireDTOs,
            Long utilisateurId,
            List<Long> attachementIds
    ) {
        Template template = templateRepository.findById(Long.valueOf(templateId))
                .orElseThrow(() -> new RuntimeException("Template introuvable"));

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Attachement> attachements = (attachementIds != null && !attachementIds.isEmpty())
                ? attachementRepository.findAllById(attachementIds)
                : List.of();

        for (DestinataireParamDTO dto : destinataireDTOs) {

            Destinataire d;

            if (dto.getId() != null) {
                d = destinataireRepository.findById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Destinataire introuvable"));

                Map<String, String> data = dto.getParams() != null ? dto.getParams() : new HashMap<>();

                if (isNotBlank(data.get("nomComplet"))) d.setNomComplet(data.get("nomComplet"));
                if (isNotBlank(data.get("email"))) d.setEmail(data.get("email"));
                if (isNotBlank(data.get("tel"))) d.setTel(data.get("tel"));
                if (isNotBlank(data.get("organisation"))) d.setOrganisation(data.get("organisation"));
                if (isNotBlank(data.get("adresse"))) d.setAdresse(data.get("adresse"));

                destinataireRepository.save(d);
            } else {
                Map<String, String> data = dto.getParams() != null ? dto.getParams() : new HashMap<>();

                d = new Destinataire();
                d.setNomComplet(data.getOrDefault("nomComplet", data.get("nom")));
                d.setEmail(data.get("email"));
                d.setTel(data.get("tel"));
                d.setOrganisation(data.get("organisation"));
                d.setAdresse(data.get("adresse"));

                destinataireRepository.save(d);
                dto.setId(d.getId());
            }

            Map<String, String> params = new HashMap<>();
            params.put("nom", d.getNomComplet());
            params.put("organisation", d.getOrganisation());
            params.put("email", d.getEmail());
            params.put("tel", d.getTel());
            params.put("adresse", d.getAdresse());
            params.put("localdate", LocalDate.now().toString());

            if (dto.getParams() != null) {
                params.putAll(dto.getParams());
            }

            String contenu = personnaliserContenuAvecMap(template.getText(), params);

            Notification notification = new Notification();
            notification.setObjet(template.getTitre());
            notification.setContenu(contenu);
            notification.setDateCreation(LocalDate.now().toString());
            notification.setSignataire(utilisateur.getNom());
            notification.setTemplate(template);
            notification.setUtilisateur(utilisateur);
            notification.setAttachements(attachements);
            notification.setDestinataire(d);
            notification.setEtat(EtatNotification.CREATED);


            // Sauvegarder la notification (pas de génération PDF ici)
            notificationRepository.save(notification);
        }
    }



    // NOUVELLE MÉTHODE: Générer PDF à la demande
    public byte[] generatePdfForNotification(Long notificationId) throws Exception {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification non trouvée"));

        if (!"COURRIER PHYSIQUE".equalsIgnoreCase(notification.getType())) {
            throw new IllegalArgumentException("Cette notification n'est pas de type courrier physique");
        }


        return pdfService.generatePdfBytes(notification);
    }

    private String personnaliserContenuAvecMap(String texte, Map<String, String> params) {
        String result = texte;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue() != null ? entry.getValue() : "");
        }

        result = result.replace("{{localdate}}", LocalDate.now().toString());

        return result;
    }

    private boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public Notification lierNotificationAEnvoi(Long notificationId, Long envoiId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable avec id: " + notificationId));

        if (notification.getEtat().equals(EtatNotification.CREATED)) {
            Envoi envoi = envoiRepository.findById(envoiId)
                    .orElseThrow(() -> new RuntimeException("Envoi introuvable avec id: " + envoiId));

            notification.setEtat(EtatNotification.PLANNED);
            notification.setEnvoi(envoi);
            return notificationRepository.save(notification);
        }
        return notificationRepository.save(notification);
    }


    public List<Notification> findByTemplateTypeAndEtat(String templateType, EtatNotification  etat) {
        // convert string to enum
        return notificationRepository.findWithTemplateByTypeAndEtat(templateType, etat);
    }
}