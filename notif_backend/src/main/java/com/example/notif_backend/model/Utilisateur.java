package com.example.notif_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departement;
    private String nom;
    private String tel;
    private String email;
    private String password;

    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> notifications;
}