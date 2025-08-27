package com.example.notif_backend.service;

import com.example.notif_backend.model.Destinataire;
import com.example.notif_backend.repository.DestinataireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinataireService {
    @Autowired
    private DestinataireRepository destinataireRepository;

    public List<Destinataire> getAll(){
        return destinataireRepository.findAll();
    }

    public Destinataire getById(Long id){
        return destinataireRepository.getById(Long.valueOf(id));
    }


    public Destinataire create(Destinataire destinataire) {
        return destinataireRepository.save(destinataire);
    }

    public void delete(Long id) {
        destinataireRepository.deleteById(id);
    }

    public Destinataire update(Long id, Destinataire updatedDestinataire) {
        Destinataire existing = destinataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé avec id: " + id));

        existing.setEmail(updatedDestinataire.getEmail());
        existing.setTel(updatedDestinataire.getTel());
        existing.setType(updatedDestinataire.getType());
        existing.setNomComplet(updatedDestinataire.getNomComplet());
        existing.setOrganisation(updatedDestinataire.getOrganisation());
        existing.setAdresse(updatedDestinataire.getAdresse());

        return destinataireRepository.save(existing);
    }


}
