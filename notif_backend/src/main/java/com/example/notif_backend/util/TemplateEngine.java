package com.example.notif_backend.util;

import java.time.LocalDate;
import java.util.Map;

public class TemplateEngine {
    public static String genererMessage(String template, Map<String, String> valeurs) {
        // Remplacer les variables fournies dans la map
        for (Map.Entry<String, String> entry : valeurs.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        // Remplacement spécial de {{localdate}} par la date du jour
        if (template.contains("{{localdate}}")) {
            template = template.replace("{{localdate}}", LocalDate.now().toString());
        }

        return template;
    }
}
