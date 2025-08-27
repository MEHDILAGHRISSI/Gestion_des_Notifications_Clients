package com.example.notif_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationRequestDTO {
    private String templateId;
    private List<DestinataireParamDTO> destinataires;
    private Long utilisateurId;

    private List<Long> attachementIds;
}
