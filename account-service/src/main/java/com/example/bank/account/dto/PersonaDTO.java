package com.example.bank.account.dto;

import lombok.Data;

@Data
public class PersonaDTO {
    private Long id;
    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}