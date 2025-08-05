package com.example.bank.account.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long clienteid;
    private String contrasena;
    private boolean estado;
    private PersonaDTO persona;
}