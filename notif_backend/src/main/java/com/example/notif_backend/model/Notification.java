package com.example.notif_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    private String objet;

    @Column(name = "contenu", columnDefinition = "TEXT")
    private String contenu;
    private String dateCreation;
    private String signataire;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "notification_attachements",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "attachements_id")
    )
    private List<Attachement> attachements = new ArrayList<>();




    @ManyToOne
    private Utilisateur utilisateur;



    @ManyToOne
    @JoinColumn(name = "envoi_id")
    @JsonIgnoreProperties("notifications")


    private Envoi envoi;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Destinataire destinataire;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatNotification etat = EtatNotification.CREATED;

    public String getType() {
        return template != null ? template.getType() : null;
    }







}

