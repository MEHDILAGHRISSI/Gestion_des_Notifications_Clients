package com.example.notif_backend.dto;

import lombok.Data;

@Data
public class UtilisateurDTO {
    private Long id;
    private String nom;
    private String email;
    private String departement;
    private String tel;
}
