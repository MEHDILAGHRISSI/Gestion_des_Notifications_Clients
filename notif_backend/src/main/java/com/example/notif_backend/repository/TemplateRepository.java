package com.example.notif_backend.repository;

import com.example.notif_backend.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
