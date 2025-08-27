package com.example.notif_backend.service;

import com.example.notif_backend.model.Envoi;

public interface EnvoiSender {
    void sendForEnvoi(Envoi envoi) throws Exception;
    boolean supports(String formeEnvoi);
}