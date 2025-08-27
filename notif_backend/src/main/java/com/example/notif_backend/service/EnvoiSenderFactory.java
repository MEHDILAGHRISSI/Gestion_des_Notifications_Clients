package com.example.notif_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvoiSenderFactory {

    @Autowired
    private List<EnvoiSender> senders;

    public EnvoiSender getSender(String formeEnvoi) {
        return senders.stream()
                .filter(sender -> sender.supports(formeEnvoi))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun service d'envoi trouvé pour le type: " + formeEnvoi));
    }
}