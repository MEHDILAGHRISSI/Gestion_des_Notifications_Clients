package com.example.notif_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  numeroEnvoi;

    private String forme;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate   datePrevueEnvoi;

    private LocalDate dateEnvoi;



   @OneToMany(mappedBy = "envoi")
//   @JsonManagedReference
    private List<Notification> notifications;




    @OneToOne(mappedBy = "envoi", cascade = CascadeType.ALL)
    @JsonIgnore
    private RetourEnvoi retourEnvoi;

    public void setNumero(Long numero) {
        this.numeroEnvoi = numero;
    }



}