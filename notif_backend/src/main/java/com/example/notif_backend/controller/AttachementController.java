package com.example.notif_backend.controller;

import com.example.notif_backend.model.Attachement;
import com.example.notif_backend.repository.AttachementRepository;
import com.example.notif_backend.service.AttachementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/attachements")
public class AttachementController {

    @Autowired
    private AttachementService attachementService;

    @Autowired
    private final AttachementRepository attachementRepository;

    public AttachementController(AttachementRepository attachementRepository) {
        this.attachementRepository = attachementRepository;
    }

    @GetMapping
    public List<Attachement> getAll() {
        return attachementService.getAll();
    }

    @GetMapping("/{id}")
    public Attachement getById(@PathVariable Long id) {
        return attachementService.getById(id);
    }

    @PostMapping
    public Attachement create(@RequestBody Attachement attachement) {
        return attachementService.create(attachement);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        attachementService.delete(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<Attachement> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Chemin absolu vers le dossier uploads, basé sur le répertoire de travail de l'application
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        // Construction du chemin complet du fichier
        String filePath = uploadDir + File.separator + file.getOriginalFilename();
        File dest = new File(filePath);

        // Déplacement du fichier vers le dossier cible
        file.transferTo(dest);

        // Création et sauvegarde de l'objet Attachement
        Attachement attachement = new Attachement();
        attachement.setFileName(file.getOriginalFilename());
        attachement.setPath(filePath);

        Attachement saved = attachementRepository.save(attachement);

        // Debug log (optionnel)
        System.out.println("Fichier sauvegardé à : " + filePath);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        Attachement attachement = attachementService.getById(id);

        File file = new File(attachement.getPath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachement.getFileName() + "\"")
                .body(resource);
    }

}
