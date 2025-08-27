package com.example.notif_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class NotificationDetailDTO {
    private Long id;
    private String objet;
    private String contenu;
    private String dateCreation;
    private String signataire;
    private UtilisateurDTO utilisateur;
    private List<EnvoiDTO> envois;
}

