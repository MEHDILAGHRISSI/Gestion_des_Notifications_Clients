package com.example.notif_backend.repository;

import com.example.notif_backend.model.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinataireRepository extends JpaRepository<Destinataire, Long> {
}
