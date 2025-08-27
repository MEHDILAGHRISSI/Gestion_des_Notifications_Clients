package com.example.notif_backend.model;

import com.example.notif_backend.model.Envoi;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetourEnvoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceEnvoi;

    private String motif;
    private String dateRetour;

    @OneToOne
    @JoinColumn(name = "envoi_id")
    private Envoi envoi;
}
