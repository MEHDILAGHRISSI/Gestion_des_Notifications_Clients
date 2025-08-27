package com.example.notif_backend.controller;

import com.example.notif_backend.model.Template;
import com.example.notif_backend.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping
    public List<Template> getAll() {
        return templateService.getAll();
    }

    @GetMapping("/{id}")
    public Template getById(@PathVariable Long  id) {
        return templateService.getById(id);
    }

    @PostMapping
    public Template create(@RequestBody Template template) {
        return templateService.create(template);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public Template update(@PathVariable Long  id, @RequestBody Template template) {
        return templateService.update(id, template);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long  id) {
        templateService.delete(id);
    }
}
