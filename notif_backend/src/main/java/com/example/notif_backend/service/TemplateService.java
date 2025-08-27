package com.example.notif_backend.service;

import com.example.notif_backend.model.Template;
import com.example.notif_backend.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;

    public List<Template> getAll() {
        return templateRepository.findAll();
    }

    public Template getById(Long  id) {
        return templateRepository.findById(id).orElse(null);
    }

    public Template create(Template template) {
        return templateRepository.save(template);
    }

    public Template update(Long  id, Template updated) {
        if (!templateRepository.existsById(id)) return null;
        updated.setId(id);
        return templateRepository.save(updated);
    }

    public void delete(Long  id) {
        templateRepository.deleteById(id);
    }
}

