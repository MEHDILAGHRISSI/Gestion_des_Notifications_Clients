package com.example.notif_backend.repository;

import com.example.notif_backend.model.RetourEnvoi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetourEnvoiRepository extends JpaRepository<RetourEnvoi, Long> {
    List<RetourEnvoi> findByEnvoiNumeroEnvoi(Long numeroEnvoi);

}
