package com.example.notif_backend.dto;

import lombok.Data;


public interface NotificationDashboardDTO {
    Long getId();
    String getObjet();
    String getDateCreation();
    String getSignataire();
    Long getEnvoisLies();
    String getFormes();
}