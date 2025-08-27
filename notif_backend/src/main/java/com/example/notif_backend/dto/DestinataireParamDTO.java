package com.example.notif_backend.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DestinataireParamDTO {
    private Long id;
    private Map<String, String> params;
}