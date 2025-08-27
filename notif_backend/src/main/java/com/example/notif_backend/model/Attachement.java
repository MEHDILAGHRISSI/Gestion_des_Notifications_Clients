package com.example.notif_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private String fileName;

    @ManyToMany(mappedBy = "attachements")
    @JsonBackReference("notification-attachements")
    private List<Notification> notifications;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}