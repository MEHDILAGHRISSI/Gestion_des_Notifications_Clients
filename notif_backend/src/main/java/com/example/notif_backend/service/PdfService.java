package com.example.notif_backend.service;

import com.example.notif_backend.model.Notification;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;


@Service
public class PdfService {



    public byte[] generatePdfBytes(Notification notification) throws Exception {
        String htmlContent = generateHtmlContent(notification);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
        }
    }

    private String generateHtmlContent(Notification notification) {
        // Récupération du logo en Base64
        String logoBase64 = getLogoBase64();

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8"/>
                <style>
                    @page {
                        margin: 2cm 2.5cm;
                        size: A4;
                    }
                    
                    body {
                        font-family: 'Times New Roman', 'Liberation Serif', serif;
                        font-size: 12pt;
                        line-height: 1.4;
                        color: #333333;
                        margin: 0;
                        padding: 0;
                    }
                    
                    .letterhead {
                        display: table;
                        width: 100%%;
                        margin-bottom: 25px;
                        padding-bottom: 10px;
                        border-bottom: 1px solid #cccccc;
                    }
                    
                    .logo-section {
                        display: table-cell;
                        width: 80px;
                        vertical-align: top;
                    }
                    
                    .logo {
                        width: 70px;
                        height: auto;
                    }
                    
                    .header-info {
                        display: table-cell;
                        vertical-align: top;
                        padding-left: 15px;
                    }
                    
                    .company-name {
                        font-size: 14pt;
                        font-weight: bold;
                        color: #2c5530;
                        margin: 0 0 2px 0;
                    }
                    
                    .company-address {
                        font-size: 9pt;
                        color: #666666;
                        line-height: 1.2;
                        margin: 0;
                    }
                    
                    .document-info {
                        margin-top: 30px;
                        margin-bottom: 25px;
                    }
                    
                    .document-title {
                        font-size: 14pt;
                        font-weight: bold;
                        text-align: center;
                        margin-bottom: 15px;
                        color: #333333;
                    }
                    
                    .document-date {
                        text-align: right;
                        font-size: 10pt;
                        color: #666666;
                        margin-bottom: 20px;
                    }
                    
                    .recipient {
                        margin-bottom: 25px;
                        font-size: 11pt;
                    }
                    
                    .recipient-label {
                        font-weight: bold;
                        margin-bottom: 5px;
                    }
                    
                    .recipient-info {
                        margin-left: 20px;
                        line-height: 1.3;
                    }
                    
                    .content {
                        margin: 25px 0;
                        text-align: justify;
                        line-height: 1.5;
                        white-space: pre-line;
                    }
                    
                    .signature {
                        margin-top: 40px;
                        text-align: right;
                    }
                    
                    .signature p {
                        margin: 3px 0;
                    }
                    
                    .signature-name {
                        font-weight: bold;
                        margin-top: 10px;
                    }
                    
                    .footer {
                        position: fixed;
                        bottom: 1cm;
                        left: 0;
                        right: 0;
                        text-align: center;
                        font-size: 8pt;
                        color: #999999;
                        border-top: 1px solid #eeeeee;
                        padding-top: 5px;
                    }
                </style>
            </head>
            <body>
                <div class="letterhead">
                    <div class="logo-section">
                        <img src="data:image/png;base64,%s" class="logo" alt="CDG"/>
                    </div>
                    <div class="header-info">
                        <div class="company-name">Caisse de Dépôt et de Gestion</div>
                        <div class="company-address">
                            Place Moulay El Hassan, Souissi<br/>
                            BP 408, Rabat 10001 - Maroc<br/>
                            Tél: +212 5 37 56 90 00
                        </div>
                    </div>
                </div>
                
                <div class="document-info">
                    <div class="document-date">Rabat, le %s</div>
                    
                    <div class="document-title">%s</div>
                </div>
                
                <div class="recipient">
                    <div class="recipient-label">À :</div>
                    <div class="recipient-info">
                        %s<br/>
                        %s<br/>
                        %s
                    </div>
                </div>
                
                <div class="content">%s</div>
                
                <div class="signature">
                    <p>Cordialement,</p>
                    <p class="signature-name">%s</p>
                </div>
                
                <div class="footer">
                    Document généré le %s
                </div>
            </body>
            </html>
            """.formatted(
                logoBase64,
                notification.getDateCreation() != null ? notification.getDateCreation() : LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                notification.getObjet() != null ? notification.getObjet() : "",
                notification.getDestinataire() != null && notification.getDestinataire().getNomComplet() != null ? notification.getDestinataire().getNomComplet() : "",
                notification.getDestinataire() != null && notification.getDestinataire().getOrganisation() != null ? notification.getDestinataire().getOrganisation() : "",
                notification.getDestinataire() != null && notification.getDestinataire().getAdresse() != null ? notification.getDestinataire().getAdresse().replace("\n", "<br/>") : "",
                notification.getContenu() != null ? notification.getContenu() : "",
                notification.getSignataire() != null ? notification.getSignataire() : "",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"))
        );
    }

    /**
     * Méthode pour récupérer le logo CDG en Base64
     * Le logo doit être placé dans src/main/resources/static/images/cdg-logo.png
     */
    private String getLogoBase64() {
        try {
            ClassPathResource resource = new ClassPathResource("static/images/cdg-logo.png");
            byte[] logoBytes = Files.readAllBytes(Paths.get(resource.getURI()));
            return Base64.getEncoder().encodeToString(logoBytes);
        } catch (Exception e) {
            // Logo par défaut (pixel transparent) en cas d'erreur
            System.err.println("Impossible de charger le logo CDG: " + e.getMessage());
            return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==";
        }
    }
}