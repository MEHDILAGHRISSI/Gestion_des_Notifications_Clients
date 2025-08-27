package com.example.notif_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class EnvoiDTO {
    private Long numeroEnvoi;
    private String dateEnvoi;
    private String datePrevueEnvoi;
    private String forme;
    private List<DestinataireDTO> destinataires;
}
