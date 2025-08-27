package com.example.notif_backend.scheduler;

import com.example.notif_backend.service.EnvoiService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnvoiScheduler {

    private final EnvoiService envoiService;

    public EnvoiScheduler(EnvoiService envoiService) {
        this.envoiService = envoiService;
    }

    // Exécuté toutes les 60 secondes
    @Scheduled(fixedRate = 60000)
    public void checkAndSendEnvois() {
        envoiService.processPendingEnvois();
    }
}
