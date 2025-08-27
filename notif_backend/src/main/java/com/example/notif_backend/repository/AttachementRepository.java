package com.example.notif_backend.repository;

import com.example.notif_backend.model.Attachement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachementRepository extends JpaRepository<Attachement, Long> {
}
